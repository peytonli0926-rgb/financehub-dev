import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import moment from 'moment'

export default {
  hidden: true,
  title: '核销回款详情',
  icon: '',
  name: 'verificationIncomeDetail'
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
      url: '/engine/verification/payback/details/page',
      method: 'post'
    }
  },
  columns: [{
    prop: 'businessDate',
    label: '回款月份',
    type: 'date',
    width: 200,
    attrs: {
      type: 'month',
      clearable: false,
      format: 'YYYY-MM',
      'value-format': 'YYYY-MM'
    },
    render ({ businessDate }) {
      return moment(businessDate).format('YYYY-MM')
    }
  },
  // {
  //   prop: 'businessDate',
  //   label: '记账日期',
  //   type: 'date',
  //   width: 200,
  //   attrs: {
  //     clearable: false,
  //     format: 'YYYY-MM',
  //     'value-format': 'YYYY-MM'
  //   }
  // },
  {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    minWidth: 300,
    tooltip: true,
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
    prop: 'clientCode',
    label: '客户编码',
    search: true,
    width: 200
  },

  {
    prop: 'financialContractStatus',
    label: '财务合同状态',
    search: true,
    rules: [{
      required: true,
      message: '请选择财务合同状态',
      trigger: 'change'
    }],
    width: 200,
    type: 'select',
    attrs: {
      filterable: true
    },
    option: dictMappingToArray(dictData, 'financial_contract_status'),
    render (row) {
      return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
    }
  },

  {
    prop: 'receivableLeaseAmount',
    label: '应收租金',
    render (row) {
      return toThousands(row.receivableLeaseAmount)
    },
    width: 200
  },
  {
    prop: 'receivableLastAmount',
    label: '应收期末残值',
    render (row) {
      return toThousands(row.receivableLastAmount)
    },
    width: 200
  },
  {
    prop: 'receivableFirstAmount',
    label: '应收首付款',
    render (row) {
      return toThousands(row.receivableFirstAmount)
    },
    width: 200
  },
  {
    prop: 'receivableProcedureAmount',
    label: '应收手续费',
    render (row) {
      return toThousands(row.receivableProcedureAmount)
    },
    width: 200
  },
  {
    prop: 'receivableInsuranceAmount',
    label: '应收保险费',
    render (row) {
      return toThousands(row.receivableInsuranceAmount)
    },
    width: 200
  },
  {
    prop: 'receiveOtherRevenues',
    label: '应收其他收入',
    render (row) {
      return toThousands(row.receiveOtherRevenues)
    },
    width: 200
  },
  {
    prop: 'receivableLastAmountd',
    label: '应收销项税',
    width: 200,
    render (row) {
      return toThousands(row.receivableLastAmountd)
    }
  },
  {
    prop: 'receiveOtherRevenuesd',
    label: '未实现融资租赁收益',
    width: 200,
    render (row) {
      return toThousands(row.receiveOtherRevenuesd)
    }
  },

  { label: '应收罚息', prop: 'recycleDefaultInterestAmount', width: 200, render: ({ recycleDefaultInterestAmount }) => toThousands(recycleDefaultInterestAmount) },
  { label: '应收变更手续费', prop: 'receiveTerminateProcedureAmount', width: 200, render: ({ receiveTerminateProcedureAmount }) => toThousands(receiveTerminateProcedureAmount) },
  { label: '承租人保证金', prop: 'receivableMarginAmount', width: 200, render: ({ receivableMarginAmount }) => toThousands(receivableMarginAmount) },
  { label: '已计提税金', prop: 'receivableOuttaxAmount', width: 200, render: ({ receivableOuttaxAmount }) => toThousands(receivableOuttaxAmount) },
  { label: '已确认收入', prop: 'leaseRevenueAmount', width: 200, render: ({ leaseRevenueAmount }) => toThousands(leaseRevenueAmount) },

  {
    prop: 'depreciationLossAmount',
    label: '拨备转回金额',
    width: 200,
    render (row) {
      return toThousands(row.depreciationLossAmount)
    }
  },
  {
    prop: 'depreciationReservesAmount',
    label: '回款前核销余额',
    width: 200,
    render (row) {
      return toThousands(row.depreciationReservesAmount)
    }
  },
  {
    prop: 'depreciationAmount',
    label: '回款后核销余额',
    width: 200,
    render (row) {
      return toThousands(row.depreciationAmount)
    }
  },
  {
    prop: 'isAbnormal',
    label: '是否异常',
    type: 'select',
    search: true,
    option: [{
      label: '否',
      value: '0'
    }, {
      label: '是',
      value: '1'
    }],
    width: 100,
    render ({ isAbnormal }) {
      const label = isAbnormal === '0' ? '否' : '是'
      return h(ElTag, () => label)
    }
  }
  ]
})
