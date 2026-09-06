import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel,dictMappingToArray, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '长期应收款',
  icon: '',
  name: 'longTermReceivables'
}

// 转出-出租登记
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
  labelWidth:'150px',
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
      url: '/engine/finance/long-receivable-register/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    each: {
      title: '分摊',
      url: '/engine/finance/long-receivable-register/apportion',
      forward: '/debtRestructuringBusiness/longRentConfirm',
      source: 'data',
      // isDisabled: false,
      condition: (data) => (data.length !== 0)
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      params: {
        url: '/engine/finance/long-receivable-register/importFile',
        title: '长期应收款',
        open: true,
        templateUrl: '/engine/finance/long-receivable-register/exportTemplate'
      }
    },
    export: {
      title: '导出',
      url: '/engine/finance/long-receivable-register/export',
      filename: '长期应收款登记.xlsx',
      isParams: true,
      fetchKey: 'idList'
    },
    voucher: {
      title: '生成凭证',
      type: 'primary',
      url: '/engine/finance/long-receivable-register/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/long-receivable-register/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/long-receivable-register/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      url: '/engine/finance/long-receivable-register/delete'
    }
  },
  columns: [

    { label: '长期应收款编号', prop: 'longReceivableNumber', search: true },
    { label: '合同编号', prop: 'contractCode', search: true },
    { label: '项目名称', prop: 'projectName', search: true },
    { label: '客户名称', prop: 'clientName', search: true,tooltip: true },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      width: 200,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '应收总额', prop: 'receivableTotal', render: ({ receivableTotal }) => toThousands(receivableTotal) },
    { label: '应收本金', prop: 'receivablePrincipal', render: ({ receivablePrincipal }) => toThousands(receivablePrincipal) },
    { label: '应收利息', prop: 'receivableInterest', render: ({ receivableInterest }) => toThousands(receivableInterest) },
    { label: '其他应收款项', prop: 'otherReceivable', render: ({ otherReceivable }) => toThousands(otherReceivable) },
    { label: '版本号', prop: 'versionNum' },
    { label: '是否仅展示最新版本',hide:true, prop: 'isLatestVersion', search: true ,type:'select',option:dictMappingToArray(dictData,'is_sys_bool')},

    
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
      width: 150,
      label: '操作',
      display: false
    }
  ]
})
