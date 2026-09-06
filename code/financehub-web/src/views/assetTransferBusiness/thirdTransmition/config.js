export default {
  hidden: false,
  title: '第三方转让',
  icon: '',
  name: 'thirdTransmition'
}

import moment from 'moment'
import { h, markRaw } from 'vue'
import { ElTag, ElDatePicker } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { changeCellValue } from '@/utils/format'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要复选框
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isShowSummary: false, // 合并计算表格
  span: 6,
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  addBtn: {
    isShow: false
  }
}

// 转让
export const transferOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/convert-transfer-third/page',
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
      type: 'primary',
      isDisabled: false,
      level: 'T',
      params: {
        url: '/engine/finance/convert-transfer-third/import',
        title: '第三方转让',
        open: true,
        templateUrl: '/engine/finance/convert-transfer-third/template'
      }
    },
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-third/export',
      level: 'T',
      fetchKey: 'idList',
      filename: '第三方转让.xlsx',
      isParams: true
    },
    voucher: {
      title: '生成凭证',
      type: 'warning',
      level: 'T',
      url: '/engine/finance/convert-transfer-third/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'T',
      url: '/engine/finance/convert-transfer-third/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'T',
      url: '/engine/finance/convert-transfer-third/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'T',
      url: '/engine/finance/convert-transfer-third/delete'
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      minWidth: 140,
      render: ({ businessDate }) => {
        return businessDate && moment(businessDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '记账日期',
      type: 'date',
      prop: 'accountDate',
      minWidth: 140,
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
      label: '转让批次',
      prop: 'batch',
      search: true,
      minWidth: 140
    },
    {
      label: '转让方',
      prop: 'transferParty',
      width: 200,
      search: true,
      type: 'select',
      tooltip: true,
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
      minWidth: 140,
      tooltip: true
    },
    {
      label: '基准日',
      prop: 'referenceDate',
      minWidth: 140,
      render: ({ referenceDate }) => {
        return referenceDate && moment(referenceDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '交易日',
      prop: 'tradeDate',
      minWidth: 140,
      render: ({ tradeDate }) => {
        return tradeDate && moment(tradeDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '转让价格',
      prop: 'transferPrice',
      minWidth: 140,
      render: ({ transferPrice }) => toThousands(transferPrice)
    },
    {
      label: '合同数量',
      prop: 'contractNum',
      minWidth: 140
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      minWidth: 140,
      render({ processStatus }) {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
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

// 科目校验
export const optionsCfgDialog = (router, dictData = {}) => ({
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
      url: '/engine/finance/convert-transfer-third-detail/check',
      method: 'post'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', minWidth: 130 },
    { label: '客户名称', prop: 'clientName', minWidth: 130 },
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
      prop: 'receivableRent',
      minWidth: 130,
      render: ({ receivableRent }) => toThousands(receivableRent)
    },
    {
      label: '应收期末残值',
      minWidth: 130,
      prop: 'receivableResidualValue',
      render: ({ receivableResidualValue }) => toThousands(receivableResidualValue)
    },
    {
      label: '应收销项税',
      prop: 'receivableOuttax',
      minWidth: 130,
      render: ({ receivableOuttax }) => toThousands(receivableOuttax)
    },
    {
      label: '未实现融资租赁收益',
      prop: 'unrealizedRevenue',
      minWidth: 160,
      render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue)
    },
    {
      label: '承租人保证金',
      prop: 'lesseeMargin',
      minWidth: 130,
      render: ({ lesseeMargin }) => toThousands(lesseeMargin)
    }
    // {
    //   label: '应收合同解约及变更手续费',
    //   prop: '',
    //   minWidth: 130
    // },
    // {
    //   label: '应收罚息',
    //   prop: '',
    //   minWidth: 130
    // },
    // {
    //   label: '应收融资租赁款组合拨备',
    //   prop: '',
    //   minWidth: 130
    // }
  ]
})

// 转付
export const TransferPaymentOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/convert-transfer-third-payment/page',
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
      url: '/engine/finance/convert-transfer-third-payment/export',
      level: 'P',
      fetchKey: 'idList',
      filename: '第三方转让-转付.xlsx',
      isParams: true
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      level: 'P',
      params: {
        url: '/engine/finance/convert-transfer-third-payment/import-payment',
        title: '第三方转让-转付',
        open: true,
        templateUrl: '/engine/finance/convert-transfer-third-payment/get-import-template'
      }
    },
    voucher: {
      title: '生成凭证',
      type: 'warning',
      level: 'P',
      url: '/engine/finance/convert-transfer-third-payment/generate-voucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'P',
      url: '/engine/finance/convert-transfer-third-payment/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'P',
      url: '/engine/finance/convert-transfer-third-payment/withdraw'
    }
    // delete: {
    //   title: '删除',
    //   type: 'danger',
    //   level: 'P',
    //   url: ''
    // }
  },
  columns: [
    {
      prop: 'tradeDate',
      label: '业务日期',
      minWidth: 140,
      render: ({ tradeDate }) => {
        return tradeDate && moment(tradeDate).format('YYYY-MM-DD')
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      search: true,
      type: 'date',
      minWidth: 140,
      attrs: {
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      },
      render: ({ accountDate }) => {
        return accountDate && moment(accountDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '转让批次',
      prop: 'batch',
      search: true,
      minWidth: 140
    },
    {
      label: '支付日期',
      prop: 'paymentDate',
      minWidth: 140,
      render: ({ paymentDate }) => {
        return paymentDate && moment(paymentDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '支付主体',
      prop: 'paymentOrgId',
      minWidth: 140
    },
    {
      label: '转付金额',
      prop: 'paymentAmount',
      minWidth: 140,
      render: ({ paymentAmount }) => toThousands(paymentAmount)
    },
    {
      label: '银行账号',
      prop: 'bankAccountCode',
      minWidth: 140
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      minWidth: 140,
      render({ processStatus }) {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
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

import USelect from '@/components/USelect/index.vue'

// 转付-生成支付信息
export const paymentFormItems = [
  {
    label: '支付日期',
    prop: 'paymentDate',
    isCustomized: false,
    fieldType: markRaw(ElDatePicker),
    props: {
      type: 'date',
      'value-format': 'YYYY-MM-DD',
      format: 'YYYY-MM-DD',
      placeholder: '请选择支付日期',
      class: 'w-100'
    },
    rules: [{ required: true, message: '请选择支付日期', trigger: ['blur', 'change'] }]
  },
  {
    label: '批次',
    prop: 'batch',
    fieldType: markRaw(USelect),
    isCustomized: true,
    props: {
      request: { url: '/engine/finance/convert-transfer-third/batchList', method: 'post' },
      placeholder: '请选择批次'
    },
    rules: [{ required: true, message: '请选择批次', trigger: ['blur', 'change'] }]
  }
]
