// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { /* dictMappingToArray, */ dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '应收服务费合同分摊',
  icon: '',
  name: 'consultationFeesPlan'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/service-fee/detail/plan',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'clientCode',
      label: '客户编号',
      width: 200,
      search: true
    },
    {
      prop: 'clientName',
      label: '客户名称',
      width: 200
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      width: 200
    },
    {
      prop: 'contractName',
      label: '合同名称',
      search: true,
      width: 200
    },
    {
      prop: 'overdueDays',
      label: '逾期天数',
      search: true,
      type: 'inputNumber',
      width: 200
    },
    {
      prop: 'overdueEarnings',
      label: '逾期收益',
      width: 200
    },
    {
      prop: 'periods',
      label: '期数',
      width: 200
    },

    {
      prop: 'rentAmount',
      label: '租金',
      width: 200,
      render (row) {
        return toThousands(row.rentAmount)
      }
    },

    {
      prop: 'planDate',
      label: '日期',
      type: 'date',
      width: 200
    },
    {
      prop: 'outflowAmount',
      label: '资金流出',
      width: 200,
      render (row) {
        return toThousands(row.outflowAmount)
      }
    },

    {
      prop: 'plannedPrincipal',
      label: '计划本金(不含税)',
      width: 200,
      render (row) {
        return toThousands(row.plannedPrincipal)
      }
    },

    {
      prop: 'principalAmount',
      label: '本金',
      width: 200,
      render (row) {
        return toThousands(row.principalAmount)
      }
    },

    {
      prop: 'principalTax',
      label: '本金-税金',
      width: 200,
      render (row) {
        return toThousands(row.principalTax)
      }
    },
    {
      prop: 'plannedInterest',
      label: '计划利息(不含税)',
      width: 200,
      render (row) {
        return toThousands(row.plannedInterest)
      }
    },
    {
      prop: 'interestAmount',
      label: '利息',
      width: 200,
      render (row) {
        return toThousands(row.interestAmount)
      }
    },

    {
      prop: 'interestTax',
      label: '利息-税金',
      width: 200
    },

    {
      prop: 'cashFlow',
      label: '现金流',
      width: 200,
      render (row) {
        return toThousands(row.cashFlow)
      }
    },

    {
      prop: 'openingAmortizedCost',
      label: '期初摊余成本',
      width: 200,
      render (row) {
        return toThousands(row.openingAmortizedCost)
      }
    },
    {
      prop: 'endingAmortizedCost',
      label: '期末摊余成本',
      width: 200,
      render (row) {
        return toThousands(row.endingAmortizedCost)
      }
    },
    {
      prop: 'actualDailyRate',
      label: '实际日利率',
      width: 200
      // render(row) {
      //   return `${ (row.actualDailyRate*100)}%`
      // }
    },

    {
      prop: 'rentalIncome',
      label: '租赁收入',
      width: 200,
      render (row) {
        return toThousands(row.rentalIncome)
      }
    },

    {
      prop: 'rentalIncomeOnBalance',
      label: '表内租赁收入',
      width: 200,
      render (row) {
        return toThousands(row.plannedPrincipal)
      }
    },
    {
      prop: 'rentalIncomeOffBalance',
      label: '表外租赁收入',
      width: 200,
      render (row) {
        return toThousands(row.rentalIncomeOffBalance)
      }
    },

    {
      prop: 'serviceFeeAmortizationRate',
      label: '服务费摊销利率',
      width: 200
    },

    {
      prop: 'xirrRate',
      label: 'XIRR',
      width: 200
    },

    {
      prop: 'serviceFeeAmortizationIncome',
      label: '服务费摊销收入',
      width: 200,
      render (row) {
        return toThousands(row.plannedPrincipal)
      }
    },

    {
      prop: 'actualRepaymentDate',
      label: '实际归还日期',
      type: 'date',
      width: 200
    },

    {
      prop: 'actualRepaymentPrincipalBalance',
      label: '实际归还本金余额',
      width: 200,
      render (row) {
        return toThousands(row.actualRepaymentPrincipalBalance)
      }
    },

    {
      prop: 'actualRepaymentPrincipalAmount',
      label: '实际归还本金发生额',
      width: 200,
      render (row) {
        return toThousands(row.actualRepaymentPrincipalAmount)
      }
    },

    {
      prop: 'actualRepaymentInteresBalance',
      label: '实际归还利息余额',
      width: 200,
      render (row) {
        return toThousands(row.actualRepaymentInteresBalance)
      }
    },

    {
      prop: 'actualRepaymentInteresAmount',
      label: '实际归还利息发生额',
      width: 200,
      render (row) {
        return toThousands(row.actualRepaymentInteresAmount)
      }
    },

    {
      prop: 'recaptureStatus',
      label: '回笼状态',
      render (row) {
        return dictMappingLabel(dictData, 'recycle_status', row.recaptureStatus)
      },
      width: 200
    }
  ]
})
