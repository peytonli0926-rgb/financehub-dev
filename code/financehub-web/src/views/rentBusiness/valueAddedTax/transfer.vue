<template>
  <UTable ref="utableRef" :options="options" :defaultParams="defaultParams">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small"
        @click="fromToRulePage(row, 'view')" v-permission="permission.viewVoucher" :disabled="!row.voucherId">查看凭证</el-button>
      <el-button type="primary" link size="small" v-permission="permission.view" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTransfer } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()
const utableRef = ref()
const store = useStore()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTransfer(router, dictData)
defineProps({
  permission: {
    type: Object,
    default: () => {
      return {}
    }
  }
})
const fromToRulePage = ({ contractId, voucherId }, type) => {
  const url = `/rentBusiness/valueAddedTaxDetail?id=${contractId}`
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId })
    return
  }
  router.push(url)
}

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
