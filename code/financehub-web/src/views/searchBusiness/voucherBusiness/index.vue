<template>
  <div class="voucher-query-page">
    <div class="page-overview">
      <div class="overview-title">
        <span class="overview-icon">
          <el-icon><Tickets /></el-icon>
        </span>
        <div>
          <div class="overview-name">凭证查询</div>
          <div class="overview-description">集中查看凭证分录、借贷金额、辅助核算信息及处理进度</div>
        </div>
      </div>
      <div class="status-flow" aria-label="凭证处理流程">
        <span class="flow-node active">已录入</span><i></i>
        <span class="flow-node">已提交</span><i></i>
        <span class="flow-node">待传送金蝶</span><i></i>
        <span class="flow-node">已传至金蝶</span>
      </div>
    </div>
    <UTable
      ref="utableRef"
      @icon-events="iconEvents"
      :get-summaries="getSummaries"
      :autoLoad="false"
      :default-params="defaultParams"
      :options="options"
      @search-form-data="refreshList"
    >
      <template #operationBtn="{ row }">
        <!-- {{ permission }} -->
        <el-link type="primary" :href="decodeURIComponent(row.easLoginUrl)" v-permission="permission.viewJD" target="_top" size="small" v-if="row.easLoginUrl">查看金蝶凭证</el-link>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { ref, onBeforeMount, onMounted, nextTick, markRaw, computed } from 'vue'
import { optionsConfig } from './config'
import { useStore } from 'vuex'
import { getTableSummaries } from '@/utils'
import moment from 'moment'
import { useRouter, useRoute } from '@toystory/lotso'
import { DArrowRight, DArrowLeft, Tickets } from '@element-plus/icons-vue'

const store = useStore()

const { router } = useRouter()
const utableRef = ref()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = ref(optionsConfig(router, dictData))

const route = useRoute()
const { query } = route.value

const iconEvents = (op) => {
  op.icon = op.arrow === 'left' ? markRaw(DArrowLeft) : markRaw(DArrowRight)
  op.arrow = op.arrow === 'left' ? 'right' : 'left'
  const { columns, ...result } = options.value
  const nColumn = columns.map((item) => {
    if (op.hideColumns.includes(item.prop)) {
      item.hide = !item.hide
      item.search = !item.search
    }
    return item
  })
  nextTick(() => {
    options.value = { columns: nColumn, ...result }
  })
}

const getSummaries = (param) => {
  return getTableSummaries(['debitAmount', 'creditAmount'], param)
}

// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
onBeforeMount(() => {
  const {
    batchType,
    voucherIdList: list,
    batchId,
    periodCode,
    easVoucherId,
    interfaceDataId
  } = query
  const voucherIdList = (list && list.split(',')) || undefined
  if (voucherIdList || batchType || batchId || easVoucherId || periodCode || interfaceDataId) {
    defaultParams.value = {
      interfaceDataId,
      voucherIdList,
      batchType,
      batchId,
      periodCode: (periodCode && parseInt(periodCode)) || undefined,
      easVoucherId
    }
    utableRef.value.requestBefore()
  } else {
    const periodCode = parseInt(moment().format('YYYYMM'))
    defaultParams.value = { periodCode }
  }
})

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.voucherBusiness
})

onMounted(() => {
  const isParamsArr = Object.keys(query)
  if (isParamsArr.length > 0) {
    refreshList()
  }
})
</script>

<style lang="scss" scoped>
.voucher-query-page {
  --hx-red: #d6081b;
  padding: 0 2px 12px;
}

.page-overview {
  min-height: 58px;
  padding: 11px 18px;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  background: linear-gradient(100deg, #fff 0%, #fff 62%, #fff3f4 100%);
  border: 1px solid #f0d9dc;
  border-radius: 10px;
  box-shadow: 0 3px 12px rgba(80, 18, 25, 0.05);
}

.overview-title {
  display: flex;
  align-items: center;
  min-width: 0;
}

.overview-icon {
  width: 36px;
  height: 36px;
  margin-right: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: none;
  color: #fff;
  font-size: 20px;
  background: linear-gradient(145deg, #ed1b2f, #bd0616);
  border-radius: 9px;
  box-shadow: 0 5px 12px rgba(214, 8, 27, 0.2);
}

.overview-name {
  color: #20232a;
  font-size: 17px;
  font-weight: 600;
  line-height: 24px;
}

.overview-description {
  color: #8a9099;
  font-size: 12px;
  line-height: 18px;
  white-space: nowrap;
}

.status-flow {
  display: flex;
  align-items: center;
  flex: none;
}

.status-flow i {
  width: 24px;
  height: 1px;
  margin: 0 5px;
  background: #e7cdd0;
}

.flow-node {
  padding: 4px 10px;
  color: #7d838c;
  font-size: 12px;
  line-height: 18px;
  background: rgba(255, 255, 255, 0.85);
  border: 1px solid #eadfe0;
  border-radius: 12px;
}

.flow-node.active {
  color: var(--hx-red);
  font-weight: 600;
  background: #fff;
  border-color: #f0aeb5;
}

:deep(.el-card) {
  border-color: #ebeef2;
  border-radius: 10px;
  box-shadow: 0 3px 12px rgba(31, 35, 41, 0.045);
}

:deep(.el-form-item__label) {
  color: #3f454d;
  font-weight: 500;
}

:deep(.el-input__wrapper),
:deep(.el-select .el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-table) {
  border-radius: 7px;
  overflow: hidden;
}

:deep(.el-table th.el-table__cell) {
  height: 46px;
  color: #4d535c;
  font-weight: 600;
  background: #fff7f7 !important;
  border-bottom-color: #efdadd;
}

:deep(.el-table td.el-table__cell) {
  height: 48px;
  color: #343942;
}

:deep(.el-table__body tr:hover > td.el-table__cell) {
  background: #fff8f8 !important;
}

:deep(.el-table__footer-wrapper td.el-table__cell) {
  color: #242931;
  font-weight: 600;
  background: #faf7f7;
}

:deep(.voucher-status-tag) {
  min-width: 64px;
  justify-content: center;
  font-weight: 500;
  border-radius: 12px;
}

@media (max-width: 1250px) {
  .status-flow {
    display: none;
  }
}
</style>
