<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
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
import { optionsConfigRansom } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import moment from 'moment'
import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => {
      return {}
    }
  }
})
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const defaultParams = ref({})
const route = useRoute()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigRansom(router, dictData)
const { setVoucherPage } = useVoucherPage()

const fromToRulePage = ({ id, voucherId }, type) => {
  const url = `/rentBusiness/TaRearrangeOther?id=${id}`
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId }, 'total')
    return
  }
  router.push(url)
}
onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id,
    reclassificationMonth: moment().format('YYYY-MM')
  }
})
</script>
