import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '核销',
  icon: '',
  name: 'verification'
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
      url: '/engine/verification/importTemplate',
      title: '核销上传',
      open: true,
      templateUrl: '/engine/verification/export'
    }
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/verification/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/verification/submit'
  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/verification/deleteByIds'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/verification/withdraw'
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
  btnConfig,
  request: { // 请求参数
    list: {
      url: '/engine/verification/page',
      method: 'post'
    }
  },
  columns: [{
    prop: 'accountDate',
    label: '记账日期',
    search: true,
    type: 'date',
    cover: ['startAccountDate', 'endAccountDate'],
    attrs: {
      type: 'daterange',
      'range-separator': '-',
      'start-placeholder': '开始时间',
      'end-placeholder': '结束时间',
      format: 'YYYY-MM-DD',
      'value-format': 'YYYY-MM-DD'
    }
  },

  {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    search: true,
    tooltip: true,
    attrs: {
      filterable: true
    },
    option: dictMappingToArray(dictData, 'company'),
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },

  {
    prop: 'verificationStatus',
    label: '核销状态',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'verification_status'),
    render (row) {
      return ((row.verificationStatus && h(ElTag, () => dictMappingLabel(dictData, 'verification_status', row.verificationStatus)))) || ''
    }
  },
  {
    prop: 'financialExpenseAmount',
    label: '财务核销敞口',
    render (row) {
      return toThousands(row.financialExpenseAmount)
    }
  },
  {
    prop: 'compensationProvisionAmount',
    label: '补提拨备',
    render (row) {
      return toThousands(row.compensationProvisionAmount)
    }
  }, {
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
    render (row) {
      return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
    }
  }, {
    prop: 'operation',
    width: 150,
    label: '操作',
    display: false
  }]
})
