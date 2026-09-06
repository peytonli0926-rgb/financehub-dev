<template>
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :autoLoad="false"
      :options="options"
      @search-form-data="searchFormData"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.viewVoucher"
          @click="fromToRulePage(row, 'view')"
          >查看凭证</el-button
        >
        <el-button
          type="primary"
          v-permission="permission.view"
          link
          size="small"
          @click="fromToRulePage(row, 'detail')"
          >查看详情</el-button
        >
      </template>
      <template #operateHeaderRight>
        <el-space>
          <el-button
            @click="viewTask"
            type="success"
            :icon="Finished"
            v-permission="permission.viewTask"
            >查看任务</el-button
          >

          <el-button
            type=""
            @click="exportFileList"
            :icon="Download"
            v-permission="permission.exportlist"
            >导出减值清单</el-button
          >
          <el-button
            @click="handleMeasurement"
            type="success"
            :icon="Document"
            v-permission="permission.viewReport"
            >查看本月减值报告</el-button
          >
          <el-button
            @click="uploadFileList"
            :icon="Upload"
            v-permission="permission.import"
            type="primary"
            >上传</el-button
          >
        </el-space>
      </template>
    </UTable>

    <UDialog
      :visible="taskVisible"
      dialogWidth="80%"
      @update:visible="(val) => (taskVisible = val)"
      title="查看任务"
      :is-footer="false"
    >
      <UTable ref="utableRefTask" :options="taskOptions"></UTable>
    </UDialog>

    <UDialog
      :visible="reportVisible"
      dialogWidth="70%"
      @update:visible="(val) => (reportVisible = val)"
      title="本月减值报告"
      :is-footer="false"
    >
      <UTable ref="utableRefReport" :options="reportOptions"></UTable>
    </UDialog>
    <UDialog
      :visible="uploadVisible"
      dialogWidth="50%"
      @update:visible="(val) => (uploadVisible = val)"
      :title="btnType === 'upload' ? '上传' : '导出减值清单'"
      :is-footer="false"
    >
      <el-table :data="excelList" border>
        <el-table-column label="文件名称" prop="name"></el-table-column>
        <el-table-column :label="btnType === 'upload' ? '上传' : '下载'">
          <template v-slot="{ row }">
            <FileUpload
              v-if="btnType === 'upload'"
              :params="{ excelType: row.excelType }"
              :fileType="['xls', 'xlsx']"
              :limit="1"
              url="/engine/finance/impairment-provision/importFile"
            />
            <el-button v-else type="primary" size="small" @click="exportExcelList(row)">{{
              row.name
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig, reportOptionsConfig, taskOptionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import {
  excelUploadListImpairmentProvision,
  excelExportListImpairmentProvision
} from '@/api/impairmentBusiness/impairmentBusiness'
import { useExport, useVoucherPage } from '@/hooks'
import FileUpload from '@/components/FileUpload/index.vue'
import { Upload, Download, Document, Finished } from '@element-plus/icons-vue'

const reportVisible = ref(false)
const uploadVisible = ref(false)
const taskVisible = ref(false)
const utableRefTask = ref()
const utableRefReport = ref()
const { handleExport } = useExport()
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()

const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const reportOptions = reportOptionsConfig(router, dictData)
const taskOptions = taskOptionsConfig(router, dictData)
const formValue = ref({})
const excelList = ref([])
const defaultParams = ref({})

const btnType = ref()
const searchFormData = (formData) => {
  formValue.value = formData
  refreshList(formData)
}

const fromToRulePage = ({ batchType, voucherId, id }, type) => {
  const url = `/impairmentBusiness/impairmentBusinessDetail?impairmentProvisionId=${id}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id }, 'total')
    return
  }
  router.push(url)
}
// 查看任务
const viewTask = () => {
  taskVisible.value = true
  setTimeout(() => {
    utableRefTask.value.requestBefore()
  }, 200)
}

// 导出清单数据
const exportExcelList = ({ name, excelType }) => {
  handleExport(
    {
      url: '/engine/finance/impairment-provision/exportImpairmentList',
      params: excelType
    },
    `${name}.xlsx`
  )
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.impairmentBusinessIndex
})
// 报告
const handleMeasurement = async () => {
  reportVisible.value = true
  setTimeout(() => {
    utableRefReport.value.requestBefore()
  }, 100)
}
//  上传列表
const uploadFileList = async () => {
  btnType.value = 'upload'
  const { code, data } = await excelUploadListImpairmentProvision()
  if (code === 200) {
    excelList.value = data
    uploadVisible.value = true
  }
}
//  下载列表
const exportFileList = async () => {
  btnType.value = 'export'
  const { code, data } = await excelExportListImpairmentProvision()
  if (code === 200) {
    excelList.value = data
    uploadVisible.value = true
  }
}

// 刷新列表
const refreshList = (formData = {}) => {
  utableRef.value.requestBefore(formData)
}
onMounted(() => {
  const { query } = route.value
  const { id } = query
  defaultParams.value = { idList: id ? [id] : [] }
  refreshList()
})
</script>
