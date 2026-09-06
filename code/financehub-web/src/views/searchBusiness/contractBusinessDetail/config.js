
import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '合同详情',
  icon: '',
  name: 'contractBusinessDetail'
}

// 回笼计划
export const recollectPlanOptionsConfig = (query) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract/selectPageByContractCode',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      isDisabled: false,
      title: '下载',
      level: 'S',
      type: 'primary',
      routeQuery: ['id'],
      url: '/engine/finance/contract/repayPlanDownload',
      isParams: true,
      filename: '回笼计划.xlsx'
    }
  },
  columns: [{
    prop: 'versionDate',
    label: '版本日期',
    search: true,
    type: 'select',
    hide: true,
    request: {
      url: `/engine/finance/contract/selectPageByContractCode/${query.id}`,
      method: 'get'
    }

  }, { // 展示数据
    prop: 'periods',
    label: '计划期数',

    width: 200
  }, {
    prop: 'planDatePeriod',
    label: '计划还款日',
    width: 200
  }, { // 展示数据
    prop: 'rentAmount',
    label: '计划租金',
    width: 200,
    render: ({ rentAmount }) => toThousands(rentAmount)
  }, {
    prop: 'principalAmount',
    label: '计划本金',
    render: ({ principalAmount }) => toThousands(principalAmount),
    width: 200
  }, {
    prop: 'interestAmount',
    label: '计划利息',
    render: ({ interestAmount }) => toThousands(interestAmount),
    width: 200
  }, {
    prop: 'actualRepaymentDate',
    label: '实收日期',
    width: 200
  }, {
    prop: 'actualRepaymentRentAmount',
    label: '实收租金',
    width: 200,
    render: ({ actualRepaymentRentAmount }) => toThousands(actualRepaymentRentAmount)
  }, {
    prop: 'actualRepaymentInteresAmount',
    label: '实收利息',
    width: 200,
    render: ({ actualRepaymentInteresAmount }) => toThousands(actualRepaymentInteresAmount)
  },
  {
    prop: 'actualRepaymentPrincipalAmount',
    label: '实收本金',
    width: 200,
    render: ({ actualRepaymentPrincipalAmount }) => toThousands(actualRepaymentPrincipalAmount)
  },
  
  {
    prop: 'adjustmentAmount',
    label: '调整金额',
    width: 200,
    render: ({ adjustmentAmount }) => toThousands(adjustmentAmount)
  },
  
  {
    prop: 'endingAmortizedCostBalance',
    label: '期末摊余成本差额',
    width: 200,
    render: ({ endingAmortizedCostBalance }) => toThousands(endingAmortizedCostBalance)
  },
  
  {
    prop: 'taBalance',
    label: '租赁收入差额',
    width: 200,
    render: ({ taBalance }) => toThousands(taBalance)
  },
  
  {
    prop: 'actualRepaymentPrincipalAmount',
    label: 'TA发生额',
    width: 200,
    render: ({ actualRepaymentPrincipalAmount }) => toThousands(actualRepaymentPrincipalAmount)
  },
  
  
  {
    prop: 'bankNum',
    label: '网银编号',
    width: 200
  }, {
    prop: 'settlementTerms',
    label: '结算方式',
    width: 200
  }
  ]
})

