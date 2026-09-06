<template>
  <div>
    <div v-if="permission">
      <slot></slot>
      <template v-for="(item, key) in btnConfig" :key="key">
        <el-button
          :type="item.type || 'default'"
          @click="operateButton(item, key)"
          :disabled="isBoolReactive[key]"
          :icon="btnIcon[key] || Document"
          v-permission="permissionIsLevel(item, key)"
          >{{ item.title }}</el-button
        >
        <!--
        <el-button v-else :type="item.type||''" :key="key+index" @click="operateButton(item,key)"
        :disabled="isBoolReactive[key]" :icon="btnIcon[key]||Document"
        >{{item.title}}</el-button> -->
      </template>
    </div>
    <UDialog
      :visible="uploadVisible"
      dialogWidth="75%"
      @update:visible="(val) => (uploadVisible = val)"
      title="下载列表"
      :is-footer="false"
    >
      <UTable
        ref="utableUploadRef"
        :defaultParams="defaultParamsBtn"
        :autoLoad="false"
        :options="optionsFile"
      >
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
import { ref, onMounted, reactive, watch } from 'vue'
import { getCommonTableList } from '@/api/common'
import { confirmEl, alertEl } from '@/utils'
import { useRouter, useRoute } from '@toystory/lotso'
import { ElMessage } from 'element-plus'
import { useExport } from '@/hooks'
import Qs from 'qs'
import { optionsFileConfig } from './config'
import {
  Warning,
  DocumentCopy,
  Plus,
  Delete,
  Edit,
  Upload,
  Download,
  WarningFilled,
  InfoFilled,
  Document,
  Finished
} from '@element-plus/icons-vue'

const btnIcon = {
  add: Plus,
  edit: Edit,
  delete: Delete,
  export: Download,
  export1: Download,
  export2: Download,
  export3: Download,
  import: Upload,
  import1: Upload,
  import2: Upload,
  import3: Upload,
  validate: InfoFilled,
  withdraw: WarningFilled,
  voucher: Document,
  submit: Finished,
  copy: DocumentCopy,
  writeOff: Warning
}
const { handleExport, commonUploadDialog } = useExport()
const { router } = useRouter()
const route = useRoute()
const btnPermission = ref({})
const uploadVisible = ref(false)
const emit = defineEmits(['refresh', 'row-operateion', 'update:loading'])
const optionsFile = optionsFileConfig(router, {})
const utableUploadRef = ref()
const defaultParamsBtn = ref({})
const props = defineProps({
  config: {
    type: Object,
    default: () => {
      return {}
    }
  },
  defaultParams: {
    type: Object,
    default: () => {}
  },
  selectedData: {
    type: Array,
    default: () => []
  },
  permission: {
    type: Object,
    default: () => ({})
  },
  formSearch: {
    type: [Object, Function],
    default: () => null
  }
})
const btnConfig = ref({})
const isBoolReactive = reactive({})
const selectionChange = (data) => {
  const result = btnConfig.value
  Object.keys(result).map((key) => {
    const { condition, isDisabled, source } = result[key]
    if (typeof condition === 'function') {
      if (isDisabled === undefined || isDisabled === null || isDisabled === true) {
        isBoolReactive[key] =
          source === 'data'
            ? !condition(data)
            : !(data.length === 0 ? false : data.every((item) => condition(item)))
      }
    }
  })
}

const permissionIsLevel = (item, key) => {
  if (item.level) {
    return btnPermission.value[item.level][key]
  } else {
    return btnPermission.value[key]
  }
}
const regCondition = (common) => {
  return {
    copy: (data) => data.length !== 0,
    export: (data) => data.length !== 0,
    validate: (data) => data.length !== 0,
    voucher: (item) => ['1', '5'].includes(item[common.disabled]),
    delete: (item) => ['1', '5'].includes(item[common.disabled]),
    submit: (item) => ['1', '5'].includes(item[common.disabled]),
    withdraw: (item) => item[common.disabled] === '2'
  }
}
const operateButton = (item, type) => {
  switch (type) {
    case 'import':
    case 'import1':
    case 'import2':
    case 'import3':
    case 'import4':
      importExcel(item)
      break
    case 'export':
    case 'export1':
    case 'export2':
    case 'export3':
    case 'export4':
      exportExcel(item)
      break
    case 'add':
      if (item.redirectUrl) {
        const _params = {}
        router.push(`${item.redirectUrl}${Qs.stringify(_params)}`)
      } else {
        emit('row-operateion', 'ADD')
      }
      break
    case 'fileListBtn':
      viewFileList(item)
      break
    default:
      submit(item)
  }
}

