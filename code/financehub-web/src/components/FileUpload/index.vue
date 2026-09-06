<template>
  <div class="upload-file">
    <el-upload
      ref="upload"
      :action="state.uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :data="params"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="true"
      :headers="state.headers"
      multiple
      :disabled="list.length === limit"
      class="upload-file-uploader"
    >
      <!-- 上传按钮 -->
      <el-button size="small" type="primary">选取文件</el-button>
      <!-- 上传提示 -->
      <template #tip>
        <div  class="el-upload__tip" v-if="isShowTip">
        请上传
        <template v-if="fileSize">
          大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
        </template>
        <template v-if="fileType">
          格式为 <b style="color: #f56c6c">{{ fileType.join(',') }}</b>
        </template>
        的文件
      </div>
      </template>

    </el-upload>

    <!-- 文件列表 -->
    <transition-group class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear" tag="ul">
      <li v-for="(file, index) in list" :key="file.uid" class="el-upload-list__item ele-upload-list__item-content">
        <el-link :href="file.url" :underline="false" target="_blank">
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </el-link>
        <div class="ele-upload-list__item-content-action">
          <el-link :underline="false" type="danger" @click="handleDelete(index)">删除</el-link>
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script setup>
import { getToken } from '@/utils/auth'
import { reactive, computed, toRefs } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  // 值
  value: [String, Object, Array],
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 30
  },
  params: {
    type: Object,
    default: () => {
      return {}
    }
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ['doc', 'xls', 'xlsx', 'ppt', 'txt', 'pdf']
  },
  limit: {
    type: Number,
    default: 9999999999
  },
  disabled: {
    type: Boolean,
    default: false
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true
  },
  // 上传地址
  url: {
    type: String,
    default: ''
  }
})
const emit = defineEmits('input')
const state = reactive({
  uploadFileUrl: import.meta.env.VITE_APP_BASE_API + props.url, // 上传的图片服务器地址
  headers: {
    Authorization: 'Bearer ' + getToken(),
    token: getToken()
  },
  fileList: []
})

const list = computed(() => {
  let temp = 1
  if (props.value) {
    // 首先将值转为数组
    const list = Array.isArray(props.value) ? props.value : [props.value]
    // 然后将数组转为对象数组
    return list.map((item) => {
      if (typeof item === 'string') {
        item = { name: item, url: item }
      }
      item.uid = item.uid || new Date().getTime() + temp++
      return item
    })
  } else {
    // state.fileList = []
    return []
  }
})
state.fileList = list.value

// 上传前校检格式和大小
function handleBeforeUpload (file) {
  // 校检文件类型
  if (props.fileType) {
    let fileExtension = ''
    if (file.name.lastIndexOf('.') > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
    }
    const isTypeOk = props.fileType.some((type) => {
      if (file.type.indexOf(type) > -1) return true
      if (fileExtension && fileExtension.indexOf(type) > -1) return true
      return false
    })
    if (!isTypeOk) {
      ElMessage.error(`文件格式不正确, 请上传${props.fileType.join('/')}格式文件!`)
      return false
    }
  }
  // 校检文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize
    if (!isLt) {
      ElMessage.error(`上传文件大小不能超过 ${props.fileSize} MB!`)
      return false
    }
  }
  // // 文件名校验 不能包含（#% ）
  // if (file.name.includes('#') || file.name.includes('%')) {
  //   props.$message.error(`文件名不能包含【#，%】`)
  //   return false
  // }
  // // 文件名校验 不能包含空格
  // if (file.name.includes(' ')) {
  //   props.$message.error(`文件名不能包含空格`)
  //   return false
  // }

  return true
}
// 文件个数超出
function handleExceed () {
  ElMessage.error(`单次只允许上传${props.limit}个文件`)
}
// 上传失败
function handleUploadError () {
  ElMessage.error('上传失败, 请重试')
}
// 上传成功回调
function handleUploadSuccess (res, file) {
  if (res.code === 200) {
    ElMessage.success(res.msg || '上传成功')
    state.fileList = toRefs([file])
    emit('input', res.data, 'upload')
  } else {
    ElMessage.error(res.msg)
  }
}
// 删除文件
function handleDelete (index) {
  state.fileList.splice(index, 1)
  emit('input', index, props.value, 'delete')
}
// 获取文件名称
function getFileName (name) {
  if (name.lastIndexOf('/') > -1) {
    return name.slice(name.lastIndexOf('/') + 1).toLowerCase()
  } else {
    return name
  }
}

</script>

<style scoped lang="scss">
.upload-file-uploader {
  margin-bottom: 5px;
}
.upload-file-list .el-upload-list__item {
  border: 1px solid #e4e7ed;
  line-height: 2;
  margin-bottom: 10px;
  position: relative;
}
.upload-file-list .ele-upload-list__item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
}
.ele-upload-list__item-content-action .el-link {
  margin-right: 10px;
  margin-left: 5px;
  width: 30px;
}
</style>
