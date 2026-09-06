import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '租金收入确认',
  icon: '',
  name: 'rentConfirm'
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
      url: '/engine/finance/rent-income-confirm/page',
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
      url: '/engine/finance/rent-income-confirm/export',
      filename: '租金收入确认.xlsx',
      isParams: true,
      fetchKey: 'idList',
      isDisabled: false
    },
    voucher1: {
      title: '生成收入确认凭证',
      type: 'success',
      url: '/engine/finance/rent-income-confirm/generateVoucher',
      condition: (item) => (['1', '5'].includes(item.processStatus))
    },
    voucher: {
      title: '生成结转凭证',
      type: 'primary',
      url: '/engine/finance/rent-income-confirm/generateCarryForwardVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/rent-income-confirm/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/rent-income-confirm/withdraw'
    }

  },
  columns: [

    { label: '租赁合同编号', search: true, prop: 'contractCode' },
    { label: '客户名称', prop: 'clientName' },
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
    { label: '当月应收租金', prop: 'thisMonthReceivableRent', render: ({ thisMonthReceivableRent }) => toThousands(thisMonthReceivableRent) },
    { label: '当月计提税金', prop: 'thisMonthTax', render: ({ thisMonthTax }) => toThousands(thisMonthTax) },
    { label: '确认收入金额', prop: 'thisMonthRentIncome', render: ({ thisMonthRentIncome }) => toThousands(thisMonthRentIncome) },

    {
      prop: 'processStatusList',
      label: '处理状态',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
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
