import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '线下合同录入',
  icon: '',
  name: 'offlineContractEnter'
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
      url: '/engine/finance/offline-contract/importData',
      title: '线下合同录入',
      open: true,
      templateUrl: '/engine/finance/offline-contract/importTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/offline-contract/export',
    isParams: true,
    fetchKey: 'idList',
    filename: '线下合同录入.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/offline-contract/voucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/offline-contract/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/offline-contract/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/finance/offline-contract/delete'
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
  dialogSpan: 12,
  span: 8,
  btnConfig: btnConfig,
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
      url: '/engine/finance/offline-contract/page',
      method: 'post'
    },
    add: {
      url: '/engine/finance/contract-status-record/save',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,
      rules: [{
        required: true,
        message: '请输入合同编码',
        trigger: 'change'
      }],
      width: 200
    }, {
      prop: 'contractName',
      label: '合同名称',
      rules: [{
        required: true,
        message: '请输入合同名称',
        trigger: 'change'
      }],
      width: 200
    },
    {
      prop: 'clientCode',
      label: '客户编号',
      width: 200
    }, {
      prop: 'clientName',
      label: '客户名称',
      search: true,
      width: 200
    },
    {
      prop: 'contractCodeM',
      label: '主合同编号',
      width: 200
    }, {
      prop: 'clientType',
      label: '客户类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'sys_client_type'),
      render (row) {
        return (row.clientType && h(ElTag, () => dictMappingLabel(dictData, 'sys_client_type', row.clientType))) || ''
      }
    }, {
      prop: 'payMethod',
      label: '还款标识',
      width: 200,
      render (row) {
        return (row.payMethod && h(ElTag, () => dictMappingLabel(dictData, 'pay_method', row.payMethod))) || ''
      }
    },
    {
      prop: 'currencyType',
      label: '币种',
      search: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      rules: [{
        required: true,
        message: '请选择币种',
        trigger: ['change']
      }],
      render (row) {
        return (row.currencyType && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) || ''
      }
    },
    {
      prop: 'leaseDateStart',
      label: '起租日',
      width: 200
    },
    {
      prop: 'leaseDateEnd',
      label: '到期日',
      width: 200
    },
    {
      prop: 'leaseType',
      label: '租赁类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'lease_type'),
      rules: [{
        required: true,
        message: '请选择租赁类型',
        trigger: ['change']
      }],
      render (row) {
        return (row.leaseType && h(ElTag, () => dictMappingLabel(dictData, 'lease_type', row.leaseType))) || ''
      }
    },
    {
      prop: 'taxRate',
      label: '税率(%)',
      width: 200
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      width: 200,
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')

    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      minWidth: 250,
      tooltip: true,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'invoiceType',
      label: '发票类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'invoice_type'),
      rules: [{
        required: true,
        message: '请选择租赁类型',
        trigger: ['change']
      }],
      render (row) {
        return (row.invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'invoice_type', row.invoiceType))) || ''
      }
    },
    {
      prop: 'incomeCalculate',
      label: '收益计算',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'is_calculate_earnings'),
      rules: [{
        required: true,
        message: '请选择收益计算',
        trigger: ['change']
      }],
      render (row) {
        return (row.incomeCalculate && h(ElTag, () => dictMappingLabel(dictData, 'is_calculate_earnings', row.incomeCalculate))) || ''
      }
    },
    {
      prop: 'incomeProvisionMethod',
      label: '收益计提方式',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'accrual_method'),
      rules: [{
        required: true,
        message: '请选择租赁类型',
        trigger: ['change']
      }],
      render (row) {
        return (row.incomeProvisionMethod && h(ElTag, () => dictMappingLabel(dictData, 'accrual_method', row.incomeProvisionMethod))) || ''
      }
    },
    {
      prop: 'invoicingFlag',
      label: '开票标识',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'invoice_flag'),
      rules: [{
        required: true,
        message: '请选择租赁类型',
        trigger: ['change']
      }],
      render (row) {
        const label = row.invoicingFlag === '0' ? '开票' : '计提'
        return (row.invoicingFlag && h(ElTag, () => label)) || ''
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
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'

    }]
})
