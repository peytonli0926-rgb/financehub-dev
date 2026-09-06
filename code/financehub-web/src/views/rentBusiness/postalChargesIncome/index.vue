<template>
  <UTable ref="utableRef" @selection-change="selectionChange" :options="options" @search-form-data="searchFormData">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" v-permission="permission.viewVoucher" :disabled="row.isGenerateVoucher==='0'" @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      <el-button type="primary" link size="small" v-permission="permission.view" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
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

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.postalChargesIncome
})
const fromToRulePage = ({ batchType, id, accountDate }, type) => {
  const url = `/rentBusiness/postalChargesIncomeDetail?postalStorageFeeId=${id}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id })
    return
  }
  router.push(url)
}
</script>
