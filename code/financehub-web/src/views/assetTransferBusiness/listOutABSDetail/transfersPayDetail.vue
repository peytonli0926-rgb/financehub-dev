<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-14 20:35:36
 * @LastEditTime: 2024-06-04 21:52:12
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <UTable ref="utableRef" :get-summaries="getSummaries" :autoLoad="false" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" :disabled="!row.voucherIds"
        @click="fromToRulePage(row, 'view')" v-permission="permission.viewVoucher">查看凭证</el-button>
    </template>

  </UTable>

</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTransfersPay } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import { getTableSummaries } from '@/utils'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTransfersPay(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({})
defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const getSummaries = (param) => {
  return getTableSummaries(['actualPrincipalAmount', 'actualInterestAmount','actualRetentionPurchaseAmount','actualPenaltyInterestAmount'], param)
}

const fromToRulePage = ({ voucherIds }) => {
  setVoucherPage({ voucherIdList: voucherIds })
}
onMounted(() => {
  const { assetAbsTransferPaymentId } = query
  defaultParams.value = { assetAbsTransferPaymentId }
  utableRef.value.requestBefore()
})

</script>
