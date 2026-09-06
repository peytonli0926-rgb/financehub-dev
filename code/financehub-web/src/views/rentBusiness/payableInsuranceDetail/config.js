import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '应付保险费详情',
  icon: '',
  name: 'payableInsuranceDetail'
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
      url: '/engine/finance/payable-insurance/detail/page',
      method: 'post'
    }
  },
  columns: [{
    prop: 'businessDate',
    label: '业务日期',
    type: 'date',
    width: 150,
    attrs: {
      type: 'date',
      // clearable: false,
      format: 'YYYY-MM-DD',
      'value-format': 'YYYY-MM-DD'
    }
  }, {
    prop: 'accountDate',
    label: '记账日期',

    type: 'date',
    width: 120,
    attrs: {
      type: 'date',
      clearable: false,
      format: 'YYYY-MM-DD',
      'value-format': 'YYYY-MM-DD'
    }
  }, {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    tooltip: true,
    minWidth: 300,
    option: dictMappingToArray(dictData, 'company'),
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
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
    prop: 'clientName',
    label: '客户名称',
    width: 200
  },

  {
    prop: 'contractStatusList',
    label: '业务合同状态',
    type: 'select',
    search: true,
    hide: true,
    attrs: {
      multiple: true,
      filterable: true
    },
    option: dictMappingToArray(dictData, 'business_contract_status'),
    width: 200
  },
  {
    prop: 'financialContractStatusList',
    label: '财务合同状态',
    search: true,
    width: 200,
    hide: true,
    type: 'select',
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'financial_contract_status')
  },

  {
    prop: 'contractStatus',
    label: '业务合同状态',
    type: 'select',
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
    render (row) {
      return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
    }
  },

  {
    prop: 'payableInsuranceBalanceReport',
    label: '保险费支付报表余额',
    render (row) {
      return toThousands(row.payableInsuranceBalanceReport)
    },
    width: 250
  },
  {
    prop: 'payableInsuranceEstimateBalanceOpening',
    label: '应付保险费-暂估期初余额',
    render (row) {
      return toThousands(row.payableInsuranceEstimateBalanceOpening)
    },
    width: 250
  },
  {
    prop: 'payableInsuranceEstimateAmountDebit',
    label: '应付保险费-暂估借方',
    render (row) {
      return toThousands(row.payableInsuranceEstimateAmountDebit)
    },
    width: 250
  },
  {
    prop: 'payableInsuranceEstimateAmountCredit',
    label: '应付保险费-暂估贷方',
    render (row) {
      return toThousands(row.payableInsuranceEstimateAmountCredit)
    },
    width: 250
  },
  {
    prop: 'payableInsuranceEstimateBalanceEnding',
    label: '应付保险费-暂估期末余额',
    render (row) {
      return toThousands(row.payableInsuranceEstimateBalanceEnding)
    },
    width: 250
  },
  {
    prop: 'payableInsuranceBalance',
    label: '应付保险费-暂估余额',
    render (row) {
      return toThousands(row.payableInsuranceBalance)
    },
    width: 250
  },
  {
    prop: 'carryoverAmount',
    label: '结转金额',
    render (row) {
      return toThousands(row.carryoverAmount)
    },
    width: 200
  },
  {
    prop: 'payableInsuranceBalanceActual',
    label: '保险费实际支付(不含税)',
    width: 250,
    render (row) {
      return toThousands(row.payableInsuranceBalanceActual)
    }
  },
  {
    prop: 'payableInsuranceBalanceLease',
    label: '起租时点保险费金额(不含税)',
    width: 250,
    render (row) {
      return toThousands(row.payableInsuranceBalanceLease)
    }
  },
  {
    prop: 'payableInsuranceBalanceStructure',
    label: '保险费交易结构调整(不含税)',
    width: 250,
    render (row) {
      return toThousands(row.payableInsuranceBalanceStructure)
    }
  },
  {
    prop: 'payableInsuranceBalanceWithdrawal',
    label: '合同撤销(不含税)',
    width: 200,
    render (row) {
      return toThousands(row.payableInsuranceBalanceWithdrawal)
    }
  },
  {
    prop: 'operation',
    width: 100,
    label: '操作',
    fixed: 'right',
    display: false
  }
  ]
})
