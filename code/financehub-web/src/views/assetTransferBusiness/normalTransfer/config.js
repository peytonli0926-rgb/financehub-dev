import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '平价转让',
  icon: '',
  name: 'normalTransfer'
}

const btnInboundConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  // import1: {
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
      url: '/engine/finance/parity-transfer/importTemplate',
      title: '平价转让',
      open: true,
      templateUrl: '/engine/finance/parity-transfer/downLoad'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/parity-transfer/export',
    level: 'F',
    // fetchKey: 'recyclingEquipmentInIdList',
    filename: '平价转让-转入.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/parity-transfer/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'F',
    url: '/engine/finance/parity-transfer/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/parity-transfer/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'F',
    url: '/engine/finance/parity-transfer/deleteByIds'
  }
}

const btnOutboundConfig = {
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
      url: '/engine/finance/internal-transfer/importFile',
      title: '内部调拨',
      open: true,
      templateUrl: '/engine/finance/internal-transfer/exportTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/internal-transfer/export',
    isParams: true,
    level: 'S',
    fetchKey: 'idList',
    filename: '平价转让-内部调拨.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/internal-transfer/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'S',
    url: '/engine/finance/internal-transfer/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/internal-transfer/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'S',
    url: '/engine/finance/internal-transfer/delete'
  }
}

// 转入
export const optionsConfigInbound = (router, dictData = {}) => ({
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
      url: '/engine/finance/parity-transfer/page',
      method: 'post'
    }
  },
  btnConfig: btnInboundConfig,
  columns: [
    { label: '业务日期', type: 'date', prop: 'businessDate', width: 100 },
    {
      label: '记账日期',
      type: 'date',
      prop: 'accountDate',
      width: 100,
      cover: ['startAccountDate', 'endAccountDate'],
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
        url: '/engine/finance/parity-transfer/batchList',
        method: 'post'
      }
    },
    {
      prop: 'transferPartyList',
      label: '转让方',
      width: 200,
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
      prop: 'transfereePartyList',
      label: '受让方',
      width: 200,
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
    { label: '转让批次', prop: 'batch', width: 200 },
    { label: '转让方', prop: 'transferParty', width: 250, tooltip: true, render: ({ transferParty }) => (transferParty && dictMappingLabel(dictData, 'company', transferParty)) || '' },
    { label: '受让方', prop: 'transfereeParty', width: 250, tooltip: true, render: ({ transfereeParty }) => (transfereeParty && dictMappingLabel(dictData, 'company', transfereeParty)) || '' },
    { label: '基准日', type: 'date', prop: 'referenceDate', width: 100 },
    { label: '交易日', type: 'date', prop: 'tradeDate', width: 100 },
    {
      label: '转让价格',
      prop: 'transferPrice',
      width: 200,
      render: ({ transferPrice }) => toThousands(transferPrice)
    },
    { label: '合同数量', prop: 'contractNum', width: 100 },
    {
      label: '转让方后需是否开票',
      prop: 'isInvoiceFlag',
      width: 200,
      render ({ isInvoiceFlag }) {
        return (
          (isInvoiceFlag &&
            h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', isInvoiceFlag))) ||
          ''
        )
      }
    },

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

// 内部调拨
export const optionsConfigOutbound = (router, dictData = {}) => ({
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
      url: '/engine/finance/internal-transfer/page',
      method: 'post'
    }
  },
  btnConfig: btnOutboundConfig,
  columns: [
    {
      label: '支付日期',
      prop: 'paymentDate',
      search: true,
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
        url: '/engine/finance/parity-transfer/batchList',
        method: 'post'
      }
    },
    { label: '转让批次', prop: 'batch' },
    {
      label: '转让方',
      prop: 'transferParty',
      tooltip: true,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.transferParty)
      }

    },
    { label: '合同编号', prop: 'contractCode' },
    { label: '金额', prop: 'amount', render: ({ amount }) => toThousands(amount) },
    { label: '财务日期', prop: 'financeDate', width: 100 },

    { label: '银行账号编码', prop: 'bankAccountCode', tooltip: true },

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
      width: 100,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

export const optionsConfigDialog = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
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
      url: '/engine/finance/recycling-equipment/remainBalance/check',
      method: 'post'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', search: true },
    { label: '客户名称', prop: 'clientName', search: true },
    {
      prop: 'orgId',
      label: '签约主体',
      width: 200,
      type: 'select',
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      label: '应收租金',
      prop: 'receivableRentBalance',
      search: true,
      render: ({ receivableRentBalance }) => toThousands(receivableRentBalance)
    },
    {
      label: '应收期末残值',
      prop: 'receivableResidualValueBalance',
      search: true,
      render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance)
    },
    {
      label: '应收销项税',
      prop: 'receivableOuttaxBalance',
      search: true,
      render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance)
    },
    {
      label: '未实现融资租赁收益',
      prop: 'unrealizedRevenueBalance',
      search: true,
      render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance)
    },
    {
      label: '承租人保证金',
      prop: 'lesseeMarginBalance',
      search: true,
      render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance)
    }
  ]
})
