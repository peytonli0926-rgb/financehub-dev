import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '减值计量',
  icon: '',
  name: 'impairmentBusinessIndex',
  sort: 4
}
// 减值计提 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },

  export: {
    title: '导出',
    url: '/engine/finance/impairment-provision/export',
    isParams: true,
    fetchKey: 'idList',
    filename: '减值计提.xlsx'
  },
  writeOff: {
    title: '冲销',
    url: '/engine/finance/impairment-provision/writeOff',
    condition: (data) => data.length !== 0,
    source: 'data'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/impairment-provision/generateVoucher'
  },

  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/impairment-provision/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/impairment-provision/withdraw'

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
      url: '/engine/finance/impairment-provision/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    {
      prop: 'accountDate',
      label: '财务日期',
      type: 'date',
      search: true,
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'impairmentType',
      label: '减值类型',
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'provision_type')
      // render ({leaseIncomeAmount}) {
      //   return toThousands(leaseIncomeAmount)
      // }
    },
    {
      prop: 'provisionTotal',
      label: '拨备合计',
      render ({ provisionTotal }) {
        return toThousands(provisionTotal)
      }
    },
    {
      prop: 'lastMonthBalance',
      label: '上月余额',
      render ({ lastMonthBalance }) {
        return toThousands(lastMonthBalance)
      }
    },
    {
      prop: 'thisMonthProvision',
      label: '本月计提',
      render ({ thisMonthProvision }) {
        return toThousands(thisMonthProvision)
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
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
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

export const reportOptionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  isSearch: false,
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/impairment-provision/getImpairmentReport',
      method: 'get'
    }
  },
  columns: [
    { label: '减值类型', prop: 'impairmentType' },
    { label: '分类结果', prop: 'classResult' },
    {
      label: '风险敞口',
      prop: 'riskExposure',
      render: ({ riskExposure }) => toThousands(riskExposure)
    },
    {
      label: '拨备合计',
      prop: 'provisionTotal',
      render: ({ provisionTotal }) => toThousands(provisionTotal)
    },
    {
      label: '上月余额',
      prop: 'lastMonthBalance',
      render: ({ lastMonthBalance }) => toThousands(lastMonthBalance)
    },
    {
      label: '本月转出',
      prop: 'thisMonthTransferOut',
      render: ({ thisMonthTransferOut }) => toThousands(thisMonthTransferOut)
    },

    {
      label: '本月计提',
      prop: 'thisMonthProvision',
      render: ({ thisMonthProvisionError, thisMonthProvision }) => {
        return `<span style="color:${thisMonthProvisionError === '1' ? 'red' : ''}">${thisMonthProvision || ''
          }</span>`
      }
    }
  ]
})

export const taskOptionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  isSearch: true,
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
      url: '/engine/finance/impairment-provision/queryUpdateTask',
      method: 'post'
    }
  },
  columns: [
    { label: '任务类型', prop: 'taskType', search: true },
    {
      label: '状态',
      prop: 'status',
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'task_status'),
      render: ({ status }) => {
        return (status && h(ElTag, () => dictMappingLabel(dictData, 'task_status', status))) || ''
      }
    },
    { label: '开始时间', prop: 'startTime' },
    { label: '结束时间', prop: 'endTime' },
    { label: '任务总条数', prop: 'dataSize' },
    { label: '成功数据条数', prop: 'dataSuccessSize' },
    { label: '失败数据条数', prop: 'dataFailedSize' },
    { label: 'excel类型', prop: 'excelType', search: true },
    { label: '文件名称', prop: 'fileName' },
    { label: '错误信息', prop: 'errorInfo',tooltip:true },
    
    { label: '单据id', prop: 'docId' },
    { label: '用户名', prop: 'userName', search: true }
  ]
})
