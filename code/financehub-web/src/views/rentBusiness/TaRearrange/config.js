import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, toThousands, dictMappingToArray } from '@/utils'

export default {
  hidden: false,
  title: 'TA重分类',
  icon: '',
  name: 'TaRearrange'
}

// 按钮
const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    level: 'F',
    params: {
      url: '/engine/finance/ta-reclassification/importFile',
      title: '重分类明细',
      open: true,
      templateUrl: '/engine/finance/ta-reclassification/exportTemplate'
    }
  },
  voucher: {
    title: '生成凭证',
    level: 'F',
    type: 'warning',
    url: '/engine/finance/ta-reclassification/generateVoucher'
  },
  submit: {
    title: '提交',
    level: 'F',
    type: 'success',
    url: '/engine/finance/ta-reclassification/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/ta-reclassification/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'F',
    url: '/engine/finance/ta-reclassification/delete'
  }
}
const btnConfig1 = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    isDisabled: false,
    level: 'S',
    params: {
      url: '/engine/finance/ta-other-payable/importFile',
      title: '重分类-其他应付款',
      open: true,
      templateUrl: '/engine/finance/ta-other-payable/exportTemplate'
    }
  },
  voucher: {
    title: '生成凭证',
    level: 'S',
    type: 'warning',
    url: '/engine/finance/ta-other-payable/generateVoucher'
  },
  submit: {
    title: '提交',
    level: 'S',
    type: 'success',
    url: '/engine/finance/ta-other-payable/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/ta-other-payable/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'S',
    url: '/engine/finance/ta-other-payable/delete'
  }
}
export const optionsConfigTransfer = (router, dictData = {}) => ({
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
      url: '/engine/finance/ta-reclassification/page',
      method: 'post'
    }
  },
  btnConfig,
  columns: [
    {
      label: '重分类月份',
      prop: 'reclassificationMonth',
      type: 'date',
      search: true,
      attrs: { type: 'month', format: 'YYYY-MM', valueFormat: 'YYYY-MM' }
    },
    {
      label: 'TA重分类金额',
      prop: 'taReclassificationAmountStr',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'ta_amount')
    },
    {
      label: 'TA重分类金额',
      prop: 'taReclassificationAmount',
      type: 'inputNumber',
      render: ({ taReclassificationAmount }) => toThousands(taReclassificationAmount)
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      render: ({ processStatus }) => {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
      }
    },

    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

export const optionsConfigRansom = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/ta-other-payable/page',
      method: 'post'
    }
  },
  btnConfig: btnConfig1,
  columns: [
    {
      label: '重分类月份',
      prop: 'reclassificationMonth',
      type: 'date',
      search: true,
      attrs: { type: 'month', format: 'YYYY-MM', valueFormat: 'YYYY-MM' }
    },
    {
      label: 'TA重分类金额',
      prop: 'reclassificationAmount',
      type: 'inputNumber',
      render: ({ reclassificationAmount }) => toThousands(reclassificationAmount)
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      render: ({ processStatus }) => {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
      }
    },

    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
