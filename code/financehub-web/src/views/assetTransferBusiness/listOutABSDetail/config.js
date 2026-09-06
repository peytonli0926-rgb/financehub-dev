// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '出表ABS详情',
  icon: '',
  name: 'listOutABSDetail'
}
//转让
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  isShowSummary: true, // 合并计算表格
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
      url: '/engine/finance/out-table-abs/detailPage',
      method: 'post'
    }
  },
  columns: [

    { label: '借款合同编号', prop: 'loanContractCode', width: 200,fixed:"left" },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '出表期数', prop: 'periods', width: 200 },
    { label: '客户名称', prop: 'clientName', width: 200 },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 250,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '财务合同状态', prop: 'financialContractStatus', width: 200 },
    { label: '封包日应收租金', prop: 'receivableRent', width: 200, render: ({ receivableRent }) => toThousands(receivableRent) },
    { label: '封包日应收残值', prop: 'receivableResidualValue', width: 200, render: ({ receivableResidualValue }) => toThousands(receivableResidualValue) },
    { label: '封包日应收销项税', prop: 'receivableOuttax', width: 200, render: ({ receivableOuttax }) => toThousands(receivableOuttax) },
    { label: '封包日未实现收益', prop: 'unrealizedRevenue', width: 200, render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue) },
    { label: '封包日承租人保证金', prop: 'lesseeMargin', width: 200, render: ({ lesseeMargin }) => toThousands(lesseeMargin) },
    { label: '发行日拨备余额', prop: 'depreciationReservesBalance', width: 200, render: ({ depreciationReservesBalance }) => toThousands(depreciationReservesBalance) },
    { label: '封包日后计提收益', prop: 'leaseRevenueBalance', width: 200, render: ({ leaseRevenueBalance }) => toThousands(leaseRevenueBalance) },
    { label: '封包日后收款', prop: 'receivableUnconfirmReceiptAmount', width: 200, render: ({ receivableUnconfirmReceiptAmount }) => toThousands(receivableUnconfirmReceiptAmount) },
    { label: '封包日后开票', prop: 'receivableOuttaxDebtRestructureAmount', width: 200, render: ({ receivableOuttaxDebtRestructureAmount }) => toThousands(receivableOuttaxDebtRestructureAmount) },
    { label: '应收融资租赁款', prop: 'financeLeaseReceivablesAmount', width: 200, render: ({ financeLeaseReceivablesAmount }) => toThousands(financeLeaseReceivablesAmount) },
    { label: '转让价格（合同维度）', prop: 'transferPrice', width: 200, render: ({ transferPrice }) => toThousands(transferPrice) },
    { label: '转让损益', prop: 'transferLossPrice', width: 200, render: ({ transferLossPrice }) => toThousands(transferLossPrice) }

  ]
})
//赎回
export const optionsConfigRansom = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  isShowSummary: true, // 合并计算表格
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
      url: '/engine/finance/asset-abs-redeem/detailPage',
      method: 'post'
    }
  },
  columns: [

    { label: '借款合同编号', prop: 'loanContractCode', width: 200,fixed:"left" },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '客户名称', prop: 'clientName', width: 200 },
    { label: '出表期数', prop: 'periods', width: 200 },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 300,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '财务合同状态', prop: 'financialContractStatus', width: 200 },

    { label: '税率', prop: 'rate', width: 200 },
    { label: '赎回起算日', prop: 'startDate', width: 200, type: 'date' },
    { label: '实际赎回日', prop: 'actualDate', width: 200, type: 'date' },
    { label: '赎回价格', prop: 'redeemPrice', width: 200, render: ({ redeemPrice }) => toThousands(redeemPrice) },
    { label: '赎回起算日租金余额', prop: 'receivableRentBalance', width: 200, render: ({ receivableRentBalance }) => toThousands(receivableRentBalance) },
    { label: '赎回起算日本金余额', prop: 'principalBalance', width: 200, render: ({ principalBalance }) => toThousands(principalBalance) },
    { label: '赎回起算日利息余额', prop: 'interestBalance', width: 200, render: ({ interestBalance }) => toThousands(interestBalance) },
    { label: '赎回起算日留购价余额', prop: 'receivableResidualValueBalance', width: 200, render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance) },
    { label: '赎回起算日保证金余额', prop: 'lesseeMarginBalance', width: 200, render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance) },
    {
      prop: 'operation',
      width: 80,
      label: '操作',
      fixed: 'right',
      display: false
    }

  ]
})
//转付
export const optionsConfigTransfersPay = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isShowSummary: true, // 合并计算表格
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
      url: '/engine/finance/asset-abs-transfer-payment/detailPage',
      method: 'post'
    }
  },
  columns: [

    { label: '借款合同编号', prop: 'loanContractCode', width: 200,fixed:"left" },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '客户名称', prop: 'clientName', width: 200 },
    { label: '出表期数', prop: 'periods', width: 200 },

    { label: '税率', prop: 'rate', width: 200 },
    { label: '实付本金', prop: 'actualPrincipalAmount', width: 200, render: ({ actualPrincipalAmount }) => toThousands(actualPrincipalAmount) },
    { label: '实付利息', prop: 'actualInterestAmount', width: 200, render: ({ actualInterestAmount }) => toThousands(actualInterestAmount) },
    { label: '实付留购价', prop: 'actualRetentionPurchaseAmount', width: 200, render: ({ actualRetentionPurchaseAmount }) => toThousands(actualRetentionPurchaseAmount) },
    { label: '实付罚息及手续费', prop: 'actualPenaltyInterestAmount', width: 200, render: ({ actualPenaltyInterestAmount }) => toThousands(actualPenaltyInterestAmount) },

    {
      prop: 'operation',
      width: 80,
      label: '操作',
      fixed: 'right',
      display: false
    }

  ]
})

export const optionsConfigDialog = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
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
      url: '/engine/finance/out-table-abs/checkPage',
      method: 'post'
    }
  },

  columns: [
    { label: '合同编号', prop: 'contractCode' },
    { label: '客户名称', prop: 'clientName' },
    {
      prop: 'orgId',
      label: '签约主体',
      width: 200,
      type: 'select',
      tooltip: true,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '应收租金', prop: 'receivableRentBalance', render: ({ receivableRentBalance }) => toThousands(receivableRentBalance) },
    { label: '应收期末残值', prop: 'receivableResidualValueBalance', render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance) },
    { label: '应收销项税', prop: 'receivableOuttaxBalance', render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance) },
    { label: '未实现融资租赁收益', prop: 'unrealizedRevenueBalance', render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance) },
    { label: '承租人保证金', prop: 'lesseeMarginBalance', render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance) },
    { label: '应收租赁款组合拨备', prop: 'depreciationReservesBalance', render: ({ depreciationReservesBalance }) => toThousands(depreciationReservesBalance) },
    { label: '应收合同解约及变更手续费', prop: 'receivableTerminateBalance', render: ({ receivableTerminateBalance }) => toThousands(receivableTerminateBalance) },
    { label: '应收罚息', prop: 'receivableDefaultInterestBalance', render: ({ receivableDefaultInterestBalance }) => toThousands(receivableDefaultInterestBalance) }

  ]
})
