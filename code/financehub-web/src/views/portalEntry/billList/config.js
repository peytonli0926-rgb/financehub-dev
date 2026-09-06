import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '我的单据',
  icon: '',
  name: 'billList'
}

const documentTypeFallback = {
  SYJT: '收益计提'
}

const documentStatusFallback = {
  0: '未录入',
  1: '已录入',
  2: '已提交',
  3: '已复核',
  4: '已传至金蝶',
  5: '已拒绝',
  6: '已冲销',
  7: '已撤回',
  9: '暂存'
}

const mergeOptions = (dictOptions, fallback) => {
  const options = [...dictOptions]
  Object.entries(fallback).forEach(([value, label]) => {
    const current = options.find((item) => String(item.value) === value)
    if (current) current.label = label
    else options.push({ value, label })
  })
  return options
}

const getChineseLabel = (dictData, dictType, value, fallback) => {
  return fallback[String(value)] || dictMappingLabel(dictData, dictType, value)
}

const commonColumns = (dictData) => [
  {
    prop: 'documentType',
    label: '单据类型',
    search: true,
    type: 'select',
    option: mergeOptions(dictMappingToArray(dictData, 'document_type'), documentTypeFallback),
    render: ({ documentType }) => {
      return (
        (documentType &&
          h(ElTag, () => getChineseLabel(dictData, 'document_type', documentType, documentTypeFallback))) ||
        ''
      )
    }
  },
  {
    prop: 'documentStatus',
    label: '单据状态',
    search: true,
    type: 'select',
    option: mergeOptions(dictMappingToArray(dictData, 'document_status'), documentStatusFallback),
    render: ({ documentStatus }) => {
      return (
        (documentStatus &&
          h(ElTag, () => getChineseLabel(dictData, 'document_status', documentStatus, documentStatusFallback))) ||
        ''
      )
    }
  },
  { prop: 'submitterNum', label: '提交人工号', search: true },
  { prop: 'submitterName', label: '提交人姓名', search: true },
  { prop: 'approverNum', label: '审批人工号', search: true },
  { prop: 'approverName', label: '审批人姓名', search: true },
  { prop: 'submitDate', label: '提交时间' },
  { prop: 'approverDate', label: '审批时间' }
]

const commonBase = {
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  }
}

// 我的单据
export const myBillOptionsConfig = (router, dictData) => ({
  ...commonBase,
  isSelection: false,
  request: {
    // 请求参数
    list: {
      url: '/engine/approve/approve/myDocumentByPage',
      method: 'post'
    }
  },
  columns: [
    ...commonColumns(dictData),
    {
      prop: 'operation',
      width: 100,
      label: '操作',
      display: false
    }
  ]
})

// 我的审批
export const myApproveOptionsConfig = (router, dictData) => ({
  ...commonBase,
  isSelection: false,
  request: {
    // 请求参数
    list: {
      url: '/engine/approve/approve/approvedByPage',
      method: 'post'
    }
  },

  columns: [
    ...commonColumns(dictData),
    {
      prop: 'operation',
      width: 210,
      label: '操作',
      display: false
    }
  ]
})

// 待我的审批
export const waitaApproveOptionsConfig = (router, dictData) => ({
  ...commonBase,
  request: {
    // 请求参数
    list: {
      url: '/engine/approve/approve/todoApproveByPage',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      getKey: 'id'
    },
    agreeAll: {
      type: 'success',
      title: '全部批量同意',
      level: 'S',
      url: '/engine/approve/approve/passAll',
      isDisabled: false
    },
    rejectAll: {
      type: 'danger',
      title: '全部批量拒绝',
      level: 'S',
      url: '/engine/approve/approve/refuseAll',
      isDisabled: false
    },
    agree: {
      type: 'success',
      title: '批量同意',
      level: 'S',
      url: '/engine/approve/approve/pass',
      condition: (data) => data.length !== 0
    },
    reject: {
      type: 'danger',
      title: '批量拒绝',
      level: 'S',
      url: '/engine/approve/approve/refuse',
      condition: (data) => data.length !== 0
    }
  },
  columns: [
    ...commonColumns(dictData),
    {
      prop: 'operation',
      width: 250,
      label: '操作',
      display: false
    }
  ]
})