// 实际开票/计提情况
export const ticketAndJitiOptionsConfig = (router, dictData) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
  request: { // 请求参数
    list: {
      url: '/etl/financial/invoice-claim/selectContractInvoiceByPage',
      method: 'post'
    }
  },
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  btnConfig: {
    export: {
      isDisabled: false,
      title: '下载',
      level: 'T',
      type: 'primary',
      routeQuery: ['id'],
      url: '/etl/financial/invoice-claim/download',
      isParams: true,
      filename: '实际开票/计提情况.xlsx'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '合同名称', prop: 'contractName', width: 200 },
    {
      label: '租赁类型',
      prop: 'leaseType',
      width: 200,
      render ({ leaseType }) {
        return (leaseType && h(ElTag, () => dictMappingLabel(dictData, 'lease_type', leaseType))) || ''
      }
    },
    { label: '开票/计提税率', prop: 'backTaxRate', width: 200 },
    { label: '开票/计提项目', prop: 'productName', width: 200 },
    {
      label: '提前开票类型',
      prop: 'invoiceType',
      width: 200,
      render ({ invoiceType }) {
        return (invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'invoice_type', invoiceType))) || ''
      }
    },
    { label: '对应期数', prop: 'periodNum', width: 200 },
    {
      label: '应开票主体',
      prop: 'orgIdName',
      width: 200
      // render ({ sellerTaxCode }) {
      //   return dictMappingLabel(dictData, 'company', sellerTaxCode)
      // }
    },
    { label: '应开票对象', prop: 'clientName', width: 200 },
    { label: '应收日期', prop: 'planRepayDate', type: 'date', width: 200 },
    { label: '收款日期', prop: 'planDate', type: 'date', width: 200 },
    { label: '应收本金', prop: 'principalAmount', width: 200, render: ({ principalAmount }) => toThousands(principalAmount) },
    { label: '应收利息', prop: 'interestAmount', width: 200, render: ({ interestAmount }) => toThousands(interestAmount) },
    { label: '应收租金/其他款项', prop: 'rentReceivableAmount', width: 200 },
    { label: '应开票/计提金额', prop: 'backTaxAmount', width: 200, render: ({ backTaxAmount }) => toThousands(backTaxAmount) },
    { label: '应开票/计提税额', prop: 'backTaxValue', width: 200, render: ({ backTaxValue }) => toThousands(backTaxValue) },
    {
      label: '实际开票主体',
      prop: 'sellerTaxCode',
      width: 200,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.sellerTaxCode)
      }
    },
    { label: '实际开票对象', prop: 'payer', width: 200 },
    { label: '实际开票/计提金额', prop: 'taxAmount', width: 200, render: ({ taxAmount }) => toThousands(taxAmount) },
    { label: '实际开票/计提税率', prop: 'taxRate', width: 200 },
    { label: '实际开票/计提税额', prop: 'taxValue', width: 200, render: ({ taxValue }) => toThousands(taxValue) },
    { label: '开票/计提日期', prop: 'documentDate', type: 'date', width: 200 },
    { label: '发票号码', prop: 'invoiceNumber', width: 200 },
    { label: '异常类型', prop: 'exceptionType', width: 200 },
    { label: '备注', prop: 'comments', width: 200 },

    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false

    }
  ]
})

// 交易信息
export const businessDealInfoOptionsConfig = (router, dictData) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract/transactionByPage',
      method: 'post'
    }
  },
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  columns: [{ // 展示数据
    prop: 'businessDate',
    label: '业务日期',
    type: 'date'
  },
  {
    prop: 'voucherDate',
    label: '记账日期',
    type: 'date',
  },
  {
    prop: 'periodCode',
    label: '会计期间',
  },

  {
    prop: 'sceneName',
    label: '业务系统场景名称',
  }, {
    prop: 'clientCode',
    label: '客户编号',
  },
  {
    prop: 'clientName',
    label: '客户名称',
  },

  // {
  //   prop: 'buybackRentAmount',
  //   label: '回笼租金',
  //   width: 200,
  //   render: ({ buybackRentAmount }) => toThousands(buybackRentAmount)
  // },
  // {
  //   prop: 'buybackLossesAmount',
  //   label: '回笼本金',
  //   width: 200,
  //   render: ({ buybackLossesAmount }) => toThousands(buybackLossesAmount)
  // },
  // {
  //   prop: 'buybackInterestAmount',
  //   label: '回笼利息',
  //   width: 200,
  //   render: ({ buybackInterestAmount }) => toThousands(buybackInterestAmount)
  // },
  // {
  //   prop: 'periods',
  //   label: '回笼期数',
  //   width: 200
  // },
  // {
  //   prop: 'recoveryPenaltyInterestAmount',
  //   label: '回收罚息',
  //   width: 200,
  //   render: ({ recoveryPenaltyInterestAmount }) => toThousands(recoveryPenaltyInterestAmount)
  // },
  // {
  //   prop: 'consultancyServiceAmount',
  //   label: '收取咨询服务费',
  //   width: 200,
  //   render: ({ consultancyServiceAmount }) => toThousands(consultancyServiceAmount)
  // },
  // {
  //   prop: 'receivableRentAmount',
  //   label: '应收租金',
  //   width: 200,
  //   render: ({ receivableRentAmount }) => toThousands(receivableRentAmount)
  // },
  // {
  //   prop: 'receivablePriInterestAmount',
  //   label: '应收本息',
  //   width: 200,
  //   render: ({ receivablePriInterestAmount }) => toThousands(receivablePriInterestAmount)
  // },
  // {
  //   prop: 'receivableInterestAmount',
  //   label: '应收利息',
  //   width: 200,
  //   render: ({ receivableInterestAmount }) => toThousands(receivableInterestAmount)
  // },
  // {
  //   prop: 'receivablePayment',
  //   label: '应收首付款',
  //   width: 200,
  //   render: ({ receivablePayment }) => toThousands(receivablePayment)
  // },
  {
    prop: 'operation',
    width: 120,
    label: '操作',
    fixed: 'right',
    display: false

  }
  ]
})

