<template>
  <div class="contract-detail" v-loading="loading">
    <el-alert v-if="loadError" :title="loadError" type="error" :closable="false" show-icon class="load-error" />

    <section class="contract-header">
      <div>
        <div class="header-kicker">单合同 · 全生命周期</div>
        <div class="contract-title-row">
          <h1>{{ contractCode }}</h1>
          <div class="contract-tags">
            <el-tag effect="plain">{{ contract.leaseType || model.leaseType || '租赁合同' }}</el-tag>
            <el-tag type="success" effect="plain">{{ model.contractStatus || contract.contractStatus || '正常' }}</el-tag>
            <el-tag type="info" effect="plain">{{ sourceName }}</el-tag>
          </div>
        </div>
        <div class="header-subtitle">
          <span>{{ contract.clientName || model.customerName || '--' }}</span><i></i>
          <span>{{ contract.businessName || contract.businessPlate || '--' }}</span><i></i>
          <span>{{ contract.contractName || model.productName || '--' }}</span>
        </div>
      </div>
      <div class="header-status">
        <span>合同期间</span>
        <strong>{{ shortDate(model.leaseStartDate || contract.leaseDateStart) }} 至 {{ shortDate(model.maturityDate || contract.leaseDateEnd) }}</strong>
        <small>{{ schedulePlans.length ? `共 ${schedulePlans.length} 期 · ${model.repaymentMethod || contract.returnType || '还租方式待补充'}` : (contract.businessName || '合同信息') }}</small>
      </div>
    </section>

    <section class="summary-grid">
      <article class="summary-card"><span>合同金额</span><strong>¥ {{ money(contract.contractAmount || model.contractAmount) }}</strong><small>{{ contract.currencyType || model.currency || 'CNY' }}</small></article>
      <article class="summary-card"><span>业务类型</span><strong>{{ contract.businessName || contract.businessCode || '--' }}</strong><small>{{ contract.businessPlate || '业务板块待补充' }}</small></article>
      <article class="summary-card accent"><span>合同利率</span><strong>{{ percent(contract.leaseInterestRateYear || model.contractRate) }}</strong><small>{{ contract.leaseType || model.leaseType || '--' }}</small></article>
      <article class="summary-card"><span>合同状态</span><strong>{{ contract.contractStatus || model.contractStatus || '--' }}</strong><small>财务状态 {{ contract.financialContractStatus || '--' }}</small></article>
      <article class="summary-card"><span>业务数据概览</span><strong>{{ balanceRows.length }} 条余额 / {{ vouchers.length }} 张凭证</strong><small>{{ schedulePlans.length }} 期回收 · {{ accrualPlans.length }} 条收益明细</small></article>
    </section>

    <el-card shadow="never" class="detail-card">
      <el-tabs v-model="activeTab" class="detail-tabs">
        <el-tab-pane label="基础信息" name="base">
          <div class="tab-body">
            <InfoSection title="合同信息" :items="contractInfoItems" />
            <InfoSection title="客户与业务信息" :items="customerInfoItems" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="交易结构" name="structure">
          <div class="tab-body">
            <div class="structure-banner">
              <div><span>合同金额</span><strong>¥ {{ money(contract.contractAmount || model.contractAmount) }}</strong></div><b>→</b>
              <div><span>应付设备款</span><strong>¥ {{ money(contract.payableDeviceAmount || model.financeAmount) }}</strong></div><b>→</b>
              <div class="highlight"><span>应收首付款</span><strong>¥ {{ money(contract.receivableFirstAmount) }}</strong></div><b>→</b>
              <div><span>名义留购价</span><strong>¥ {{ money(contract.retainedPrice || model.residualValue) }}</strong></div>
            </div>
            <InfoSection title="融资租赁结构" :items="structureInfoItems" />
            <InfoSection title="计息与回收安排" :items="repaymentInfoItems" />
          </div>
        </el-tab-pane>

        <el-tab-pane :label="`交易流水（${vouchers.length}）`" name="transactions">
          <TableTitle title="合同交易流水" desc="按业务发生时间倒序展示，可穿透查看对应凭证" />
          <el-table :data="vouchers" border stripe max-height="520" empty-text="暂无交易流水">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="businessDate" label="业务日期" width="168" />
            <el-table-column prop="voucherDate" label="记账日期" width="168" />
            <el-table-column prop="periodCode" label="会计期间" width="100" align="center" />
            <el-table-column prop="sceneName" label="业务场景" width="120" />
            <el-table-column prop="eventName" label="事件名称" width="210" show-overflow-tooltip />
            <el-table-column label="数据来源" width="160" show-overflow-tooltip><template #default="{ row }">{{ sourceLabel(row.systemCode || row.source) }}</template></el-table-column>
            <el-table-column prop="voucherNum" label="凭证号" width="100" />
            <el-table-column prop="voucherSummary" label="交易摘要" min-width="320" show-overflow-tooltip />
            <el-table-column label="状态" width="100" align="center"><template #default="{ row }"><el-tag size="small" :type="row.voucherStatus === '3' ? 'success' : 'info'">{{ voucherStatusName(row.voucherStatus) }}</el-tag></template></el-table-column>
            <el-table-column label="操作" width="100" fixed="right" align="center"><template #default="{ row }"><el-button type="primary" link :disabled="!row.id" @click="viewVoucher(row.id)">查看凭证</el-button></template></el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane :label="`科目余额（${balanceRows.length}）`" name="balances">
          <div v-if="settlementCheck.visible" class="settlement-check">
            <div class="settlement-check-title">
              <strong>结清校验</strong>
              <span>取最后一笔交易后的合同科目余额</span>
            </div>
            <div class="settlement-check-item" :class="settlementCheck.assetPassed ? 'passed' : 'failed'">
              <span>资产类科目</span>
              <strong>{{ settlementCheck.assetPassed ? '全部为 0.00' : `${settlementCheck.nonZeroAssetCount} 项未清零` }}</strong>
              <small>未清余额绝对值合计 ¥ {{ money(settlementCheck.assetBalance) }}</small>
            </div>
            <div class="settlement-check-item" :class="settlementCheck.profitLossPassed ? 'passed' : 'failed'">
              <span>损益类科目</span>
              <strong>{{ settlementCheck.profitLossPassed ? '保留累计损益' : '累计损益为 0.00' }}</strong>
              <small>余额绝对值合计 ¥ {{ money(settlementCheck.profitLossBalance) }}</small>
            </div>
            <el-tag :type="settlementCheck.passed ? 'success' : 'danger'" effect="dark">
              {{ settlementCheck.passed ? '结清校验通过' : '结清校验未通过' }}
            </el-tag>
          </div>
          <TableTitle title="合同科目发生额与余额" desc="按交易事件汇总发生额并逐笔结转余额；未发生的发生额显示 0.00" />
          <el-table :data="balanceRows" border stripe max-height="620" empty-text="暂无合同科目余额">
            <el-table-column type="index" label="序号" width="60" align="center" fixed="left" />
            <el-table-column prop="periodCode" label="会计期间" width="100" fixed="left" />
            <el-table-column label="业务事件" width="145" fixed="left"><template #default="{ row }"><b>{{ balanceSceneName(row) }}</b><small class="scene-code">{{ row.sceneCode }}</small></template></el-table-column>
            <el-table-column prop="businessDate" label="业务日期" width="168" />
            <el-table-column prop="voucherDate" label="记账日期" width="168" />
            <el-table-column v-for="subject in activeBalanceSubjects" :key="subject.amount" :label="subject.label" align="center">
              <el-table-column label="发生额" width="130" align="right"><template #default="{ row }">{{ money(row[subject.amount]) }}</template></el-table-column>
              <el-table-column label="余额" width="130" align="right"><template #default="{ row }"><b>{{ money(row[subject.balance]) }}</b></template></el-table-column>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right" align="center"><template #default="{ row }"><el-button type="primary" link :disabled="!row.voucherId" @click="viewVoucher(row.voucherId)">查看凭证</el-button></template></el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane :label="`回收计划（${schedulePlans.length}）`" name="repayment">
          <TableTitle title="租金回收计划" desc="计划应收与实际回收并列展示" :total="`计划租金合计：¥ ${money(plannedRentTotal)}`" />
          <el-table :data="schedulePlans" border stripe max-height="540" empty-text="暂无回收计划">
            <el-table-column prop="periods" label="期次" width="70" align="center" fixed="left" />
            <el-table-column prop="planDate" label="计划还款日" width="120" fixed="left" />
            <MoneyColumn label="计划租金" prop="rentAmount" width="125" />
            <MoneyColumn label="计划本金" prop="principalAmount" width="125" />
            <MoneyColumn label="计划利息" prop="interestAmount" width="115" />
            <el-table-column prop="actualRepaymentDate" label="实际回收日" width="120" />
            <el-table-column label="实收租金" width="125" align="right"><template #default="{ row }">{{ nullableMoney(row.actualRepaymentRentAmount) }}</template></el-table-column>
            <el-table-column label="实收本金" width="125" align="right"><template #default="{ row }">{{ nullableMoney(row.actualRepaymentPrincipalAmount) }}</template></el-table-column>
            <el-table-column label="实收利息" width="115" align="right"><template #default="{ row }">{{ nullableMoney(row.actualRepaymentInteresAmount) }}</template></el-table-column>
            <MoneyColumn label="期初摊余成本" prop="openingAmortizedCost" width="140" />
            <MoneyColumn label="期末摊余成本" prop="endingAmortizedCost" width="140" />
            <el-table-column label="回收状态" width="100" fixed="right" align="center"><template #default="{ row }"><el-tag size="small" :type="row.actualRepaymentDate ? 'success' : 'warning'">{{ row.actualRepaymentDate ? '已回收' : '待回收' }}</el-tag></template></el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane :label="`收益计提明细（${accrualPlans.length}）`" name="income">
          <TableTitle title="XIRR 收益计提明细" desc="包含月末计提和还款日收益摊销节点" :total="`计划收益合计：¥ ${money(incomeTotal)}；分润费合计：¥ ${money(profitSharingTotal)}`" />
          <el-table :data="accrualPlans" border stripe max-height="540" empty-text="暂无收益计提明细">
            <el-table-column type="index" label="序号" width="60" align="center" fixed="left" />
            <el-table-column prop="planDate" label="计提日期" width="115" fixed="left" />
            <el-table-column label="节点类型" width="105" align="center"><template #default="{ row }"><el-tag size="small" :type="row.periods == null ? 'danger' : 'info'">{{ row.periods == null ? '月末计提' : `第 ${row.periods} 期` }}</el-tag></template></el-table-column>
            <el-table-column label="实际日利率" width="115" align="right"><template #default="{ row }">{{ dailyRate(row.actualDailyRate) }}</template></el-table-column>
            <MoneyColumn label="期初摊余成本" prop="openingAmortizedCost" width="145" />
            <MoneyColumn label="本期现金流" prop="cashFlow" width="130" />
            <el-table-column label="本期 XIRR 收益" width="145" align="right"><template #default="{ row }"><b class="income-value">{{ money(row.rentalIncome) }}</b></template></el-table-column>
            <MoneyColumn label="分润费分摊额" prop="profitSharingAllocationAmount" width="145" />
            <MoneyColumn label="本期以前累计" prop="rentalIncomeBeforeTotal" width="140" />
            <MoneyColumn label="本期以后待摊" prop="rentalIncomeAfterTotal" width="140" />
            <MoneyColumn label="期末摊余成本" prop="endingAmortizedCost" width="145" />
            <el-table-column label="表内/表外" width="95" align="center"><template #default="{ row }">{{ row.onAndOffBalanceSheet === '1' ? '表外' : '表内' }}</template></el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="风险信息" name="risk">
          <div class="tab-body"><InfoSection title="合同风险状态" :items="riskInfoItems" /></div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { computed, defineComponent, h, onMounted, ref } from 'vue'
