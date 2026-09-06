<template>
  <div>
    <!-- {{ permission }} -->
    <el-card shadow="never" v-if="isSearchForm" :body-style="{ padding: '12px 12px 10px' }">
      <USearch
        ref="searchFormRef"
        form-type="search"
        :modelValue="searchForm"
        :columns="searchFormColumn"
        :span="options.span || 6"
        :label-position="options.labelPosition"
        :label-width="options.labelWidth"
        :isResetBtn="options.isResetBtn"
        :searchBtnShow="options.searchBtnShow"
        :isSearchBtn="options.isSearchBtn"
        @update:modelValue="(newValue) => (searchForm = newValue)"
        @search-submit="searchSubmit"
      ></USearch>
    </el-card>
    <div class="u-m-t-12">
      <el-card shadow="never" :body-style="{ padding: '12px 12px 15px' }">
        <div class="u-rela u-flex u-row-between u-p-b-10" v-if="rebaseOptions.isOperateHeader">
          <div class="">
            <slot name="operateHeaderLeft"> {{ rebaseOptions.leftCardName || '' }}</slot>
          </div>
          <div class="u-flex-1 u-text-right">
            <el-space>
              <slot name="operateHeaderRight" />
              <template v-if="permission">
                <UButtonOperate
                  v-if="rebaseOptions.btnConfig"
                  :permission="permission"
                  @row-operateion="rowOperateion"
                  :config="rebaseOptions.btnConfig"
                  @update:loading="(val) => (loading = val)"
                  :form-search="getSearchParams"
                  :selected-data="selectedData"
                  :loading="loading"
                  @refresh="requestBefore"
                />
              </template>
            </el-space>
          </div>
        </div>
        <el-table
          ref="tableRef"
          :summary-method="getSummaries"
          :show-summary="rebaseOptions.isShowSummary"
          highlight-current-row
          @select="selectClick"
          :height="rebaseOptions.tableHeight"
          stripe
          :border="isBorderTable"
          v-loading="loading"
          :row-key="rebaseOptions.rowKey"
          :data="dataSource"
          @selection-change="handleSelectionChange"
        >
          <!--多选 -->
          <el-table-column
            v-if="isSelectionTable"
            type="selection"
            width="55"
            :fixed="isIndexFixed"
          />
          <!-- 序号 -->
          <el-table-column
            align="center"
            label="序号"
            type="index"
            :width="rebaseOptions.indexWidth || 80"
            :fixed="isIndexFixed"
            v-if="isIndexTable"
          >
            <template v-slot="{ $index }">{{ pageNumComputed + ($index + 1) }}</template>
          </el-table-column>

          <UTableColumn
            :permission="permission"
            @icon-events="iconEvents"
            @row-operateion="rowOperateion"
            :operateBtn="operateBtn"
            v-for="column of rebaseOptions.columns"
            :key="column.prop"
            :columns="column"
          >
            <template v-for="(item, key, index) in $slots" :key="index" #[key]="propsData">
              <slot :name="key" v-bind="propsData"></slot>
            </template>
          </UTableColumn>
        </el-table>
        <div v-if="isPaginationTable" class="u-m-t-10 u-flex u-row-right">
          <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 50, 100]"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>
    <UDialog
      :permission="permission"
      :dialog-width="options.dialogWidth"
      :title="dialogTitle"
      v-if="visible"
      :visible="visible"
      @update:visible="(vi) => (visible = vi)"
      @handle-submit="handleDialogSubmit"
    >
      <!-- {{ formAddAndEditModel }} -->
      <USearch
        ref="dialogFormRef"
        :label-width="options.dialogLabelWidth || options.labelWidth"
        :isSearchBtn="false"
        :modelValue="formAddAndEditModel"
        :columns="showAddAndEditColumn"
        :span="options.dialogSpan || 24"
        :operate-type="operateType"
        @update:modelValue="(newValue) => (formAddAndEditModel = newValue)"
      ></USearch>
    </UDialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed, onActivated, watchEffect } from 'vue'
import { ElMessage } from 'element-plus'
import { getCommonTableList } from '@/api/common'
import { confirmEl, formatUrlParams } from '@/utils'
import { useStore } from 'vuex'
import { useRouter, useRoute } from '@toystory/lotso'
import Qs from 'qs'

