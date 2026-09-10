export default {
  hidden: true,
  title: '租金剩余本金、保证金明细表',
  icon: '',
  name: 'remainRentalDetails'
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
      url: '/engine/finance/report/remain-rent-principal-deposit-detail/page',
      method: 'post'
    }
  },
  columns: [
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
      prop: 'includeTax',
      label: '是否含税',
      hide: true,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'tax_yes_no')
    },
    {
      prop: 'amountType',
      label: '金额类型',
      hide: true,
      search: true,
      type: 'select',
      isSearchRequired: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'tjbb_cash_type'),
      rules: [
        {
          required: true,
          message: '请选择合同编号',
          trigger: 'change'
        }
      ]
    },
    {
      prop: 'currency',
      label: '币种',
      hide: true,
      search: true,
      type: 'select',
      attrs: {
        filterable: true
        // multiple: true,
        // 'collapse-tags': true,
        // 'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_currency_type')
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
    }
  ]
})