const viewFileList = async ({ params }) => {
  uploadVisible.value = true
  defaultParamsBtn.value = { ...params }
  setTimeout(() => {
    utableUploadRef.value.requestBefore()
  }, 300)
}

// 获取路由地址参数
const getRouteQuery = (routeQuery) => {
  const { query } = route.value
  const param = {}
  if (Array.isArray(routeQuery)) {
    routeQuery.map((item) => {
      param[item] = query[item]
    })
  }
  return param
}

// 上传
const importExcel = ({ params, routeQuery }) => {
  const param = getRouteQuery(routeQuery)
  commonUploadDialog({
    ...params,
    data: {
      ...params.data,
      ...param
    }
  })
}

// 导出
const exportExcel = async ({
  filename,
  params,
  url,
  isParams,
  fetchKey,
  getKey,
  isFull,
  routeQuery,
  isAsyncFile
}) => {
  const { common } = props.config
  const searchValue = await props.formSearch()
  const ids = props.selectedData.map((item) => item[getKey || common.getKey])

  const routeParam = Array.isArray(routeQuery) ? getRouteQuery() : {}
  const _params = isParams
    ? fetchKey
      ? { ...props.defaultParams, ...searchValue, [fetchKey]: ids, ...routeParam, ...params }
      : { ...props.defaultParams, ...searchValue, ...routeParam, ...params }
    : isFull
      ? props.selectedData
      : ids

  if (isAsyncFile) {
    confirmEl('是否确认导出该数据吗?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      const { code, data, msg } = await getCommonTableList({
        url: url,
        method: 'post',
        params: _params
      }).catch((e) => {
        emit('update:loading', false)
        Promise.reject(new Error(e))
      })
      if (code === 200) {
        alertEl(
          `文件【${data.fileName}】正在生成中，请于5分钟之后，在页面点击【查看下载】查看生成的文件`
        )
      } else {
        ElMessage.error(msg)
      }
    })
  } else {
    handleExport(
      {
        url: url,
        params: _params
      },
      filename
    )
  }
}

// 提交
const submit = ({
  forward,
  isParams,
  url,
  title,
  method = 'post',
  getKey,
  redirectUrl,
  query,
  fetchKey,
  beforeParam,
  refreshDelay = 0
}) => {
  if (redirectUrl) {
    const [item] = props.selectedData
    const _params = {}
    query.map((ii) => {
      _params[ii] = item[ii]
    })
    router.push(`${redirectUrl}${Qs.stringify(_params)}`)
    return
  }
  confirmEl(`您确定${title}该数据吗?`).then(async () => {
    const { common } = props.config
    const searchValue = await props.formSearch()
    emit('update:loading', true)
    const ids = props.selectedData.map((item) => item[getKey || common.getKey])

    let params = {}

    if (beforeParam && typeof beforeParam === 'function') {
      params = beforeParam(searchValue, props.selectedData)
    } else {
      params = isParams
        ? fetchKey
          ? { ...searchValue, [fetchKey]: ids }
          : { ...searchValue }
        : ids
    }

    const { code, data, msg } = await getCommonTableList({
      url: url,
      method,
      params
    }).catch((e) => {
      emit('update:loading', false)
      Promise.reject(new Error(e))
    })
    if (data && forward) {
      emit('update:loading', false)
      return router.push(forward)
    }

    code === 200 ? ElMessage.success(msg || '操作成功') : ElMessage.error(msg)
    if (code === 200 && refreshDelay > 0) {
      setTimeout(() => emit('refresh'), refreshDelay)
    } else {
      emit('refresh')
    }
  })
}

const getIsBoolReactive = () => {
  const { common, ..._config } = props.config
  btnConfig.value = _config
  Object.keys(_config).map((key) => {
    const { isDisabled } = _config[key]
    if (isDisabled === undefined || isDisabled === null || isDisabled === true) {
      isBoolReactive[key] = true
      if (!_config[key].condition) {
        const _regCondition = regCondition(common)
        _config[key].condition = _regCondition[key]
      }
    }
  })
}
watch(
  () => props.selectedData,
  (n, o) => {
    if (JSON.stringify(n) !== JSON.stringify(o)) {
      selectionChange(n)
    }
  },
  { deep: true, immediate: true }
)
watch(
  () => props.permission,
  (n, o) => {
    if (JSON.stringify(n) !== JSON.stringify(o)) {
      btnPermission.value = n
    }
  },
  { deep: true, immediate: true }
)

onMounted(() => {
  getIsBoolReactive()
})
</script>

<style lang="scss" scoped></style>
