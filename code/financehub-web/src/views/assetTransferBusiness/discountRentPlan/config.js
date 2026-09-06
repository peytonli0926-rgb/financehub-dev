export default {
  hidden: true,
  title: '租金计划',
  icon: '',
  name: 'discountRentPlan'
}

import { changeCellValue } from '@/utils/format'

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
      url: '/engine/finance/convert-transfer-plan/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'convertTransferId'
    },
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-plan/export',
      filename: '折价转让-租金计划报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    { label: '原合同号', prop: 'oldContractCode', minWidth: 140 },
    { label: '新合同号', prop: 'newContractCode', minWidth: 160, tooltip: true },
    {
      label: '计划日期',
      prop: 'planDate',
      minWidth: 130,
      format: 'date',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    { label: '期数', prop: 'periods', width: 90 },
    {
      label: '应收租金',
      prop: 'receivableRent',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收本金',
      prop: 'receivablePrincipal',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收利息',
      prop: 'receivableInterest',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收期末残值',
      prop: 'receivableEndingSalvage',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    }
  ]
})
