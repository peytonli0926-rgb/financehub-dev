<template>
    <div class="income-detail-page">
        <section class="detail-overview">
            <div class="overview-main">
                <span class="overview-icon"><el-icon><DataLine /></el-icon></span>
                <div>
                    <h2>单月收益计提明细</h2>
                    <p>按合同查看本月收益计提结果，并追溯回收计划与生成凭证</p>
                </div>
            </div>
            <div class="overview-flow" aria-label="收益计提数据链路">
                <span><el-icon><List /></el-icon>回收计划</span>
                <i></i>
                <span><el-icon><DataLine /></el-icon>收益计提</span>
                <i></i>
                <span><el-icon><Tickets /></el-icon>会计凭证</span>
            </div>
        </section>

        <div class="detail-table">
          <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"
            @search-form-data="searchFormData">
            <template #operateHeaderLeft>
                <div class="table-heading">
                    <span class="heading-mark"></span>
                    <strong>计提结果</strong>
                    <span>关键合同信息及计提处理结果</span>
                </div>
            </template>
            <template #operationBtn="{ row }">
                <div class="row-actions">
                    <el-button type="primary" v-permission="permission.viewVoucher" plain size="small"
                        @click="viewVoucher(row)" :disabled="!row.voucherId">查看凭证</el-button>
                    <el-button type="primary" v-permission="permission.view" link size="small"
                        @click="viewRepaymentPlan(row)">查看详情</el-button>
                </div>
            </template>
          </UTable>
        </div>

        <el-dialog v-model="planVisible" width="96%" top="3vh" class="repayment-plan-dialog"
            destroy-on-close append-to-body>
            <template #header>
                <div class="plan-dialog-header">
                    <div class="plan-title">回收计划明细</div>
                    <div class="plan-contract">{{ selectedContractCode }}</div>
                </div>
            </template>
            <div class="plan-toolbar">
                <div class="plan-tip">
                    <span class="tip-icon">i</span>
                    <span>业务系统回收计划与 XIRR 实际利率法收益测算结果</span>
                </div>
                <el-radio-group v-model="planView" size="small">
                    <el-radio-button label="core">核心计划</el-radio-button>
                    <el-radio-button label="cashFlow">现金流构成</el-radio-button>
                </el-radio-group>
            </div>
            <div class="plan-legend">
                <span><i class="legend-dot outflow"></i>起租资金流出</span>
                <span><i class="legend-dot payment"></i>合同回款节点</span>
                <span><i class="legend-dot accrual"></i>月末收益计提节点</span>
            </div>
            <UTable v-if="planVisible" :key="planView" :defaultParams="planParams" :options="planOptions" />
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, computed, watch } from 'vue'
import { DataLine, List, Tickets } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { optionsConfig as planOptionsConfig } from '../incomeProvisionPlan/config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const planView = ref('core')
const planOptions = computed(() => planOptionsConfig(router, dictData, planView.value))
const route = useRoute()
const { setVoucherPage } = useVoucherPage()
const defaultParams = computed(() => ({
  leaseIncomeId: route.value.query?.leaseIncomeId
}))
const planVisible = ref(false)
const selectedContractCode = ref('')
const planParams = computed(() => ({ contractCode: selectedContractCode.value }))

const searchFormData = () => {}

const viewVoucher = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: [voucherId] })
}

const viewRepaymentPlan = ({ contractCode }) => {
  selectedContractCode.value = contractCode
  planVisible.value = true
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.incomeProvisionDetail
})

watch(
  () => route.value.query?.leaseIncomeId,
  (leaseIncomeId, previousId) => {
    if (leaseIncomeId && previousId && leaseIncomeId !== previousId) {
      utableRef.value?.requestBefore({ leaseIncomeId })
    }
  }
)

</script>

<style lang="scss" scoped>
.income-detail-page {
  --hx-red: #d6081b;
  --hx-red-dark: #a90716;
  padding: 0 2px 14px;
}

