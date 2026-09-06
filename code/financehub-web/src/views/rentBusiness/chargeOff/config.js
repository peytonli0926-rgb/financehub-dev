import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: 'ChargeOff',
  icon: '',
  name: 'chargeOff'
}
// 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '新增上传',
    type: 'primary',
    isDisabled: false,
    level: 'F',
    params: {
      url: '/engine/finance/charge-off/importTemplate',
      title: 'chargeOff',
      data: { type: '0' },
      open: true,
      templateUrl: [
        {
          url: '/engine/finance/charge-off/download',
          title: '上传chargeOff',
          method: 'Post',
          params: { type: '0' }
        }
      ]
    }
  },
  import1: {
    title: '修改上传',
    type: 'primary',
    isDisabled: false,
    level: 'F',
    params: {
      url: '/engine/finance/charge-off/importTemplate',
      title: 'chargeOff',
      data: { type: '1' },
      open: true,
      templateUrl: [
        {
          url: '/engine/finance/charge-off/download',
          title: '修改chargeOff',
          method: 'Post',
          params: { type: '1' }
        }
      ]
    }
  },
  // voucher: {
  //   title: '生成凭证',
  //   type: 'warning',
  //   url: '/engine/verification/generateVoucher',
  // },
  submit: {
    title: '提交',
    level: 'F',
    type: 'success',
    url: '/engine/finance/charge-off/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/charge-off/withdraw'
  },
  delete: {
    title: '删除',
    level: 'F',
    type: 'danger',
    url: '/engine/verification/deleteByIds'
  }
}
const commonColumn = (dictData) => [
  {
    prop: 'contractCode',
    label: '合同编号',
    search: true,
    width: 200
  },

  {
    prop: 'orgIdList',
    label: '签约主体',
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
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    tooltip: true,
    width: 300,
    render(row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },

  {
    prop: 'verificationStatus',
    label: '核销状态',
    width: 200,
    type: 'select',
    render({ verificationStatus }) {
      return (
        (verificationStatus &&
          h(ElTag, () =>
            dictMappingLabel(dictData, 'charge_off_verification_status', verificationStatus)
          )) ||
        ''
      )
    }
  },
  {
    prop: 'verificationStatusList',
    label: '核销状态',
    width: 200,
    search: true,
    hide: true,
    type: 'select',
    attrs: {
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'charge_off_verification_status')
  },

  {
    prop: 'clientName',
    label: '客户名称',
    width: 200,
    search: true
  },

  {
    prop: 'verificationDate',
    label: '核销日期',
    width: 200,
    search: true,
    type: 'date',
    cover: ['startVerificationDate', 'endVerificationDate'],
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
    prop: 'financialExpenseAmount',
    label: '财务核销敞口',
    width: 200,
    render({ financialExpenseAmount }) {
      return toThousands(financialExpenseAmount)
    }
  },
  {
    prop: 'provisionReversalAmount',
    label: '拨备转回金额',
    width: 200,
    render({ provisionReversalAmount }) {
      return toThousands(provisionReversalAmount)
    }
  },
  {
    prop: 'badDebtWriteOffBalance',
    label: '坏账核销余额',
    width: 200,
    render({ badDebtWriteOffBalance }) {
      return toThousands(badDebtWriteOffBalance)
    }
  },

  {
    prop: 'taxVerificationDate',
    label: '税务核销日期',
    width: 200,
    search: true,
    type: 'date',
    cover: ['startTaxVerificationDate', 'endTaxVerificationDate'],
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
    prop: 'taxVerificationAmount',
    label: '税务核销金额',
    width: 200,
    render({ taxVerificationAmount }) {
      return toThousands(taxVerificationAmount)
    }
  }
]

// 手工
export const optionsConfigPageOne = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8,
  btnConfig,
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
      url: '/engine/finance/charge-off/page',
      method: 'post'
    }
  },
  columns: [
    ...commonColumn(dictData),
    // {
    //   prop: 'processStatusList',
    //   label: '处理状态',
    //   search: true,
    //   hide: true,
    //   type: 'select',
    //   attrs: {
    //     multiple: true,
    //     'collapse-tags': true,
    //     'collapse-tags-tooltip': true
    //   },
    //   option: dictMappingToArray(dictData, 'process_status')
    // },

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
    }
  ]
})

// 汇总
export const optionsConfigPageTwo = (router, dictData = {}) => ({
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
      url: '/engine/finance/charge-off/summaryPage',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    // export: {
    //   title: '导出',
    //   level: 'S',
    //   type: 'primary',
    //   url: '/engine/finance/charge-off/export',
    //   filename: 'chargeOff汇总.xlsx',
    //   isFull: true
    // }
    fileListBtn: {
      title: '查看列表',
      isDisabled: false,
      level: 'S',
      params: {
        moduleName: 'charge_off_summary',
        businessScene: 'charge_off_summary'
      }
    },
    export: {
      title: '导出',
      isDisabled: false,
      url: '/engine/finance/charge-off/export',
      isParams: true,
      level: 'S',
      filename: 'chargeOff汇总.xlsx',
      isAsyncFile: true
    }
  },
  columns: [
    // {
    //   prop: 'periodCode',
    //   label: '查询期间',
    //   search: true,
    //   type: 'date',
    //   attrs: {
    //     type: 'month',
    //     // 'range-separator': '-',
    //     // 'start-placeholder': '开始时间',
    //     // 'end-placeholder': '结束时间',
    //     format: 'YYYY-MM',
    //     'value-format': 'YYYYMM'
    //   },
    //   hide: true

    // },
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      type: 'select',
      display: false,
      isDefaultvalue: true,
      attrs: {
        filterable: true
      },
      hide: true,
      request: {
        // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    ...commonColumn(dictData),
    // {
    //   prop: 'processStatusList',
    //   label: '处理状态',
    //   search: true,
    //   hide: true,
    //   type: 'select',
    //   attrs: {
    //     multiple: true,
    //     'collapse-tags': true,
    //     'collapse-tags-tooltip': true
    //   },
    //   option: dictMappingToArray(dictData, 'process_status')
    // },

    // {
    //   prop: 'processStatus',
    //   label: '处理状态',
    //   width: 100,
    //   render(row) {
    //     return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
    //   }
    // },
    {
      prop: 'operation',
      width: 100,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

export const optionsFileConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8, // 查询区域每一个表单要展示的宽度
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
      url: '/engine/finance/contract-status-record/fileList',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'fileName',
      label: '文件名称'
    },
    {
      prop: 'operation',
      label: '操作',
      display: false,
      width: 100
    }
  ]
})
