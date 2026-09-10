import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '单月收益计提明细',
  icon: '',
  name: 'incomeProvisionDetail'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
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
      url: '/engine/finance/lease-income/detail/page',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      type: 'primary',
      isDisabled: false,
      url: '/engine/finance/lease-income/detail/export',
      isParams: true,
      filename: '单月收益计提明细.xlsx'
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '计提月份',
      alias: '月份',
      type: 'date',
      search: true,
      width: 200,
      cover: ['businessStartDate', 'businessEndDate'],
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始月份',
        'end-placeholder': '结束月份',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      }
    },
    {
      label: '签约主体',
      prop: 'orgId',
      minWidth: 250,
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      label: '系统来源',
      prop: 'systemCodeList',
      width: 200,
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source')
    },

    {
      label: '系统来源',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      render ({ systemCode }) {
        const sourceName = ['CYCXT', 'RETAIL_FINANCE_LEASE'].includes(systemCode)
          ? '零售融资租赁业务系统'
          : dictMappingLabel(dictData, 'sys_form_source', systemCode)
        return (systemCode && h(ElTag, () => sourceName)) || ''
      }

    },
    { label: '合同编号', search: true, prop: 'contractCode', width: 200 },
    { label: '客户名称', search: true, prop: 'clientName', width: 200 },

    {
      label: '业务大类',
      prop: 'businessTypeList',
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'business_category'),

      hide: true,
      width: 200,
      search: true
    },
    {
      label: '业务大类',
      prop: 'businessType',
      width: 200,
      render ({ businessType }) {
        return (businessType && h(ElTag, () => dictMappingLabel(dictData, 'business_category', businessType))) || ''
      }
    },
    {
      prop: 'specialContractFlag',
      label: '特殊合同标识',
      type: 'select',
      width: 200,
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'is_sys_bool')
    },
    {
      prop: 'financialContractStatusList',
      label: '特殊合同状态',
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
      option: dictMappingToArray(dictData, 'financial_contract_status')
    },

    { label: '业务类型', prop: 'businessName', width: 200 },

    {
      prop: 'contractCodeList',
      label: '合同状态',
      type: 'select',
      search: true,
      hide: true,
      width: 200,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      }
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      }
    },
    {
      prop: 'currencyType',
      label: '币种',
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render (row) {
        return (row.currencyType && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) || ''
      }
    },
    { label: '会计起租日', prop: 'leaseDateStart', type: 'date', width: 200 },
    { label: '结束日', prop: 'leaseDateEnd', type: 'date', width: 200 },
    { label: '税率(%)', prop: 'taxRate', width: 200 },
    { label: '财务IRR', prop: 'xirrRate', width: 200 },
    { label: '未实现收益总额', prop: 'unrealizedRevenue', width: 200, render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue) },
    { label: '本月以前', prop: 'rentalIncomeBeforeTotal', width: 200, render: ({ rentalIncomeBeforeTotal }) => toThousands(rentalIncomeBeforeTotal) },
    { label: '本月', prop: 'rentalIncome', width: 200, render: ({ rentalIncome }) => toThousands(rentalIncome) },
    { label: '本月之后', prop: 'rentalIncomeAfterTotal', width: 200, render: ({ rentalIncomeAfterTotal }) => toThousands(rentalIncomeAfterTotal) },
    { label: '逾期天数', prop: 'overdueDays', width: 200 },
    { label: '上期实收期间(考虑期初期末)', prop: 'previousPaidPeriod', type: 'date', width: 230 },
    { label: '逾期收益', prop: 'overdueEarnings', width: 200 },
    { label: '当月逾期调整额', prop: 'overdueAdjustmentAmount', width: 200, render: ({ overdueAdjustmentAmount }) => toThousands(overdueAdjustmentAmount) },
    { label: '分润费分摊额', prop: 'profitSharingAllocationAmount', width: 200, render: ({ profitSharingAllocationAmount }) => toThousands(profitSharingAllocationAmount) },
    { label: '合计入账金额', prop: 'totalRecordedAmount', width: 200, render: ({ totalRecordedAmount }) => toThousands(totalRecordedAmount) },
    { label: '实收-已确认', prop: 'confirmedActualReceipt', width: 200, render: ({ confirmedActualReceipt }) => toThousands(confirmedActualReceipt) },
    { label: 'TA金额', prop: 'taAmount', width: 200, render: ({ taAmount }) => toThousands(taAmount) },
    {

      prop: 'manualLeaseFlag',
      label: '手工起租',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      render ({ manualLeaseFlag }) {
        return (manualLeaseFlag && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', manualLeaseFlag))) || ''
      },
      width: 200
    },
    {
      label: 'abs赎回',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      prop: 'absMark',
      width: 200,
      render ({ absMark }) {
        return (absMark && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', absMark))) || ''
      }
    },
    {
      prop: 'manualChangeMark',
      label: '手工调整标志',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      width: 200,
      render ({ manualChangeMark }) {
        return (manualChangeMark && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', manualChangeMark))) || ''
      }
    },
    {
      label: '当月是否计提',
      type: 'select',
      search: true,
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      prop: 'accrued',
      width: 200,
      render ({ accrued }) {
        return (accrued && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', accrued))) || ''
      }
    },
    {
      prop: 'incomeProvisionMethod',
      label: '计提方式',
      search: true,
      type: 'select',
      width: 200,
      option: dictMappingToArray(dictData, 'accrual_method'),
      render ({ incomeProvisionMethod }) {
        return (incomeProvisionMethod && h(ElTag, () => dictMappingLabel(dictData, 'accrual_method', incomeProvisionMethod))) || ''
      }
    },

    {
      label: '计提凭证状态',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'voucher_status'),
      prop: 'voucherStatus',
      width: 200,
      render ({ voucherStatus }) {
        return (voucherStatus && h(ElTag, () => dictMappingLabel(dictData, 'voucher_status', voucherStatus))) || ''
      }
    },
    {
      label: '开票标识',
      prop: 'invoicingFlag',
      width: 200,
      render (row) {
        return (row.invoicingFlag && h(ElTag, () => dictMappingLabel(dictData, 'invoice_flag', row.invoicingFlag))) || ''
      }
    },
    {
      label: '异常类型',
      prop: 'exceptionType',
      type: 'select',
      search: true,
      option: dictMappingToArray(dictData, 'exception_type'),
      width: 200,
      render (row) {
        return (row.exceptionType && h(ElTag, () => dictMappingLabel(dictData, 'exception_type', row.exceptionType))) || ''
      }
    },

    // {
    //   prop: 'financialContractStatus',
    //   label: '财务合同状态',
    //   width: 200,
    //   search: true,
    //   type: 'select',
    //   attrs: {
    //     filterable: true
    //   },
    //   option: dictMappingToArray(dictData, 'financial_contract_status'),
    //   render (row) {
    //     return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
    //   }
    // },

    // {

    //   prop: 'lastRepaymentDate',
    //   label: '最后还款日',
    //   type: 'date',
    //   width: 200
    // },

    {
      prop: 'repaymentSituation',
      label: '还款情况变化',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      width: 200,
      render ({ repaymentSituation }) {
        return (repaymentSituation && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', repaymentSituation))) || ''
      }
    },

    // { label: "计提凭证状态", prop: "", width: 200, },

    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false

    }
  ]
})
