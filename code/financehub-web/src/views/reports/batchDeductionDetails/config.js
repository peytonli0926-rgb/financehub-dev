export default {
  hidden: true,
  title: '批扣详情',
  icon: '',
  name: 'batchDeductionDetails'
}

// dictMappingLabel dictMappingToArray
import { toThousands } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  isSearch: false,
  rowKey: 'id', // 表格唯一id
  span: 6, // 查询区域每一个表单要展示的宽度
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/system-bank-mapping/detail',
      method: 'post'
    }
  },
  btnConfig: {
    // common: {
    //   disabled: 'processStatus',
    //   getKey: 'id'
    // },
    export: {
      title: '导出',
      url: '/engine/finance/system-bank-mapping/detail/export',
      filename: '已勾稽明细报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '交易日期',
      width: 110,
      render: ({ businessDate }) => businessDate && businessDate.split(' ')[0]
    },
    { prop: 'ebankSerialNumber', label: '大网银编号', minWidth: 190, tooltip: true },
    { prop: 'deductBatchNo', label: '小网银编号', minWidth: 180, tooltip: true },
    {
      prop: 'businessMatchAmount',
      label: '网银金额',
      minWidth: 140,
      render: ({ businessMatchAmount }) => toThousands(businessMatchAmount)
    },
    {
      prop: 'collectAmount',
      label: '批扣金额',
      minWidth: 140,
      render: ({ collectAmount }) => toThousands(collectAmount)
    },
    {
      prop: 'diffAmount',
      label: '差额',
      minWidth: 140,
      render: ({ diffAmount }) => toThousands(diffAmount)
    }
  ]
})
