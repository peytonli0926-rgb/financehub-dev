import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '值映射配置',
  icon: '',
  name: 'mappingConfig'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  labelWidth: '130px',
  // dialogWidth:'40%',
  // delBtn:{
  //   label:'删除',
  //   isShow:false
  // },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  request: { // 请求参数
    list: {
      url: '/engine/scene/field-mapping/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/field-mapping/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/field-mapping/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/field-mapping/delete/$[id]',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'systemCode',
    label: '系统来源',
    type: 'select',

    option: dictMappingToArray(dictData, 'sys_form_source'),
    search: true,
    // attrs: {
    //   disabled: false,
    //   ADD: false,
    //   EDIT: true
    // },
    rules: [{
      required: true,
      message: '请输系统来源',
      trigger: 'change'
    }],
    render (row) {
      return (row.systemCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) || ''
    }
  }, {
    prop: 'fieldName',
    label: '字段名称',
    type: 'select',
    request: {
      url: '/engine/scene/fields/getAllFields',
      method: 'get'
    },
    keyValue: {
      label: 'fieldName',
      value: 'fieldName'
    },
    relation: ['fieldCode'],
    search: true,
    attrs: {
      filterable: true
    },
    rules: [{
      required: true,
      message: '请输入字段名称',
      trigger: 'change'
    }]
  }, {
    prop: 'fieldCode',
    label: '字段编码',
    hide: true,
    rules: [{
      required: true,
      message: '请输入字段编码',
      trigger: 'change'
    }],
    attrs: {
      disabled: true
    }
  }, {
    prop: 'sourceValue',
    label: '源值',
    search: true,
    rules: [{
      required: true,
      message: '请输入源值',
      trigger: 'change'
    }]
  }, {
    prop: 'targetValue',
    label: '目标值',
    rules: [{
      required: true,
      message: '请输入目标值',
      trigger: ['change']
    }],
    attrs: {

    }
  },
  {
    prop: 'targetFieldCode',
    label: '目标字段编码'
  },

  {
    prop: 'operation',
    width: 120,
    label: '操作',
    display: false

  }]
})
