export default {
  hidden: true,
  title: '付款单详情',
  icon: '',
  name: 'paymentOrderDetail'
}

import moment from 'moment'
import { toThousands } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
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
      url: '/engine/finance/voucherReport/paymentDetailQuery',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      // disabled: 'processStatus',
      getKey: 'orgId'
    },
    export: {
      title: '导出',
      url: '/engine/finance/voucherReport/paymentDetailQuery/export',
      filename: '付款详情报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    { prop: 'orgName', label: '签约主体', minWidth: 120, tooltip: true },
    {
      prop: 'businessDate',
      label: '交易日期',
      width: 170,
      render: ({ businessDate }) => {
        return moment(businessDate).format('YYYY-MM-DD')
      }
    },
    { prop: 'paymentIdentifier', label: '付款编号', minWidth: 140 },
    {
      prop: 'debitAmount',
      label: '借方',
      minWidth: 150,
      render: ({ debitAmount }) => toThousands(debitAmount)
    },
    {
      prop: 'creditAmount',
      label: '贷方',
      minWidth: 150,
      render: ({ creditAmount }) => toThousands(creditAmount)
    },
    {
      prop: 'diffAmount',
      label: '差额',
      minWidth: 150,
      render: ({ diffAmount }) => toThousands(diffAmount)
    },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
