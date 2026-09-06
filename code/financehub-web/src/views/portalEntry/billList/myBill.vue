<template>
  <UTable ref="utableRef" :options="options">
    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        link
        size="small"
        :disabled="!row.url && row.documentType !== 'SYJT'"
        @click="fromToRulePage(row, 'detail')"
        v-permission="permission.view"
        >查看详情</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { myBillOptionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = myBillOptionsConfig(router, dictData)

const fromToRulePage = ({ documentId, documentType, url }, type) => {
  if (type === 'view') {
    setVoucherPage({ batchId: documentId, batchType: documentType })
    return
  }
  const detailUrl = documentType === 'SYJT'
    ? `/measurementEngine/incomeProvisionDetail?leaseIncomeId=${documentId}`
    : url
  router.push(detailUrl)
}
</script>
