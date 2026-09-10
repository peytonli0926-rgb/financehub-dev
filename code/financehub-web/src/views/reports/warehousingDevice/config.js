
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
export default {
  hidden: true,
  title: '财务入库出库报表',
  icon: '',
  name: 'warehousingDevice'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8, // 查询区域每一个表单要展示的宽度
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
      url: '/engine/finance/report/inbound-outbound/report',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      type: 'select',
      display: false,
      isDefaultvalue: true,
      attrs: {
        filterable: true
      },
      hide: true,
      request: {
        // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    { prop: 'contractCode', label: '合同编码', width: 200 },

    // { prop: 'clientCode', label: '客户编码', width: 200 },
    { prop: 'clientName', search: true, label: '客户名称', width: 200 },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      minWidth: 300,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'inboundDate',
      label: '入库日期',
      width: 200,
      search: true,
      type: 'date',
      cover: ['inboundDateStart', 'inboundDateEnd'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'receivableRentBalance',
      label: '应收租金',
      width: 200,
      render: ({ receivableRentBalance }) => toThousands(receivableRentBalance)
    },
    {
      prop: 'receivableResidualValueBalance',
      label: '应收期末残值',
      width: 200,
      render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance)
    },
    {
      prop: 'receivableServiceOuttaxAmount',
      label: '应交销项税',
      width: 200,
      render: ({ receivableServiceOuttaxAmount }) => toThousands(receivableServiceOuttaxAmount)
    },
    {
      prop: 'unrealizedRevenueBalance',
      label: '未实现收益',
      width: 200,
      render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance)
    },
    {
      prop: 'lesseeMarginBalance',
      label: '承租人保证金',
      width: 200,
      render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance)
    },

    { prop: 'financialExposure', label: '财务敞口', width: 200, render: ({ financialExposure }) => toThousands(financialExposure) },
    {
      prop: 'recyclingEquipmentCost',
      label: '回收设备成本',
      width: 200,
      render: ({ recyclingEquipmentCost }) => toThousands(recyclingEquipmentCost)
    },
    {
      prop: 'recyclingEquipmentCost',
      label: '回收设备成本科目余额',
      width: 250,
      render: ({ recyclingEquipmentAccountBalance }) => toThousands(recyclingEquipmentAccountBalance)
    },
    {
      prop: 'provisionForImpairment',
      label: '入库时计提减值',
      width: 200,
      render: ({ provisionForImpairment }) => toThousands(provisionForImpairment)
    },

    {
      prop: 'substractBalance',
      label: '回收设备减值',
      width: 200,
      render: ({ substractBalance }) => toThousands(substractBalance)
    },

    {
      prop: 'netWorth',
      label: '净值',
      width: 200,
      render: ({ netWorth }) => toThousands(netWorth)
    },
    {
      prop: 'provisionalReceiptsBalance',
      label: '暂收款项',
      width: 200,
      render: ({ provisionalReceiptsBalance }) => toThousands(provisionalReceiptsBalance)
    },

    {
      prop: 'outboundDate',
      label: '出库日期',
      width: 200,
      type: 'date',
      search: true,
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

    {
      prop: 'outboundType',
      label: '出库类型',
      width: 200,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'outbound_type'),
      render ({ outboundType }) {
        return dictMappingLabel(dictData, 'outbound_type', outboundType)
      }
    },
    { prop: 'dealAmount', label: '处置金额', width: 200 },

    {
      prop: 'receivableOuttaxBalance',
      label: '应收销项税',
      width: 200,
      render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance)
    },
    {
      prop: 'profitLoss',
      label: '融资租赁资产处置损益',
      width: 200,
      render: ({ profitLoss }) => toThousands(profitLoss)
    }

  ]
})
