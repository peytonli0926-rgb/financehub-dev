export default {
  hidden: false,
  title: '折价转让',
  icon: '',
  name: 'discountTransfer'
}

import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { changeCellValue } from '@/utils/format'
import moment from 'moment'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  span: 8, // 查询区域每一个表单要展示的宽度
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  }
}

const btnTransferConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  // import1: { // 本次不做
  //   title: '上传收款信息',
  //   type: 'success',
  //   isDisabled: false,
  //   level: 'F',
  //   params: {
  //     url: '/engine/finance/parity-transfer/importPaymentInfo',
  //     title: '收款信息',
  //     open: true,
  //     templateUrl: '/engine/finance/parity-transfer/downLoadPaymentInfo'
  //   }
  // },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    level: 'F',
    params: {
      url: '/engine/finance/convert-transfer/importFile',
      title: '折价转让',
      open: true,
      templateUrl: '/engine/finance/convert-transfer/exportTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/convert-transfer/export',
    level: 'F',
    fetchKey: 'idList',
    filename: '折价转让-转让.xlsx',
    isParams: true
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/convert-transfer/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'F',
    url: '/engine/finance/convert-transfer/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/convert-transfer/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'F',
    url: '/engine/finance/convert-transfer/delete'
  }
}

// 转让
export const transferOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/convert-transfer/page',
      method: 'post'
    }
  },
  btnConfig: btnTransferConfig,
  columns: [
    {
      label: '记账日期',
      type: 'date',
      prop: 'accountDate',
      hide: true,
      cover: ['accountStartDate', 'accountEndDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      },
      search: true
    },
    {
      label: '业务日期',
      prop: 'businessDate',
      minWidth: 120,
      format: 'date',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '财务日期',
      prop: 'financeDate',
      minWidth: 120,
      render: ({ financeDate }) => {
        return moment(financeDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '转让批次',
      prop: 'batchList',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/convert-transfer/batchList',
        method: 'post'
      }
    },
    { label: '转让批次', prop: 'batch', minWidth: 120 },
    {
      prop: 'transferPartyList',
      label: '转让方',
      minWidth: 200,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '转让方',
      prop: 'transferParty',
      minWidth: 150,
      tooltip: true,
      format: 'dict',
      dictKey: 'company',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    {
      prop: 'transfereePartyList',
      label: '受让方',
      minWidth: 200,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '受让方',
      prop: 'transfereeParty',
      minWidth: 150,
      tooltip: true,
      format: 'dict',
      dictKey: 'company',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    { label: '基准日', type: 'date', prop: 'referenceDate', minWidth: 120 },
    { label: '交易日', type: 'date', prop: 'tradeDate', minWidth: 120 },
    {
      label: '转让价格',
      prop: 'transferPrice',
      minWidth: 150,
      render: ({ transferPrice }) => toThousands(transferPrice)
    },
    { label: '合同数量', prop: 'contractNum', minWidth: 100 },
    {
      label: '转让方后需是否开票',
      prop: 'isInvoiceFlag',
      minWidth: 160,
      format: 'dictTag',
      dictKey: 'is_sys_bool',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      format: 'dictTag',
      dictKey: 'process_status',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    {
      prop: 'operation',
      width: 200,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

// 转让-科目校验
export const optionsConfigDialog = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'convertTransferId', // 表格唯一id
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
      url: '/engine/finance/convert-transfer-detail/check',
      method: 'post'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', minWidth: 130 },
    { label: '客户名称', prop: 'clientCode', minWidth: 130 },
    {
      prop: 'orgId',
      label: '签约主体',
      minWidth: 140,
      type: 'select',
      tooltip: true,
      format: 'dict',
      dictKey: 'company',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    {
      label: '应收租金',
      prop: 'receivableRentBalance',
      minWidth: 130,
      render: ({ receivableRentBalance }) => toThousands(receivableRentBalance)
    },
    {
      label: '应收期末残值',
      minWidth: 130,
      prop: 'receivableResidualValueAmount',
      render: ({ receivableResidualValueAmount }) => toThousands(receivableResidualValueAmount)
    },
    {
      label: '应收销项税',
      prop: 'receivableOuttaxBalance',
      minWidth: 130,
      render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance)
    },
    {
      label: '未实现融资租赁收益',
      prop: 'leaseRevenueBalance',
      minWidth: 160,
      render: ({ leaseRevenueBalance }) => toThousands(leaseRevenueBalance)
    },
    {
      label: '承租人保证金',
      prop: 'lesseeMarginBalance',
      minWidth: 130,
      render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance)
    }
  ]
})

const btnInternalConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    level: 'S',
    isDisabled: false,
    params: {
      url: '/engine/finance/discounted-transfer-internal/importFile',
      title: '内部调拨',
      open: true,
      templateUrl: '/engine/finance/discounted-transfer-internal/exportTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/discounted-transfer-internal/export',
    isParams: true,
    level: 'S',
    fetchKey: 'idList',
    filename: '折价转让-内部调拨.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/discounted-transfer-internal/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'S',
    url: '/engine/finance/discounted-transfer-internal/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/discounted-transfer-internal/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'S',
    url: '/engine/finance/discounted-transfer-internal/delete'
  }
}

// 内部调拨
export const internalOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/discounted-transfer-internal/page',
      method: 'post'
    }
  },
  btnConfig: btnInternalConfig,
  columns: [
    {
      label: '支付日期',
      prop: 'paymentDate',
      search: true,
      hide: true,
      type: 'date',
      attrs: {
        type: 'date',
        valueFormat: 'YYYY-MM-DD',
        format: 'YYYY-MM-DD'
      }
    },
    {
      label: '转让批次',
      prop: 'batchList',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/convert-transfer/batchList',
        method: 'post'
      }
    },
    {
      label: '转让批次',
      prop: 'batch',
      minWidth: 120,
      tooltip: true
    },
    {
      label: '转让方',
      prop: 'transferParty',
      minWidth: 120,
      tooltip: true,
      render(row) {
        return dictMappingLabel(dictData, 'company', row.transferParty)
      }
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 120,
      tooltip: true
    },
    {
      label: '财务日期',
      prop: 'financeDate',
      minWidth: 120
    },
    {
      label: '金额',
      prop: 'amount',
      minWidth: 120,
      render: ({ amount }) => toThousands(amount)
    },
    {
      label: '支付日期',
      prop: 'paymentDate',
      minWidth: 120
    },
    {
      label: '银行账号',
      prop: 'bankAccountCode',
      minWidth: 120,
      tooltip: true
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      minWidth: 120,
      format: 'dictTag',
      dictKey: 'process_status',
      render(row, item) {
        return changeCellValue(row, item, dictData)
      }
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

import { ElDatePicker } from 'element-plus'
import USelect from '@/components/USelect/index.vue'
import { markRaw } from 'vue'

// 内部调拨-生成支付信息
export const internalFormItems = [
  {
    label: '财务日期',
    prop: 'financeDate',
    isCustomized: false,
    fieldType: markRaw(ElDatePicker),
    props: {
      type: 'date',
      'value-format': 'YYYY-MM-DD',
      format: 'YYYY-MM-DD',
      placeholder: '请选择财务日期',
      class: 'w-100'
    },
    rules: [{ required: true, message: '请选择财务日期', trigger: ['blur', 'change'] }]
  },
  {
    label: '转让批次',
    prop: 'batchList',
    fieldType: markRaw(USelect),
    isCustomized: true,
    props: {
      request: { url: '/engine/finance/convert-transfer/batchList', method: 'post' },
      attrs: { filterable: true, multiple: true },
      placeholder: '请选择转让批次'
    },
    rules: [{ required: true, message: '请选择转让批次', trigger: ['blur', 'change'] }]
  }
]
