<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-21 15:46:44
 * @LastEditTime: 2024-06-06 21:37:22
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <UTable ref="utableRef" :options="options" >
    <template #operationBtn="{ row }">
      <el-button type="primary" v-permission="permission.viewVoucher" link size="small" :disabled="row.isGenerateVoucher === '0'"
        @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      <el-button type="primary" v-permission="permission.view"  link size="small" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
    </template>

  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTransferPay } from './config'
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
const options = optionsConfigTransferPay(router, dictData)

const fromToRulePage = ({ id, batchType, periodCode }, type) => {
  const url = `/assetTransferBusiness/listOutABSDetail?assetAbsTransferPaymentId=${id}&type=transfersPay`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id, periodCode },"total")
    return
  }
  router.push(url)
}


</script>
