import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '接口配置',
  icon: '',
  name: 'interfaceConfigLegacy'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  leftCardName: '',
  span: 8,
  rowKey: 'id', // 表格唯一id
  request: { // 请求参数
    list: {
      url: '/engine/scene/fields/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/fields/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/fields/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/fields/delete/$[id]',
      method: 'post'
    }
  },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  columns: [{ // 展示数据
    prop: 'sceneName',
    label: '场景名称',
    rules: [{
      required: true,
      message: '请输入场景名称',
      trigger: 'change'
    }],
    attrs: {
      disabled: true
    }

  }, { // 展示数据
    prop: 'fieldName',
    label: '字段名称',
    search: true,
    attrs: {
    },
    rules: [{
      required: true,
      message: '请输入字段名称',
      trigger: 'change'
    }]
  }, {
    prop: 'fieldCode',
    label: '字段编码',
    search: true,
    rules: [{
      required: true,
      message: '请输入字段编码',
      trigger: 'change'
    }],
    attrs: {
      ADD: false,
      EDIT: true

    }
  }, { // 展示数据
    prop: 'dataType',
    label: '数据类型',
    type: 'select',
    option: dictMappingToArray(dictData, 'sys_database_column_type') || [],
    rules: [{
      required: true,
      message: '请输入数据类型',
      trigger: 'change'
    }],
    render (row) {
      return (row.dataType && h(ElTag, () => dictMappingLabel(dictData, 'sys_database_column_type', row.dataType))) || ''
    }

  }, {
    prop: 'operation',
    width: 200,
    label: '操作',
    display: false

  }]
})

export const detailOptionsConfig = (dictData = {}) => ({
  isIndex: true,
  isSelection: false,
  isPagination: true,
  isBorder: true,
  isOperateHeader: true,
  rowKey: 'id',
  span: 8,
  request: {
    list: {
      url: '/engine/scene/fields/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/fields/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/fields/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/fields/delete/$[id]',
      method: 'post'
    }
  },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增明细字段'
    }
  },
  columns: [{
    prop: 'fieldName',
    label: '字段名称',
    search: true,
    rules: [{
      required: true,
      message: '请输入字段名称',
      trigger: 'change'
    }]
  }, {
    prop: 'fieldCode',
    label: '字段编码',
    search: true,
    rules: [{
      required: true,
      message: '请输入字段编码',
      trigger: 'change'
    }],
    attrs: {
      ADD: false,
      EDIT: true
    }
  }, {
    prop: 'dataType',
    label: '数据类型',
    type: 'select',
    option: (dictMappingToArray(dictData, 'sys_database_column_type') || [])
      .filter(item => item.value !== 'List'),
    rules: [{
      required: true,
      message: '请选择数据类型',
      trigger: 'change'
    }],
    render (row) {
      return (row.dataType && h(ElTag, () => dictMappingLabel(dictData, 'sys_database_column_type', row.dataType))) || ''
    }
  }, {
    prop: 'requiredFlag',
    label: '是否必填',
    type: 'select',
    value: '1',
    option: [{ label: '是', value: '1' }, { label: '否', value: '0' }],
    render (row) {
      return h(ElTag, { type: row.requiredFlag === '1' ? 'danger' : 'info' }, () => row.requiredFlag === '1' ? '是' : '否')
    }
  }, {
    prop: 'sortNo',
    label: '排序',
    type: 'inputNumber',
    value: 1,
    attrs: {
      min: 0,
      'controls-position': 'right'
    }
  }, {
    prop: 'operation',
    width: 120,
    label: '操作',
    display: false
  }]
})
