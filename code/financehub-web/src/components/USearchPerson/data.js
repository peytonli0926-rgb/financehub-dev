
export const optionsConfig = (router) => ({
  isIndex: true, // 是否需要序号
  isSelection: true, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'staffCode', // 表格唯一id
  leftCardName: '',
  isResetBtn: false,
  tableHeight: 300,
  labelWidth: '70px',
  span: 8,
  request: { // 请求参数
    list: {
      url: '/engine/integration/org/queryStaffList',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'staffCode',
    label: '员工号',
    search: true,
    // isSearchRequired:true,
    rules: [{
      required: true,
      message: '请输入员工号',
      trigger: 'change'
    }]
  }, {
    prop: 'staffName',
    label: '姓名',
    isSearchRequired: true,
    search: true,
    rules: [{
      required: true,
      message: '请输入姓名',
      trigger: 'change'
    }]
  },
  {
    prop: 'positionName',
    label: '岗位名称'
  },
  {
    prop: 'gender',
    label: '性别'
  }
  ]
})
