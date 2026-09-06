import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, dictMappingToArray } from '@/utils'

export default {
  hidden: false,
  title: '凭证模板配置',
  icon: '',
  name: 'sceneConfig'
}
export const optionsConfig = (router, dictData) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  editBtn: {
    label: '编辑场景',
    isShow: true
  },
  delBtn: {
    isShow: true
  },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  leftCardName: '',
  request: { // 请求参数
    list: {
      url: '/engine/scene/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/delete/$[id]',
      method: 'post'
    },
    detail: {
      url: '/engine/scene/get/$[id]',
      method: 'get'
    }
  },
  columns: [{ // 展示数据
    prop: 'sceneName',
    label: '场景名称',
    search: true,
    rules: [{
      required: true,
      message: '请输入场景名称',
      trigger: 'change'
    }]
  }, {
    prop: 'sceneCode',
    label: '场景编码',
    search: true,
    option: [],
    rules: [{
      required: true,
      message: '请输入场景编码',
      trigger: 'change'
    }],
    attrs: {
      ADD: false,
      disabled: false,
      EDIT: true

    }
  }, { // 展示数据
    prop: 'scenePeriod',
    type: 'select',
    label: '场景周期',
    search: true,
    option: dictMappingToArray(dictData, 'sys_scene_period'),
    rules: [{
      required: true,
      message: '请输入字典键值',
      trigger: 'change'
    }],
    render (row) {
      return (row.scenePeriod && h(ElTag, () => dictMappingLabel(dictData, 'sys_scene_period', row.scenePeriod))) || ''
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
    prop: 'operation',
    width: 250,
    label: '操作',
    display: false

  }]
})
