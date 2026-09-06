export default {
  hidden: false,
  title: '业业核对-应付保证金',
  icon: '',
  name: 'payableMargin'
}

import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'
import { h } from 'vue'
import { ElTag } from 'element-plus'
import moment from 'moment'

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

const columnsCommon = (dictData) => [
  {
    prop: 'orgId',
    label: '签约主体',
    type: 'select',
    tooltip: true,
    display: false,
    search: true,
    minWidth: 150,
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'company'),
    render(row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },
  {
    prop: 'exceptionFlag',
    label: '异常情况',
    search: true,
    minWidth: 130,
    hide: true,
    type: 'select',
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'check_account_exception_flag')
  },
  {
    prop: 'periodCode',
    label: '会计期间',
    search: true,
    hide: true,
    type: 'select',
    attrs: {
      filterable: true
    },
    minWidth: 200,
    request: {
      url: '/engine/scene/account-period/queryAll',
      method: 'post'
    },
    keyValue: {
      label: 'periodName',
      value: 'periodCode'
    }
  },
  {
    prop: 'contractStatus',
    label: '业务合同状态',
    type: 'select',
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
    hide: true,
    search: true,
    type: 'select',
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    option: dictMappingToArray(dictData, 'financial_contract_status')
  },
  {
    prop: 'accountCode',
    label: '科目编码',
    minWidth: 200,
    hide: true,
    search: true,
    type: 'select',
    attrs: {
      filterable: true,
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true
    },
    request: { url: '/engine/scene/account/listAll', method: 'post' },
    keyValue: {
      label: 'accountCode',
      value: 'accountCode',
      formatLabel: ['accountCode', 'accountName']
    }
  },
  {
    prop: 'guiAnFlag',
    label: '是否是贵安现代物流合同',
    search: true,
    hide: true,
    type: 'select',
    minWidth: 200,
    option: dictMappingToArray(dictData, 'sys_yes_no')
    // render: ({ guiAnFlag }) => {
    //   return (
    //     (guiAnFlag && h(ElTag, () => dictMappingLabel(dictData, 'sys_yes_no', guiAnFlag))) || ''
    //   )
    // }
  }
]

const tableCommon = (dictData) => [
  {
    prop: 'accountCode',
    label: '科目编码',
    minWidth: 130
  },
  {
    prop: 'accountName',
    label: '科目名称',
    minWidth: 140,
    render: ({ accountName }) => {
      return h(ElTag, () => accountName)
    }
  },
  {
    prop: 'contractStatus',
    label: '业务合同状态',
    minWidth: 130,
    render({ contractStatus }) {
      return (contractStatus && h(ElTag, () => contractStatus)) || ''
    }
  },
  {
    prop: 'financialContractStatus',
    label: '财务合同状态',
    minWidth: 130,
    render({ financialContractStatus }) {
      return (financialContractStatus && h(ElTag, () => financialContractStatus)) || ''
    }
  },
  {
    label: '合同起租日',
    prop: 'leaseDateStart',
    minWidth: 120,
    render: ({ leaseDateStart }) => {
      return (leaseDateStart && moment(leaseDateStart).format('YYYY-MM-DD')) || ''
    }
  },
  {
    prop: 'leaseDateEnd',
    label: '合同到期日',
    minWidth: 120,
    render: ({ leaseDateEnd }) => {
      return (leaseDateEnd && moment(leaseDateEnd).format('YYYY-MM-DD')) || ''
    }
  },
  {
    prop: 'guiAnFlag',
    label: '是否是贵安现代物流合同',
    minWidth: 200,
    render: ({ guiAnFlag }) => {
      return (
        (guiAnFlag && h(ElTag, () => dictMappingLabel(dictData, 'sys_yes_no', guiAnFlag))) || ''
      )
    }
  },
  {
    prop: 'balance',
    label: '业务系统余额',
    minWidth: 130,
    render: ({ balance }) => toThousands(balance)
  },
  {
    prop: 'balanceFinance',
    label: '中台余额',
    minWidth: 150,
    render: ({ balanceFinance }) => toThousands(balanceFinance)
  },
  {
    prop: 'balanceDiff',
    label: '差额',
    minWidth: 150,
    render: ({ balanceDiff }) => toThousands(balanceDiff)
  },
  {
    prop: 'exceptionFlagStr',
    label: '异常情况',
    minWidth: 150,
    render({ exceptionFlagStr }) {
      return (exceptionFlagStr && h(ElTag, () => exceptionFlagStr)) || ''
    }
  }
]

// 合同维度
export const contractOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/check-account-detail-record/yfbzj/resultPage',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/check-account-detail-record/yfbzj/export',
      filename: '应付保证金合同维度报表.xlsx',
      isDisabled: false,
      isParams: true,
      level: 'h'
    }
  },
  columns: [
    {
      label: '来源系统',
      prop: 'dbCode',
      minWidth: 140,
      type: 'select',
      search: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render({ dbCode }) {
        return (
          (dbCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', dbCode))) || ''
        )
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      minWidth: 200,
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
    ...columnsCommon(dictData),
    {
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 150
    },
    ...tableCommon(dictData)
  ]
})

// 客户维度
export const customerOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/check-account-detail-record/yfbzjkh/resultPage',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/check-account-detail-record/yfbzjkh/export',
      filename: '应付保证金客户维度报表.xlsx',
      isDisabled: false,
      isParams: true,
      level: 'c'
    }
  },
  columns: [
    {
      label: '来源系统',
      prop: 'dbCode',
      minWidth: 140,
      type: 'select',
      search: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render({ dbCode }) {
        return (
          (dbCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', dbCode))) || ''
        )
      }
    },
    {
      prop: 'clientCode',
      label: '客户编号',
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
        url: '/engine/finance/client/page',
        method: 'post'
      },
      keyValue: {
        label: 'clientCode',
        value: 'clientCode',
        formatLabel: ['clientCode', 'clientName']
      }
    },
    // {
    //   prop: 'clientName',
    //   label: '客户名称',
    //   type: 'select-pagination',
    //   hide: true,
    //   search: true,
    //   display: false,
    //   attrs: {
    //     multiple: true
    //   },
    //   searchName: 'clientCodeOrName',
    //   request: { url: '/engine/finance/client/page', method: 'post' },
    //   keyValue: {
    //     label: 'clientName',
    //     value: 'clientName',
    //     formatLabel: ['clientName', 'clientCode']
    //   }
    // },
    ...columnsCommon(dictData),
    {
      prop: 'clientCode',
      label: '客户编码',
      minWidth: 150,
      tooltip: true
    },
    {
      prop: 'clientName',
      label: '客户名称',
      minWidth: 130,
      tooltip: true
    },
    ...tableCommon(dictData)
  ]
})
