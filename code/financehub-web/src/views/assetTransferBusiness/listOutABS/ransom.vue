<template>
  <UTable ref="utableRef"  :options="options" >
    <template #operationBtn="{ row }">
      <el-button type="primary" v-permission="permission.viewVoucher" link size="small" :disabled="row.isGenerateVoucher === '0'"
        @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigRansom } from './config'
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
const options = optionsConfigRansom(router, dictData)

const fromToRulePage = ({ id, batchType, periodCode }, type) => {
  const url = `/assetTransferBusiness/listOutABSDetail?assetAbsRedeemId=${id}&type=ransom`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id, periodCode },"total")
    return
  }
  router.push(url)
}


</script>
