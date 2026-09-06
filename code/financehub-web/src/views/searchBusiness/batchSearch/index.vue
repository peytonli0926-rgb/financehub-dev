<template>
  <div v-if="options.columns.length > 0">
    <UTable
      ref="utableRef"
      :autoLoad="false"
      :default-params="defaultParams"
      :options="options"
      @search-form-data="refreshList"
    >
      <template #operateHeaderRight>
        <el-button
          @click="exportExcel"
          :icon="Document"
          v-permission="permission.fileList"
          type="primary"
          >查看下载</el-button
        >
        <el-button @click="downVisible = true" :icon="Download" v-permission="permission.export"
          >下载</el-button
        >
      </template>
    </UTable>
    <UDialog
      :visible="downVisible"
      @handle-submit="exportFile"
      dialogWidth="20%"
      @update:visible="(val) => (downVisible = val)"
      title="系统提示"
    >
      <div class="u-m-b-10">请选择下载的数据源：</div>
      <el-form :model="formDown" ref="ruleFormDownRef">
        <el-form-item
          prop="exportType"
          :rules="{ required: true, message: '请选择数据源', trigger: ['blur', 'change'] }"
        >
          <el-radio-group v-model="formDown.exportType">
            <el-radio value="common" label="common">下载原表</el-radio>
            <el-radio value="detail" label="detail">下载详情</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </UDialog>
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
import { ref, onBeforeMount, computed, watch } from 'vue'
import { optionsConfig } from './config'
import { useStore } from 'vuex'
import { useRouter } from '@toystory/lotso'
import { getTableHeader, batchExport } from '@/api/searchBusiness/batchSearch'
import { Download, Document } from '@element-plus/icons-vue'
import { useExport } from '@/hooks'
import { ElMessage } from 'element-plus'
import moment from 'moment'
import { optionsFileConfig } from '@/utils/fileListConfig'
import { alertEl } from '@/utils'

const formDown = ref({
  exportType: 'common'
})
const store = useStore()
const { handleExport } = useExport()
const { router } = useRouter()
const utableRef = ref()
const uploadVisible = ref(false)
const downVisible = ref(false)
const utableUploadRef = ref()
const ruleFormDownRef = ref()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = ref({})
const optionsFile = optionsFileConfig('/engine/finance/batch-query-upload-record/fileList')
// const route = useRoute()
// const { query } = route.value

// 刷新列表
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
const queryTableHeader = async () => {
  try {
    const { data } = await getTableHeader()
    const { columns, ...result } = optionsConfig(router, dictData)
    const _data = data.map((item) => ({ ...item, width: data.length > 9 ? 200 : '' }))
    options.value = {
      columns: [...columns, ..._data],
      ...result
    }
  } catch (error) {
    ElMessage.error(error)
  }
}

const exportFile = async () => {
  const _params = await utableRef.value.getSearchParams()
  const { data, code, msg } = await batchExport({ ..._params, ...formDown.value })
  if (code === 200) {
    downVisible.value = false
    alertEl(
      `文件【${data.fileName}】正在生成中，请于10分钟之后，在页面点击【查看下载】查看生成的文件`
    )
  } else {
    ElMessage.error(msg)
  }
}
const exportExcel = async () => {
  uploadVisible.value = true
  setTimeout(() => {
    utableUploadRef.value.requestBefore()
  }, 300)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.batchSearch
})
// 如果有上传就要更新列表
const updateImportExcelStatus = computed(() => {
  const dictData = store.getters['useUploadDialog/getUploadSuccess']
  return dictData
})
watch(updateImportExcelStatus, (n, o) => {
  if (n.date !== o.date) {
    queryTableHeader()
  }
})

onBeforeMount(() => {
  queryTableHeader()
  defaultParams.value = {
    queryDate: moment().format('YYYY-MM-DD')
  }
})
</script>

<style lang="scss" scoped></style>
