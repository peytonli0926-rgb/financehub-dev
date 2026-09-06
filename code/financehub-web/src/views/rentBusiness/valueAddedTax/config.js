import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '增值税',
  icon: '',
  name: 'valueAddedTax'
}

// 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  dataAsync: {
    title: '数据同步',
    type: 'success',
    level: 'F',
    isDisabled: false,
    url: '/engine/finance/pay-vat/payVatGetContract'
  },
  export: {
    title: '导出',
    level: 'F',
    isParams: true,
    isDisabled:false,
    url: '/engine/finance/pay-vat/export',
    fetchKey: 'idList',
    filename: '应交增值税对账.xlsx'
  },
  // voucher: {
  //   title: '生成凭证',
  //   level: 'F',
  //   type: 'warning',
  //   url: '/engine/finance/pay-vat/generateVoucher'
  // },
  customVoucher: {
    title: '生成手工凭证',
    level: 'F',
    type: 'primary',
    query: ['clientCode', 'orgId', 'contractCode'],
    redirectUrl: '/customVoucher/customVoucherAdd?'
  },
  submit: {
    title: '提交',
    level: 'F',
    type: 'success',
    url: '/engine/finance/pay-vat/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/pay-vat/withdraw'
  }
}
export const optionsConfigTransfer = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
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
      url: '/engine/finance/pay-vat/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    { label: '合同编号', prop: 'contractCode', width: 200, search: true },
    { label: '合同类型', prop: 'contractType', width: 200, search: true },
    { label: '租赁类型', prop: 'leaseType', width: 200 },
    { label: '税率', prop: 'taxRate', width: 200 },
    { label: '客户编号', prop: 'clientCode', width: 200, search: true },
    { label: '客户名称', prop: 'clientName', width: 200 },
    {
      label: '开票标识',
      prop: 'invoicingFlag',
      width: 100,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'invoice_flag'),
      render (row) {
        return (
          (row.invoicingFlag &&
            h(ElTag, () => dictMappingLabel(dictData, 'invoice_flag', row.invoicingFlag))) ||
          ''
        )
      }
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      width: 200,
      search: true,
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (
          (row.contractStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'business_contract_status', row.contractStatus)
            )) ||
          ''
        )
      }
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      width: 200,
      type: 'select',
      search: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status'),
      render (row) {
        return (
          (row.financialContractStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus)
            )) ||
          ''
        )
      }
    },
    {
      prop: 'orgId',
      label: '开票主体',
      type: 'select',
      tooltip: true,
      search: true,
      minWidth: 250,
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'exclusionScopeTaxList',
      label: '数据范围',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      minWidth: 250,
      option: dictMappingToArray(dictData, 'exclusion_scope_tax')

    },

    {
      label: '应开税额',
      prop: 'taxPayable',
      width: 200,
      render: ({ taxPayable }) => toThousands(taxPayable)
    },
    {
      label: '已计提税额',
      prop: 'taxAccrued',
      width: 200,
      render: ({ taxAccrued }) => toThousands(taxAccrued)
    },
    {
      label: '已开票税额',
      prop: 'taxInvoiced',
      width: 200,
      render: ({ taxInvoiced }) => toThousands(taxInvoiced)
    },
    {
      label: '实际剩余',
      prop: 'actualTaxBalance',
      width: 200,
      render: ({ actualTaxBalance }) => toThousands(actualTaxBalance)
    },
    {
      label: '应剩余税额（按计划）',
      prop: 'shouldTaxBalance',
      width: 200,
      render: ({ shouldTaxBalance }) => toThousands(shouldTaxBalance)
    },
    {
      label: '科目余额',
      prop: 'accountBalance',
      width: 200,
      render: ({ accountBalance }) => toThousands(accountBalance)
    },
    {
      label: '报表余额',
      prop: 'reportBalance',
      width: 200,
      render: ({ reportBalance }) => toThousands(reportBalance)
    },
    { label: '异常类型', prop: 'exceptionType', width: 200 },
    { label: '租赁合同号', prop: 'contractCodeM', width: 200, search: true },
    {
      label: '服务费实收金额',
      prop: 'serviceFeeReceived',
      width: 200,
      render: ({ serviceFeeReceived }) => toThousands(serviceFeeReceived)
    },
    { label: '服务费实收日期', prop: 'serviceFeeReceivedDate', type: 'date', width: 200 },
    {
      label: '留购价余额',
      prop: 'retainedPriceBalance',
      width: 200,
      render: ({ retainedPriceBalance }) => toThousands(retainedPriceBalance)
    },
    { label: '合同起租日期', prop: 'leaseDateStart', type: 'date', width: 200 },
    { label: '预计合同结束日期', prop: 'leaseDateEnd', type: 'date', width: 200 },

    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
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

export const optionsConfigRansom = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
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
      url: '/engine/finance/receive-tax/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    import: {
      title: '上传',
      level: 'S',
      type: 'primary',
      isDisabled: false,
      params: {
        url: '/engine/finance/receive-tax-detail/importFile',
        title: '应收销项税对账',
        open: true,
        templateUrl: '/engine/finance/receive-tax-detail/exportTemplate'
      }
    },
    export: {
      title: '导出',
      type: 'default',
      level: 'S',
      isParams: true,
      isDisabled:false,
      url: '/engine/finance/receive-tax/export',
      fetchKey: 'idList',
      filename: '应收销项税对账.xlsx'
    }
  },
  columns: [
    { label: '签约主体', prop: 'sellerName' },
    { label: '合同编号', prop: 'contractCode' },
    { label: '是否开票', prop: 'isInvoiced' },
    { label: '开票/计提名称', prop: 'productName' },
    { label: '发票号码', prop: 'invoiceNumber' },
    { label: '税额', prop: 'taxValue' },
    { label: '税率', prop: 'taxRate' },
    { label: '开票明细数据', prop: 'invoiceDetailTax' },
    { label: '差异情况', prop: 'differenceSituation' }
  ]
})
