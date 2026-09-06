<template>
  <UTable ref="utableRef" :options="options" :defaultParams="defaultParams">
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
        :disabled="!row.voucherId"
        v-permission="permission.viewVoucher"
        @click="fromToRulePage(row, 'view')"
        >查看凭证</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTransfer } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import moment from 'moment'

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
const fromToRulePage = ({ id, voucherId }, type) => {
  const url = `/rentBusiness/TaRearrangeDetail?id=${id}`
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId }, 'total')
    return
  }
  router.push(url)
}

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    reclassificationMonth: moment().format('YYYY-MM'),
    id
  }
})
</script>
