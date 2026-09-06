export default {
  hidden: true,
  title: '转让合同余额详情',
  icon: '',
  name: 'transContractBalanceDetail'
}

import { toThousands, dictMappingLabel, dictMappingToArray } from '@/utils'
import { ElTag } from 'element-plus'
import { h } from 'vue'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: true,
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
      url: '/engine/finance/convert-transfer-contract-fee/detail',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-contract-fee/detail/export',
      filename: '转让合同费用详情.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'uploadDate',
      label: '业务日期',
      width: 140
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      width: 140
    },
    {
      label: '签约主体',
      prop: 'orgId',
      minWidth: 140,
      tooltip: true,
      render({ orgId }) {
        return dictMappingLabel(dictData, 'company', orgId)
      }
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      search: true,
      minWidth: 140
    },
    {
      label: '费用类型',
      prop: 'transferFeeType',
      minWidth: 200,
      type: 'select',
      search: true,
      option: dictMappingToArray(dictData, 'charge_type'),
      render({ transferFeeType }) {
        return (
          (transferFeeType &&
            h(ElTag, () => dictMappingLabel(dictData, 'charge_type', transferFeeType))) ||
          ''
        )
      }
    },
    {
      prop: 'transferFee',
      label: '金额',
      minWidth: 140,
      render: ({ transferFee }) => toThousands(transferFee)
    },
    {
      label: '成本中心',
      prop: 'costCenter',
      search: true,
      minWidth: 140
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
