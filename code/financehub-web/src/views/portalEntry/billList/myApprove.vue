<template>
  <UTable ref="utableRef" :options="options" @search-form-data="searchFormData">
    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.view"
        @click="fromToRulePage(row, 'detail')"
        >查看详情</el-button
      >
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.viewVoucher"
        @click="fromToRulePage(row, 'view')"
        >查看凭证</el-button
      >
      <el-button
        v-if="row.documentStatus === '3'"
        type="danger"
        link
        size="small"
        @click="returnDocument(row)"
        >退回</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { myApproveOptionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import { portalApproveReturn } from '@/api/portalEntry'
import { confirmEl } from '@/utils'
import { ElMessage } from 'element-plus'

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
const options = myApproveOptionsConfig(router, dictData)
const formValue = ref({})

const searchFormData = (formData) => {
  formValue.value = formData
}

const fromToRulePage = ({ documentId, documentType, url }, type) => {
  if (type === 'view') {
    const isTotal = ['ZJZR', 'ZJZRDB', 'DSFZR', 'ZRFY'].includes(documentType)
    setVoucherPage({ batchId: documentId, batchType: documentType }, isTotal ? 'total' : null)
    return
  }
  const detailUrl = documentType === 'SYJT'
    ? `/measurementEngine/incomeProvisionDetail?leaseIncomeId=${documentId}`
    : url
  router.push(detailUrl)
}

const returnDocument = ({ id }) => {
  confirmEl('确定将该已审批单据退回吗？').then(async () => {
    const { code } = await portalApproveReturn([id])
    if (code === 200) {
      ElMessage.success('单据已退回')
      utableRef.value.requestBefore()
    }
  })
}
</script>