const emit = defineEmits([
  'selection-change',
  'request-before',
  'row-del',
  'search-form-data',
  'icon-events'
])
const searchForm = ref({})
const formAddAndEditModel = ref({})
const dialogFormRef = ref()
const operateType = ref('ADD')
const visible = ref(false)
const dataSource = ref([])
const formCountList = ref([])
const store = useStore()
const tableRef = ref()
const searchFormRef = ref()
const { router } = useRouter()
const route = useRoute()
const selectedData = ref([])
let isMountedCompleted = false
// 分页参数
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})
const loading = ref(false)
const dialogTitle = ref('')
const TITLE_ENMU = {
  ADD: '新增',
  EDIT: '编辑'
}

const props = defineProps({
  defaultParams: {
    type: Object,
    default: () => {}
  },
  options: {
    type: Object,
    default: () => {},
    require: true
  },
  autoLoad: {
    type: Boolean,
    default: () => true
  },
  listBefore: {
    type: Function,
    default: (val = {}) => val
  },
  listAfter: {
    type: Function
  },
  updateBefore: {
    type: Function,
    default: (val = {}) => val
  },
  addBefore: {
    type: Function,
    default: (val = {}) => val
  },
  getSummaries: {
    type: Function,
    default: () => 0
  },
  data: {
    type: [Array, undefined]
    // default: () => []
  }
})

// 获取权限
const permission = computed(() => {
  const { name } = route.value
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions[name]
})

const newOptions = ref(props.options || [])
const iconEvents = (op) => {
  // const { columns, ...result } = rebaseOptions.value
  // const nColumn = columns.map(item => {
  //   if (op.hideColumns.includes(item.prop)) {
  //     item.hide = !item.hide
  //     // item.search = !item.search
  //   }
  //   return item
  // })
  // rebaseOptions.value = { columns: nColumn, ...result }
  emit('icon-events', op)
}

const getSearchParams = async (parma = {}) => {
  const param = props.listBefore()
  const defaultParams = await mergeFomrChildrenItem()
  const paramsObj = await coverColumn(defaultParams, { ...(searchForm?.value ?? {}), ...parma })
  return {
    ...param,
    ...paramsObj
  }
}

// 重新构造options
const rebaseOptions = computed({
  get: () => {
    return newOptions.value
  },
  set: (value) => {
    newOptions.value = value
  }
})

// 计算合并按钮显示
const operateBtn = computed(() => {
  const {
    addBtn = {
      label: '新增',
      isShow: true
    },
    editBtn = {
      label: '编辑',
      isShow: true
    },
    delBtn = {
      label: '删除',
      isShow: true
    }
  } = props.options
  //
  return {
    addBtn,
    editBtn,
    delBtn
  }
})

// 修改序号计数设置
const pageNumComputed = computed(() => {
  const { isIndexOrder = true } = rebaseOptions.value
  const { pageNum, pageSize } = pagination.value
  if (isIndexOrder) {
    return (pageNum - 1) * pageSize
  }
  return ''
})
// 是否展示列表序號
const isIndexTable = computed(() => {
  const { isIndex = true } = rebaseOptions.value
  return isIndex
})
// 是否展示列表序號悬浮
const isIndexFixed = computed(() => {
  const { isIndexFixed = true } = rebaseOptions.value
  return isIndexFixed
})

// 是否展示查詢
const isSearchForm = computed(() => {
  const { isSearch = true } = rebaseOptions.value
  return isSearch
})
// 是否展示列表多選
const isSelectionTable = computed(() => {
  const { isSelection = true } = rebaseOptions.value
  return isSelection
})
// 是否展示列表裱框
const isBorderTable = computed(() => {
  const { isBorder = true } = rebaseOptions.value
  return isBorder
})
// 是否展示分頁
const isPaginationTable = computed(() => {
  const { isPagination = true } = rebaseOptions.value
  return isPagination
})

// 监听默认值
watch(
  () => props?.defaultParams ?? {},
  (n) => {
    searchForm.value = n
  },
  {
    deep: true,
    immediate: true
  }
)

// 对外暴露调用接口
const requestBefore = async (params = {}) => {
  getTableList(params)
}

