import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, dictMappingToArray } from '@/utils'

export default {
  hidden: true,
  title: '主数据管理',
  icon: '',
  name: 'indicator',
  sort: 2
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
  request: { // 请求参数
    list: {
      url: '/moji/demo/indicator/page',
      method: 'post'
    }
    /*add: {
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
    }*/
  },
  /*btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },*/
  columns: [
    {
      prop: 'indicatorName',
      label: '指标名称',
      search: true
    },
    {
      prop: 'indicatorCode',
      label: '指标编码',
      search: true
    },
    {
      prop: 'status',
      label: '状态'
    },
    {
      prop: 'masterData',
      label: '主数据',
      search: true,
      type: 'select',
      value: '1',
      option: [{
        label: '客户',
        value: '1'
      }, {
        label: '战略',
        value: '2'
      }]
    }
  ]
})
