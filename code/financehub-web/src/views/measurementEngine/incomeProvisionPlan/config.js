// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { /* dictMappingToArray,  dictMappingLabel, */toThousands } from '@/utils'

export default {
  hidden: true,
  title: '回收计划明细',
  icon: '',
  name: 'incomeProvisionPlan'
}

const amountColumn = (prop, label, width = 122) => ({
  prop,
  label,
  width,
  align: 'right',
  render: (row) => toThousands(row[prop])
})

const coreColumns = [
  { width: 64, prop: 'periods', label: '期次', align: 'center', fixed: 'left' },
  { width: 112, prop: 'planDate', type: 'date', label: '应还日期', align: 'center', fixed: 'left' },
  amountColumn('rentAmount', '应收租金'),
  amountColumn('principalAmount', '应收本金'),
  amountColumn('interestAmount', '应收含税利息', 132),
  amountColumn('cashFlow', '净现金流'),
  amountColumn('openingAmortizedCost', '期初摊余成本', 138),
  amountColumn('rentalIncome', '当期XIRR收益', 138),
  amountColumn('endingAmortizedCost', '期末摊余成本', 138),
  { width: 105, prop: 'xirrRate', label: 'XIRR(%)', align: 'right' },
  { width: 100, prop: 'recaptureStatus', label: '回收状态', align: 'center' },
  { width: 110, prop: 'onAndOffBalanceSheet', label: '表内/表外', align: 'center' }
]

const cashFlowColumns = [
  { width: 64, prop: 'periods', label: '期次', align: 'center', fixed: 'left' },
  { width: 112, prop: 'planDate', type: 'date', label: '应还日期', align: 'center', fixed: 'left' },
  amountColumn('plannedPrincipal', '本金现金流', 132),
  amountColumn('plannedInterest', '利息现金流（不含税）', 160),
  amountColumn('outflowAmount', '资金流出'),
  amountColumn('cashFlow', '净现金流'),
  amountColumn('rentAmount', '应收租金'),
  amountColumn('principalAmount', '应收本金'),
  amountColumn('interestAmount', '应收含税利息', 132),
  amountColumn('rentalIncome', '当期XIRR收益', 138),
  { width: 105, prop: 'xirrRate', label: 'XIRR(%)', align: 'right' }
]

export const optionsConfig = (router, dictData = {}, view = 'core') => ({
  isIndex: true, // 是否需要序号
  indexWidth: 52,
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  tableHeight: 480,
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
      filename: '回收计划明细.xlsx'
    }
  },
  columns: view === 'cashFlow' ? cashFlowColumns : coreColumns
})
