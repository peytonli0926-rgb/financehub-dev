import { h } from 'vue'
import { ElButton } from 'element-plus'

export default {
  hidden: false,
  title: '参数配置',
  icon: '',
  name: 'dictConfig'
}

export const optionsConfig = (router) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'dictId', // 表格唯一id
  leftCardName: '',
  // delBtn:{
  //   label:'删除',
  //   isShow:false
  // },
  request: { // 请求参数
    list: {
      url: '/admin/dict/type/list',
      method: 'get'
    },
    add: {
      url: '/admin/dict/type',
      method: 'post'
    },
    edit: {
      url: '/admin/dict/type/$[dictId]',
      method: 'put'
    },
    del: {
      url: '/admin/dict/type/$[dictId]',
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
    prop: 'dictName',
    label: '参数名称',
    rules: [{
      required: true,
      message: '请输入参数名称',
      trigger: 'change'
    }],
    search: true,
    render (row) {
      return h(ElButton, {
        'v-permission': 'dict_btn_redirect',
        type: 'primary',
        link: true,
        onClick: () => {
          router.push(`/engineConfig/dictDetail?dictType=${row.dictType}&title=${encodeURIComponent(row.dictName)}`)
        }
      }, () => row.dictName)
    }
  }, {
    prop: 'dictType',
    label: '参数类型',
    search: true,
    option: [],
    attrs: {
      disabled: false,
      EDIT: true,
      ADD: false
    },
    rules: [{
      required: true,
      message: '请输入参数类型',
      trigger: 'change'
    }]
  },
  {
    prop: 'status',
    label: '状态',
    type: 'select',
    dictType: 'sys_normal_disable',
    value: '0',
    keyValue: {
      label: 'dictLabel',
      value: 'dictValue'
    },
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
