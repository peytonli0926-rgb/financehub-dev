export default {
  hidden: false,
  title: '角色管理',
  icon: '',
  name: 'roles'
}
export const optionsConfig = (router) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  request: { // 请求参数
    list: {
      url: '/admin/sys-internal-role/page',
      method: 'post'
    },
    add: {
      url: '/admin/sys-internal-role/save',
      method: 'post'
    },
    edit: {
      url: '/admin/sys-internal-role/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/admin/sys-internal-role/delete/$[id]',
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
  columns: [
    {
      prop: 'roleCode',
      label: '角色编码',
      search: true,
      rules: [{
        required: true,
        message: '请输入角色编码',
        trigger: 'change'
      }]
    },
    { // 展示数据
      prop: 'roleName',
      label: '角色名称',
      search: true,
      rules: [{
        required: true,
        message: '请输入角色名称',
        trigger: 'change'
      }]
    }, {
      prop: 'status',
      label: '状态',
      type: 'select',
      search: true,
      rules: [{
        required: true,
        message: '请选择状态',
        trigger: 'change'
      }],
      option: [{
        label: '开启',
        value: '0'
      }, {
        label: '禁用',
        value: '1'
      }],
      slot: true,
      render ({ status }) {
        return status === '1' ? '禁用' : '启用'
      }
    }, {
      prop: 'operation',
      width: 320,
      label: '操作',
      display: false
    }]
})
