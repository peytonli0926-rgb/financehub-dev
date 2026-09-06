import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '尾差调整详情',
  icon: '',
  name: 'endAdjustDetail'
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
      url: '/engine/finance/tail-difference-adjustment-detail/page',
      method: 'post'
    }

  },
  columns: [{
    prop: 'contractCode',
    label: '合同编号',
    search: true,

    width: 200
  },

  {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    tooltip: true,
    width: 300,
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },
  {
    prop: 'businessDate',
    label: '业务日期',
    type: 'date',
    width: 150
  },
  {
    prop: 'accountDate',
    label: '记账日期',
    type: 'date',
    width: 150
  },
  {
    prop: 'accountCode',
    label: '科目代码',
    width: 200
  },
  {
    prop: 'accountName',
    label: '科目名称',
    width: 200
  },
  {
    prop: 'accountBalance',
    label: '科目余额',
    width: 200,
    render ({ accountBalance }) {
      return toThousands(accountBalance)
    }
  },
  {
    label: '应收租金',
    prop: 'receivableRent',
    width: 200,
    render ({ receivableRent }) {
      return toThousands(receivableRent)
    }
  },
  {
    label: '应收期末残值',
    prop: 'receivableResidualValue',
    width: 200,
    render ({ receivableResidualValue }) {
      return toThousands(receivableResidualValue)
    }
  },
  {
    label: '未实现融资收益-待摊收益',
    prop: 'rentalIncomeAfterTotal',
    width: 200,
    render ({ rentalIncomeAfterTotal }) {
      return toThousands(rentalIncomeAfterTotal)
    }
  },
  {
    label: '未实现融资租赁收益-待摊收益',
    prop: 'rentalIncomeAfterLeaseTotal',
    width: 230,
    render ({ rentalIncomeAfterLeaseTotal }) {
      return toThousands(rentalIncomeAfterLeaseTotal)
    }
  },
  {
    label: '应付租赁设备款',
    prop: 'payableDevice',
    width: 200,
    render ({ payableDevice }) {
      return toThousands(payableDevice)
    }
  },
  {
    label: '应付其他款项',
    prop: 'payableOther',
    width: 200,
    render ({ payableOther }) {
      return toThousands(payableOther)
    }
  },
  {
    label: '合同状态',
    prop: 'contractStatus',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'business_contract_status'),
    width: 200,
    render ({ contractStatus }) {
      return (contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', contractStatus))) || ''
    }
  },
  {
    prop: 'operation',
    width: 100,
    label: '操作',
    fixed: 'right',
    display: false
  }
  ]
})
