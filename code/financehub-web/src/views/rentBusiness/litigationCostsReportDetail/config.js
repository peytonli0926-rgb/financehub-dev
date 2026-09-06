
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '诉讼费报表详情',
  icon: '',
  name: 'litigationCostsReportDetail'
}
export const optionsReportConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  dialogSpan: 12,
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
      url: '/engine/court-cost/reportForm/details',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,
      fixed: 'left',
      width: 200
    },
    // {
    //   prop: 'contractName',
    //   label: '合同名称',
    //   width: 200
    // },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      minWidth: 250,
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
      prop: 'accountDate',
      label: '记账日期',
      width: 170,
      type: 'date'
    },
    {
      prop: 'voucherSummary',
      label: '凭证摘要',
      width: 250,
      tooltip: true
    },

    {
      label: '应收诉讼费科目',
      prop: 'receivable',
      align: 'center',
      children: [
        {
          prop: 'litigationExpensesBalance',
          label: '期初余额',
          render (row) {
            return toThousands(row.litigationExpensesBalance)
          },
          width: 200
        },
        {
          prop: 'receivableRentPay',
          label: '诉讼费支付',
          render (row) {
            return toThousands(row.receivableRentPay)
          },
          width: 200
        },
        {
          prop: 'receivableRentRecover',
          label: '诉讼费收回',
          render (row) {
            return toThousands(row.receivableRentRecover)
          },
          width: 200
        },
        {
          prop: 'transgerCostAmount',
          label: '诉讼费转费用',
          render (row) {
            return toThousands(row.transgerCostAmount)
          },
          width: 200
        },

        {
          prop: 'litigationExpensesEndBalance',
          label: '期末余额',
          render (row) {
            return toThousands(row.litigationExpensesEndBalance)
          },
          width: 200
        }
      ]
    },
    {
      prop: 'litigationl',
      label: '管理费用-诉讼费科目',
      width: 200,
      align: 'center',
      children: [{
        prop: 'litigationExpensesPayTotal',
        label: '支付金额',
        render (row) {
          return toThousands(row.litigationExpensesPayTotal)
        },
        width: 200
      },
      {
        prop: 'litigationExpensesRecoverTotal',
        label: '收回金额',
        render (row) {
          return toThousands(row.litigationExpensesRecoverTotal)
        },
        width: 200
      }]
    },

    {
      prop: 'receivable',
      label: '代收款项科目',
      width: 200,
      align: 'center',
      children: [
        {
          prop: 'receivableRentPayTotal',
          label: '支付金额',
          render (row) {
            return toThousands(row.receivableRentPayTotal)
          },
          width: 200
        },

        {
          prop: 'receivableRentRecoverTotal',
          label: '收回金额',
          render (row) {
            return toThousands(row.receivableRentRecoverTotal)
          },
          width: 200
        }]
    }
  ]
})
