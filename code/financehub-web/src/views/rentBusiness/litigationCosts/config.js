import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '诉讼费',
  icon: '',
  name: 'litigationCosts'
}

// 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    level: 'F',
    params: {
      url: '/engine/court-cost/importFile',
      title: '诉讼费转费用',
      open: true,
      templateUrl: '/engine/court-cost/exportTemplate'
    }
  },
  export: {
    title: '导出',
    type: 'primary',
    level: 'F',
    isParams: true,
    url: '/engine/court-cost/export',
    fetchKey: 'courtCostIdList',
    filename: '诉讼费转费用.xlsx'
  },
  voucher: {
    title: '生成凭证',
    level: 'F',
    type: 'warning',
    url: '/engine/court-cost/generateVoucher'
  },
  delete: {
    title: '删除',
    level: 'F',
    type: 'danger',
    url: '/engine/court-cost/deleteByIds'
  },
  submit: {
    title: '提交',
    level: 'F',
    type: 'success',
    url: '/engine/court-cost/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/court-cost/withdraw'
  }
}

export const optionsConfig = (router, dictData = {}) => ({
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
  request: { // 请求参数
    list: {
      url: '/engine/court-cost/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
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
    }, {
      prop: 'transgerCostAmount',
      label: '转费用金额',
      render (row) {
        return toThousands(row.transgerCostAmount)
      }
    },

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
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    }, {
      prop: 'operation',
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'

    }]
})

export const optionsReportConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  // span: 8,
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
      url: '/engine/court-cost/reportForm/page',
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
      level: 'S',
      isParams: true,
      filename: '诉讼费报表.xlsx',
      url: '/engine/court-cost/reportForm/export'
    }
  },
  columns: [
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
      hide: true,
      width: 150,
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
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,
      // fixed: "left",
      width: 200
    },
    // {
    //   prop: 'contractName',
    //   label: '合同名称',
    //   width: 200
    // },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      minWidth: 250,
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      label: '应收诉讼费科目',
      prop: 'receivable',
      align: 'center',
      children: [
        {
          prop: 'litigationExpensesBalance',
          label: '期初余额',
          render (row) {
            return toThousands(row.litigationExpensesBalance)
          },
          width: 200
        },
        {
          prop: 'receivableRentPay',
          label: '诉讼费支付',
          render (row) {
            return toThousands(row.receivableRentPay)
          },
          width: 200
        },
        {
          prop: 'receivableRentRecover',
          label: '诉讼费收回',
          render (row) {
            return toThousands(row.receivableRentRecover)
          },
          width: 200
        },
        {
          prop: 'transgerCostAmount',
          label: '诉讼费转费用',
          render (row) {
            return toThousands(row.transgerCostAmount)
          },
          width: 200
        },

        {
          prop: 'litigationExpensesEndBalance',
          label: '期末余额',
          render (row) {
            return toThousands(row.litigationExpensesEndBalance)
          },
          width: 200
        }
      ]
    },
    {
      prop: 'litigationl',
      label: '管理费用-诉讼费科目',
      width: 200,
      align: 'center',
      children: [{
        prop: 'litigationExpensesPayTotal',
        label: '支付金额',
        render (row) {
          return toThousands(row.litigationExpensesPayTotal)
        },
        width: 200
      },
      {
        prop: 'litigationExpensesRecoverTotal',
        label: '收回金额',
        render (row) {
          return toThousands(row.litigationExpensesRecoverTotal)
        },
        width: 200
      }]
    },

    {
      prop: 'receivable',
      label: '代收款项科目',
      width: 200,
      align: 'center',
      children: [
        {
          prop: 'receivableRentPayTotal',
          label: '支付金额',
          render (row) {
            return toThousands(row.receivableRentPayTotal)
          },
          width: 200
        },

        {
          prop: 'receivableRentRecoverTotal',
          label: '收回金额',
          render (row) {
            return toThousands(row.receivableRentRecoverTotal)
          },
          width: 200
        }]
    },
    {
      prop: 'operation',
      label: '操作',
      width: 100,
      display: false,
      fixed: 'right'

    }
  ]
})
