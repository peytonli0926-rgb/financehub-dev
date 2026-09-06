<template>
    <div>
      <UTable ref="utableRef" :defaultParams="defaultParams"  @search-form-data="refreshList" :autoLoad="false"  :options="options">

        <template #operationBtn="{row}">
          <el-button size="small" type="primary" @click="turnToPage(row,'account')" link>本期明细</el-button>
          <el-button size="small" type="primary"  @click="turnToPage(row,'contract')" link >本期合同明细</el-button>
        </template>
      </UTable>

    </div>
  </template>

<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import moment from 'moment'
import { useStore } from 'vuex'
const defaultParams = ref({
  periodCode: [parseInt(moment().format('YYYYMM')), parseInt(moment().format('YYYYMM'))],
  searchType: 'query'
})
const { router } = useRouter()
const store = useStore()
const utableRef = ref()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const turnToPage = ({ contractCode, accountCode, orgId }, targetType) => {
  const { periodCode } = defaultParams.value
  const [periodCodeStart, periodCodeEnd] = periodCode || []
  router.push(`/searchBusiness/currentAmount?periodCodeStart=${periodCodeStart}&periodCodeEnd=${periodCodeEnd}&contractCode=${contractCode || ''}&accountCode=${accountCode || ''}&orgId=${orgId || ''}&targetType=${targetType}`)
}
// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
</script>

  <style lang="scss" scoped></style>