// 组装查询数据-主要用于一个参数要分解多个字段查询
const coverColumn = (formAllColumnList, formValue = {}) => {
  return new Promise((resolve) => {
    const values = {}
    for (const item in formValue) {
      const column = formAllColumnList.find((it) => it.prop === item)
      if (
        column &&
        ['date', 'inputrange', 'selectrange'].includes(column.type) &&
        Reflect.has(column, 'cover')
      ) {
        const [startDate, endDate] = column.cover
        const [date1, date2] = formValue[item] || []
        values[startDate] = date1
        values[endDate] = date2
      } else {
        values[item] = formValue[item]
      }
    }
    resolve(values)
  })
}
// table选择项发生变化时会触发该事件
const selectClick = (selection) => {
  if (rebaseOptions.value.selectionType === 'single') {
    if (selection.length > 1) {
      const delRow = selection.shift()
      tableRef.value.toggleRowSelection(delRow, false) // 用于多选表格，切换某一行的选中状态，如果使用了第二个参数，则是设置这一行选中与否（selected 为 true 则选中）
    }
  }
}
// 合并嵌套表单展示出来
const mergeFomrChildrenItem = async () => {
  const { columns = [] } = rebaseOptions.value

  return new Promise((resolve) => {
    let allColumns = []
    const funColumns = (arr) => {
      arr.map((item) => {
        if (item.children && item.children.length > 0) {
          funColumns(item.children)
        } else {
          if (item.search && !Reflect.has(item, 'children')) {
            allColumns = [...allColumns, item]
          }
        }
      })
    }
    funColumns(columns)
    searchFormColumn.value = allColumns
    resolve(allColumns)
  })
}
// 计算需要展示的字段
const showAddAndEditColumn = computed(() => {
  return props.options.columns.filter((item) => item.display === undefined || item.display === true)
})

// 过滤搜索条件
const searchFormColumn = computed({
  get: () => {
    return formCountList.value
  },
  set: (value) => {
    formCountList.value = value
  }
})
// 获取列表接口
const getTableList = async (params = {}) => {
  try {
    const { pageNum, pageSize } = pagination.value
    const { request } = rebaseOptions.value
    const { ...result } = request.list

    loading.value = true
    const _params = await getSearchParams(params)
    let page = {}
    if (rebaseOptions.value.isPagination) {
      page = { pageNum, pageSize }
    }
    const {
      code,
      msg,
      data = {},
      rows,
      total: t
    } = await getCommonTableList({
      ...result,
      params: {
        ...page,
        ..._params
      }
    }).catch(() => {
      loading.value = false
    })
    if (code !== 200) {
      ElMessage.error(msg || '系统错误')
      loading.value = false
      return
    }
    if (rebaseOptions.value.isPagination) {
      const { records, total } = data || {}
      loading.value = false
      pagination.value.total = t || total || 0
      dataSource.value = props.listAfter
        ? props.listAfter(rows || records || [])
        : rows || records || []
    } else {
      dataSource.value = props.listAfter ? props.listAfter(data) : data
      loading.value = false
    }
    isMountedCompleted = false
  } catch (error) {
    loading.value = false
    throw error
  }
}
// 分页
const handleCurrentChange = (page) => {
  pagination.value.pageNum = page
  requestBefore()
}
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  requestBefore()
}
// 获取默认值
const getDefaultValue = () => {
  const defaultValueArray = showAddAndEditColumn.value.filter((item) => item.value)
  const defaultValueObj = {}
  defaultValueArray.map((item) => {
    defaultValueObj[item.prop] = item.value
  })
  return defaultValueObj
}
// 按钮操作
const rowOperateion = (type, row = {}) => {
  const { addBtn, delBtn, editBtn } = operateBtn.value
  dialogTitle.value = TITLE_ENMU[type]
  operateType.value = type
  if (type === 'ADD' && addBtn.isShow) {
    const param = props.addBefore()
    const defaultValue = getDefaultValue()
    visible.value = true
    formAddAndEditModel.value = Object.assign(
      {},
      { ...props.defaultParams, ...row, ...defaultValue, ...param }
    )
  } else if (type === 'EDIT' && editBtn.isShow) {
    const { detail = {} } = rebaseOptions.value.request
    if (detail && detail.url) {
      getDetail(row)
    } else {
      if (editBtn.path) {
        const url = formatUrlParams(editBtn.path, row)

        const query = {}
        if (editBtn.params) {
          for (const key in editBtn.params) {
            query[key] = row[key]
          }
        }
        const _path = `${url}?${Qs.stringify(query)}`
        console.log(_path)
        router.push(_path)
      } else {
        // 处理 inputNumber 非数字
        const inputNumberArr = []
        rebaseOptions.value.columns?.forEach((item) => {
          if (item.type && item.type === 'inputNumber') {
            inputNumberArr.push(item.prop)
          }
        })

        const _row = JSON.parse(JSON.stringify(row))
        inputNumberArr.forEach((item) => {
          _row[item] = typeof _row[item] !== 'number' ? Number(_row[item]) : _row[item]
        })

        formAddAndEditModel.value = _row
        visible.value = true
      }
    }
  } else if (type === 'DEL' && delBtn.isShow) {
    confirmEl('您确定要删除该条数据吗？').then(() => {
      remove(row)
    })
  } else {
    // emit(type, row)
  }
}
// 获取数据详情
const getDetail = async (params) => {
  const url = formatUrlParams(rebaseOptions.value.request.detail.url, params)
  const { code, data, msg } = await getCommonTableList({
    ...rebaseOptions.value.request.detail,
    url
  }).catch(() => {
    ElMessage.error(msg)
  })
  if (code === 200) {
    formAddAndEditModel.value = Object.assign({}, data)
    visible.value = true
  }
}
// 搜索
const searchSubmit = (formValue) => {
  pagination.value.currentPage = 1
  if (props.autoLoad) {
    getTableList(formValue)
  } else {
    emit('search-form-data', formValue)
  }
}

