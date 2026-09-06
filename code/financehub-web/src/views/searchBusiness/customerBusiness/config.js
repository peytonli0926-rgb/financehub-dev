// export default {
//   hidden: false,
//   title: '客户查询',
//   icon: '',
//   name: 'customerBusiness'
// }
export const optionsConfig = () => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  labelWidth: '150px',
  request: { // 请求参数
    list: {
      url: '/engine/finance/client/page',
      method: 'post'
    },
    add: {
      url: '/engine/scene/account/save',
      method: 'post'
    },
    edit: {
      url: '/engine/scene/account/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/engine/scene/account/delete/$[id]',
      method: 'post'
    }
  },
  columns: [{ // 展示数据
    prop: 'accountName',
    label: '承租人名称',
    search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'accountCode',
    label: '评估主体所在省',
    search: true,
    width: 200,
    align: 'center'

  }, { // 展示数据
    prop: 'accountCategory',
    label: '评估主体所在市',
    search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'industry',
    label: '客户行业',
    search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'fundType',
    label: '客户行业国际大类',
    search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'fundType',
    label: '一大一小标签',
    // search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'fundType',
    label: '客户性质标签',
    // search: true,
    width: 200,
    align: 'center'

  }, {
    prop: 'fundType',
    label: '应收余额',
    align: 'center',
    children: [{
      prop: 'fundType',
      label: '应收租金余额',
      width: 200,
      align: 'center'

    }, {
      prop: 'fundType',
      label: '应收首付余额',
      search: true,
      width: 200,
      align: 'center'

    }, {
      prop: 'fundType',
      label: '应收手续费余额',
      search: true,
      width: 200,
      align: 'center'

    }, {
      prop: 'fundType',
      label: '应收保险费余额',
      search: true,
      width: 200,
      align: 'center'

    }, {
      prop: 'fundType',
      label: '应收保险费余额',
      width: 200,
      align: 'center'

    }]
  }
    //  {
    //   prop: 'operation',
    //   width: 120,
    //   label: '操作',
    //   fixed: 'right',
    //   display: false

  // }
  ]
})
