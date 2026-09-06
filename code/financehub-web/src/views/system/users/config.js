export default {
  hidden: false,
  title: '用户管理',
  icon: '',
  name: 'users'
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
  delBtn: {
    show: false
  },
  editBtn: {
    show: false
  },
  request: { // 请求参数
    list: {
      url: '/admin/sys-internal-user/page',
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
      prop: 'userName',
      label: '用户',
      search: true,
      rules: [{
        required: true,
        message: '请输入用户',
        trigger: 'change'
      }]
    },
    { // 展示数据
      prop: 'roleList',
      label: '角色名称',
      search: true,
      rules: [{
        required: true,
        message: '请输入角色名称',
        trigger: 'change'
      }],
      render ({ roleList }) {
        const _list = (roleList || []).map(item => item.roleName)
        return _list.toString()
      }
    }, { // 展示数据
      prop: 'subUserList',
      label: '管理用户',
      search: true,
      rules: [{
        required: true,
        message: '请输入角色名称',
        trigger: 'change'
      }],
      render ({ subUserList = [] }) {
        const _list = (subUserList || []).map(item => item.userName)
        return _list.toString()
      }
    }, {
      prop: 'operation',
      width: 220,
      label: '操作',
      display: false
    }]
})
