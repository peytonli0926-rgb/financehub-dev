import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '出表ABS',
  icon: '',
  name: 'listOutABS'
}

// 转让按钮
const btnTransferConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    level: 'F',
    isDisabled: false,
    params: {
      url: '/engine/finance/out-table-abs/importTemplate',
      title: '出表ABS-转出',
      open: true,
      templateUrl: '/engine/finance/out-table-abs/downLoad'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/out-table-abs/export',
    filename: '出表ABS-转出.xlsx',
    level: 'F'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/out-table-abs/generateVoucher'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'F',
    url: '/engine/finance/out-table-abs/deleteByIds'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'F',
    url: '/engine/finance/out-table-abs/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/out-table-abs/withdraw'
  }
}

// 赎回按钮
const btnRansomConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    level: 'S',
    params: {
      url: '/engine/finance/asset-abs-redeem/importTemplate',
      title: '出表ABS-赎回',
      open: true,
      templateUrl: '/engine/finance/asset-abs-redeem/downLoad'
    }
  },
  export: {
    title: '导出',
    level: 'S',
    url: '/engine/finance/asset-abs-redeem/export',
    filename: '出表ABS-赎回.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/asset-abs-redeem/generateVoucher'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'S',
    url: '/engine/finance/asset-abs-redeem/deleteByIds'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'S',
    url: '/engine/finance/asset-abs-redeem/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/asset-abs-redeem/withdraw'
  }
}

// 转付按钮
const btnTransferPayConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    level: 'Fo',
    isDisabled: false,
    params: {
      url: '/engine/finance/asset-abs-transfer-payment/importTemplate',
      title: '出表ABS-转付',
      open: true,
      templateUrl: '/engine/finance/asset-abs-transfer-payment/downLoad'
    }
  },
  export: {
    title: '导出',
    level: 'Fo',
    url: '/engine/finance/asset-abs-transfer-payment/export',
    filename: '出表ABS-转付.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'Fo',
    url: '/engine/finance/asset-abs-transfer-payment/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'Fo',
    url: '/engine/finance/asset-abs-transfer-payment/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'Fo',
    url: '/engine/finance/asset-abs-transfer-payment/withdraw'
  }
}

