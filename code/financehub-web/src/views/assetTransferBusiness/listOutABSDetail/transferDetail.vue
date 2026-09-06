<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-14 20:35:36
 * @LastEditTime: 2024-06-04 21:48:40
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <div>
    <UTable ref="utableRef" :get-summaries="getSummaries" :autoLoad="false" :defaultParams="defaultParams" :options="options">
      <template #operateHeaderRight>
        <el-button @click="handleMeasurement" v-permission="permission.tranValidate" type="success">校验</el-button>
      </template>
    </UTable>
    <UDialog v-if="dialogVisible" @update:visible="dialogVisible = $event" title="科目校验" dialog-width="90%" :is-footer="false"
      :visible="dialogVisible">
      <UTable ref="utableDialogRef" :defaultParams="defaultParamsd"  :options="optionsDialog" :autoLoad="false"/>
    </UDialog>
  </div>
</template> 
<script setup>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { getTableSummaries } from '@/utils'
import { optionsConfig, optionsConfigDialog } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { confirmEl } from '@/utils'

const { router } = useRouter()
const utableRef = ref()
const defaultParamsd = ref({})
const utableDialogRef = ref()
const dialogVisible = ref(false)
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']

const options = optionsConfig(router, dictData)
const optionsDialog = optionsConfigDialog(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({})


const getSummaries = (param) => {
  return getTableSummaries(['receivableRent', 'receivableResidualValue','receivableOuttax','unrealizedRevenue','lesseeMargin','depreciationReservesBalance','leaseRevenueBalance','receivableUnconfirmReceiptAmount','receivableOuttaxDebtRestructureAmount','financeLeaseReceivablesAmount','transferPrice','transferLossPrice'], param)
}

//  校验
const handleMeasurement = async () => {
  confirmEl('您确定进行校验操作吗？').then(async () => {
    const { outTableAbsId } = defaultParams.value
    defaultParamsd.value = {
      id: outTableAbsId
    }
    dialogVisible.value = true
    setTimeout(() => {
      utableDialogRef.value.requestBefore()
    }, 300)
  })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.listOutABSDetail
})
onMounted(() => {
  const { outTableAbsId } = query
  defaultParams.value = { outTableAbsId }
  utableRef.value.requestBefore()
})

</script>
