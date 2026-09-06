import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '成本类',
  icon: '',
  name: 'costCategorize'
}

const commonColumn = (dictData) => [
  {
    label: '业务日期',
    prop: 'businessDate',
    width: 200,
    type: 'date',
    search: true,
    cover: ['startBusinessDate', 'endBusinessDate'],
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
    label: '财务日期',
    prop: 'financialDate',
    width: 200,
    type: 'date',
    search: true,
    cover: ['startFinancialDate', 'endFinancialDate'],
    attrs: {
      type: 'daterange',
      'range-separator': '-',
      'start-placeholder': '开始时间',
      'end-placeholder': '结束时间',
      format: 'YYYY-MM-DD',
      'value-format': 'YYYY-MM-DD'
    }
  },

  { label: '合同编号', prop: 'contractCode', width: 200, search: true },
  { label: '合同名称', prop: 'contractName', width: 200, search: true },
  {
    prop: 'contractStatus',
    label: '合同状态',
    width: 200,
    option: dictMappingToArray(dictData, 'business_contract_status'),
    render({ contractStatus }) {
      return (
        (contractStatus &&
          h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', contractStatus))) ||
        ''
      )
    }
  },
  {
    prop: 'contractStatusList',
    label: '合同状态',
    type: 'select',
    width: 200,
    search: true,
    hide: true,
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'business_contract_status')
  },

  {
    prop: 'financialContractStatus',
    label: '财务合同状态',
    width: 200,
    type: 'select',

    render({ financialContractStatus }) {
      return (
        (financialContractStatus &&
          h(ElTag, () =>
            dictMappingLabel(dictData, 'financial_contract_status', financialContractStatus)
          )) ||
        ''
      )
    }
  },

  {
    prop: 'financialContractStatusList',
    label: '财务合同状态',
    type: 'select',
    width: 200,
    search: true,
    hide: true,
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'financial_contract_status')
  },

  {
    label: '费用类型',
    prop: 'channelType',
    width: 200,
    // option: dictMappingToArray(dictData, 'charge_type'),
    render({ channelType }) {
      return (
        (channelType && h(ElTag, () => dictMappingLabel(dictData, 'charge_type', channelType))) ||
        ''
      )
    }
  },
  {
    label: '是否区分合同状态',
    prop: 'isContractStatus',
    width: 200,
    render({ isContractStatus }) {
      return (
        (isContractStatus &&
          h(ElTag, () =>
            dictMappingLabel(dictData, 'is_differ_contract_status', isContractStatus)
          )) ||
        ''
      )
    }
  }
]

const commonBase = {
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
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
      url: '/engine/finance/cost-channel-fee/page',
      method: 'post'
    }
  }
}

const commonProcessStatus = (dictData, title) => [
  {
    prop: 'processStatusList',
    label: '处理状态',
    search: true,
    hide: true,
    type: 'select',
    attrs: {
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'process_status')
  },
  {
    prop: 'processStatus',
    label: '处理状态',
    width: 100,
    render(row) {
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
    display: false,
    fixed: 'right'
  }
]

const btnConfig = (otherBtn = {}, title, expenseMainCategoryType, level) => {
  return {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      url: '/engine/finance/cost-channel-fee/exportFile',
      filename: `出表ABS-${title}.xlsx`,
      fetchKey: 'idList',
      isParams: true,
      level,
      params: { expenseMainCategoryType }
    },
    ...otherBtn,
    voucher: {
      title: '生成凭证',
      type: 'warning',
      level,
      url: '/engine/finance/cost-channel-fee/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      level,
      url: '/engine/finance/cost-channel-fee/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level,
      url: '/engine/finance/cost-channel-fee/withdraw'
    }
  }
}

