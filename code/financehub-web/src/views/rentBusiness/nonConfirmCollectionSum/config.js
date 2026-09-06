import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '未确认收款',
  icon: '',
  name: 'nonConfirmCollectionSum'
}

// 汇总表
export const optionsConfigSumTable = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  labelWidth: '200px',
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
      url: '/engine/finance/non-confirm-collection-sum/page',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      level: 'F',
      isDisabled: false,
      url: '/engine/finance/non-confirm-collection-sum/export',
      filename: '未确认收款-汇总表.xlsx',
      isParams: true
    },
    import: {
      title: '批量认领',
      isDisabled: false,
      type: 'primary',
      level: 'F',
      params: {
        url: '/engine/finance/non-confirm-collection-sum/batchClaimConfirm',
        title: '批量认领',
        open: true,
        templateUrl: '/engine/finance/non-confirm-collection-sum/batchClaimConfirmTemplateDownload',
        logUrl: '/engine/finance/non-confirm-collection-sum/downloadErrFile'
      }
    },
    import1: {
      title: '批量修改入账日期',
      isDisabled: false,
      type: 'primary',
      level: 'F',
      params: {
        url: '/engine/finance/non-confirm-collection-sum/modifyIncomeDateUpload',
        title: '批量修改入账日期',
        open: true,
        templateUrl: '/engine/finance/non-confirm-collection-sum/modifyIncomeDateTemplateDownload'
      }
    }
  },
  columns: [
    { label: '到账主体', prop: 'collectionAccountsBank', width: 250 },
    { label: '资金系统网银编号', prop: 'ebankNumber', search: true, width: 200, tooltip: true },
    {
      label: '认领主体',
      prop: 'orgName',
      width: 250,
      tooltip: true,
      render: (row) => dictMappingLabel(dictData, 'company', row.orgId)
    },

    {
      label: '业务系统网银编号/批次号',
      prop: 'ebankSerialNumber',
      tooltip: true,
      width: 400,
      search: true
    },
    { label: '到账银行账号', prop: 'collectionAccountsBankNo', width: 200, search: true },
    {
      label: '网银到账金额',
      prop: 'bankAmount',
      width: 200,
      render: ({ bankAmount }) => toThousands(bankAmount)
    },
    {
      label: '剩余未确认金额',
      prop: 'remainNonConfirmAmount',
      width: 200,
      render: ({ remainNonConfirmAmount }) => toThousands(remainNonConfirmAmount)
    },
    {
      label: '已确认金额',
      prop: 'claimAmount',
      width: 200,
      render: ({ claimAmount }) => toThousands(claimAmount)
    },
    {
      label: '网银余额非零',
      prop: 'notZero',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'not_zero')
    },
    {
      prop: 'operation',
      width: 320,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
// 明细表
export const optionsConfigDetail = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/non-confirm-collection-second-detail/queryThirdDetailDataByPage',
      method: 'post'
    }
  },
  columns: [
    { label: '到账主体', prop: 'collectionAccountsBank', width: 300 },
    {
      label: '认领主体',
      prop: 'orgId',
      width: 300,
      render: (row) => dictMappingLabel(dictData, 'company', row.orgId)
    },
    { label: '业务系统网银编号/批次号', prop: 'ebankSerialNumber', width: 200, search: true },
    { label: '业务系统批扣流水号', prop: 'businessEbankNumber', width: 200, search: true },
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      render: (row) => {
        return (
          (row.systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) ||
          ''
        )
      }
    },
    { label: '最后变化日期', prop: 'lastChangeDate', type: 'date', width: 200, search: true },
    { label: '网银到账日期', prop: 'businessDate', type: 'date', width: 200, search: true },
    {
      label: '币种',
      prop: 'currencyType',
      width: 200,
      render: (row) => {
        return (
          (row.currencyType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) ||
          ''
        )
      }
    },
    {
      label: '网银到账金额',
      prop: 'bankAmount',
      width: 200,
      render: ({ bankAmount }) => toThousands(bankAmount)
    },
    { label: '剩余未确认金额', prop: 'remainNonConfirmAmount', width: 200 },
    {
      label: '已确认金额',
      prop: 'confirmedAmount',
      width: 200,
      render: ({ confirmedAmount }) => toThousands(confirmedAmount)
    },
    {
      label: '网银确认记录',
      prop: 'ebankConfirmRecord',
      width: 200,
      render: ({ ebankConfirmRecord }) => {
        const ddd = ebankConfirmRecord.map((item) => {
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
    {
      label: '网银余额非零',
      prop: 'notZero',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'not_zero')
    },
    {
      prop: 'exceptionType',
      label: '异常类型',
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'exception_type'),
      render: (row) => {
        return (
          (row.exceptionType &&
            h(ElTag, () => dictMappingLabel(dictData, 'exception_type', row.exceptionType))) ||
          ''
        )
      },
      width: 200
    }
  ]
})
// 对账表
export const optionsConfigBalanceTable = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  labelWidth: '150px',
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
      url: '/engine/finance/non-confirm-collection-account-checking/queryNonConfirmAccountChecking',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'recordStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      url: '/engine/finance/non-confirm-collection-account-checking/export',
      filename: '对账表.xlsx',
      // fetchKey: 'idList',
      isDisabled: false,
      isDialog: true,
      isParams: true,
      level: 'T',
      tooltips: ''
    },
    import: {
      title: '批量修改',
      isDisabled: false,
      type: 'primary',
      level: 'T',
      params: {
        url: '/engine/finance/non-confirm-collection-account-checking/batchModifyUpload',
        title: '批量修改',
        open: true,
        templateUrl: '/engine/finance/non-confirm-collection-account-checking/batchModify'
      }
    },

    submit: {
      title: '确认对账',
      type: 'success',
      level: 'T',
      isDisabled: false,
      url: '/engine/finance/non-confirm-collection-account-checking/confirmAccountingChecking',
      isParams: true
    },
    import2: {
      title: '上传非租结果',
      isDisabled: false,
      type: 'primary',
      level: 'T',
      params: {
        url: '/engine/finance/non-confirm-collection-account-checking/nonLeaseResultUpload',
        title: '上传非租结果',
        open: true,
        templateUrl: '/engine/finance/non-confirm-collection-account-checking/uploadNonLeaseResult'
      }
    }
  },
  columns: [
    {
      label: '对账月份',
      prop: 'checkingMonth',
      width: 200,
      search: true,
      type: 'date',
      hide: true,
      attrs: {
        type: 'month',
        format: 'YYYY-MM',
        valueFormat: 'YYYY-MM'
      }
    },
    {
      label: '对账月份',
      prop: 'accountCheckingMonth',
      width: 200,
      type: 'date',
      attrs: {
        type: 'date',
        format: 'YYYY-MM',
        valueFormat: 'YYYY-MM'
      }
    },
    {
      label: '到账主体',
      prop: 'collectionAccountsBankCode',
      width: 200,
      type: 'select',
      tooltip: true,
      search: true,

      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      minWidth: 250,
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.collectionAccountsBankCode)
      }
    },
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      search: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render: (row) => {
        return (
          (row.systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) ||
          ''
        )
      }
    },
    { label: '业务系统网银编号/批次号', prop: 'businessEbankNumber', width: 200 },
    { label: '业务系统批扣流水号', prop: 'ebankSerialNumber', width: 200 },
    { label: '入账日期', prop: 'businessHappenDate', type: 'date', width: 100 },
    {
      label: '账龄分类',
      prop: 'accountAgeClass',
      width: 200,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'aging_classification'),
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      render: ({ accountAgeClass }) => {
        return (
          (accountAgeClass &&
            h(ElTag, () => dictMappingLabel(dictData, 'aging_classification', accountAgeClass))) ||
          ''
        )
      }
    },
    {
      label: '本月初余额',
      prop: 'monthInitBalance',
      width: 200,
      render: ({ monthInitBalance }) => toThousands(monthInitBalance)
    },
    {
      label: '本月贷方发生额',
      prop: 'curMonthCreditAmount',
      width: 200,
      render: ({ curMonthCreditAmount }) => toThousands(curMonthCreditAmount)
    },
    {
      label: '本月余额',
      prop: 'curMonthBalance',
      width: 200,
      render: ({ curMonthBalance }) => toThousands(curMonthBalance)
    },
    {
      label: '系统金额',
      prop: 'systemAmount',
      width: 200,
      render: ({ systemAmount }) => toThousands(systemAmount)
    },
    {
      label: '差额',
      prop: 'diffAmount',
      width: 200,
      render: ({ diffAmount }) => toThousands(diffAmount)
    },
    { label: '财务对账备注', prop: 'accountCheckingComments', width: 200 },
    {
      label: '财务部初分类',
      prop: 'financialPrimaryClassic',
      width: 200,
      search: true,
      type: 'select',
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'financial_initial_lassification'),
      render: ({ financialPrimaryClassic }) => {
        return (
          (financialPrimaryClassic &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'financial_initial_lassification', financialPrimaryClassic)
            )) ||
          ''
        )
      }
    },
    {
      label: '运营部确认款项性质',
      prop: 'confirmAccountProperty',
      width: 200,
      search: true,
      type: 'select',
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'collection_properities'),
      render: ({ confirmAccountProperty }) => {
        return (
          (confirmAccountProperty &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'collection_properities', confirmAccountProperty)
            )) ||
          ''
        )
      }
    }
  ]
})
