import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '应收服务费合同分摊结果',
  icon: '',
  name: 'consultationFeesDetail'
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
      url: '/engine/finance/service-fee/detail/page',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'systemCodeList',
    label: '所属系统',
    type: 'select',
    hide: true,
    option: dictMappingToArray(dictData, 'sys_form_source'),
    search: true,
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    render (row) {
      return (row.systemCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) || ''
    }
  }, {
    prop: 'businessDate',
    label: '计提月份',
    type: 'date',
    search: true,
    attrs: {
      type: 'month'
    },
    width: 200
  }, {
    prop: 'accountDate',
    label: '记账日期',
    type: 'date',
    width: 200,
    attrs: {
      type: 'date',
      format: 'YYYY-MM',
      'value-format': 'YYYY-MM'
    }
  },
  {
    prop: 'businessDate1',
    label: '月份',
    type: 'date',
    width: 200,
    hide: true,
    search: true,
    cover: ['businessStartDate', 'businessEndDate'],
    attrs: {
      type: 'monthrange',
      'range-separator': '至',
      'start-placeholder': '开始月份',
      'end-placeholder': '截止月份',
      'value-format': 'YYYY-MM-DD'
    }
  },

  {
    prop: 'contractCode',
    label: '合同编号',
    width: 200
  },
  {
    prop: 'serviceFeeNo',
    label: '服务协议号',
    width: 200
  },
  {
    prop: 'clientCode',
    label: '客户编码',
    width: 200
  },

  {
    prop: 'clientName',
    label: '客户名称',
    width: 200
  },

  {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    tooltip: true,
    minWidth: 250,
    option: dictMappingToArray(dictData, 'company'),
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },
  {
    prop: 'orgIdList',
    label: '签约主体',
    type: 'select',
    search: true,
    hide: true,
    tooltip: true,
    attrs: {
      multiple: true,
      filterable: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'company')
  },
  {
    prop: 'serviceOrgIdList',
    label: '服务费签约主体',
    type: 'select',
    search: true,
    hide: true,
    tooltip: true,
    attrs: {
      multiple: true,
      filterable: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'company')
  },
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
    prop: 'serviceOrgId',
    label: '服务费签约主体',
    option: dictMappingToArray(dictData, 'company'),
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    },
    width: 200

  },
  {
    prop: 'allocationMethod',
    label: '分摊分式',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'allocation_method'),
    render (row) {
      return (row.allocationMethod && h(ElTag, () => dictMappingLabel(dictData, 'allocation_method', row.allocationMethod))) || ''
    },
    width: 200
  },

  {
    prop: 'businessName',
    label: '业务类型',
    width: 200
  },
  {
    prop: 'leaseDateStart',
    label: '会计起租日',
    type: 'date',
    width: 200
  },
  {
    prop: 'leaseDateEnd',
    label: '合同约定到期日',
    type: 'date',
    width: 200
  },
  {
    prop: 'observedExpirationDate',
    label: '观察期到期日',
    type: 'date',
    width: 200
  },

  {
    prop: 'serviceFeeAllocationNoTax',
    label: '应分摊的服务费收入(税后)',
    width: 200,
    render (row) {
      return toThousands(row.serviceFeeAllocationNoTax)
    }
  },
  {
    prop: 'lastMonthServiceFeeAllocationNoTax',
    label: '上月服务费应分摊金额（税后）',
    width: 300,
    render (row) {
      return toThousands(row.lastMonthServiceFeeAllocationNoTax)
    }
  },

  {
    prop: 'reclassificationAdjustmentNoTaxAmount',
    label: '本月重分类调整(税后)',
    width: 200,
    render (row) {
      return toThousands(row.reclassificationAdjustmentNoTaxAmount)
    }
  },

  {
    prop: 'accrualType',
    label: '计提类型',
    option: dictMappingToArray(dictData, 'accrual_type'),
    render (row) {
      return (row.accrualType && h(ElTag, () => dictMappingLabel(dictData, 'accrual_type', row.accrualType))) || ''
    },
    width: 200
  },

  {
    prop: 'beforeXYearMonthAmount',
    label: '本期以前',
    width: 200,
    render (row) {
      return toThousands(row.beforeXYearMonthAmount)
    }
  },

  {
    prop: 'xyearMonthAdjustmentAmount',
    label: '本期调整',
    width: 200,
    render (row) {
      return toThousands(row.xyearMonthAdjustmentAmount)
    }
  },
  {
    prop: 'allocationAfterXYearMonthBalance',
    label: '本期摊销后余额',
    width: 200,
    render (row) {
      return toThousands(row.allocationAfterXYearMonthBalance)
    }
  },

  {
    prop: 'periods',
    label: '本期分摊期数',
    width: 200
  },

  {
    prop: 'accumulatedAccruedAmount',
    label: '累计已计提金额',
    width: 200,
    render (row) {
      return toThousands(row.accumulatedAccruedAmount)
    }
  },
  {
    prop: 'notAccruedAmount',
    label: '实际未计提金额',
    width: 200,
    render (row) {
      return toThousands(row.notAccruedAmount)
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
    prop: 'financialContractStatusList',
    label: '特殊合同状态',
    width: 200,
    search: true,
    type: 'select',
    attrs: {
      multiple: true,
      filterable: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'financial_contract_status')

  },
  {
    prop: 'financialContractStatus',
    label: '特殊合同状态',
    width: 200,
    type: 'select',
    render (row) {
      return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
    }
  },

  {
    prop: 'processStatus',
    label: '处理状态',
    width: 100,
    render (row) {
      return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
    }
  },
  {
    prop: 'voucherStatus',
    label: '计提凭证状态',
    type: 'select',
    search: true,
    hide: true,
    option: dictMappingToArray(dictData, 'voucher_status'),
    // render (row) {
    //   return (row.allocationCompletionMark && h(ElTag, () => dictMappingLabel(dictData, 'voucher_status', row.allocationCompletionMark))) || ''
    // },
    width: 200
  },
  {
    prop: 'allocationCompletionMark',
    label: '分摊完结标记',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'allocation_completion_mark'),
    render (row) {
      return (row.allocationCompletionMark && h(ElTag, () => dictMappingLabel(dictData, 'allocation_completion_mark', row.allocationCompletionMark))) || ''
    },
    width: 200
  },

  {
    prop: 'exceptionTypeList',
    label: '异常类型',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'exception_type'),
    width: 200
  },
  {
    prop: 'exceptionType',
    label: '异常类型',
    option: dictMappingToArray(dictData, 'exception_type'),
    render (row) {
      return (row.exceptionType && h(ElTag, () => dictMappingLabel(dictData, 'exception_type', row.exceptionType))) || ''
    },
    width: 200
  },

  {
    prop: 'operation',
    width: 150,
    label: '操作',
    fixed: 'right',
    display: false

  }]
})
