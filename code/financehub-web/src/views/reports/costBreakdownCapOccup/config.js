export default {
  hidden: false,
  title: '资金占用成本明细',
  icon: '',
  name: 'costBreakdownCapOccup'
}

import { dictMappingToArray } from '@/utils'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
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

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/report/fund-occupation-cost/page',
      method: 'post'
    }
  },
  columns: [
    {
      label: '签约主体',
      prop: 'orgIds',
      search: true,
      hide: true,
      isSearchRequired: true,
      type: 'select',
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
      label: '合同编号',
      prop: 'contractCodeList',
      search: true,
      hide: true,
      type: 'select-pagination',
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
      label: '承租人',
      prop: 'clientName',
      search: true,
      hide: true,
      type: 'select-pagination',
      display: false,
      attrs: {
        multiple: true
      },
      searchName: 'clientCodeOrName',
      request: { url: '/engine/finance/client/page', method: 'post' },
      keyValue: {
        label: 'clientName',
        value: 'clientName',
        formatLabel: ['clientName', 'clientCode']
      }
    },
    {
      label: '所属部门',
      prop: 'deptList',
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
        url: '/engine/finance/report/fund-occupation-cost/dept',
        method: 'get'
      }
    },
    {
      prop: 'bussinessDate',
      label: '会计期间',
      type: 'select',
      search: true,
      hide: true,
      minWidth: 120,
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
      },
      slot: true
    },
    {
      label: '资金成本',
      prop: 'decZijcb',
      search: true,
      hide: true,
      type: 'inputrange',
      cover: ['decZijcbStart', 'decZijcbEnd'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小值',
        'end-placeholder': '最大值'
      }
    },
    {
      label: '是否含税',
      prop: 'includeTax',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'tax_yes_no')
    },
    {
      label: '逾期天数',
      prop: 'overdueDays',
      search: true,
      hide: true,
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
      label: '业务类型',
      prop: 'leaseType',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'lease_type')
    },
    {
      label: '币种',
      prop: 'currency',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_currency_type')
    },
    {
      label: '税率',
      prop: 'taxRate',
      search: true,
      hide: true,
      type: 'inputNumber',
      value: 1,
      attrs: {
        'controls-position': 'right'
      }
    }
  ]
})
