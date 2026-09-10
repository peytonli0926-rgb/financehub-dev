export default {
  hidden: true,
  title: '收益全生命周期明细表',
  icon: '',
  name: 'fullLifeCycleDetail'
}

import { dictMappingToArray } from '@/utils'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
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

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/report/full-life-cycle-income-detail/page',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/report/full-life-cycle-income-detail/export',
      filename: '收益全生命周期明细表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      label: '签约主体',
      prop: 'orgIdList',
      search: true,
      type: 'select',
      hide: true,
      isSearchRequired: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      rules: [
        {
          required: true,
          message: '请选择签约主体',
          trigger: 'blur'
        }
      ]
    },
    {
      prop: 'contractCodeList',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      isSearchRequired: true,
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
      },
      rules: [
        {
          required: true,
          message: '请选择合同编号',
          trigger: 'change'
        }
      ]
    },
    {
      prop: 'periodCode',
      label: '期间',
      hide: true,
      search: true,
      type: 'select',
      attrs: {
        filterable: true,
        'collapse-tags': true
      },
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
    {
      prop: 'clientName',
      label: '客户名称',
      hide: true,
      search: true
    },
    {
      prop: 'incomeType',
      label: '收入类型',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'allocation_method')
    },
    {
      prop: 'includeTax',
      label: '是否含税',
      hide: true,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'tax_yes_no')
    },
    {
      prop: 'overdueDays',
      label: '逾期天数',
      hide: true,
      search: true,
      type: 'inputrange',
      cover: ['overdueDaysStart', 'overdueDaysEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    },
    {
      prop: 'plannedTotalAmount',
      label: '计划总额',
      hide: true,
      search: true,
      type: 'inputrange',
      cover: ['plannedTotalAmountStart', 'plannedTotalAmountEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    },
    {
      prop: 'apportionTotalAmount',
      label: '分摊总额',
      hide: true,
      search: true,
      type: 'inputrange',
      cover: ['apportionTotalAmountStart', 'apportionTotalAmountEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    },
    {
      prop: 'diffAmount',
      label: '差额',
      hide: true,
      search: true,
      type: 'inputrange',
      cover: ['diffAmountStart', 'diffAmountEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    },
    {
      prop: 'taxRate',
      label: '税率',
      hide: true,
      search: true,
      type: 'inputNumber',
      value: 1,
      attrs: {
        'controls-position': 'right'
      }
    },
    {
      prop: 'xirr',
      label: '财务XIRR',
      search: true,
      hide: true,
      minWidth: 150,
      type: 'inputrange',
      cover: ['xirrStart', 'xirrEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    }
  ]
})
