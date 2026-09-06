import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '应付保险费',
  icon: '',
  name: 'payableInsurance'
}

// 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    isDisabled: false,
    type: 'primary',
    params: {
      url: '/engine/finance/payable-insurance/importData',
      title: '应付保险费',
      open: true,
      templateUrl: '/engine/finance/payable-insurance/importTemplate'
    }
  },
  fileListBtn: {
    title: '查看列表',
    isDisabled: false,
    url: '/engine/finance/margin-contract-balance/export',
    isParams: true,
    isAsyncFile: true,
    params: {
      moduleName: 'payable_insurance',
      businessScene: 'payableInsurance'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/payable-insurance/export',
    filename: '应付保险费.xlsx',
    isDisabled: false,
    fetchKey: 'payableInsuranceIdList',
    isParams: true,
    isAsyncFile: true
  },

  info: {
    title: '生成信息',
    type: 'success',
    isDisabled: false,
    isParams: true,
    url: '/engine/finance/payable-insurance/generate'
  },

  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/payable-insurance/voucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/payable-insurance/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/payable-insurance/withdraw'
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
  btnConfig,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/payable-insurance/page',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      type: 'date',
      attrs: {
        type: 'date',
        // clearable: false,
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      alias: '财务日期',
      search: true,
      type: 'date',
      attrs: {
        type: 'date',
        // clearable: false,
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      hide: true,
      option: dictMappingToArray(dictData, 'company'),
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      prop: 'carryoverAmount',
      label: '保险费结转金额',
      render: (row) => {
        return toThousands(row.carryoverAmount)
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
      render: (row) => {
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
