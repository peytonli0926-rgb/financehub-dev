export default {
  hidden: false,
  title: '转让其他',
  icon: '',
  name: 'transferOther'
}

import moment from 'moment'
import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

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
      url: '/engine/finance/converter-transfer-other/page',
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
      url: '/engine/finance/converter-transfer-other/export',
      level: 'T',
      filename: '其他-转让.xlsx',
      isParams: true,
      isDisabled: false
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      level: 'T',
      params: {
        url: '/engine/finance/converter-transfer-other/importExcel',
        title: '其他-转让',
        open: true,
        templateUrl: '/engine/finance/converter-transfer-other/download-template'
      }
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'T',
      url: '/engine/finance/converter-transfer-other/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'T',
      url: '/engine/finance/converter-transfer-other/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'T',
      url: '/engine/finance/converter-transfer-other/delete'
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      minWidth: 140
    },
    {
      label: '转让方',
      prop: 'transferPartyList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
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
      prop: 'transfereePartyList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'clientName',
      label: '客户名称',
      minWidth: 140,
      tooltip: true
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      minWidth: 140,
      render({ financialContractStatus }) {
        return (financialContractStatus && h(ElTag, () => financialContractStatus)) || ''
      }
    },
    {
      prop: 'transferParty',
      label: '转让方',
      minWidth: 160,
      tooltip: true
    },
    {
      prop: 'transfereeParty',
      label: '受让方',
      minWidth: 160,
      tooltip: true
    },
    {
      prop: 'accountDate',
      label: '转让时间',
      minWidth: 140
    },
    {
      prop: 'referenceDate',
      label: '转让基准日',
      minWidth: 140
    },
    {
      prop: 'invoiceFlag',
      label: '转让方后续开票', // 0否 1是；这个没有单独的字典
      minWidth: 140,
      render({ invoiceFlag }) {
        return (invoiceFlag && h(ElTag, () => (invoiceFlag === '1' ? '是' : '否'))) || ''
      }
    },
    {
      prop: 'otherIncome',
      label: '其他收入',
      minWidth: 150,
      render: ({ rentActual }) => toThousands(rentActual)
    },
    {
      prop: 'otherOutcome',
      label: '其他成本',
      minWidth: 150,
      render: ({ rentActual }) => toThousands(rentActual)
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      minWidth: 140,
      render({ processStatus }) {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
      }
    }
  ]
})

// 转让合同租金计划
export const TransferContractRentPlanOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/converter-transfer-other/plan',
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
      url: '/engine/finance/converter-transfer-other/plan-export',
      level: 'R',
      filename: '其他-转让合同租金计划.xlsx',
      isParams: true
    }
  },
  columns: [
    {
      prop: 'contractCodes',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'code',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'date',
      label: '日期',
      search: true,
      type: 'date',
      hide: true,
      minWidth: 130,
      cover: ['startDate', 'endDate'],
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
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 140
    },
    {
      prop: 'planDate',
      label: '日期',
      minWidth: 140
    },
    {
      prop: 'periods',
      label: '期数',
      minWidth: 140
    },
    {
      prop: 'rentAmount',
      label: '应收租金',
      minWidth: 150,
      render: ({ rentAmount }) => toThousands(rentAmount)
    },
    {
      prop: 'principalAmount',
      label: '应收本金',
      minWidth: 150,
      render: ({ principalAmount }) => toThousands(principalAmount)
    },
    {
      prop: 'interestAmount',
      label: '应收利息',
      minWidth: 150,
      render: ({ interestAmount }) => toThousands(interestAmount)
    },
    {
      prop: 'actualRepaymentDate',
      label: '实收日期',
      minWidth: 140
    },
    {
      prop: 'actualAmount',
      label: '实收租金',
      minWidth: 150,
      render: ({ actualAmount }) => toThousands(actualAmount)
    },
    {
      prop: 'actualRepaymentAmount',
      label: '实收本金',
      minWidth: 150,
      render: ({ actualRepaymentAmount }) => toThousands(actualRepaymentAmount)
    },
    {
      prop: 'actualRepaymentInteresAmount',
      label: '实收利息',
      minWidth: 150,
      render: ({ actualRepaymentInteresAmount }) => toThousands(actualRepaymentInteresAmount)
    },
    {
      prop: 'taxValue',
      label: '实际开票',
      minWidth: 150,
      render: ({ taxValue }) => toThousands(taxValue)
    }
  ]
})

// 转付
export const TransferPaymentOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/converter-transfer-other-payment/page',
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
      url: '/engine/finance/converter-transfer-other-payment/export',
      level: 'P',
      filename: '其他-转付.xlsx',
      isParams: true
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      level: 'P',
      params: {
        url: '/engine/finance/converter-transfer-other-payment/import-payment',
        title: '其他-转付',
        open: true,
        templateUrl: '/engine/finance/converter-transfer-other-payment/download-template'
      }
    },
    voucher: {
      title: '生成凭证',
      type: 'warning',
      level: 'P',
      url: '/engine/finance/converter-transfer-other-payment/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'P',
      url: '/engine/finance/converter-transfer-other-payment/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'P',
      url: '/engine/finance/converter-transfer-other-payment/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'P',
      url: '/engine/finance/converter-transfer-other-payment/delete'
    }
  },
  columns: [
    {
      prop: 'accountDate',
      label: '记账日期',
      search: true,
      type: 'date',
      minWidth: 130,
      cover: ['accountStartDate', 'accountEndDate'],
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
      prop: 'contractCodes',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'code',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 140
    },
    {
      prop: 'planDate',
      label: '计划日期',
      minWidth: 140,
      render: ({ planDate }) => {
        return planDate && moment(planDate).format('YYYY-MM-DD')
      }
    },
    {
      prop: 'period',
      label: '期数',
      minWidth: 140
    },
    {
      prop: 'rentAmount',
      label: '应付租金',
      minWidth: 150,
      render: ({ rentAmount }) => toThousands(rentAmount)
    },
    {
      prop: 'principalAmount',
      label: '应付本金',
      minWidth: 150,
      render: ({ principalAmount }) => toThousands(principalAmount)
    },
    {
      prop: 'interestAmount',
      label: '应付利息',
      minWidth: 150,
      render: ({ interestAmount }) => toThousands(interestAmount)
    },
    {
      prop: 'actualDate',
      label: '实付日期',
      minWidth: 140,
      render: ({ actualDate }) => {
        return actualDate && moment(actualDate).format('YYYY-MM-DD')
      }
    },
    {
      prop: 'rentActual',
      label: '实付租金',
      minWidth: 150,
      render: ({ rentActual }) => toThousands(rentActual)
    },
    {
      prop: 'principalActual',
      label: '实付本金',
      minWidth: 150,
      render: ({ principalActual }) => toThousands(principalActual)
    },
    {
      prop: 'interestActual',
      label: '实付利息',
      minWidth: 150,
      render: ({ interestActual }) => toThousands(interestActual)
    },
    {
      prop: 'calculateDeductions',
      label: '计算扣额',
      minWidth: 150,
      render: ({ calculateDeductions }) => toThousands(calculateDeductions)
    },
    {
      prop: 'bankAccountCode',
      label: '银行账号',
      minWidth: 150,
      tooltip: true
    },
    {
      prop: 'processStatus',
      label: '处理状态',
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
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
