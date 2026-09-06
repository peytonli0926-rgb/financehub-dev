import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, dictMappingToArray } from '@/utils'

export default {
  hidden: true,
  title: '未确认收款详情',
  icon: '',
  name: 'nonConfirmCollectionSumDetail'
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
  isSearch: true,
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
      url: '/engine/finance/non-confirm-collection-second-detail/queryDetailPageData',
      method: 'post'
    }
  },
  columns: [
    { label: '到账主体', prop: 'collectionAccountsBank', width: 250 },
    { label: '资金系统网银编号', prop: 'ebankNumber', width: 200 },
    {
      label: '认领主体',
      prop: 'orgId',
      width: 250,
      render: (row) => dictMappingLabel(dictData, 'company', row.orgId)
    },
    { label: '业务系统网银编号/批次号', prop: 'ebankSerialNumber', width: 200 },
    { label: '到账银行账号', prop: 'collectionAccountsBankNo', width: 200 },
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render (row) {
        return (
          (row.systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) ||
          ''
        )
      }
    },
    {
      label: '网银确认日期',
      search: true,
      type: 'date',
      cover: ['ebankConfirmDateStart', 'ebankConfirmDateEnd'],
      attrs: {
        type: 'daterange',
        valueFormat: 'YYYY-MM-DD',
        format: 'YYYY-MM-DD'
      },
      prop: 'businessHappenDate',
      width: 200
    },
    {
      label: '网银到账日期',
      search: true,
      type: 'date',
      cover: ['ebankIncomeDateStart', 'ebankIncomeDateEnd'],
      attrs: {
        type: 'daterange',
        valueFormat: 'YYYY-MM-DD',
        format: 'YYYY-MM-DD'
      },
      prop: 'businessDate',
      width: 200
    },
    {
      label: '币种',
      prop: 'currencyType',
      width: 200,
      render (row) {
        return (
          (row.currencyType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) ||
          ''
        )
      }
    },
    { label: '网银到账金额', prop: 'bankAmount', width: 200 },
    { label: '剩余未确认金额', prop: 'remainNonConfirmAmount', width: 200 },
    { label: '已确认金额', prop: 'confirmedAmount', width: 200 },
    {
      label: '网银确认记录',
      prop: 'ebankConfirmRecord',
      width: 400,
      render: ({ ebankConfirmRecord }) => {
        const ddd = ebankConfirmRecord.map((item) => {
          return `<div>${item.ebankConfirmDate || ' '} ${item.contractCode || ' '} ${item.ebankConfirmComments || ' '} ${item.confirmAmount}</div>`
        })
        return ddd.join('')
      }
    },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
