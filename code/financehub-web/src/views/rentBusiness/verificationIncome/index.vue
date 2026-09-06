<template>
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :options="options"
      @selection-change="selectionChange"
      @search-form-data="searchFormData"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          v-permission="permission.view"
          link
          size="small"
          @click="fromToRulePage(row)"
          >查看详情</el-button
        >
      </template>

      <!-- <template #operateHeaderRight>
        <el-button type="primary" v-permission="permission.export" :icon="Download" @click="exportExcel" :disabled="isBoolReactive.exportDisabled">下载</el-button>
      </template> -->
    </UTable>
  </div>
</template>
<script setup>
import { ref, reactive, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'
import Qs from 'qs'
// import { useExport } from '@/hooks'
// import { Download } from '@element-plus/icons-vue'

// const { handleExport } = useExport()
const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
const formValue = ref({})
const isBoolReactive = reactive({
  exportDisabled: true
})
const selectedData = ref([])

const defaultParams = {
  businessDate: [moment().format('YYYY-MM'), moment().format('YYYY-MM')]
}
const searchFormData = (formData) => {
  formValue.value = formData
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.verificationIncome
})
// 多选数据
const selectionChange = (data) => {
  const exportStatus = data.length !== 0
  isBoolReactive.exportDisabled = !exportStatus
  selectedData.value = data
}
// 导出
// const exportExcel = () => {
//   handleExport({
//     url: '/engine/verification/payback/export',
//     params: selectedData.value
//   }, '核销回款.xlsx')
// }
// 跳转
const fromToRulePage = ({ reversalProvisionAmount, revenueFinance, ...result }) => {
  router.push(`/rentBusiness/verificationIncomeDetail?${Qs.stringify(result)}`)
}
</script>
