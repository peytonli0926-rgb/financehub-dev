import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '回收设备',
  icon: '',
  name: 'recallDevice'
}

const btnInboundConfig = {
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
      url: '/engine/finance/recycling-equipment/importTemplate',
      title: '回收设备-财务入库',
      open: true,
      templateUrl: '/engine/finance/recycling-equipment/exportTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/recycling-equipment/exportSum',
    isParams: true,
    level: 'F',
    fetchKey: 'recyclingEquipmentInIdList',
    filename: '回收设备-财务入库.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/recycling-equipment/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'F',
    url: '/engine/finance/recycling-equipment/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'F',
    url: '/engine/finance/recycling-equipment/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'F',
    url: '/engine/finance/recycling-equipment/delete'
  }
}

const btnOutboundConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },
  import: {
    title: '上传',
    type: 'primary',
    level: 'S',
    isDisabled: false,
    params: {
      url: '/engine/finance/recycling-equipment-out/importTemplate',
      title: '回收设备-财务出库',
      open: true,
      templateUrl: '/engine/finance/recycling-equipment-out/exportTemplate'
    }
  },
  export: {
    title: '导出',
    url: '/engine/finance/recycling-equipment-out/exportDetail',
    isParams: true,
    level: 'S',
    fetchKey: 'recyclingEquipmentOutIdList',
    filename: '回收设备-财务出库.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/recycling-equipment-out/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    level: 'S',
    url: '/engine/finance/recycling-equipment-out/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    level: 'S',
    url: '/engine/finance/recycling-equipment-out/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    level: 'S',
    url: '/engine/finance/recycling-equipment-out/delete'
  }
}

// 财务入库
export const optionsConfigInbound = (router, dictData = {}) => ({
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
      url: '/engine/finance/recycling-equipment/page',
      method: 'post'
    }
  },
  btnConfig: btnInboundConfig,
  columns: [
    {
      prop: 'orgIds',
      label: '签约主体',
      width: 200,
      search: true,
      hide: true,

      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '入库日期',
      prop: 'inboundDate',
      type: 'date',
      search: true,
      width: 110,
      cover: ['inboundDateStart', 'inboundDateEnd'],
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
    { label: '签约主体', prop: 'orgName', tooltip: true },
    {
      label: '财务敞口',
      prop: 'financialExposure',
      render: ({ financialExposure }) => toThousands(financialExposure)
    },
    {
      label: '回收设备成本',
      prop: 'recyclingEquipmentCost',
      render: ({ recyclingEquipmentCost }) => toThousands(recyclingEquipmentCost)
    },
    {
      label: '入库时计提减值',
      prop: 'provisionForImpairment',
      render: ({ provisionForImpairment }) => toThousands(provisionForImpairment)
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render(row) {
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      width: 200,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

// 财务出库
export const optionsConfigOutbound = (router, dictData = {}) => ({
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
      url: '/engine/finance/recycling-equipment-out/page',
      method: 'post'
    }
  },
  btnConfig: btnOutboundConfig,
  columns: [
    {
      prop: 'orgIds',
      label: '签约主体',
      width: 200,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    { label: '合同编号', prop: 'contractCode', search: true },
    { label: '客户名称', prop: 'clientName', search: true, tooltip: true },
    {
      label: '出库日期',
      prop: 'outboundDate',
      type: 'date',
      search: true,
      width: 100,
      cover: ['outboundDateStart', 'outboundDateEnd'],
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
    { label: '签约主体', prop: 'orgName', tooltip: true },

    {
      label: '回收设备成本',
      prop: 'recyclingEquipmentCost',
      render: ({ recyclingEquipmentCost }) => toThousands(recyclingEquipmentCost)
    },
    {
      label: '回收设备减值',
      prop: 'provisionForImpairment',
      render: ({ provisionForImpairment }) => toThousands(provisionForImpairment)
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render(row) {
        return h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))
      }
    },
    {
      prop: 'operation',
      width: 100,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

export const optionsConfigDialog = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
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
      url: '/engine/finance/recycling-equipment/remainBalance/check',
      method: 'post'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', search: true },
    { label: '客户名称', prop: 'clientName', search: true },
    {
      prop: 'orgId',
      label: '签约主体',
      width: 200,
      type: 'select',
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      label: '应收租金',
      prop: 'receivableRentBalance',
      search: true,
      render: ({ receivableRentBalance }) => toThousands(receivableRentBalance)
    },
    {
      label: '应收期末残值',
      prop: 'receivableResidualValueBalance',
      search: true,
      render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance)
    },
    {
      label: '应收销项税',
      prop: 'receivableOuttaxBalance',
      search: true,
      render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance)
    },
    {
      label: '未实现融资租赁收益',
      prop: 'unrealizedRevenueBalance',
      search: true,
      render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance)
    },
    {
      label: '承租人保证金',
      prop: 'lesseeMarginBalance',
      search: true,
      render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance)
    }
  ]
})
