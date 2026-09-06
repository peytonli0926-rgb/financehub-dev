
import { toThousands } from '@/utils'

export default {
  hidden: true,
  title: '线下合同录入详情',
  icon: '',
  name: 'offlineContractEnterDetail'
}
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
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
      url: '/engine/finance/offline-contract/detail/plan',
      method: 'post'
    }
  },
  columns: [

    {
      prop: 'contractCode',
      label: '合同编码',
      width: 200
    }, {
      prop: 'period',
      label: '期数',
      width: 200
    },
    {
      prop: 'planDate',
      label: '还款日',
      width: 200
    }, {
      prop: 'rentAmount',
      label: '应收租金',
      render: ({ rentAmount }) => toThousands(rentAmount)
    },
    {
      prop: 'principalAmount',
      label: '本金',
      render: ({ principalAmount }) => toThousands(principalAmount)
    },
    {
      prop: 'interestAmount',
      label: '利息',
      render: ({ interestAmount }) => toThousands(interestAmount)
    }]
})
