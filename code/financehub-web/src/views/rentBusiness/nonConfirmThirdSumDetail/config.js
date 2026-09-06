
import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '明细',
  icon: '',
  name: 'nonConfirmThirdSumDetail'
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
  span: 8,
  labelWidth: '180px',
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
      url: '/engine/finance/non-confirm-collection-second-detail/queryThirdDetailPageData',
      method: 'post'
    }
  },
  columns: [

    { label: '到账主体', tooltip:true, prop: 'collectionAccountsBank', width: 250 },
    { label: '认领主体',  tooltip:true,prop: 'orgId', width: 250,

      render (row) {
         return dictMappingLabel(dictData, 'company', row.orgId)
      }
     },
    { label: '业务系统网银编号/批次号', prop: 'businessEbankNumber', width: 200, search: true },
    { label: '业务系统批扣流水号', prop: 'ebankSerialNumber', width: 200, search: true },
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
      label: '网银到账日期',
      prop: 'businessDate',
      width: 200,
      search: true,
      type: 'date',
      cover: ['businessDateStart', 'businessDateEnd'],
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }

    },
    {
      label: '最后变化日期',
      prop: 'lastChangeDate',
      width: 200,
      search: true,
      type: 'date',
      alias: '区间',
      cover: ['lastChangeDateStart', 'lastChangeDateEnd'],
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '最后变化开始日期',
        'end-placeholder': '最后变化结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    { label: '币种', prop: 'currencyType', width: 200 },
    { label: '网银到账金额', prop: 'bankAmount', width: 200, render: ({ bankAmount }) => toThousands(bankAmount) },
    { label: '剩余未确认金额', prop: 'remainNonConfirmAmount', width: 200 },
    { label: '已确认金额', prop: 'confirmedAmount', width: 200, render: ({ confirmedAmount }) => toThousands(confirmedAmount) },
    {
      label: '网银确认记录',
      prop: 'ebankConfirmRecord',
      tooltip:true,
      width: 400,
      render: ({ ebankConfirmRecord }) => {
        const ddd = ebankConfirmRecord.map(item => {
          return `<div>${item.ebankConfirmDate} ${item.contractCode} ${item.ebankConfirmComments} ${item.confirmAmount}</div>`
        })
        return ddd.join('')
      }
    },
    { label: '网银是否业务相关', prop: 'isRelationAndEbank', width: 200 },
    { label: '到账银行账号', prop: 'collectionAccountsBankNo', width: 200, search: true },
    { label: '到帐银行账号名称', prop: 'collectionAccountsBankNoName', width: 200 },
    { label: '付款客户', prop: 'clientName', width: 200, search: true },
    { label: '银行交易摘要', prop: 'bankSummary', width: 200 },
    { label: '银行交易备注', prop: 'comment', width: 200 },
    { label: '付款方账户', prop: 'clientAccountsBankNo', width: 200 },
    { label: '历史网银编号调整记录', prop: 'adjustmentRecordHistory', width: 200 },
    { label: '备注', prop: 'remark', width: 200 },
    { label: '网银余额非零', prop: 'isZeroForNonConfirmAmount', type: 'select', search: true, hide: true, option: dictMappingToArray(dictData, 'not_zero') },
    { label: '异常类型', prop: 'exceptionType', width: 200 }
  ]
})