// 多修操作
const handleSelectionChange = (val) => {
  selectedData.value = val
  emit('selection-change', val)
}
// 删除
const remove = async (params) => {
  const url = formatUrlParams(rebaseOptions.value.request.del.url, params)
  const { code, msg } = await getCommonTableList({
    ...rebaseOptions.value.request.del,
    url
  }).catch(() => {
    ElMessage.error(msg)
  })
  if (code === 200) {
    getTableList()
  } else {
    ElMessage({ type: 'error', zIndex: 9999, message: msg })
  }
}
// 编辑和修改
const addAndEditFun = async (type, params) => {
  const url = formatUrlParams(rebaseOptions.value.request[type].url, params)
  const { code, msg } = await getCommonTableList({
    ...rebaseOptions.value.request[type],
    url,
    params
  }).catch(() => {
    ElMessage.error(msg)
  })
  if (code === 200) {
    getTableList()
    visible.value = false
  } else {
    ElMessage({ type: 'error', zIndex: 9999, message: msg })
  }
}

// 弹框提交
const handleDialogSubmit = async () => {
  const valid = await dialogFormRef.value.validateField()
  if (!valid) return
  switch (operateType.value) {
    case 'ADD': {
      const params = props.addBefore(formAddAndEditModel.value)
      console.log(params)
      addAndEditFun('add', params)
      break
    }
    case 'EDIT': {
      const params = props.updateBefore(formAddAndEditModel.value)
      addAndEditFun('edit', params)
      break
    }
  }
}
// 如果有上传就要更新列表
const updateImportExcelStatus = computed(() => {
  const dictData = store.getters['useUploadDialog/getUploadSuccess']
  return dictData
})
watch(
  updateImportExcelStatus,
  (n, o) => {
    const { name } = route.value
    console.log(n, o, name, n.date !== o.date, name, n.routerName)
    if (n.date !== o.date && name === n.routerName) {
      requestBefore()
    }
  },
  { deep: true }
)

onMounted(async () => {
  if (props.autoLoad) {
    isMountedCompleted = true
    requestBefore()
  }
})
onActivated(() => {
  if (!isMountedCompleted && props.autoLoad) {
    requestBefore()
  }
})
watchEffect(() => {
  mergeFomrChildrenItem()
})

defineExpose({
  getSearchParams,
  loading,
  requestBefore
})
</script>

<style lang="scss" scoped></style>
