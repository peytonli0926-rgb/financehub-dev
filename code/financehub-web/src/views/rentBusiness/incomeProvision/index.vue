<template>
  <UTable ref="utableRef" :options="options">
    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        v-permission="permission.viewVoucher"
        link
        size="small"
        :disabled="row.isGenerateVoucher === '0'"
        @click="fromToRulePage(row, 'view')"
        >查看凭证</el-button
      >
      <el-button
        type="primary"
        v-permission="permission.view"
        link
        size="small"
        @click="fromToRulePage(row, 'detail')"
        >查看详情</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const fromToRulePage = ({ id, batchType }, type) => {
  const url = `/incomeProvisionAccess/incomeProvisionDetail?leaseIncomeId=${id}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id }, 'total')
    return
  }
  router.push(url)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.incomeProvision
})
</script>
