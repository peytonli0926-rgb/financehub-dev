<template>
  <div>

    <UTable ref="utableRef" :autoLoad="false" @selection-change="selectionChange" :defaultParams="defaultParams"
      :options="options" @search-form-data="searchFormData">
      <template #operationBtn="{ row }">
        <el-button type="primary" link size="small" @click="fromToRulePage(row)"
          :disabled="!row.voucherId" v-permission="permission.viewVoucher">查看凭证</el-button>
      </template>
      <template #operateHeaderRight>
        <el-button @click="handleMeasurement"   v-permission="permission.validate" type="warning">校验</el-button>
      </template>
    </UTable>
    <UDialog v-if="dialogVisible" @update:visible="dialogVisible = $event" title="科目校验" dialog-width="80%" :is-footer="false"
      :visible="dialogVisible">
      <UTable ref="utableDialogRef" :defaultParams="defaultParamsd"  :options="optionsDialog" :autoLoad="false"/>
    </UDialog>
  </div>

</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig, optionsConfigDialog } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import { confirmEl } from '@/utils'

const { router } = useRouter()
const utableRef = ref()
const utableDialogRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const optionsDialog = optionsConfigDialog(router, dictData)
const route = useRoute()
const { query } = route.value
const { setVoucherPage } = useVoucherPage()
const defaultParams = ref({})
const defaultParamsd = ref({})
const selectedData = ref([])
const dialogVisible = ref(false)

const searchFormData = (formData) => {
  utableRef.value.requestBefore(formData)
}

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId })
}
const selectionChange = (data) => {
  selectedData.value = data
}
//  校验
const handleMeasurement = async () => {
  confirmEl('您确定进行校验操作吗？').then(async () => {
    const { parityTransferId } = defaultParams.value
    defaultParamsd.value = {
      id: parityTransferId
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
  return btnPermissions.normalTransferDetail
})

onActivated(() => {
  const { parityTransferId } = query
  defaultParams.value = { parityTransferId }
  utableRef.value.requestBefore()
})

</script>
