
export default {
  hidden: true,
  title: '分配管理用户',
  icon: '',
  name: 'usersManage'
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
      url: '/admin/sys-internal-sub-user/page',
      method: 'post'
    },
    add: {
      url: '/admin/sys-internal-sub-user/save',
      method: 'post'
    },
    edit: {
      url: '/admin/sys-internal-sub-user/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/admin/sys-internal-sub-user/delete/$[id]',
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
      prop: 'parentUserName',
      label: '上级用户',
      display: false
    },
    {
      prop: 'subUserId',
      label: '管理用户',
      type: 'select',
      request: {
        url: '/admin/sys-internal-user/listByCondition',
        method: 'post'
      },
      keyValue: {
        label: 'userName',
        value: 'id'
      },
      hide: true,
      rules: [{
        required: true,
        message: '请选择用户',
        trigger: 'change'
      }],
      selectionType: 'single'
    },
    {
      prop: 'subUserName',
      label: '管理用户',
      display: false,
      search: true
    }, {
      prop: 'operation',
      width: 80,
      label: '操作',
      display: false
    }]
})
