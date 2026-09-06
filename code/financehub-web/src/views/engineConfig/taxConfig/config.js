import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '税号配置',
  icon: '',
  name: 'taxConfig'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'dictId', // 表格唯一id
  leftCardName: '',
  dialogSpan: 12,
  dialogWidth: '40%',
  // dialogWidth:'40%',
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  request: { // 请求参数
    list: {
      url: '/engine/scene/tax-rate/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/tax-rate/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/tax-rate/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/tax-rate/delete/$[id]',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'businessName',
    label: '业务名称',
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
      disabled: false,
      ADD: false,
      EDIT: true
    },
    rules: [{
      required: true,
      message: '请输入业务名称',
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
  }, {
    prop: 'fundType',
    label: '金额类型',
    search: true,
    type: 'select',
    option: dictMappingToArray(dictData, 'sys_cash_type'),
    rules: [{
      required: true,
      message: '请输入金额类型',
      trigger: 'change'
    }],
    attrs: {
      filterable: true,
      disabled: false,
      ADD: false,
      EDIT: true
    },
    render (row) {
      return (row.fundType && h(ElTag, () => dictMappingLabel(dictData, 'sys_cash_type', row.fundType))) || ''
    }
  },
  {
    prop: 'leaseType',
    label: '租赁类型',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'LEASE_TYPE'),
    rules: [{
      required: true,
      message: '请选择租赁类型',
      trigger: ['change']
    }],
    render (row) {
      return (row.leaseType && h(ElTag, () => dictMappingLabel(dictData, 'LEASE_TYPE', row.leaseType))) || ''
    }
  },
  {
    prop: 'leaseSubType',
    label: '租赁方式',
    type: 'select',
    search: true,
    option: dictMappingToArray(dictData, 'LEASE_METHOD'),
    render (row) {
      return (row.leaseSubType && h(ElTag, () => dictMappingLabel(dictData, 'LEASE_METHOD', row.leaseSubType))) || ''
    }
  },
  {
    prop: 'taxRate',
    label: '税率(%)',
    type: 'inputNumber',
    rules: [{
      required: true,
      message: '请输入税率',
      trigger: ['change']
    }],
    attrs: {
      min: 0,
      max: 100

    }
  }, {
    prop: 'enableFlag',
    label: '状态',
    type: 'select',
    value: '1',
    option: [{
      label: '正常',
      value: '1'
    }, {
      label: '停用',
      value: '0'
    }],
    slot: true
  }, {
    prop: 'enableDate',
    label: '生效日期',
    // search: true,
    type: 'date',
    rules: [{
      message: '请选择生效日期',
      trigger: 'change'
    }],
    attrs: {
      disabled: false,
      placeholder: '请选择生效日期',
      format: 'YYYY-MM-DD',
      'value-format': 'YYYY-MM-DD'
    }

  }, {
    prop: 'operation',
    width: 120,
    label: '操作',
    display: false

  }]
})
