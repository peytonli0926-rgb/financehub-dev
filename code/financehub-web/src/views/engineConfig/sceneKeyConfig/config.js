import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '场景字段',
  icon: '',
  name: 'sceneKeyConfig'
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
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
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
    width: 120,
    label: '操作',
    display: false

  }]
})
