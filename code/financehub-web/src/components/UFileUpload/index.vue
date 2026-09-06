<template>
  <UDialog
    :title="upload.title"
    v-if="upload.open"
    :visible="upload.open"
    dialogWidth="500px"
    @update:visible="closeDialog"
    @handle-submit="submitFileForm"
  >
    <el-upload
      ref="uploadRef"
      :limit="1"
      :data="upload.data"
      :accept="upload.accept"
      :headers="upload.headers"
      :action="upload.url"
      :disabled="upload.isUploading"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
      drag
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip u-m-t-10">
          <span>{{ upload.tip }}</span>
          <template v-if="isArrayOfTemplateUrl">
            <div class="u-p-t-10" v-for="(lk, index) of upload.templateUrl" :key="index">
              <el-link
                type="primary"
                style="font-size: 12px; vertical-align: baseline"
                @click="importTemplate(lk)"
                >下载【{{ lk.title }}】模版</el-link
              >
            </div>
          </template>
          <el-link
            type="primary"
            v-else-if="upload.templateUrl"
            style="font-size: 14px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          >

          <div class="text-right u-m-t-10">
            <el-link
              type="danger"
              v-if="errorStatus"
              style="font-size: 14px; vertical-align: baseline"
              @click="downloadErrLog"
              >下载错误信息</el-link
            >
          </div>
        </div>
      </template>
    </el-upload>
  </UDialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getToken } from '@/utils/auth'
import { useExport } from '@/hooks'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import moment from 'moment'
import { useRoute } from '@toystory/lotso'
const { handleExport } = useExport()
const store = useStore()
const uploadRef = ref()
const errorStatus = ref(false)

const upload1 = ref({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: '',
  // 是否禁用上传
  isUploading: false,
  accept: '.xlsx',
  // 提示用语,
  tip: '仅允许导入xlsx格式文件。',
  // 模版下载地址
  templateUrl: '',
  // 额外参数
  data: {},
  // 设置上传的请求头部
  headers: { Authorization: getToken() },
  // 上传的地址
  url: '',
  logUrl: ''
})
// 合并属性
const upload = computed(() => {
  const prefix = import.meta.env.VITE_APP_BASE_API
  const { url, ...result } = store.getters['useUploadDialog/getUploadConfig']
  errorStatus.value = false
  return Object.assign({}, upload1.value, { ...result, url: prefix + url })
})

// 合并属性
const isArrayOfTemplateUrl = computed(() => {
  const url = upload.value.templateUrl

  return Array.isArray(url)
})

// 关闭
const closeDialog = () => {
  store.dispatch('useUploadDialog/setUploadConfig', {
    open: false,
    uuid: +new Date()
  })
}

/** 下载模板操作 */
const importTemplate = ({ title, ...item }) => {
  if (item.url) {
    handleExport(item, `${title}模版.xlsx`, true)
  } else {
    handleExport({ url: upload.value.templateUrl }, `${upload.value.title}模版.xlsx`, true)
  }
}
// 文件上传中处理
const handleFileUploadProgress = () => {
  upload1.value.isUploading = true
}
// 文件上传成功处理
const handleFileSuccess = (response) => {
  if (response.code === 200) {
    upload1.value.isUploading = false
    uploadRef.value.clearFiles()
    const route = useRoute()
    const { name } = route.value
    if (['特殊合同状态', '手工凭证'].includes(upload.value.title)) {
      if (typeof response.data === 'string' && response.data !== '') {
        ElMessage.warning({ zIndex: 999999, message: response.data })
      } else {
        closeDialog()
        ElMessage.success({ zIndex: 999999, message: '上传成功' })
        store.dispatch('useUploadDialog/setUploadSuccess', {
          routerName: name,
          date: +new Date(),
          ...response.data
        })
      }
    } else {
      if (response.data === 'Errs') {
        errorStatus.value = true
        ElMessage.error({
          zIndex: 999999,
          message: response.msg || '文件校验未通过，请下载错误信息进行修改'
        })
        store.dispatch('useUploadDialog/setUploadSuccess', {
          routerName: name,
          date: +new Date(),
          ...response.data
        })
      } else {
        ElMessage.success({ zIndex: 999999, message: '上传成功' })
        store.dispatch('useUploadDialog/setUploadSuccess', {
          routerName: name,
          date: +new Date(),
          ...response.data
        })
        closeDialog()
      }
    }
  } else {
    ElMessage.error({ zIndex: 999999, message: response.msg })
    upload1.value.isUploading = false
    uploadRef.value.clearFiles()
  }
}
//错误日志
const downloadErrLog = () => {
  handleExport(
    { url: upload.value.logUrl },
    `${upload.value.title}错误日志${moment().format('YYYYMMDDHHMMSS')}.xlsx`,
    true
  )
}
// 提交上传文件
const submitFileForm = () => {
  uploadRef.value.submit()
}
</script>