.detail-overview {
  min-height: 72px;
  padding: 13px 20px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  overflow: hidden;
  background:
    radial-gradient(circle at 86% 40%, rgba(214, 8, 27, 0.09), transparent 24%),
    linear-gradient(105deg, #fff 0%, #fff 64%, #fff5f5 100%);
  border: 1px solid #f0dadd;
  border-radius: 10px;
  box-shadow: 0 3px 12px rgba(76, 17, 24, 0.05);
}

.overview-main {
  display: flex;
  align-items: center;
  min-width: 0;
}

.overview-icon {
  width: 42px;
  height: 42px;
  margin-right: 13px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: none;
  color: #fff;
  font-size: 23px;
  background: linear-gradient(145deg, #ed1b2f, var(--hx-red-dark));
  border-radius: 10px;
  box-shadow: 0 6px 15px rgba(214, 8, 27, 0.2);
}

.overview-main h2 {
  margin: 0;
  color: #20232a;
  font-size: 18px;
  font-weight: 600;
  line-height: 26px;
}

.overview-main p {
  margin: 1px 0 0;
  color: #858b94;
  font-size: 12px;
  line-height: 18px;
  white-space: nowrap;
}

.overview-flow {
  display: flex;
  align-items: center;
  flex: none;
}

.overview-flow span {
  padding: 6px 11px;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: #656b74;
  font-size: 12px;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #ecdfe1;
  border-radius: 15px;
}

.overview-flow span:nth-of-type(2) {
  color: var(--hx-red);
  font-weight: 600;
  border-color: #f0aeb5;
}

.overview-flow i {
  width: 22px;
  height: 1px;
  margin: 0 5px;
  background: #e5c9cc;
}

.table-heading {
  display: flex;
  align-items: center;
  gap: 8px;
  min-height: 30px;
}

.heading-mark {
  width: 3px;
  height: 16px;
  background: var(--hx-red);
  border-radius: 2px;
}

.table-heading strong {
  color: #272b32;
  font-size: 15px;
  font-weight: 600;
}

.table-heading > span:last-child {
  color: #969ca5;
  font-size: 12px;
}

.row-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  white-space: nowrap;
}

.row-actions :deep(.el-button + .el-button) {
  margin-left: 0;
}

.detail-table :deep(.el-card) {
  border-color: #e8ebf0;
  border-radius: 10px;
  box-shadow: 0 3px 12px rgba(31, 35, 41, 0.045);
}

.detail-table :deep(.el-form-item__label) {
  color: #454b54;
  font-weight: 500;
}

.detail-table :deep(.el-input__wrapper),
.detail-table :deep(.el-select .el-input__wrapper) {
  border-radius: 6px;
}

.detail-table :deep(.el-table) {
  border-radius: 7px;
  overflow: hidden;
}

.detail-table :deep(.el-table th.el-table__cell) {
  height: 44px;
  color: #4d535c;
  font-weight: 600;
  background: #fff7f7 !important;
  border-bottom-color: #efdadd;
}

.detail-table :deep(.el-table td.el-table__cell) {
  height: 50px;
  color: #343942;
}

.detail-table :deep(.el-table__body tr:hover > td.el-table__cell) {
  background: #fff8f8 !important;
}

.detail-table :deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: var(--hx-red);
}

.plan-dialog-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.plan-title {
  color: #252932;
  font-size: 18px;
  font-weight: 600;
}

.plan-contract {
  color: #d6081b;
  font-size: 13px;
  font-weight: 500;
}

.plan-toolbar {
  min-height: 48px;
  padding: 0 14px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(90deg, #fafafa, #fff7f7);
  border: 1px solid #eee3e4;
  border-radius: 8px;
}

.plan-tip {
  display: flex;
  align-items: center;
  color: #747b85;
  font-size: 13px;
}

.tip-icon {
  width: 18px;
  height: 18px;
  margin-right: 8px;
  color: #fff;
  font-size: 12px;
  line-height: 18px;
  text-align: center;
  background: #9da3ab;
  border-radius: 50%;
}

.plan-legend {
  padding: 10px 4px 0;
  display: flex;
  align-items: center;
  gap: 22px;
  color: #858b94;
  font-size: 12px;
}

.legend-dot {
  width: 7px;
  height: 7px;
  margin-right: 6px;
  display: inline-block;
  border-radius: 50%;
}

.legend-dot.outflow { background: #d6081b; }
.legend-dot.payment { background: #3b82f6; }
.legend-dot.accrual { background: #e7a23b; }

:deep(.repayment-plan-dialog) {
  border-radius: 12px;
  overflow: hidden;
}

:deep(.repayment-plan-dialog .el-dialog__header) {
  padding: 16px 20px;
  margin-right: 0;
  border-bottom: 1px solid #eceef2;
}

:deep(.repayment-plan-dialog .el-dialog__body) {
  padding: 14px 18px 18px;
}

:deep(.repayment-plan-dialog .el-card) {
  border-color: #ebeef2;
  border-radius: 8px;
  box-shadow: none;
}

:deep(.repayment-plan-dialog .el-table th.el-table__cell) {
  color: #4f5660;
  font-weight: 600;
  background: #fff7f7 !important;
}

:deep(.repayment-plan-dialog .el-table td.el-table__cell) {
  height: 48px;
}

:deep(.repayment-plan-dialog .el-table__body tr:hover > td.el-table__cell) {
  background: #fff8f8 !important;
}

:deep(.plan-toolbar .el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #d6081b;
  border-color: #d6081b;
  box-shadow: -1px 0 0 0 #d6081b;
}

@media (max-width: 1280px) {
  .overview-flow {
    display: none;
  }
}
</style>
