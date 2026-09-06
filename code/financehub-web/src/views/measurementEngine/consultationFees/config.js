import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '分摊引擎',
  icon: '',
  name: 'consultationFees'
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
      url: '/engine/finance/service-fee/page',
      method: 'post'
    }
  },
  btnConfig:{
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      params: {
        url: '/engine/finance/service-fee/importData',
      title: '咨询服务费分摊',
      open: true,
      templateUrl: '/engine/finance/service-fee/importTemplate'
      }
    },
    export:{
      title: '导出',
      url: '/engine/finance/service-fee/export',
      isDisabled:false,
      isParams:true,
      fetchKey:'idList',
      filename:'咨询服务费分摊.xlsx'
    },
    desVoucher:{
      title: '冲销凭证',
      type: 'danger',
      url: '/engine/finance/service-fee/reversal/voucher',
      condition:(item)=>{
        return item.isGenerateVoucher === '3'
      }
    },
    voucher: {
      title: '生成凭证',
      type: 'warning',
      url: '/engine/finance/service-fee/voucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/service-fee/submit'
    },
    delete: {
      title: '删除',
      type: 'danger',
      url: '/engine/finance/service-fee/delete'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/service-fee/withdraw'
    }
  },
  columns: [{
    prop: 'accountDate',
    label: '记账日期',
    type: 'date',
    attrs: {
      type: 'date',
      format: 'YYYY-MM',
      'value-format': 'YYYY-MM'
    }
  },
  {
    prop: 'businessDate',
    label: '业务日期',
    alias: '计提月份',
    search: true,
    type: 'date',
    attrs: {
      type: 'month',
      format: 'YYYY-MM',
      'value-format': 'YYYY-MM'
    }
  },

  {
    prop: 'serviceFeeAmortizationIncome',
    label: '服务费摊销收入',
    render (row) {
      return toThousands(row.serviceFeeAmortizationIncome)
    }
  },
  {
    prop: 'serviceOrgIdList',
    label: '服务费签约主体',
    type: 'select',
    search: true,
    hide: true,
    width: 200,
    attrs: {
      multiple: true,
      'collapse-tags': true,
      'collapse-tags-tooltip': true,
      filterable: true
    },
    option: dictMappingToArray(dictData, 'company')

  },
  {
    prop: 'serviceOrgId',
    label: '服务费签约主体',
    tooltip: true,
    render (row) {
      return dictMappingLabel(dictData, 'company', row.orgId)
    }
  },
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
  }, {
    prop: 'operation',
    width: 150,
    label: '操作',
    display: false

  }]
})