import { ElDescriptions, ElDescriptionsItem, ElTableColumn } from 'element-plus'
import { useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import { getVehicleLifecycle } from '@/api/searchBusiness/contractBusiness'

const InfoSection = defineComponent({
  props: { title: String, items: { type: Array, default: () => [] } },
  setup: props => () => h('section', { class: 'info-section' }, [
    h('div', { class: 'section-heading' }, props.title),
    h(ElDescriptions, { column: 4, border: true }, () => props.items.map(entry => h(ElDescriptionsItem, { key: entry.label, label: entry.label, span: entry.span || 1 }, () => entry.value ?? '--')))
  ])
})
const TableTitle = defineComponent({
  props: { title: String, desc: String, total: String },
  setup: props => () => h('div', { class: 'table-toolbar' }, [h('div', [h('strong', props.title), h('span', props.desc)]), props.total ? h('b', { class: 'toolbar-total' }, props.total) : null])
})
const MoneyColumn = defineComponent({
  props: { label: String, prop: String, width: { type: [String, Number], default: 145 } },
  setup: props => () => h(ElTableColumn, { label: props.label, width: props.width, align: 'right' }, { default: ({ row }) => money(row[props.prop]) })
})

const route = useRoute()
const { setVoucherPage } = useVoucherPage()
const loading = ref(false)
const loadError = ref('')
const activeTab = ref('base')
const detail = ref({})
const contract = computed(() => detail.value.contract || {})
const model = computed(() => detail.value.businessModel || {})
const plans = computed(() => detail.value.repaymentPlans || [])
const byBusinessDateDesc = rows => [...(rows || [])].sort((left, right) => {
  const timeOrder = String(right.businessDate || right.voucherDate || '')
    .localeCompare(String(left.businessDate || left.voucherDate || ''))
  if (timeOrder !== 0) return timeOrder
  const rightSequence = String(right.id || right.voucherId || '')
  const leftSequence = String(left.id || left.voucherId || '')
  return rightSequence.length - leftSequence.length || rightSequence.localeCompare(leftSequence)
})
const vouchers = computed(() => byBusinessDateDesc(detail.value.vouchers))
const balanceRows = computed(() => byBusinessDateDesc(detail.value.balances))
const balanceSubjects = [
  { label: '融资租赁资产成本', amount: 'leaseAssetCostAmount', balance: 'leaseAssetCostBalance' },
  { label: '融资租赁资产', amount: 'leaseAssetMovableLeasebackAmount', balance: 'leaseAssetMovableLeasebackBalance' },
  { label: '应收租赁本金', amount: 'leasePrincipalReceivableAmount', balance: 'leasePrincipalReceivableBalance' },
  { label: '应收租赁利息', amount: 'leaseInterestReceivableAmount', balance: 'leaseInterestReceivableBalance' },
  { label: '应收利息增值税', amount: 'leaseInterestVatReceivableAmount', balance: 'leaseInterestVatReceivableBalance' },
  { label: '应收留购价', amount: 'residualValueReceivableAmount', balance: 'residualValueReceivableBalance' },
  { label: '应收留购价增值税', amount: 'residualValueVatReceivableAmount', balance: 'residualValueVatReceivableBalance' },
  { label: '未实现融资收益-利息', amount: 'unearnedLeaseInterestAmount', balance: 'unearnedLeaseInterestBalance' },
  { label: '未实现融资收益-利息税', amount: 'unearnedLeaseInterestVatAmount', balance: 'unearnedLeaseInterestVatBalance' },
  { label: '未实现融资收益-留购价', amount: 'unearnedResidualValueAmount', balance: 'unearnedResidualValueBalance' },
  { label: '未实现融资收益-留购价税', amount: 'unearnedResidualValueVatAmount', balance: 'unearnedResidualValueVatBalance' },
  { label: '融资租赁利息收入', amount: 'leaseInterestIncomeAmount', balance: 'leaseInterestIncomeBalance' },
  { label: '应收车辆清分款', amount: 'vehicleProfitSharingReceivableAmount', balance: 'vehicleProfitSharingReceivableBalance' },
  { label: '应付车辆分润费', amount: 'vehicleProfitSharingPayableAmount', balance: 'vehicleProfitSharingPayableBalance' },
  { label: '车辆资产管理费', amount: 'vehicleProjectServiceFeeExpenseAmount', balance: 'vehicleProjectServiceFeeExpenseBalance' },
  { label: '应付车辆管理费', amount: 'vehicleManagementFeePayableAmount', balance: 'vehicleManagementFeePayableBalance' },
  { label: '印花税费用', amount: 'stampDutyExpenseAmount', balance: 'stampDutyExpenseBalance' },
  { label: '应交印花税', amount: 'stampDutyPayableAmount', balance: 'stampDutyPayableBalance' }
  ,{ label: '逾期本金', amount: 'overdueLeasePrincipalAmount', balance: 'overdueLeasePrincipalBalance' }
  ,{ label: '逾期利息', amount: 'overdueLeaseInterestAmount', balance: 'overdueLeaseInterestBalance' }
  ,{ label: '逾期利息增值税', amount: 'overdueLeaseInterestVatAmount', balance: 'overdueLeaseInterestVatBalance' }
  ,{ label: '逾期留购价', amount: 'overdueResidualValueAmount', balance: 'overdueResidualValueBalance' }
  ,{ label: '逾期留购价增值税', amount: 'overdueResidualValueVatAmount', balance: 'overdueResidualValueVatBalance' }
  ,{ label: '贴息待收进项税', amount: 'inputVatReceivableAmount', balance: 'inputVatReceivableBalance' }
  ,{ label: '逾期罚息收入', amount: 'penaltyInterestIncomeAmount', balance: 'penaltyInterestIncomeBalance' }
]
const activeBalanceSubjects = computed(() => balanceSubjects.filter(subject => balanceRows.value.some(row => Number(row[subject.amount] || 0) !== 0 || Number(row[subject.balance] || 0) !== 0)))
const assetBalanceFields = [
  'leaseAssetCostBalance', 'leaseAssetMovableLeasebackBalance', 'leaseAssetConstructionLeasebackBalance',
  'leasePrincipalReceivableBalance', 'leaseInterestReceivableBalance', 'leaseInterestVatReceivableBalance',
  'residualValueReceivableBalance', 'residualValueVatReceivableBalance',
  'overdueLeasePrincipalBalance', 'overdueLeaseInterestBalance', 'overdueLeaseInterestVatBalance',
  'overdueResidualValueBalance', 'overdueResidualValueVatBalance',
  'vehicleProfitSharingReceivableBalance', 'inputVatReceivableBalance'
]
const profitLossBalanceFields = [
  'leaseInterestIncomeBalance', 'penaltyInterestIncomeBalance', 'earlyTerminationIncomeBalance',
  'earlySettlementPenaltyIncomeBalance', 'vehicleProjectServiceFeeExpenseBalance',
  'stampDutyExpenseBalance', 'bankServiceFeeExpenseBalance', 'paymentChannelFeeExpenseBalance',
  'mortgageServiceFeeExpenseBalance', 'leaseReceivableImpairmentLossBalance'
]
const settlementCheck = computed(() => {
  const status = model.value.contractStatus || contract.value.contractStatus || ''
  const visible = String(status).includes('结清') && balanceRows.value.length > 0
  const terminal = balanceRows.value[0] || {}
  const nonZeroAssetCount = assetBalanceFields.filter(key => Math.abs(Number(terminal[key] || 0)) >= 0.005).length
  const assetBalance = assetBalanceFields.reduce((sum, key) => sum + Math.abs(Number(terminal[key] || 0)), 0)
  const profitLossBalance = profitLossBalanceFields.reduce((sum, key) => sum + Math.abs(Number(terminal[key] || 0)), 0)
  const assetPassed = nonZeroAssetCount === 0
  const profitLossPassed = profitLossBalance >= 0.005
  return { visible, nonZeroAssetCount, assetBalance, profitLossBalance, assetPassed, profitLossPassed, passed: assetPassed && profitLossPassed }
})
const contractCode = computed(() => model.value.contractCode || contract.value.contractCode || '--')
const schedulePlans = computed(() => plans.value.filter(row => Number(row.periods) > 0))
const accrualPlans = computed(() => plans.value.filter(row => row.periods == null || Number(row.rentalIncome || 0) !== 0))
const plannedRentTotal = computed(() => schedulePlans.value.reduce((sum, row) => sum + Number(row.rentAmount || 0), 0))
const incomeTotal = computed(() => accrualPlans.value.reduce((sum, row) => sum + Number(row.rentalIncome || 0), 0))
const profitSharingTotal = computed(() => accrualPlans.value.reduce((sum, row) => sum + Number(row.profitSharingAllocationAmount || 0), 0))
const sourceLabels = {
  RETAIL_FINANCE_LEASE: '零售融资租赁',
  CYCXT: '零售融资租赁',
  FINANCE_LEASE: '融资租赁',
  OPERATING_LEASE: '经营租赁',
  HOUSEHOLD_PV: '户用光伏',
  TREASURY: '资金系统',
  IMPAIRMENT: '减值系统',
  FINHUB: '业财中台'
}
const sourceLabel = value => sourceLabels[value] || value || '--'
const sourceName = computed(() => sourceLabel(model.value.sourceSystem || contract.value.systemCode))

const money = value => Number(value || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
const nullableMoney = value => value == null ? '--' : money(value)
const shortDate = value => value ? String(value).slice(0, 10) : '--'
const percent = value => {
  if (value == null || value === '') return '--'
  const number = Number(value)
  return `${(Math.abs(number) <= 1 ? number * 100 : number).toFixed(4)}%`
}
const dailyRate = value => value == null ? '--' : `${Number(value).toFixed(6)}%`
const field = (label, value, span) => ({ label, value: value || value === 0 ? value : '--', span })
const voucherStatusName = value => ({ 0: '待生成', 1: '待传送', 2: '已提交', 3: '已传送', 4: '已冲销' }[value] || value || '--')
const balanceSceneName = row => {
  const voucher = vouchers.value.find(item => String(item.id) === String(row.voucherId))
  return voucher?.eventName || voucher?.sceneName || row.sceneCode || '--'
}
const viewVoucher = id => setVoucherPage({ voucherIdList: [id] })

const contractInfoItems = computed(() => [
  field('合同编号', contractCode.value), field('合同名称', model.value.productName || contract.value.contractName), field('签约主体', model.value.orgId || contract.value.orgId), field('合同状态', model.value.contractStatus || contract.value.contractStatus),
  field('签约日期', shortDate(model.value.contractSignDate || contract.value.businessDate)), field('起租日期', shortDate(model.value.leaseStartDate || contract.value.leaseDateStart)), field('到期日期', shortDate(model.value.maturityDate || contract.value.leaseDateEnd)), field('数据来源', sourceName.value)
])
const customerInfoItems = computed(() => [
  field('客户编号', contract.value.clientCode || model.value.customerCode), field('客户名称', contract.value.clientName || model.value.customerName), field('业务编码', contract.value.businessCode), field('业务类型', contract.value.businessName),
  field('业务板块', contract.value.businessPlate || model.value.businessLine), field('租赁类型', contract.value.leaseType || model.value.leaseType), field('还租方式', contract.value.returnType || model.value.leaseMethod), field('客户类型', contract.value.clientType)
])
const structureInfoItems = computed(() => [
  field('合同金额', `¥ ${money(contract.value.contractAmount || model.value.contractAmount)}`), field('应付设备款', `¥ ${money(contract.value.payableDeviceAmount || model.value.financeAmount)}`), field('应收首付款', `¥ ${money(contract.value.receivableFirstAmount)}`), field('名义留购价', `¥ ${money(contract.value.retainedPrice || model.value.residualValue)}`),
  field('应收履约保证金', `¥ ${money(contract.value.receivableMarginAmount)}`), field('应收手续费', `¥ ${money(contract.value.receivableProcedureAmount)}`), field('渠道费用', `¥ ${money(contract.value.channelFees)}`), field('币种', contract.value.currencyType || model.value.currency)
])
const repaymentInfoItems = computed(() => [field('合同利率', percent(model.value.contractRate || contract.value.leaseInterestRateYear)), field('实际收益率 XIRR', percent(model.value.xirrRate)), field('总期数', `${model.value.totalTerms || schedulePlans.value.length || 0} 期`), field('回款频率', model.value.repaymentFrequency), field('还款方式', model.value.repaymentMethod), field('收益计提方式', contract.value.incomeProvisionMethod), field('收益计算标识', contract.value.incomeCalculate), field('开票标识', contract.value.invoicingFlag)])
const riskInfoItems = computed(() => [field('五级分类', contract.value.classificationFive || model.value.fiveClass), field('拨备类型', contract.value.provisionType), field('减值阶段', model.value.impairmentStage), field('特殊标识', contract.value.specialFlag || model.value.specialStatus || '无'), field('业务合同状态', contract.value.contractStatus || model.value.contractStatus), field('财务合同状态', contract.value.financialContractStatus), field('余额事件', `${balanceRows.value.length} 个`), field('凭证记录', `${vouchers.value.length} 张`)])

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await getVehicleLifecycle(route.value.query.id)
    detail.value = data || {}
  } catch (error) {
    loadError.value = error?.message || '合同详情加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
})
</script>

