import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '诉讼费转费用详情',
  icon: '',
  name: 'litigationCostsByCostsDetail'
}
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  dialogSpan: 12,
  span: 8,
  labelWidth: '100px',
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
      url: '/engine/court-cost/details/page',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      search: true,
      width: 150,
      cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      rules: [{
        required: true,
        message: '请选择签约主体',
        trigger: 'change'
      }],
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true
    }, {
      prop: 'transgerCostAmount',
      label: '转费用金额',
      render (row) {
        return toThousands(row.transgerCostAmount)
      }
    },

    {
      prop: 'costCenter',
      label: '成本中心',
      search: true
    }
  ]
})
