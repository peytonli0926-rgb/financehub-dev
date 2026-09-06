export const optionsFileConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8, // 查询区域每一个表单要展示的宽度
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/batch-query-upload-record/fileList',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'fileName',
      label: '文件名称',
      minWidth: 180
    },
    {
      prop: 'fileUploadBy',
      label: '下载人',
      minWidth: 130
    },
    {
      prop: 'fileUploadTime',
      label: '下载时间',
      width: 180
    },
    {
      prop: 'operation',
      label: '操作',
      display: false,
      width: 100
    }
  ]
})