// 转让
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
      url: '/engine/finance/out-table-abs/page',
      method: 'post'
    }
  },
  btnConfig: btnTransferConfig,
  columns: [
    { label: '业务日期', prop: 'businessDate', width: 200, type: 'date' },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
      width: 200,
      cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },

    { label: '借款合同编号', prop: 'loanContractCode', width: 200 },
    {
      label: '借款合同编号',
      prop: 'loanContractCodeList',
      width: 200,
      search: true,
      type: 'select-pagination',

      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        readonly: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: {
        label: 'name',
        value: 'code'
      }
    },
    {
      label: '出表期数',
      prop: 'periodsList',
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      search: true,
      option: dictMappingToArray(dictData, 'transfer_period')
    },
    {
      label: '出表期数',
      prop: 'periods',
      width: 200,
      render ({ periods }) {
        return dictMappingLabel(dictData, 'transfer_period', periods)
      }
    },

    { label: '管理人', prop: 'administrator', width: 200 },
    { label: '封包日', prop: 'closeDate', width: 200, type: 'date' },
    { label: '发行日', prop: 'releaseDate', width: 200, type: 'date' },
    {
      label: '转让价格',
      prop: 'transferPrice',
      width: 200,
      render: (transferPrice) => toThousands({ transferPrice })
    },
    { label: '合同数量', prop: 'contractNum', width: 200 },
    { label: '计算周期', prop: 'calculationPeriod', width: 200 },
    { label: '转付周期', prop: 'transferPeriod', width: 200 },
    { label: '兑付周期', prop: 'cashPeriod', width: 200 },
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
// 赎回
export const optionsConfigRansom = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/asset-abs-redeem/page',
      method: 'post'
    }
  },
  btnConfig: btnRansomConfig,
  columns: [
    { label: '业务日期', prop: 'businessDate', width: 200, type: 'date' },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
      width: 200,
      cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },

    { label: '借款合同编号', prop: 'loanContractCode', width: 200 },
    {
      label: '借款合同编号',
      prop: 'loanContractCodeList',
      width: 200,
      search: true,
      type: 'select-pagination',

      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: {
        label: 'name',
        value: 'code'
      }
    },
    {
      label: '出表期数',
      prop: 'periodsList',
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      search: true,
      option: dictMappingToArray(dictData, 'transfer_period')
    },
    {
      label: '出表期数',
      prop: 'periods',
      width: 200,
      render ({ periods }) {
        return dictMappingLabel(dictData, 'transfer_period', periods)
      }
    },
    { label: '实际赎回日', prop: 'actualDate', width: 200, type: 'date' },
    { label: '赎回起算日', prop: 'startDate', width: 200, type: 'date' },
    {
      label: '赎回价格',
      prop: 'redeemPriceTotal',
      width: 200,
      render: (redeemPriceTotal) => toThousands({ redeemPriceTotal })
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
// 合同计划表
export const optionsConfigRentPlan = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
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
      url: '/engine/finance/out-table-abs/rentPlanPage',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      type: 'primary',
      isDisabled: false,
      level: 'T',
      url: '/engine/finance/out-table-abs/rentPlanExport',
      isParams: true,
      filename: '出表ABS-出表合同租金计划.xlsx'
    }
  },
  columns: [
    // { label: '业务日期', prop: 'businessDate', width: 200, type: 'date' },
    {
      prop: 'planDate',
      label: '日期',
      type: 'date',
      search: true,
      width: 200,
      cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },

    { label: '借款合同编号', prop: 'loanContractCode', width: 200 },
    {
      label: '借款合同编号',
      prop: 'loanContractCodeList',
      width: 200,
      search: true,
      type: 'select-pagination',

      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: {
        label: 'name',
        value: 'code'
      }
    },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    {
      label: '合同编号',
      prop: 'contractCodeList',
      width: 200,
      search: true,
      type: 'select-pagination',
      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/contract/allContractPage',
        method: 'post',
        params: { asstType: '合同编号' }
      },
      keyValue: {
        label: 'contractCode',
        value: 'contractCode'
      }
    },
    {
      label: '出表期数',
      prop: 'periodsList',
      width: 200,
      hide: true,
      type: 'select',
      search: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'transfer_period')
    },

    {
      label: '出表期数',
      prop: 'periods',
      width: 200,
      render ({ periods }) {
        return dictMappingLabel(dictData, 'transfer_period', periods)
      }
    },
    { label: '税率', prop: 'rate', width: 200, type: 'date' },
    {
      label: '应收本金',
      prop: 'principalAmount',
      width: 200,
      render: (principalAmount) => toThousands({ principalAmount })
    },
    {
      label: '应收利息',
      prop: 'interestAmount',
      width: 200,
      render: (interestAmount) => toThousands({ interestAmount })
    },
    {
      label: '实收本金',
      prop: 'actualRepaymentPrincipalAmount',
      width: 200,
      render: (actualRepaymentPrincipalAmount) => toThousands({ actualRepaymentPrincipalAmount })
    },
    {
      label: '实收利息',
      prop: 'actualRepaymentInteresAmount',
      width: 200,
      render: (actualRepaymentInteresAmount) => toThousands({ actualRepaymentInteresAmount })
    },
    {
      label: '实收留购价',
      prop: 'receivableResidualValueAmount',
      width: 200,
      render: (receivableResidualValueAmount) => toThousands({ receivableResidualValueAmount })
    },
    {
      label: '实收罚息及手续费',
      prop: 'receivableTerminateAmount',
      width: 200,
      render: (receivableTerminateAmount) => toThousands({ receivableTerminateAmount })
    }
  ]
})
// 转付
export const optionsConfigTransferPay = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/asset-abs-transfer-payment/page',
      method: 'post'
    }
  },
  btnConfig: btnTransferPayConfig,
  columns: [
    { label: '业务日期', prop: 'businessDate', width: 200, type: 'date' },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
      width: 200,
      cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },

    { label: '借款合同编号', prop: 'loanContractCode', width: 200 },
    {
      label: '借款合同编号',
      prop: 'loanContractCodeList',
      width: 200,
      search: true,
      type: 'select-pagination',

      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: {
        label: 'name',
        value: 'code'
      }
    },
    {
      label: '出表期数',
      prop: 'periodsList',
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      search: true,
      option: dictMappingToArray(dictData, 'transfer_period')
    },
    {
      label: '出表期数',
      prop: 'periods',
      width: 200,
      render ({ periods }) {
        return dictMappingLabel(dictData, 'transfer_period', periods)
      }
    },

    {
      label: '代收款项_租金',
      prop: 'receivablesRentalAmount',
      width: 200,
      render: ({ receivablesRentalAmount }) => toThousands(receivablesRentalAmount)
    },
    {
      label: '代收款项_残值',
      prop: 'actualRetentionPurchaseAmount',
      width: 200,
      render: ({ actualRetentionPurchaseAmount }) => toThousands(actualRetentionPurchaseAmount)
    },
    {
      label: '代收款项_其他',
      prop: 'actualPenaltyInterestAmount',
      width: 200,
      render: ({ actualPenaltyInterestAmount }) => toThousands(actualPenaltyInterestAmount)
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