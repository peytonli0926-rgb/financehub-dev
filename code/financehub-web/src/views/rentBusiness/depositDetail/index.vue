<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          v-permission="permission.viewVoucher"
          link
          size="small"
          @click="fromToRulePage(row)"
          :disabled="!row.voucherId"
          >查看凭证</el-button
        >
      </template>

      <!-- <template #operateHeaderRight>
        <el-button @click="exportExcel"  v-permission="permission.export" type="primary">导出</el-button>
      </template> -->
    </UTable>
  </div>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

// const { handleExport } = useExport()
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({
  // balanceDate: '2023-09-25'
})

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.depositDetail
})
const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: [voucherId] })
}
// const exportExcel = () => {
//   const { batchId, marginType } = query
//   const label = marginType === '1' ? '重分类' : '利息计提'
//   handleExport(
//     {
//       url: '/engine/finance/margin-contract-balance/export',
//       params: {
//         batchId,
//         marginType
//       }
//     },
//     `保证金${label}详情.xlsx`
//   )
// }
onActivated(() => {
  const { batchId, marginType } = query
  defaultParams.value = { batchId, marginType }
  utableRef.value.requestBefore()
})
</script>
