
export default {
  hidden: true,
  title: '分配角色用户',
  icon: '',
  name: 'rolesUserAuth'
}
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  editBtn: {
    show: false
  },
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  request: { // 请求参数
    list: {
      url: '/admin/sys-internal-role-user/page',
      method: 'post'
    },
    add: {
      url: '/admin/sys-internal-role-user/save',
      method: 'post'
    },
    edit: {
      url: '/admin/sys-internal-role-user/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/admin/sys-internal-role-user/delete/$[id]',
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
    { // 展示数据
      prop: 'roleName',
      label: '角色',
      display: false
    },
    {
      prop: 'userCode',
      label: '用户',
      type: 'person',
      hide: true,
      rules: [{
        required: true,
        message: '请选择用户',
        trigger: 'change'
      }],
      selectionType: 'single'
    },
    {
      prop: 'userName',
      label: '用户',
      display: false,
      search: true
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
      width: 120,
      label: '操作',
      display: false
    }]
})
