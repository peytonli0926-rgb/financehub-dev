import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

const processStatusOptions = [
  { value: '0', label: '未录入' },
  { value: '1', label: '已录入' },
  { value: '2', label: '已提交' },
  { value: '3', label: '复核通过' },
  { value: '4', label: '已传至金蝶' },
  { value: '5', label: '复核失败' },
  { value: '6', label: '已冲销' }
]

const processStatusLabel = (status) => {
  const option = processStatusOptions.find(item => item.value === String(status))
  return option ? option.label : '未知状态'
}

export default {
  hidden: false,
  title: '收益计提',
  icon: '',
  name: 'incomeProvision'
}

const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    params: {
      url: '/engine/finance/lease-income/importData',
      title: '收益计提',
      open: true,
      templateUrl: '/engine/finance/lease-income/importTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/lease-income/export',
    filename: '收益计提.xlsx',
    fetchKey: 'idList',
    isParams: true
  },
  month: {
    title: '生成本月收益计提',
    url: '/engine/finance/lease-income/generate',
    type: 'success',
    fetchKey: 'idList',
    isDisabled: false,
    isParams: true,
    // 后台计提采用异步任务，等待落库后再刷新列表，避免首次生成仍显示“暂无数据”。
    refreshDelay: 3000
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/lease-income/voucher',
    // 凭证在线程池中异步生成，待凭证头、分录及计提标识落库后再刷新。
    refreshDelay: 3000

  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/finance/lease-income/delete'
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/lease-income/submit',
    condition: (item) => ['1', '5'].includes(item.processStatus) && item.isGenerateVoucher === '1'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/lease-income/withdraw'
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
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  request: { // 请求参数
    list: {
      url: '/engine/finance/lease-income/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      hide: true,

      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '系统来源',
      prop: 'systemCodeList',
      width: 200,
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        filterable: true
      },
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_form_source')
    },

    {
      prop: 'orgId',
      label: '签约主体',
      tooltip: true,
      width: 250,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      label: '系统来源',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      render ({ systemCode }) {
        return (systemCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', systemCode))) || ''
      }

    },
    {
      prop: 'businessDate',
      label: '计提月份',
      search: true,
      type: 'date',
      attrs: {
        type: 'month',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      attrs: {
        type: 'date',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      }
    },
    {
      prop: 'leaseIncomeAmount',
      label: '租赁收益',
      render (row) {
        return toThousands(row.leaseIncomeAmount)
      }
    },
    {
      prop: 'unrealizedRevenue',
      label: '未实现收益',
      render (row) {
        return toThousands(row.unrealizedRevenue)
      }
    },

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
      option: processStatusOptions
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return row.processStatus !== null && row.processStatus !== undefined
          ? h(ElTag, () => processStatusLabel(row.processStatus))
          : ''
      }
    }, {
      prop: 'operation',
      width: 150,
      label: '操作',
      display: false

    }]
})
