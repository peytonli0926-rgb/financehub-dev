<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
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
      <template #operateHeaderRight>
        <el-button @click="viewFileList" v-permission="permission.viewFile" :icon="Files" type=""
          >查看下载</el-button
        >
        <el-button @click="exportExcel" v-permission="permission.export" :icon="Download" type=""
          >导出</el-button
        >
      </template>
    </UTable>
    <UDialog
      :visible="uploadVisible"
      dialogWidth="75%"
      @update:visible="(val) => (uploadVisible = val)"
      title="下载列表"
      :is-footer="false"
    >
      <UTable ref="utableUploadRef" :autoLoad="false" :options="optionsFile">
        <template #operationBtn="{ row }">
          <el-button
            type="primary"
            link
            size="small"
            :disabled="row.executeStatus !== 'Finish'"
            @click="
              handleExport({ url: `/engine/finance/report/downloadFile/${row.id}` }, row.fileName)
            "
            >下载</el-button
          >
        </template>
      </UTable>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig, optionsFileConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { alertEl, confirmEl } from '@/utils'
import Qs from 'qs'
import { Download, Files } from '@element-plus/icons-vue'
import { exportContractStatus } from '@/api/rentBusiness/specialContractStatus'
import { useExport } from '@/hooks'

const { handleExport } = useExport()
const { router } = useRouter()
const utableRef = ref()
const utableUploadRef = ref()
const uploadVisible = ref(false)
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const optionsFile = optionsFileConfig(router, dictData)
const defaultParams = ref({})
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.specialContractStatus
})
const viewFileList = async () => {
  uploadVisible.value = true
  setTimeout(() => {
    utableUploadRef.value.requestBefore()
  }, 300)
}
const exportExcel = () => {
  confirmEl('您确定要导出该数据吗').then(async () => {
    const _params = await utableRef.value.getSearchParams()
    const { data } = await exportContractStatus(_params)
    alertEl(
      `文件【${data.fileName}】正在生成中，请于5分钟之后，在页面点击【查看下载】查看生成的文件`
    )
  })
}
const fromToRulePage = ({ contractCode, orgId }) => {
  const params = {
    contractCode,
    orgIdList: orgId
  }
  router.push(`/rentBusiness/specialContractStatusDetail?${Qs.stringify(params)}`)
}
</script>
