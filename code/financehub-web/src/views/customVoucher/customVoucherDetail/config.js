import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '凭证录入详情',
  icon: '',
  name: 'customVoucherDetail',
  sort: 4
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
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
      url: '/engine/finance/manual-voucher/page',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      display: false,
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
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 250,
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
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
      request: { // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      display: false,
      type: 'date',
      cover: ['businessDateStart', 'businessDateEnd'],
      width: 110,
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
      prop: 'voucherDate',
      label: '记账日期',
      search: true,
      display: false,
      type: 'date',
      cover: ['voucherDateStart', 'voucherDateEnd'],
      width: 110,
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
      prop: 'voucherTypeList',
      label: '凭证类型',
      search: true,
      type: 'select',
      hide: true,
      display: false,
      width: 120,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_voucher_type')

    },
    {
      width: 200,
      prop: 'voucherType',
      label: '凭证类型',
      render(row) {
        return (row.voucherType && h(ElTag, () => dictMappingLabel(dictData, 'sys_voucher_type', row.voucherType))) || ''
      }
    },
    {
      prop: 'voucherNum',
      label: '凭证号',
      display: false,
      width: 100
    },

    {
      prop: 'sceneCodeList',
      label: '业务场景',
      search: true,
      type: 'select',
      display: false,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/scene/list',
        method: 'post'
      },
      keyValue: {
        label: 'sceneName',
        value: 'sceneCode'
      }
    },
    {
      prop: 'sceneName',
      label: '业务场景',
      display: false,
      width: 200
    },

    {
      prop: 'subSceneTypeList',
      label: '细分场景',
      width: 200,
      display: false,
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_sub_scene_type')

    },
    {
      prop: 'subSceneType',
      label: '细分场景',
      width: 200,
      display: false,
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_sub_scene_type'),
      render(row) {
        return (row.subSceneType && h(ElTag, () => dictMappingLabel(dictData, 'sys_sub_scene_type', row.subSceneType))) || ''
      }
    },
    { width: 200, prop: 'voucherSummary', label: '摘要内容' },

    {
      prop: 'accountCodeList',
      label: '科目代码',
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: { url: '/engine/scene/account/listAll', method: 'post' },
      keyValue: { label: 'accountCode', value: 'accountCode', formatLabel: ['accountCode', 'accountName'] },
      width: 200
    },
    {
      prop: 'accountCode',
      label: '科目代码',
      width: 200
    },

    // {
    //   prop: 'accountNameList',
    //   label: '科目名称',
    //   search: true,
    //   type: 'select',
    //   hide: true,
    //   display: false,
    //   attrs: {
    //     filterable: true,
    //     multiple: true,
    //     'collapse-tags': true,
    //     'collapse-tags-tooltip': true
    //   },
    //   width: 200,
    //   request: { url: '/engine/scene/account/listAll', method: 'post' },
    //   keyValue: { label: 'accountName', value: 'accountName' }
    // },
    {
      prop: 'accountName',
      label: '科目名称',
      tooltip: true,
      width: 220
    },
    {
      prop: 'currency',
      label: '币种',
      search: true,
      display: false,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render(row) {
        return (row.currency && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currency))) || ''
      }
    },
    { width: 200, prop: 'rate', label: '汇率' },
    { label: '借方金额', prop: 'debitAmount', width: 200, render: ({ debitAmount }) => toThousands(debitAmount) },
    { label: '贷方金额', prop: 'creditAmount', width: 200, render: ({ creditAmount }) => toThousands(creditAmount) },
    { label: '辅助账摘要', prop: 'subsidiaryAccount', width: 200 },

    { // 展示数据
      prop: 'contractCodeList',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      width: 200,
      hide: true,
      attrs: {
        multiple: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      display: false,
      hide: false,
      width: 200
    },
    {
      prop: 'clientName',
      label: '客户名称',
      display: false,
      width: 300
    },
    {
      prop: 'loansContractCodeList',
      label: '借款合同编号',
      display: false,
      search: true,
      hide: true,
      type: 'select-pagination',
      width: 200,
      attrs: {
        multiple: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: { label: 'name', value: 'code', formatLabel: ['code', 'name'] }
    },

    {
      prop: 'loansContractCode',
      label: '借款合同编号',
      display: false,
      hide: false,
      width: 200
    },
    { label: '银行账号', prop: 'bankNo', width: 200 },
    { // 展示数据
      prop: 'preparerNameList',
      label: '制单人',
      search: true,
      type: 'select-pagination',
      hide: true,
      width: 200,
      request: {
        url: '/admin/sys-internal-user/page', method: 'post'
      },
      attrs: {
        multiple: true
      },
      searchName: 'userName',
      keyValue: {
        label: 'userName',
        value: 'userCode',
        formatLabel: ['userName', 'userCode']
      }
    },
    {
      prop: 'createUserName',
      label: '制单人',
      display: false,
      width: 200
    },
    {
      prop: 'recheckUserName',
      label: '复核人',
      display: false,
      search: true,
      width: 200
    },
    {
      prop: 'processStatusList',
      label: '处理状态',
      display: false,
      width: 100,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    }, {
      prop: 'processStatus',
      label: '处理状态',
      display: false,
      width: 100,
      render(row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    },
    {
      label: '是否涉及其他客户及辅助账',
      prop: ' isRelatedOtherCustomer',
      width: 200,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      render(row) {
        return (row.isRelatedOtherCustomer && h(ElTag, () => dictMappingLabel(dictData, 'is_sys_bool', row.isRelatedOtherCustomer))) || ''
      }
    }

  ]

})
