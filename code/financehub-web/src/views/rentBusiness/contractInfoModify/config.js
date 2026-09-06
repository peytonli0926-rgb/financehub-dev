
import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '合同信息修改',
  icon: '',
  name: 'contractInfoModify'
}

const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },

  createjyjg: {
    title: '生成交易结构调整凭证',
    type: 'default',
    query: ['clientCode', 'orgId', 'contractCode'],
    redirectUrl: '/customVoucher/customVoucherAdd?',
    condition: (data) => (data.length === 1),
    source: 'data'
  },
  createchjh: {
    title: '生成偿还计划修改凭证',
    type: 'warning',
    url: '/engine/finance/contract-his/voucher',
    condition: (item) => (item.isPlanVoucher === '0')
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    params: {
      url: '/engine/finance/contract-his/importData',
      title: '合同信息修改',
      open: true,
      templateUrl: '/engine/finance/contract-his/importTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/contract-his/export',
    isParams: true,
    fetchKey: 'contractCodeList',
    getKey: 'contractCode',
    filename: '线下合同录入.xlsx'
  },

  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/contract-his/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/contract-his/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/finance/contract-his/delete'
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
      url: '/engine/finance/contract-his/page',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'contractCodeM',
      label: '主合同编号',
      width: 200
    },
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
      prop: 'clientName',
      label: '客户名称',
      search: true,
      width: 200
    },
    {
      prop: 'clientCode',
      label: '客户编号',
      width: 200
    },

    {
      prop: 'clientType',
      label: '客户类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'sys_client_type'),
      render (row) {
        return (row.clientType && h(ElTag, () => dictMappingLabel(dictData, 'sys_client_type', row.clientType))) || ''
      }
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
      prop: 'orgIdOrIdentityCard',
      label: '组织结构代码/身份证号',
      width: 200
    },

    {
      prop: 'leaseDateStart',
      label: '起租日',
      type: 'date',
      width: 200
    },
    {
      prop: 'leaseDateEnd',
      label: '到期日',
      type: 'date',
      width: 200
    },
    {
      prop: 'leaseType',
      label: '租赁类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'lease_type'),
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
      prop: 'currencyType',
      label: '币种',
      search: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render (row) {
        return (row.currencyType && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) || ''
      }
    },
    {
      prop: 'invoiceType',
      label: '租金发票类型',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'invoice_type'),
      render (row) {
        return (row.invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'invoice_type', row.invoiceType))) || ''
      }
    },
    {
      prop: 'invoicingFlag',
      label: '开票标识',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'invoice_flag'),
      render (row) {
        return (row.invoicingFlag && h(ElTag, () => dictMappingLabel(dictData, 'invoice_flag', row.invoicingFlag))) || ''
      }
    },
    {
      prop: 'incomeCalculate',
      label: '收益计算',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'is_calculate_earnings'),
      render (row) {
        const label = row.incomeCalculate === '0' ? '否' : '是'
        return (row.incomeCalculate && h(ElTag, () => label)) || ''
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
      prop: 'contractStatus',
      label: '业务合同状态',
      type: 'select',
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 200
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      width: 200,
      type: 'select',
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status'),
      render (row) {
        return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
      }
    },

    {
      prop: 'contractCreateDept',
      label: '合同出单部门',
      width: 200
    },
    {
      prop: 'systemCode',
      label: '所属系统',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render (row) {
        return (row.systemCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) || ''
      }
    },
    {
      prop: 'payableDeviceAmount',
      label: '设备金额',
      width: 200,
      render (row) {
        return toThousands(row.payableDeviceAmount)
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
      option: dictMappingToArray(dictData, 'modify_status')
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'modify_status', row.processStatus))) || ''
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'

    }]
})
