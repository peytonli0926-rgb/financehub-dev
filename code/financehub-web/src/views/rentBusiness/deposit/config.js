import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '保证金',
  icon: '',
  name: 'deposit'
}

// 按钮
export const btnConfig = {
  common: {
    disabled: 'marginStatus',
    getKey: 'batchId'
  },

  fileListBtn: {
    title: '查看列表',
    isDisabled: false,
    params: {
      moduleName: 'margin_contract',
      businessScene: 'marginContract'
    }
  },
  export: {
    title: '导出',
    isDisabled: false,
    url: '/engine/finance/margin-contract-balance/export',
    isParams: true,
    filename: '保证金.xlsx',
    isAsyncFile: true
  },
  cflxx: {
    title: '生成重分类信息',
    isParams: true,
    isDisabled: false,
    url: '/engine/finance/margin-contract-balance/generate/reclassification'
  },

  lxjtxx: {
    title: '生成利息计提信息',
    isParams: true,
    isDisabled: false,
    url: '/engine/finance/margin-contract-balance/generate/interest-provision'
  },

  cflpz: {
    title: '生成重分类凭证',
    type: 'success',
    isParams: true,
    fetchKey: 'batchIdList',
    condition: (item) => item.isGenerateVoucher === '0',

    url: '/engine/finance/margin-contract-balance/voucher/reclassification'
  },

  lxjtpz: {
    title: '生成利息计提凭证',
    type: 'success',
    isParams: true,
    fetchKey: 'batchIdList',
    condition: (item) => item.isGenerateVoucher === '0',

    url: '/engine/finance/margin-contract-balance/voucher/interest-provision'
  },

  submit: {
    title: '提交',
    type: 'success',
    isParams: true,
    fetchKey: 'batchIdList',
    url: '/engine/finance/margin-contract-balance/submmit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    isParams: true,
    fetchKey: 'batchIdList',
    url: '/engine/finance/margin-contract-balance/withdraw'
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
      url: '/engine/finance/margin-contract-balance/summary/page',
      method: 'post'
    }
  },
  columns: [
  
    {
      prop: 'balanceDate',
      label: '业务日期',
      alias: '年月',
      search: true,
      type: 'date',
      isSearchRequired: true,
      // cover: ['leaseDateStart', 'leaseDateEnd'],
      hide: true,
      attrs: {
        type: 'month',
        clearable: false,
        // 'range-separator': '-',
        // 'start-placeholder': '开始时间',
        // 'end-placeholder': '结束时间',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      },
      rules: [
        {
          required: true,
          message: '请选择年月',
          trigger: 'change'
        }
      ]
    },
    {
      prop: 'marginType',
      label: '类型',
      search: true,
      type: 'select',
      hide: true,
      option: dictMappingToArray(dictData, 'margin_type')
    },
    {
      prop: 'businessDate',
      type: 'date',
      label: '业务日期'
    },
    {
      prop: 'financeDate',
      type: 'date',
      label: '财务日期'
    },

    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'company'),
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 250,
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'accountCodeList',
      label: '科目名称',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        multiple: true
      },
      option: dictMappingToArray(dictData, 'sys_margin_subject'),
      render: (row) => {
        return dictMappingLabel(dictData, 'sys_margin_subject', row.orgId)
      }
    },
    {
      prop: 'accountName',
      label: '科目名称'
    },
    {
      prop: 'withinOneYearDeposit',
      label: '应付一年内到期保证金',
      width: 200,
      render: (row) => {
        return toThousands(row.withinOneYearDeposit) // h(ElTag, () => dictMappingLabel(dictData, 'sys_margin_subject_reclassified', row.withinOneYearDeposit))
      }
    },
    {
      prop: 'depositInterestExpense',
      label: '保证金利息支出',
      render: (row) => {
        return toThousands(row.depositInterestExpense)
      }
    },
    {
      prop: 'depositInterestIncome',
      label: '保证金利息收入',
      render: (row) => {
        return toThousands(row.depositInterestIncome)
      }
    },
    {
      prop: 'marginStatus',
      label: '状态',
      width: 100,
      render: (row) => {
        return (
          (row.marginStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'margin_status', row.marginStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      display: false
    }
  ]
})
