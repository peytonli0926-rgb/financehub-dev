<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operateHeaderRight>
        <el-button
          @click="exportExcel"
          :icon="Document"
          v-permission="permission.fileList"
          type="primary"
          >查看下载</el-button
        >
        <el-button @click="exportFile" :icon="Download" v-permission="permission.export"
          >下载</el-button
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
import { ref, onBeforeMount, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'
import { optionsFileConfig } from '@/utils/fileListConfig'
import { batchExport } from '@/api/reports/rentalTable.js'
import { Download, Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { alertEl, confirmEl } from '@/utils'
import { useExport } from '@/hooks'

const { handleExport } = useExport()
const defaultParams = ref({})
const { router } = useRouter()
const utableRef = ref()
const utableUploadRef = ref()
const uploadVisible = ref(false)
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const optionsFile = optionsFileConfig('/engine/finance/report/inbound-outbound/fileList')

const exportExcel = async () => {
  uploadVisible.value = true
  setTimeout(() => {
    utableUploadRef.value.requestBefore()
  }, 300)
}
const exportFile = async () => {
  confirmEl('您确定要导出该数据吗').then(async () => {
    const _params = await utableRef.value.getSearchParams()
    const { data, code, msg } = await batchExport(_params)
    if (code === 200) {
      alertEl(
        `文件【${data.fileName}】正在生成中，请于10分钟之后，在页面点击【查看下载】查看生成的文件`
      )
    } else {
      ElMessage.error(msg)
    }
  })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.warehousingDevice
})

onBeforeMount(() => {
  const periodCode = parseInt(moment().format('YYYYMM'))
  defaultParams.value = { periodCode }
})
</script>

<style lang="scss" scoped></style>