// gps
export const gpsConfig = (router, dictData = {}) => {
  return {
    ...commonBase,
    btnConfig: btnConfig(
      {
        import: {
          title: '上传',
          type: 'primary',
          level: 'F',
          isDisabled: false,
          params: {
            url: '/engine/finance/cost-channel-fee/importTemplate',
            title: 'GPS',
            data: {
              expenseType: '1',
              fileType: '1'
            },
            open: true,
            templateUrl: [
              {
                url: '/engine/finance/cost-channel-fee/gpsAndBraceletExport',
                title: 'GPS'
              }
            ]
          }
        }
      },
      'GPS',
      '1',
      'F'
    ),
    columns: [
      ...commonColumn(dictData),
      {
        label: '实付金额（含税）',
        prop: 'actualAmount',
        width: 200,
        render: ({ actualAmount }) => toThousands(actualAmount)
      },
      {
        label: '对应交易结构金额（不含税）',
        prop: 'noTaxTransactionAmount',
        width: 250,
        render: ({ noTaxTransactionAmount }) => toThousands(noTaxTransactionAmount)
      },
      {
        label: '税额',
        prop: 'taxAmount',
        width: 200,
        render: ({ taxAmount }) => toThousands(taxAmount)
      },
      {
        label: '发票不含税额',
        prop: 'noTaxAmount',
        width: 200,
        render: ({ noTaxAmount }) => toThousands(noTaxAmount)
      },
      ...commonProcessStatus(dictData)
    ]
  }
}
// device
export const deviceConfig = (router, dictData = {}) => ({
  ...commonBase,
  btnConfig: btnConfig(
    {
      import: {
        title: '上传',
        type: 'primary',
        isDisabled: false,
        level: 'S',
        params:{
          url: '/engine/finance/cost-channel-fee/importTemplate',
          title:'手环设备款',
        data: {
          expenseType: '2',
          fileType: '1'
        },
        open: true,
        templateUrl: [
          {
            url: '/engine/finance/cost-channel-fee/gpsAndBraceletExport',
            title: '手环设备款'
          }
        ]
        }
      }
    },
    '手环设备款',
    '2',
    'S'
  ),
  columns: [
    ...commonColumn(dictData),

    {
      label: '实付金额（含税）',
      prop: 'actualAmount',
      width: 200,
      render: ({ actualAmount }) => toThousands(actualAmount)
    },
    {
      label: '对应交易结构金额（不含税）',
      prop: 'noTaxTransactionAmount',
      width: 250,
      render: ({ noTaxTransactionAmount }) => toThousands(noTaxTransactionAmount)
    },
    {
      label: '税额',
      prop: 'taxAmount',
      width: 200,
      render: ({ taxAmount }) => toThousands(taxAmount)
    },
    {
      label: '发票不含税额',
      prop: 'noTaxAmount',
      width: 200,
      render: ({ noTaxAmount }) => toThousands(noTaxAmount)
    },
    ...commonProcessStatus(dictData)
  ]
})
// 经销商服务费&外部渠道费&海通渠道费
export const serviceConfig = (router, dictData = {}) => ({
  ...commonBase,
  btnConfig: btnConfig(
    {
      import: {
        title: '支付上传',
        type: 'primary',
        level: 'T',
        isDisabled: false,
        params: {
          url: '/engine/finance/cost-channel-fee/importTemplate',
          title: '支付',
          data: {
            expenseType: '3',
            fileType: '1'
          },
          open: true,
          templateUrl: [{
            url: '/engine/finance/cost-channel-fee/export?fileType=1',
            title: '支付'
          }]
        }
      },
      import1: {
        title: '税率分摊上传',
        type: 'primary',
        level: 'T',
        isDisabled: false,
        params: {
          url: '/engine/finance/cost-channel-fee/importTemplate',
          title: '税率分摊',
          data: {
            expenseType: '3',
            fileType: '2'
          },
          open: true,
          templateUrl: [{
            url: '/engine/finance/cost-channel-fee/export?fileType=2',
            title: '税率分摊'
          }]
        }
      }
    },
    '经销商服务费&外部渠道费&海通渠道费',
    '3',
    'T'
  ),
  columns: [
    ...commonColumn(dictData),

    { label: '渠道方编码', prop: 'channelCode', width: 200 },
    { label: '渠道方名称', prop: 'channelName', width: 200 },
    { label: '主机厂', prop: 'hostFactory', width: 200 },
    {
      label: '实付金额（含税）',
      prop: 'actualAmount',
      width: 200,
      render: ({ actualAmount }) => toThousands(actualAmount)
    },
    {
      label: '对应交易结构金额（不含税）',
      prop: 'noTaxTransactionAmount',
      width: 250,
      render: ({ noTaxTransactionAmount }) => toThousands(noTaxTransactionAmount)
    },
    { label: '交易结构调整类型', prop: 'structureType', width: 200 },
    {
      label: '税额',
      prop: 'taxAmount',
      width: 200,
      render: ({ taxAmount }) => toThousands(taxAmount)
    },
    {
      label: '发票不含税额',
      prop: 'noTaxAmount',
      width: 200,
      render: ({ noTaxAmount }) => toThousands(noTaxAmount)
    },
    ...commonProcessStatus(dictData)
  ]
})
// carFee
export const carFeeConfig = (router, dictData = {}) => ({
  ...commonBase,
  btnConfig: btnConfig(
    {
      import: {
        title: '上传',
        type: 'primary',
        level: 'Fo',
        isDisabled: false,
        params: {
          url: '/engine/finance/cost-channel-fee/importTemplate',
          title: '收车费&抵押费&解抵押费',
          data: {
            expenseType: '4',
            fileType: '1'
          },
          open: true,
          templateUrl: [
            {
              url: '/engine/finance/cost-channel-fee/gpsAndBraceletExport',
              title: '收车费&抵押费&解抵押费'
            }
          ]
        }
      },
    //   import: {
    //     title: '支付上传',
    //     type: 'primary',
    //     level: 'Fo',
    //     isDisabled: false,
    //     params: {
    //       url: '/engine/finance/cost-channel-fee/importTemplate',
    //       title: '支付',
    //       data: {
    //         expenseType: '4',
    //         fileType: '1'
    //       },
    //       open: true,
    //       templateUrl: [{
    //         url: '/engine/finance/cost-channel-fee/export?fileType=1',
    //         title: '支付'
    //       }]
    //     }
    //   },
    //   import1: {
    //     title: '税率分摊上传',
    //     type: 'primary',
    //     level: 'Fo',
    //     isDisabled: false,
    //     params: {
    //       url: '/engine/finance/cost-channel-fee/importTemplate',
    //       title: '税率分摊',
    //       data: {
    //         expenseType: '4',
    //         fileType: '2'
    //       },
    //       open: true,
    //       templateUrl: [{
    //         url: '/engine/finance/cost-channel-fee/export?fileType=2',
    //         title: '税率分摊'
    //       }]
    //     }
    //   }
    },
    '收车费&抵押费&解抵押费',
    '4',
    'Fo'
  ),
  columns: [
    ...commonColumn(dictData),
    
    {
      label: '实付金额（含税）',
      prop: 'actualAmount',
      width: 200,
      render: ({ actualAmount }) => toThousands(actualAmount)
    },
    {
      label: '对应交易结构金额（不含税）',
      prop: 'noTaxTransactionAmount',
      width: 250,
      render: ({ noTaxTransactionAmount }) => toThousands(noTaxTransactionAmount)
    },
    {
      label: '税额',
      prop: 'taxAmount',
      width: 200,
      render: ({ taxAmount }) => toThousands(taxAmount)
    },
    {
      label: '发票不含税额',
      prop: 'noTaxAmount',
      width: 200,
      render: ({ noTaxAmount }) => toThousands(noTaxAmount)
    },

    ...commonProcessStatus(dictData)
  ]
})
