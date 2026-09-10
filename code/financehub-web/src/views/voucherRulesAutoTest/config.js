export default {
  hidden: true,
  title: '凭证规则自动测试',
  icon: '',
  name: 'voucherRulesAutoTestMain'
}

import moment from 'moment'
import { h } from 'vue'
import { ElTag } from 'element-plus'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearchBtn: true,
  searchBtnShow: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  span: 6, // 查询区域每一个表单要展示的宽度
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  }
}

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    list: {
      url: '/engine/finance/voucher-rule-check/queryRunRulesList',
      method: 'post'
    }
  },
  btnConfig: {},
  columns: [
    {
      prop: 'eventFrom',
      label: '事件编号从',
      type: 'inputNumber',
      search: true,
      hide: true,
      value: 1,
      attrs: {
        'controls-position': 'right'
      }
    },
    {
      prop: 'eventTo',
      label: '到',
      type: 'inputNumber',
      search: true,
      hide: true,
      value: 1,
      attrs: {
        'controls-position': 'right'
      }
    },
    {
      prop: 'taskId',
      label: '任务编号',
      minWidth: 120
    },
    {
      prop: 'beginTime',
      label: '开始执行时间',
      width: 160,
      render: ({ beginTime }) => {
        return (beginTime && moment(beginTime).format('YYYY-MM-DD HH:mm')) || ''
      }
    },
    {
      prop: 'fullFlag',
      label: '是否全量运行',
      width: 150,
      render: ({ fullFlag }) => {
        return (fullFlag && h(ElTag, () => (fullFlag === 'Y' ? '是' : '否'))) || ''
      }
    },
    {
      prop: 'finishTime',
      label: '完成时间',
      width: 160,
      render: ({ finishTime }) => {
        return (finishTime && moment(finishTime).format('YYYY-MM-DD HH:mm')) || ''
      }
    },
    {
      prop: 'stateName', // state--10：新建；20：执行中；30：执行成功；40：执行失败
      label: '状态',
      width: 120,
      render: ({ stateName }) => {
        return (stateName && h(ElTag, () => stateName)) || ''
      }
    },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
