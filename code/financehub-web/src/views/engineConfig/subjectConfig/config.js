import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'
export default {
  hidden: false,
  title: '科目配置',
  icon: '',
  name: 'subjectConfig'
}
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  dialogSpan: 12,
  span: 8,
  dialogWidth: '40%',
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  request: { // 请求参数
    list: {
      url: '/engine/scene/account/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/account/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/account/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/account/delete/$[id]',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'businessName',
    label: '业务类型名称',
    type: 'select',
    request: {
      url: '/engine/scene/business/list',
      method: 'post'
    },
    keyValue: {
      label: 'businessName',
      value: 'businessName'
    },
    relation: ['businessCode'],
    search: true,
    attrs: {
      ADD: false,
      EDIT: true
    },
    rules: [{
      required: true,
      message: '请输入业务类型名称',
      trigger: 'change'
    }]
  }, {
    prop: 'businessCode',
    label: '业务编码',
    rules: [{
      required: true,
      message: '请输入业务编码',
      trigger: 'change'
    }],
    attrs: {
      disabled: true
    }
  }, { // 展示数据
    prop: 'accountName',
    label: '科目名称',
    search: true,
    attrs: {
    },
    rules: [{
      required: true,
      message: '请输入科目名称',
      trigger: 'change'
    }]
  }, {
    prop: 'accountCode',
    label: '科目编码',
    search: true,
    rules: [{
      required: true,
      message: '请输入科目编码',
      trigger: 'change'
    }],
    attrs: {
      ADD: false,
      EDIT: true
      // SEARCH: true
    }
  },

  { // 展示数据
    prop: 'settlementType',
    label: '核算范围',
    search: true,
    type: 'select',
    option: dictMappingToArray(dictData, 'account_settlement_type'),
    rules: [{
      required: true,
      message: '请选择核算范围',
      trigger: 'change'
    }],
    render (row) {
      return (row.settlementType && h(ElTag, () => dictMappingLabel(dictData, 'account_settlement_type', row.settlementType))) || ''
    }
  },
  {
    prop: 'fundType',
    label: '金额类型',
    search: true,
    type: 'select',
    // dictType: 'sys_cash_type',
    option: dictMappingToArray(dictData, 'sys_cash_type'),
    rules: [{
      required: true,
      message: '请选择金额类型',
      trigger: 'change'
    }],
    attrs: {
      filterable: true
    },
    render (row) {
      return (row.fundType && h(ElTag, () => dictMappingLabel(dictData, 'sys_cash_type', row.fundType))) || ''
    }
  }, { // 展示数据
    prop: 'accountCategory',
    label: '科目类别',
    search: true,
    type: 'select',
    option: dictMappingToArray(dictData, 'sys_subject_type'),
    rules: [{
      required: true,
      message: '请选择科目类别',
      trigger: 'change'
    }],
    render (row) {
      return (row.accountCategory && h(ElTag, () => dictMappingLabel(dictData, 'sys_subject_type', row.accountCategory))) || ''
    }
  }, { // 展示数据
    prop: 'debitCreditType',
    label: '余额方向',
    search: true,
    type: 'select',
    option: dictMappingToArray(dictData, 'sys_debit_credit'),
    rules: [{
      required: true,
      message: '请选择余额方向',
      trigger: 'change'
    }],
    render (row) {
      return (row.debitCreditType && h(ElTag, () => dictMappingLabel(dictData, 'sys_debit_credit', row.debitCreditType))) || ''
    }
  },
  {
    prop: 'assistFlags',
    label: '辅助核算维度',
    search: true,
    type: 'select',
    option: dictMappingToArray(dictData, 'sys_assist_client'),
    attrs: {
      multiple: true
    },
    render (row) {
      const value = dictMappingLabel(dictData, 'sys_assist_client', row.assistFlags)
      return (value && value.toString()) || ''
    }
  },
  {
    prop: 'operation',
    width: 120,
    label: '操作',
    display: false

  }]
})
