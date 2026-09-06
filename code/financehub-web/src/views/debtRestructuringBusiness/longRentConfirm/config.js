import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '长期应收款-收入确认',
  icon: '',
  name: 'longRentConfirm'
}

// 转入登记
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/long-income-confirm/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },

    export: {
      title: '导出',
      url: '/engine/finance/long-income-confirm/export',
      filename: '长期应收款-收入确认.xlsx',
      isParams: true,
      isDisabled: false,
      fetchKey: 'idList'
    },
    voucher: {
      title: '生成凭证',
      type: 'primary',
      url: '/engine/finance/long-income-confirm/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/long-income-confirm/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/long-income-confirm/withdraw'
    }

  },
  columns: [
    { label: '长期应收款编号', search: true, prop: 'longReceivableNumber' },
    { label: '合同编号', prop: 'contractCode', search: true },
    {
      label: '记账月份',
      type: 'date',
      search: true,
      prop: 'accountMonth',
      attrs: {
        type: 'month',
        format: 'YYYY-MM',
        valueFormat: 'YYYY-MM'
      }
    },
    { label: '确认收入金额', prop: 'confirmIncomeAmount', render: ({ confirmIncomeAmount }) => toThousands(confirmIncomeAmount) },
    // {
    //   prop: 'processStatusList',
    //   label: '处理状态',
    //   search: true,
    //   hide: true,
    //   type: 'select',
    //   attrs: {
    //     multiple: true,
    //     'collapse-tags': true,
    //     'collapse-tags-tooltip': true
    //   },
    //   option: dictMappingToArray(dictData, 'process_status')
    // },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    },
    {
      prop: 'operation',
      width: 100,
      label: '操作',
      display: false
    }
  ]
})
