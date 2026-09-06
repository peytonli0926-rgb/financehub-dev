
import { toThousands } from '@/utils'

export default {
  hidden: true,
  title: '长期应收款-单合同详情',
  icon: '',
  name: 'longTermReceivablesDetail'
}

// 转入登记
export const optionsConfigTurnInto = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/long-repayment-plan/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      type: 'primary',
      url: '/engine/finance/long-repayment-plan/export',
      filename: '偿还计划.xlsx',
      level: 'F',
      isParams: true,
      isDisabled: false,
      fetchKey: 'idList'
    }
  },
  columns: [
    { label: '长期应收款编号', prop: 'longReceivableNumber', width: 200 },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '应收日期', prop: 'receivableDate', width: 200 },
    { label: '应收总额', prop: 'receivableTotal', width: 200, render: ({ receivableTotal }) => toThousands(receivableTotal) },
    { label: '应收本金', prop: 'receivablePrincipal', width: 200, render: ({ receivablePrincipal }) => toThousands(receivablePrincipal) },
    { label: '应收利息', prop: 'receivableInterest', width: 200, render: ({ receivableInterest }) => toThousands(receivableInterest) },
    { label: '实收日期', prop: 'actualRepaymentDate', width: 200 },
    { label: '实收金额', prop: 'repaymentAmount', width: 200, render: ({ repaymentAmount }) => toThousands(repaymentAmount) },
    { label: '开票日期', prop: 'invoiceDate', width: 200 },
    { label: '开票金额', prop: 'invoiceAmount', width: 200, render: ({ invoiceAmount }) => toThousands(invoiceAmount) },
    {
      prop: 'operation',
      width: 100,
      fixed: 'right',
      label: '操作',
      display: false
    }
  ]
})

// 转出-出售登记
export const optionsConfigTurnOutSale = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/long-apportion/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      type: 'primary',
      url: '/engine/finance/long-apportion/export',
      filename: '分摊表.xlsx',
      level: 'S',
      isParams: true,
      isDisabled: false,
      fetchKey: 'idList'
    }

  },
  columns: [
    { label: '长期应收款编号', prop: 'longReceivableNumber', width: 200 },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '日期', prop: 'receivableDate', width: 200 },
    { label: '应收总额', prop: 'receivableTotal', width: 200, render: ({ receivableTotal }) => toThousands(receivableTotal) },
    { label: '应收本金', prop: 'receivablePrincipal', width: 200, render: ({ receivablePrincipal }) => toThousands(receivablePrincipal) },
    { label: '应收利息', prop: 'receivableInterest', width: 200, render: ({ receivableInterest }) => toThousands(receivableInterest) },
    { label: '剩余本金', prop: 'residualPrincipal', width: 200, render: ({ residualPrincipal }) => toThousands(residualPrincipal) },
    { label: '摊余成本', prop: 'amortizedCost', width: 200, render: ({ amortizedCost }) => toThousands(amortizedCost) },
    { label: '确认收入', prop: 'confirmIncome', width: 200, render: ({ confirmIncome }) => toThousands(confirmIncome) },
    {
      prop: 'operation',
      width: 100,
      fixed: 'right',
      label: '操作',
      display: false
    }
  ]
})
