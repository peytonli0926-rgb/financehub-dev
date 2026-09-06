<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams"  @search-form-data="refreshList" :autoLoad="false" :options="options">
      <template #contractCode="{ row }">
        <div  @click="turnToPage(row,'code')">
          <el-link type="primary">
            {{ row.contractCode  }}
          </el-link>
        </div>
      </template>
      <template #monthDebitAmount="{ row }">
        <div style="text-align: right;" @click="turnToPage(row)">
          <el-link type="primary">
            <component :is="toThousands(row.monthDebitAmount)"></component>
          </el-link>
        </div>
      </template>
      <template #monthCreditAmount="{ row }">
        <div style="text-align: right;" @click="turnToPage(row)">
          <el-link type="primary">
            <component :is="toThousands(row.monthCreditAmount)"></component>
          </el-link>
        </div>
      </template>
    </UTable>

  </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { toThousands } from '@/utils'
import moment from 'moment'
import { ElMessage } from 'element-plus'

const defaultParams = ref({})

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const turnToPage = ({ contractCode, contractId, accountCode, orgId }, type) => {
  const { periodCode } = defaultParams.value
  if (type === 'code') {
    if (!contractId) {
      ElMessage.error('合同编号不存在，请检查数据!')
      return
    }
    router.push({
      name: 'contractBusinessDetail',
      query: {
        id: contractId
      }
    })
    return
  }
  router.push(`/searchBusiness/currentAmount?periodCodeStart=${periodCode[0] || ''}&periodCodeEnd=${periodCode[1] || ''}&contractCode=${contractCode || ''}&accountCode=${accountCode || ''}&orgId=${orgId || ''}&targetType=assist`)
}
onBeforeMount(() => {
  defaultParams.value = {
    periodCode: [parseInt(moment().format('YYYYMM')), parseInt(moment().format('YYYYMM'))],
    searchType: 'query'
  }
})
// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
</script>

<style lang="scss" scoped></style>
