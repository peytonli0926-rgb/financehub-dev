<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-14 20:35:36
 * @LastEditTime: 2024-06-04 21:55:08
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <UTable ref="utableRef"  :get-summaries="getSummaries" :autoLoad="false" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" :disabled="!row.voucherIds"
        @click="fromToRulePage(row, 'view')">查看凭证</el-button>
    </template>

    <template #operateHeaderRight>
      <el-button @click="handleMeasurement" v-permission="permission.recallValidate" type="success">校验</el-button>
      <!-- <el-button @click="exportExcel" v-permission="permission.recallExport" type="primary">导出</el-button> -->
      <el-button @click="importExcel" v-permission="permission.shimport" type="">上传</el-button>
    </template>
  </UTable>

</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigRansom } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useExport, useVoucherPage } from '@/hooks'
import { confirmEl } from '@/utils'
import {
  checkPageListOutABSRansom
} from '@/api/assetTransferBusiness/listOutABS'
import { ElMessage } from 'element-plus'
import { getTableSummaries } from '@/utils'

const { setVoucherPage } = useVoucherPage()
const { handleExport, commonUploadDialog } = useExport()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigRansom(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({})

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.listOutABSDetail
})

const getSummaries = (param) => {
  return getTableSummaries(['redeemPrice','receivableRentBalance', 'principalBalance','receivableResidualValueBalance','lesseeMarginBalance'], param)
}
// 上传
const importExcel = () => {
  commonUploadDialog({
    url: '/engine/finance/asset-abs-redeem/detailImport',
    title: '出表ABS-赎回',
    open: true,
    data: {
      assetAbsRedeemId: defaultParams.value.assetAbsRedeemId
    },
    templateUrl: ''
  })
}
// 导出
// const exportExcel = () => {
//   handleExport({
//     url: `/engine/finance/asset-abs-redeem/detailExport/${defaultParams.value.assetAbsRedeemId}`,
//     method: 'get'
//   }, '出表ABS-赎回.xlsx')
// }
const handleMeasurement = async () => {
  confirmEl('您确定进行校验操作吗？').then(async () => {
    const { assetAbsRedeemId } = defaultParams.value
    const { code } = await checkPageListOutABSRansom({
      id: assetAbsRedeemId
    })
    if (code === 200) {
      ElMessage.success('操作成功')
    }
  })
}
const fromToRulePage = ({ voucherIds }) => {
  setVoucherPage({ voucherIdList: voucherIds })
}
onMounted(() => {
  const { assetAbsRedeemId } = query
  defaultParams.value = { assetAbsRedeemId }
  utableRef.value.requestBefore()
})

</script>
