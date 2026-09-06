export default {
  hidden: true,
  title: '凭证查询',
  icon: '',
  name: 'voucherInquiryReport'
}

import { h } from 'vue'
import { ElTag } from 'element-plus'
import { toThousands, dictMappingLabel } from '@/utils'

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
      url: '/engine/finance/voucher/query',
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
      type: 'primary',
      url: '/engine/finance/voucher/export',
      filename: '凭证查询.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'orgName',
      label: '签约主体',
      minWidth: 120,
      tooltip: true,
      render: ({ orgId }) => {
        return (orgId && dictMappingLabel(dictData, 'company', orgId)) || ''
      }
    },
    { prop: 'businessDate', label: '交易日期', minWidth: 120 },
    { prop: 'voucherDate', label: '记账日期', minWidth: 120 },
    {
      prop: 'sceneName',
      label: '业务场景',
      minWidth: 120,
      render({ sceneName }) {
        return (sceneName && h(ElTag, () => sceneName)) || ''
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 120,
      tooltip: true
    },
    { prop: 'accountCode', label: '科目编码', minWidth: 100 },
    { prop: 'accountName', label: '科目名称', minWidth: 140, tooltip: true },
    {
      prop: 'debitAmount',
      label: '借方',
      minWidth: 120,
      render: ({ debitAmount }) => toThousands(debitAmount)
    },
    {
      prop: 'creditAmount',
      label: '贷方',
      minWidth: 120,
      render: ({ creditAmount }) => toThousands(creditAmount)
    }
  ]
})
