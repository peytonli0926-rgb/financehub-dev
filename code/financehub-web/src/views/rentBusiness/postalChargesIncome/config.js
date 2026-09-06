
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { h } from 'vue'
import { ElTag } from 'element-plus'
export default {
  hidden: true,
  title: '邮储手续费收入',
  icon: '',
  name: 'postalChargesIncome'
}

const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },

  avgInfo: {
    title: '生成分摊信息',
    url: '/engine/finance/postal-storage-fee/generate',
    type: 'success',
    isDisabled: false,
    isParams: true
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/postal-storage-fee/voucher'

  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/postal-storage-fee/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/postal-storage-fee/submit'
  }
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
  span: 8,
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
      url: '/engine/finance/postal-storage-fee/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      type: 'date',
      // cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'date',
        // type: 'monthrange',
        // clearable: false,
        // 'range-separator': '-',
        // 'start-placeholder': '开始月份',
        // 'end-placeholder': '结束月份',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'accountDate',
      label: '财务日期',
      search: true,
      type: 'date',
      // cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'date',
        // type: 'monthrange',
        // clearable: false,
        // 'range-separator': '-',
        // 'start-placeholder': '开始月份',
        // 'end-placeholder': '结束月份',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      hide: true,
      search: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'payableProcedureCost',
      label: '支付手续费金额',
      render (row) {
        return toThousands(row.payableProcedureCost)
      }
    },
    {
      prop: 'allocationAmount',
      label: '当期分摊金额',
      render (row) {
        return toThousands(row.allocationAmount)
      }
    },
    {
      prop: 'allocationBalance',
      label: '分摊余额',
      render (row) {
        return toThousands(row.allocationBalance)
      }
    }, {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    }, {
      prop: 'operation',
      width: 150,
      label: '操作',
      display: false

    }]
})
