
export default {
  hidden: true,
  title: '参数类型',
  icon: '',
  name: 'dictDetail'
}

export const optionsConfig = () => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  leftCardName: '',
  rowKey: 'dictCode', // 表格唯一id
  request: { // 请求参数
    list: {
      url: '/admin/dict/data/list',
      method: 'get'
    },
    add: {
      url: '/admin/dict/data',
      method: 'post'
    },
    edit: {
      url: '/admin/dict/data/$[dictCode]',
      method: 'put'
    },
    del: {
      url: '/admin/dict/data/$[dictCode]',
      method: 'delete'
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
    prop: 'dictType',
    label: '参数类型',
    attrs: {
      disabled: true
    },
    hide: true

  }, {
    prop: 'dictLabel',
    label: '参数标签',
    search: true,
    option: [],
    rules: [{
      required: true,
      message: '请输入字典标签',
      trigger: 'change'
    }]
  }, { // 展示数据
    prop: 'dictValue',
    label: '参数键值',
    search: true,
    rules: [{
      required: true,
      message: '请输入参数键值',
      trigger: 'change'
    }],
    attrs: {
      disabled: false,
      EDIT: true,
      ADD: false
    }

  }, { // 展示数据
    prop: 'dictSort',
    label: '参数排序',
    type: 'inputNumber',
    value: 1,
    rules: [{
      required: true,
      message: '请输入参数排序',
      trigger: 'change'
    }],
    attrs: {
      'controls-position': 'right',
      min: 0
    }

  }, {
    prop: 'status',
    label: '状态',
    type: 'select',
    dictType: 'sys_normal_disable',
    keyValue: {
      label: 'dictLabel',
      value: 'dictValue'
    },
    value: '0',
    slot: true
  }, {
    prop: 'remark',
    label: '备注',
    attrs: {
      type: 'textarea'
    }

  }, {
    prop: 'operation',
    width: 120,
    label: '操作',
    display: false

  }]
})
