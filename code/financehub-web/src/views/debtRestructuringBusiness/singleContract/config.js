
import { toThousands } from '@/utils'
export default {
  hidden: true,
  title: '单合同租金情况',
  icon: '',
  name: 'singleContract'
}

// 转入登记
export const optionsConfig = (router, dictData = {}) => ({
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
      url: '/engine/finance/rent-register-detail/page',
      method: 'post'
    }
  },

  columns: [

    { label: '房产租赁合同编号', width: 200, prop: 'contractCode' },
    { label: '应付日期', width: 200, prop: 'planDate', type: 'date' },
    { label: '所属期', width: 200, prop: 'period' },
    { label: '应收租金', width: 200, prop: 'receivableRent' },
    { label: '当月应收租金', width: 200, prop: 'thisMonthReceivableRent', render: ({ thisMonthReceivableRent }) => toThousands(thisMonthReceivableRent) },
    { label: '当月计提税金', width: 200, prop: 'thisMonthTax', render: ({ thisMonthTax }) => toThousands(thisMonthTax) },
    { label: '当月租金收入', width: 200, prop: 'thisMonthRentIncome', render: ({ thisMonthRentIncome }) => toThousands(thisMonthRentIncome) },
    { label: '实际回笼日期', width: 200, prop: 'actualRepaymentDate', type: 'date' },
    { label: '回笼金额', width: 200, prop: 'repaymentAmount', render: ({ repaymentAmount }) => toThousands(repaymentAmount) },
    { label: '网银编号', width: 200, prop: 'ebankSerialNumber' },
    { label: '开票日期', width: 200, prop: 'invoiceDate', type: 'date' },
    { label: '开票金额', width: 200, prop: 'invoiceAmount', render: ({ invoiceAmount }) => toThousands(invoiceAmount) },
    {
      prop: 'operation',
      width: 100,
      fixed: 'right',
      label: '操作',
      display: false
    }
  ]
})