// 交叉销售分成
export const crossSaleOptionsConfig = (router, dictData) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract/salesBonusPage',
      method: 'post'
    }
  },
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  columns: [
    { label: '公司', prop: 'orgId', tooltip: true, render: ({ orgId }) => dictMappingLabel(dictData, 'company', orgId) },
    { label: '部门', prop: 'deptName' },
    { label: '员工', prop: 'staffName' },
    { label: '占比', prop: 'salesPercentage' },
    { label: '金额', prop: 'amount', render: ({ amount }) => toThousands(amount) }
  ]
})

// 合同科目余额信息
export const contractSubjectOptionsConfig = (router, dictData) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract/accountBalanceByPage',
      method: 'post'
    }
  },
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  columns: [
    { // 展示数据
      prop: 'businessDate',
      label: '业务日期',
      width: 200,
      type: 'date'
    },
    {
      prop: 'voucherDate',
      label: '记账日期',
      width: 200,
      type: 'date'
    },
    {
      prop: 'periodCode',
      label: '会计期间',
      width: 200
    },

    {
      prop: 'sceneName',
      label: '业务系统场景名称',
      width: 200
    },
    { label: '应收租金余额', prop: 'receivableRentBalance', width: 200, render: ({ receivableRentBalance }) => toThousands(receivableRentBalance) },
    { label: '应收租金发生额', prop: 'receivableRentAmount', width: 200, render: ({ receivableRentAmount }) => toThousands(receivableRentAmount) },
    { label: '未实现收益余额', prop: 'unrealizedRevenueBalance', width: 200, render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance) },
    { label: '未实现收益发生额', prop: 'unrealizedRevenueAmount', width: 200, render: ({ unrealizedRevenueAmount }) => toThousands(unrealizedRevenueAmount) },
    { label: '融资租赁收益余额', prop: 'leaseRevenueBalance', width: 200, render: ({ leaseRevenueBalance }) => toThousands(leaseRevenueBalance) },
    { label: '融资租赁收益发生额', prop: 'leaseRevenueAmount', width: 200, render: ({ leaseRevenueAmount }) => toThousands(leaseRevenueAmount) },
    { label: '减值准备余额', prop: 'depreciationReservesBalance', width: 200, render: ({ depreciationReservesBalance }) => toThousands(depreciationReservesBalance) },
    { label: '减值准备发生额', prop: 'depreciationReservesAmount', width: 200, render: ({ depreciationReservesAmount }) => toThousands(depreciationReservesAmount) },
    { label: '应收销项税余额', prop: 'receivableOuttaxBalance', width: 200, render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance) },
    { label: '应收销项税发生额', prop: 'receivableOuttaxAmount', width: 200, render: ({ receivableOuttaxAmount }) => toThousands(receivableOuttaxAmount) },
    { label: '应收服务费-销项税余额', prop: 'receivableServiceOuttaxBalance', width: 200, render: ({ receivableServiceOuttaxBalance }) => toThousands(receivableServiceOuttaxBalance) },
    { label: '应收服务费-销项税发生额', prop: 'receivableServiceOuttaxAmount', width: 200, render: ({ receivableServiceOuttaxAmount }) => toThousands(receivableServiceOuttaxAmount) },
    { label: '应付设备款余额', prop: 'payableDeviceBalances', width: 200, render: ({ payableDeviceBalances }) => toThousands(payableDeviceBalances) },
    { label: '应付设备款发生额', prop: 'payableDeviceAmounts', width: 200, render: ({ payableDeviceAmounts }) => toThousands(payableDeviceAmounts) },
    { label: '保证金余额', prop: 'lesseeMarginBalance', width: 200, render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance) },
    { label: '保证金发额', prop: 'lesseeMarginAmount', width: 200, render: ({ lesseeMarginAmount }) => toThousands(lesseeMarginAmount) }

  ]
})
