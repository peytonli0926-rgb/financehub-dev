export default {
  hidden: false,
  title: 'Value-Map',
  icon: '',
  name: 'valueMap'
}
export const optionsConfig = (router) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  // delBtn:{
  //   label:'删除',
  //   isShow:false
  // },
  
  /*btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },*/
  
})