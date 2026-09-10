import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '凭证录入',
  icon: '',
  name: 'customVoucherIndex',
  sort: 4
}
export const excludes = [
  '手工凭证-认领',
  '手工凭证-冲销',
  '手工凭证-修改网银编号',
  '手工凭证-修改入账日期'
]

const isOpea = (item) => {
  if (!item.subSceneType) {
    return !excludes.includes(item.sceneName)
  }
  return true
}

const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  add: {
    title: '新增',
    isDisabled: false,
    redirectUrl: '/customVoucher/customVoucherAdd',
    type: 'primary'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    params: {
      url: '/engine/finance/manual/importTemplate',
      title: '凭证录入',
      open: true,
      templateUrl: '/engine/finance/manual/export'
    }
  },

  writeOff: {
    title: '冲销',
    url: '/engine/finance/manual/writeOff',
    type: 'warning',
    condition: (item) =>
      isOpea(item) && item.isWriteOff === '0' && ['3', '4'].includes(item.processStatus)
  },
  copy: {
    title: '复制',
    url: '/engine/finance/manual/copy',
    condition: (item) => isOpea(item)
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/manual/submit',
    condition: (item) => isOpea(item) && ['1', '5'].includes(item.processStatus)
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/manual/withdraw',
    condition: (item) =>
      !excludes.includes(item.sceneName) && item.subSceneType && item.processStatus === '2'
  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/finance/manual/deleteByIds',
    condition: (item) => isOpea(item) && ['1', '5'].includes(item.processStatus)
  }
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8,
  // isSearch: true,
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/manual/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    { label: '凭证号', prop: 'voucherNum', width: 200, search: true },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'company'),
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 250,
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      width: 200,
      prop: 'voucherDate',
      label: '记账日期',
      type: 'date',
      search: true,
      cover: ['startVoucherDate', 'endVoucherDate'],
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    { width: 200, prop: 'businessDate', label: '业务日期', type: 'date' },
    { label: '会计期间', prop: 'periodCode', width: 200 },
    {
      width: 200,
      prop: 'voucherType',
      search: true,
      type: 'select',
      label: '凭证类型',
      option: dictMappingToArray(dictData, 'sys_voucher_type'),
      render: (row) => {
        return (
          (row.voucherType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_voucher_type', row.voucherType))) ||
          ''
        )
      }
    },
    { width: 200, prop: 'voucherSummary', label: '摘要内容' },
    {
      label: '场景名称',
      prop: 'sceneName',
      width: 200,
      search: true,
      type: 'select',
      request: {
        url: '/engine/scene/list',
        method: 'post'
      },
      attrs: {
        filterable: true
      },
      keyValue: { label: 'sceneName', value: 'sceneName' }
    },
    {
      width: 200,
      prop: 'subSceneType',
      label: '细分场景',
      type: 'select',
      search: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'sys_sub_scene_type'),
      render: ({ subSceneType }) => {
        return (
          (subSceneType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_sub_scene_type', subSceneType))) ||
          ''
        )
      }
    },

    { width: 200, prop: 'rate', label: '汇率' },
    {
      width: 200,
      prop: 'currencyCode',
      label: '币种',
      render: (row) => {
        return (
          (row.currencyCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyCode))) ||
          ''
        )
      }
    },
    // { label: "附件数量", prop: "attachmentNum", width: 200, },
    {
      prop: 'processStatusList',
      label: '处理状态',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render: (row) => {
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'
    }
  ]
})
