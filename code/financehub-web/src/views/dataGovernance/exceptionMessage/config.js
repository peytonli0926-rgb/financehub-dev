import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '异常追踪台',
  icon: '',
  name: 'exceptionMessage',
  sort: 3
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  request: { // 请求参数
    list: {
      url: '/engine/rule/mq-error-message/page',
      method: 'post'
    }
  },
  columns: [
    { width: 100, prop: 'messageBody', label: '系统来源' },
    { width: 200, prop: 'createTime', label: '日期', search: true,
      type: 'date',
      cover: ['createTimeStart', 'createTimeEnd'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
     },
    
    {
      search: true,
      width: 100, 
      prop: 'originalRoutingKey',
      label: '指标编码'
    },
    { width: 200, prop: 'originalExchange', label: '指标名称', search: true },
    { width: 200, prop: 'exceptionMessage', label: '校验结果' },
    { width: 150, prop: 'exceptionStacktrace', label: '指标所属部门' },
    { width: 200, prop: 'createBy', label: '指标所属负责人' },
    {
      width: 100,
      prop: 'status',
      label: '处理状态'
    }
    // {prop: 'operation',width: 120,label: '操作',display: false}
  ]
})