<style lang="scss" scoped>
.contract-detail{min-height:100%;padding:16px;color:#253247;background:#f3f5f8}.load-error{margin-bottom:12px}.contract-header{display:flex;min-height:108px;padding:20px 26px;color:#fff;background:linear-gradient(115deg,#97000d,#cf0717 52%,#e83d49);border-radius:8px;box-shadow:0 8px 22px rgba(157,0,15,.14);align-items:center;justify-content:space-between}.header-kicker{margin-bottom:6px;font-size:12px;opacity:.78;letter-spacing:1.5px}.contract-title-row{display:flex;align-items:center;gap:18px}.contract-title-row h1{margin:0;font-size:26px}.contract-tags{display:flex;gap:7px}.contract-tags :deep(.el-tag){color:#fff;background:rgba(255,255,255,.12);border-color:rgba(255,255,255,.45)}.header-subtitle{display:flex;margin-top:10px;font-size:13px;align-items:center;gap:10px;opacity:.92}.header-subtitle i{width:3px;height:3px;background:#fff;border-radius:50%;opacity:.7}.header-status{display:flex;min-width:300px;padding-left:28px;border-left:1px solid rgba(255,255,255,.28);flex-direction:column}.header-status span,.header-status small{font-size:12px;opacity:.78}.header-status strong{margin:7px 0 5px;font-size:17px}.summary-grid{display:grid;margin:12px 0;grid-template-columns:repeat(5,minmax(0,1fr));gap:10px}.summary-card{position:relative;min-height:86px;padding:15px 17px;overflow:hidden;background:#fff;border:1px solid #e8ebf0;border-radius:7px}.summary-card:before{position:absolute;top:17px;left:0;width:3px;height:24px;background:#c7ccd5;content:''}.summary-card.accent:before{background:#d70817}.summary-card span,.summary-card small{display:block;color:#8993a4;font-size:12px}.summary-card strong{display:block;margin:8px 0 5px;overflow:hidden;font-size:19px;white-space:nowrap;text-overflow:ellipsis}.summary-card.accent strong,.income-value{color:#d70817}.detail-card{border:1px solid #e5e9ef;border-radius:7px}.detail-card :deep(.el-card__body){padding:0 18px 18px}.detail-tabs :deep(.el-tabs__header){margin:0}.detail-tabs :deep(.el-tabs__nav-wrap){padding:0 4px}.detail-tabs :deep(.el-tabs__item){height:52px;padding:0 21px;font-size:14px}.detail-tabs :deep(.el-tabs__item.is-active){color:#d70817;font-weight:600}.detail-tabs :deep(.el-tabs__active-bar){height:3px;background:#d70817}.tab-body{padding-top:4px}.info-section{margin-top:14px}.info-section :deep(.section-heading){margin-bottom:10px;padding-left:9px;color:#1e2c42;border-left:3px solid #d70817;font-size:15px;font-weight:600}.info-section :deep(.el-descriptions__label){width:126px;color:#737f91;background:#f8f9fb!important;font-weight:400}.info-section :deep(.el-descriptions__content){color:#1f2d42;font-weight:500}.structure-banner{display:grid;margin:16px 0 20px;padding:22px 24px;background:#f8f9fb;border:1px solid #e7eaf0;border-radius:7px;grid-template-columns:1fr 36px 1fr 36px 1fr 36px 1fr;align-items:center}.structure-banner div{display:flex;min-height:68px;padding:12px 14px;background:#fff;border:1px solid #e6e9ee;border-radius:6px;flex-direction:column;justify-content:center}.structure-banner span{color:#8690a1;font-size:12px}.structure-banner strong{margin-top:8px;font-size:18px}.structure-banner .highlight{border-color:#efb4ba}.structure-banner .highlight strong{color:#d70817}.structure-banner>b{color:#b7bec9;font-size:22px;text-align:center}.settlement-check{display:grid;margin:16px 0 6px;padding:14px 16px;background:#f8fafc;border:1px solid #dfe4eb;border-radius:7px;grid-template-columns:1.2fr 1fr 1fr auto;gap:12px;align-items:center}.settlement-check-title{display:flex;flex-direction:column}.settlement-check-title span,.settlement-check-item small{margin-top:4px;color:#8791a1;font-size:12px}.settlement-check-item{padding:10px 12px;background:#fff;border-left:3px solid #d70817;border-radius:4px}.settlement-check-item span,.settlement-check-item strong{display:block}.settlement-check-item.passed{border-left-color:#16a34a}.settlement-check-item.failed{border-left-color:#d70817}.table-toolbar{display:flex;min-height:58px;padding:10px 2px;align-items:center;justify-content:space-between}.table-toolbar :deep(strong){display:block;margin-bottom:4px;font-size:15px}.table-toolbar :deep(span){color:#8b95a5;font-size:12px}.table-toolbar :deep(.toolbar-total){color:#d70817;font-size:14px}.balance-detail{padding:12px 28px;background:#f7f9fc}.scene-code{display:block;margin-top:3px;color:#929cab}.positive{color:#15803d}.negative{color:#d70817}@media(max-width:1500px){.summary-grid{grid-template-columns:repeat(3,1fr)}.detail-tabs :deep(.el-tabs__item){padding:0 14px}.settlement-check{grid-template-columns:1fr 1fr}}@media(max-width:1100px){.contract-header{align-items:flex-start;flex-direction:column;gap:18px}.header-status{padding-left:0;border:0}.summary-grid{grid-template-columns:repeat(2,1fr)}.structure-banner{grid-template-columns:1fr;gap:8px}.structure-banner>b{transform:rotate(90deg)}}
</style>
