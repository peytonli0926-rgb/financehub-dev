<template>
  <section class="documents-card">
    <header class="documents-header">
      <div><h2>我的单据</h2><span>各来源系统通用业务单据</span></div>
      <el-form :inline="true" :model="query" class="document-search" @submit.prevent>
        <el-form-item label="单据编号"><el-input v-model.trim="query.orderId" clearable placeholder="请输入单据编号" @keyup.enter="search" /></el-form-item>
        <el-form-item label="合同编号"><el-input v-model.trim="query.contractCode" clearable placeholder="请输入合同编号" @keyup.enter="search" /></el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="query.messageStatus" clearable placeholder="全部状态">
            <el-option label="待处理" value="NOT_EXECUTE" /><el-option label="处理中" value="RUNNING" />
            <el-option label="处理成功" value="SUCCESS" /><el-option label="处理失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" :loading="loading" @click="search"><el-icon><Search /></el-icon>查询</el-button><el-button @click="reset">重置</el-button></el-form-item>
      </el-form>
    </header>

    <el-table v-loading="loading" :data="rows" stripe class="document-table" empty-text="暂无单据">
      <el-table-column prop="orderId" label="单据编号" min-width="185" show-overflow-tooltip />
      <el-table-column label="单据来源" min-width="175" show-overflow-tooltip><template #default="{ row }"><el-tag effect="plain">{{ sourceSystemName(row) }}</el-tag></template></el-table-column>
      <el-table-column label="业务事件" width="115"><template #default="{ row }"><el-tag effect="plain" type="danger">{{ eventName(row) }}</el-tag></template></el-table-column>
      <el-table-column label="客户名称" min-width="150" show-overflow-tooltip><template #default="{ row }">{{ customerName(row) }}</template></el-table-column>
      <el-table-column label="合同编号" min-width="180" show-overflow-tooltip><template #default="{ row }">{{ contractCode(row) }}</template></el-table-column>
      <el-table-column label="业务日期" width="115"><template #default="{ row }">{{ dateOnly(row.businessDate || content(row).business_date) }}</template></el-table-column>
      <el-table-column label="状态" width="115"><template #default="{ row }"><span class="status" :class="row.messageStatus">{{ statusText[row.messageStatus] || row.messageStatus }}</span></template></el-table-column>
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.messageStatus === 'SUCCESS'" link type="primary" @click="viewVoucher(row)">查看凭证</el-button>
          <el-button v-else-if="row.messageStatus === 'RUNNING'" link type="primary" loading disabled>生成中</el-button>
          <el-button v-else link type="danger" @click="execute(row)">{{ row.messageStatus === 'FAILED' ? '重新执行' : '立即执行' }}</el-button>
          <el-button link type="info" @click="showDetail(row)">查看报文</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination"><el-pagination v-model:current-page="query.pageNum" v-model:page-size="query.pageSize" layout="total, prev, pager, next" :total="total" @current-change="load" /></div>

    <el-dialog v-model="detailVisible" title="接口单据原始报文" width="820px" top="6vh" destroy-on-close>
      <div class="detail-summary" v-if="detail"><span>单据编号：{{ detail.orderId }}</span><span>合同编号：{{ detail.contractCode }}</span><span>事件：{{ eventName(detail) }}</span></div>
      <pre class="json-viewer">{{ formattedJson }}</pre>
      <template #footer><el-button @click="detailVisible = false">关闭</el-button><el-button type="primary" @click="copyJson">复制JSON</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getRawDocument, getVoucherInterfaceDataId, pageRawDocuments, retryRawDocument } from '@/api/portalEntry/myDocuments'
import { useVoucherPage } from '@/hooks'

const loading = ref(false)
const rows = ref([])
const total = ref(0)
const detail = ref(null)
const detailVisible = ref(false)
const { setVoucherPage } = useVoucherPage()
let pollingTimer
const query = reactive({ pageNum: 1, pageSize: 5, orderId: '', contractCode: '', messageStatus: '' })
const statusText = { NOT_EXECUTE: '待生成', RUNNING: '凭证生成中', SUCCESS: '已生成凭证', FAILED: '生成失败' }
const sourceSystemNames = {
  OPERATING_LEASE: '经营租赁业务系统',
  FINANCE_LEASE: '融资租赁业务系统',
  HOUSEHOLD_PV: '户用光伏业务系统',
  RETAIL_FINANCE_LEASE: '零售融资租赁业务系统',
  CYCXT: '零售融资租赁业务系统',
  TREASURY: '资金系统',
  IMPAIRMENT: '减值系统'
}
const content = (row) => row?.messageContent || {}
const sourceSystemName = (row) => sourceSystemNames[row?.systemCode || content(row).source_system] || content(row).systemName || content(row).system_name || row?.systemCode || content(row).source_system || '-'
const eventNames = {
  C005: '到并核销租金', C043: '前期逾期留购价',
  CR000: '收到现金折扣', CR001: '收取保证金', CR003: '购入租赁资产', CR005: '到并核销租金',
  CR006: '款项无法确认', CR007: '人工明确款项用途', CR008: '收到款项核销', CR025: '支付资产管理费',
  CR029: '退回平台合作方提前结清贴息金额', CR033: '退回的分润费', CR036: '支付银行手续费',
  CR040: '抵押服务费', CR041: '支付通联手续费', CR043: '前期逾期留购价', CR044: '渠道商分成结算', CR056: '合作方提前结清',
  RF001: '普通退款', RF002: '保证金退款', RF003: '未确认款退款',
  SC001: '厂商贴息确认', SC002: '平台贴息确认', SC003: '提前结清贴息冲回',
  OD001: '本金转逾期', OD002: '利息转逾期', OD003: '留购价转逾期', OD004: '逾期罚息确认',
  TS001: '租金计划调整', TS002: '租金信息变更', TS003: '结清', TS004: '起租后GPS加装',
  TS005: '留购价反向', TS006: '费用减免租金', TS007: '费用减免留购价', TS008: '天津车辆处置结清',
  TS009: '提前留购', TS010: '尾款调整租金', TS011: '车辆处置', TS012: '车辆买断',
  AA001: '项目承租人发生变更', AA002: '因辅助核算项目挂错，调整相关科目',
  OT001: '内部资金调拨', OT002: '头寸调拨', OT003: '季度结息', OT004: '季度结息',
  OT005: '多支付分润费挂账', OT006: '金额记错，调整相关科目', OT007: '调整违约金分成',
  OT008: '促销核准后，补差合作方分润费'
}
const eventName = (row) => {
  const value = content(row).event_name || content(row).sourceEventCode || content(row).sceneName || content(row).scene_name || (row.sceneCode === 'HTQZ' ? '起租' : row.sceneCode)
  return eventNames[value] || value
}
const customerName = (row) => content(row).customer_name || content(row).clientName || content(row).client_name || '-'
const contractCode = (row) => row?.contractCode || content(row).contract_no || content(row).contractCode || '-'
const dateOnly = (value) => value ? String(value).slice(0, 10) : '-'
const formattedJson = computed(() => JSON.stringify(content(detail.value), null, 2))

