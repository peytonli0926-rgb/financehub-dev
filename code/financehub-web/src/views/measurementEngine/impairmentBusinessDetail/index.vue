<template>
  <div>
    <UTable
      ref="utableRef"
      :autoLoad="false"
      :defaultParams="defaultParams"
      :options="options"
      @search-form-data="searchFormData"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          link
          size="small"
          :disabled="!row.voucherId"
          @click="fromToRulePage(row)"
          >查看凭证</el-button
        >
      </template>
      <template #operateHeaderLeft>
        <div class="u-flex">
          <span
            ><el-statistic
              :precision="2"
              title="风险敞口合计"
              :value-style="{ 'font-size': '16px', 'font-weight': 'bold' }"
              :value="totalResult.riskExposureSummary || 0"
          /></span>
          <span class="u-m-l-20 u-m-r-20"
            ><el-statistic
              :precision="2"
              :value-style="{ 'font-size': '16px', 'font-weight': 'bold' }"
              title="上月余额合计"
              :value="totalResult.lastMonthBalanceSummary || 0"
          /></span>
          <span class="u-m-l-20 u-m-r-20">
            <el-statistic
              :precision="2"
              :value-style="{ 'font-size': '16px', 'font-weight': 'bold' }"
              title="本月余额合计"
              :value="totalResult.provisionTotalSummary || 0"
          /></span>
          <span
            ><el-statistic
              :precision="2"
              :value-style="{ 'font-size': '16px', 'font-weight': 'bold' }"
              title="本月计提合计"
              :value="totalResult.thisMonthProvisionSummary || 0"
          /></span>
        </div>
      </template>
    </UTable>
  </div>
</template>
<script setup>
import { ref, onMounted, onBeforeMount } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'
import { summaryImpairmentProvision } from '@/api/impairmentBusiness/impairmentBusiness'
import { ElMessage } from 'element-plus'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
const formValue = ref({})
const route = useRoute()
const { query } = route.value
const totalResult = ref({})
const defaultParams = ref({})
const { setVoucherPage } = useVoucherPage()
const searchFormData = (formData) => {
  formValue.value = formData
  refreshList(formData)
}

// 汇总数据
const summaryImpairmentProvisionTotal = async (param) => {
  try {
    const { data } = await summaryImpairmentProvision({ ...defaultParams.value, ...param })
    totalResult.value = data || {}
  } catch (error) {
    ElMessage.error(error)
  }
}

// 刷新列表
const refreshList = async (val = {}) => {
  utableRef.value.requestBefore(val)
  summaryImpairmentProvisionTotal(val)
}

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId })
}
onBeforeMount(() => {
  const { impairmentProvisionId } = query
  defaultParams.value = { impairmentProvisionId }
})
onMounted(() => {
  refreshList()
})
</script>
