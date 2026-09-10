// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { /* dictMappingToArray,  dictMappingLabel, */toThousands } from '@/utils'

export default {
  hidden: true,
  title: '单合同收益计提明细',
  icon: '',
  name: 'incomeProvisionPlan'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  tableHeight: 550,
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
      url: '/engine/finance/lease-income/detail/plan',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      isDisabled: false,
      url: '/engine/finance/repayment-plan-provision/export',
      routeQuery: ['contractCode'],
      isParams: true,
      filename: '单合同收益计提明细.xlsx'
    }
  },
  columns: [
    { width: 200, prop: 'contractCode', label: '合同编号' },
    { width: 200, prop: 'periods', label: '计划期数' },
    { width: 200, prop: 'planDate', type: 'date', label: '计划还款日' },
    { width: 200, prop: 'actualRepaymentDate', label: '实际还款日' },
    { width: 200, prop: 'rentAmount', label: '计划租金', render: ({ rentAmount }) => toThousands(rentAmount) },
    { width: 200, prop: 'principalAmount', label: '计划本金', render: ({ principalAmount }) => toThousands(principalAmount) },
    { width: 200, prop: 'interestAmount', label: '计划利息', render: ({ interestAmount }) => toThousands(interestAmount) },
    { width: 200, prop: 'actualRepaymentRentAmount', label: '实收租金', render: ({ actualRepaymentRentAmount }) => toThousands(actualRepaymentRentAmount) },
    { width: 200, prop: 'actualRepaymentInteresAmount', label: '实收利息', render: ({ actualRepaymentInteresAmount }) => toThousands(actualRepaymentInteresAmount) },
    { width: 200, prop: 'actualRepaymentPrincipalAmount', label: '实收本金', render: ({ actualRepaymentPrincipalAmount }) => toThousands(actualRepaymentPrincipalAmount) },
    { width: 200, prop: 'taReclassification', label: 'TA重分类' },
    { width: 200, prop: 'outflowAmount', label: '资金流出', render: ({ outflowAmount }) => toThousands(outflowAmount) },
    { width: 200, prop: 'plannedInterest', label: '资金流入', render: ({ plannedInterest }) => toThousands(plannedInterest) },
    { width: 200, prop: 'plannedPrincipal', label: '偿还本金', render: ({ plannedPrincipal }) => toThousands(plannedPrincipal) },
    { width: 200, prop: 'cashFlow', label: '净流量', render: ({ cashFlow }) => toThousands(cashFlow) },
    { width: 200, prop: 'endingAmortizedCost', label: '摊余成本(期末)', render: ({ endingAmortizedCost }) => toThousands(endingAmortizedCost) },
    { label: '差异', prop: 'differentAmount', width: 200, render: ({ differentAmount }) => toThousands(differentAmount) },

    { width: 200, prop: 'rentalIncome', label: '租赁收益', render: ({ rentalIncome }) => toThousands(rentalIncome) },
    { width: 200, prop: 'profitSharingAllocationAmount', label: '分润费分摊额', render: ({ profitSharingAllocationAmount }) => toThousands(profitSharingAllocationAmount) },
    { width: 200, prop: 'xirrRate', label: 'XIRR(实际利率)' },
    { width: 200, prop: 'recaptureStatus', label: '回笼情况' },
    { width: 200, prop: 'overdueDays', label: '系统逾期天数' },
    { width: 200, prop: 'laborOverdueDays', label: '人工逾期天数' },
    { width: 200, prop: 'rentalIncomeOffBalance', label: '逾期收益' },
    { width: 200, prop: 'rentalIncomeOnBalanceConfirmed', label: '已确认逾期收益(表外)' },
    { width: 200, prop: 'accumulatedActualRepaymentInteresAmount', label: '累计实收利息（不含税）', render: ({ accumulatedActualRepaymentInteresAmount }) => toThousands(accumulatedActualRepaymentInteresAmount) },
    { width: 350, prop: 'paidInHandlingFeesAddOtherIncomeSubCosts', label: '实收手续费+实收其他收入-实付成本', render: ({ paidInHandlingFeesAddOtherIncomeSubCosts }) => toThousands(paidInHandlingFeesAddOtherIncomeSubCosts) },
    { width: 200, prop: 'rentalIncomeOffBalanceConfirmed', label: '已确认租赁收益(表内)' }
  ]
})