async function load (silent = false) {
  if (!silent) loading.value = true
  try {
    const { data } = await pageRawDocuments({ ...query })
    rows.value = data?.records || []
    total.value = Number(data?.total || 0)
    schedulePolling()
  } finally { if (!silent) loading.value = false }
}
function schedulePolling () {
  clearTimeout(pollingTimer)
  if (rows.value.some(row => row.messageStatus === 'RUNNING')) {
    pollingTimer = setTimeout(() => load(true), 1500)
  }
}
function search () { query.pageNum = 1; load() }
function reset () { query.orderId = ''; query.contractCode = ''; query.messageStatus = ''; search() }
async function showDetail (row) {
  const { data } = await getRawDocument(row.id)
  detail.value = data || row
  detailVisible.value = true
}
async function copyJson () {
  await navigator.clipboard.writeText(formattedJson.value)
  ElMessage.success('JSON 已复制')
}
async function execute (row) {
  const isRetry = row.messageStatus === 'FAILED'
  try {
    await ElMessageBox.confirm(`确定${isRetry ? '重新' : '立即'}执行单据“${row.orderId}”并生成凭证吗？`, isRetry ? '重新执行' : '立即执行', { confirmButtonText: '确定执行', cancelButtonText: '取消', type: 'warning' })
  } catch (_) {
    return
  }
  const { code, msg } = await retryRawDocument(row.id)
  if (code === 200) {
    row.messageStatus = 'RUNNING'
    row.errorInfo = null
    ElMessage.success('已提交，正在生成凭证')
    schedulePolling()
  } else {
    ElMessage.error(msg || '提交失败')
  }
}
async function viewVoucher (row) {
  const { data } = await getVoucherInterfaceDataId(row.id)
  if (!data) {
    ElMessage.error('未找到该单据生成的凭证')
    return
  }
  setVoucherPage({ interfaceDataId: data })
}
onMounted(load)
onBeforeUnmount(() => clearTimeout(pollingTimer))
</script>

<style lang="scss" scoped>
$red:#d70d18;$line:#e9edf2;
.documents-card{margin-top:7px;padding:0 12px 12px;background:#fff;border:1px solid $line;border-radius:7px;box-shadow:0 3px 10px rgba(30,34,40,.025)}
.documents-header{display:flex;align-items:center;justify-content:space-between;padding:10px 3px 7px;>div{display:flex;align-items:center}h2{margin:0;font-size:14px;&:before{display:inline-block;width:3px;height:14px;margin:0 7px -2px 0;content:'';background:$red;border-radius:2px}}span{margin-left:10px;color:#a0a5ad;font-size:9px}}
.document-search{display:flex;align-items:center;gap:6px;.el-form-item{margin:0}.el-input{width:165px}.el-select{width:125px}:deep(.el-form-item__label){font-size:12px}:deep(.el-button--primary){background:$red;border-color:$red}:deep(.el-button--primary:hover){background:#b90b14;border-color:#b90b14}}
.document-table{width:100%;:deep(th.el-table__cell){color:#555;background:#fafafa}:deep(.el-button--primary.is-link){color:$red}}
.status{display:inline-flex;align-items:center;color:#60656f;&:before{width:7px;height:7px;margin-right:6px;content:'';background:#a8adb5;border-radius:50%}&.NOT_EXECUTE:before{background:#f0a020}&.RUNNING:before{background:#409eff}&.SUCCESS:before{background:#52b976}&.FAILED:before{background:$red}}
.pagination{display:flex;justify-content:flex-end;padding-top:10px}:deep(.el-pager li.is-active){background:$red!important;color:#fff!important}
.detail-summary{display:flex;gap:24px;margin-bottom:10px;padding:10px 12px;color:#666;background:#fafafa;border:1px solid $line;border-radius:5px;font-size:12px}
.json-viewer{max-height:62vh;margin:0;padding:16px;overflow:auto;color:#313846;background:#f7f8fa;border:1px solid #e2e6ec;border-radius:6px;font:12px/1.65 Consolas,Monaco,monospace;white-space:pre-wrap;word-break:break-all}
@media(max-width:1250px){.documents-header{align-items:flex-start;flex-direction:column;gap:10px}.document-search{flex-wrap:wrap}}
</style>
