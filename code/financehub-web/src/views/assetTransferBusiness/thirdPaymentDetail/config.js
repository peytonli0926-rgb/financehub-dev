export default {
  hidden: true,
  title: '第三方转付详情',
  icon: '',
  name: 'thirdPaymentDetail'
}

import { toThousands, dictMappingLabel } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'convertTransferId', // 表格唯一id
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
      url: '/engine/finance/convert-transfer-third-payment/detail',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-contract-fee/detail/export',
      filename: '转付详情.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    { label: '转让批次', prop: 'batch', minWidth: 120 },
    { label: '支付日期', prop: 'paymentDate', minWidth: 140 },
    {
      label: '支付主体',
      prop: 'paymentOrgId',
      minWidth: 200,
      tooltip: true,
      render({ paymentOrgId }) {
        return dictMappingLabel(dictData, 'company', paymentOrgId)
      }
    },
    { label: '合同号', prop: 'contractCode', minWidth: 140, tooltip: true },
    {
      label: '转付金额',
      prop: 'paymentAmount',
      minWidth: 140,
      render: ({ paymentAmount }) => toThousands(paymentAmount)
    },
    { label: '银行账号', prop: 'bankAccountCode', minWidth: 140, tooltip: true },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
