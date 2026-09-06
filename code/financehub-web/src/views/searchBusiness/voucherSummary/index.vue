<template>
  <div>
    <UTable
      ref="utableRef"
      :get-summaries="getSummaries"
      :autoLoad="false"
      :default-params="defaultParams"
      :options="options"
      @search-form-data="refreshList"
    >
    </UTable>
  </div>
</template>

<script setup>
import { ref, onBeforeMount, onMounted } from 'vue'
import { optionsConfig } from './config'
import { useStore } from 'vuex'
import { getTableSummaries } from '@/utils'
import moment from 'moment'
import { useRouter, useRoute } from '@toystory/lotso'

const store = useStore()

const { router } = useRouter()
const utableRef = ref()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = ref(optionsConfig(router, dictData))

const route = useRoute()
const { query } = route.value

const getSummaries = (param) => {
  return getTableSummaries(['debitAmount', 'creditAmount'], param)
}

// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
onBeforeMount(() => {
  const {
    batchType,
    voucherIdList: list,
    batchId,
    periodCode,
    easVoucherId,
    interfaceDataId
  } = query
  const voucherIdList = (list && list.split(',')) || undefined
  if (voucherIdList || batchType || batchId || easVoucherId || periodCode || interfaceDataId) {
    defaultParams.value = {
      interfaceDataId,
      voucherIdList,
      batchType,
      batchId,
      periodCode: (periodCode && parseInt(periodCode)) || undefined,
      easVoucherId
    }
  } else {
    const periodCode = parseInt(moment().format('YYYYMM'))
    defaultParams.value = { periodCode }
  }
})
onMounted(() => {
  const isParamsArr = Object.keys(query)
  if (isParamsArr.length > 0) {
    refreshList()
  }
})
</script>

<style lang="scss" scoped></style>
