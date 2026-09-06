<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
       @search-form-data="searchFormData">
       <template #operateHeaderRight>
        <el-button type="warning" @click="validate" v-permission="permission.validate">校验</el-button>
      </template>

    </UTable>

    <UDialog v-if="dialogVisible" @update:visible="dialogVisible = $event" title="校验结果" dialog-width="80%" :is-footer="false"
      :visible="dialogVisible">
      <UTable ref="utableDialogRef" :defaultParams="defaultParamsD" :options="optionsDialog" :autoLoad="false"/>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { optionsConfig, optionsConfigDialog } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { confirmEl } from '@/utils'
// import moment from 'moment'
const { router } = useRouter()
const store = useStore()
const utableDialogRef = ref()
const dialogVisible = ref(false)
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const optionsDialog = optionsConfigDialog(router, dictData)
const utableRef = ref()
const route = useRoute()
const { query } = route.value
const defaultParams = ref({})
const defaultParamsD=ref({})
const searchFormData = (formData) => {
  refreshList(formData)
}

// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
// 校验
const validate = () => {
  const { verificationId } = query
  confirmEl('您确定要校验数据吗?').then(async () => {
    defaultParamsD.value={id:verificationId}
    dialogVisible.value = true
    setTimeout(() => {
      utableDialogRef.value.requestBefore()
    }, 200)
  })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.verificationDetail
})
onActivated(() => {
  const { verificationId } = query
  defaultParams.value = { verificationId }
  
  refreshList()
})

</script>
