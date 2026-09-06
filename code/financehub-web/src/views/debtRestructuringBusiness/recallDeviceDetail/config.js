// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { toThousands } from '@/utils'

export default {
  hidden: true,
  title: '回收设备详情',
  icon: '',
  name: 'recallDeviceDetail'
}

// 财务入库详情
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
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
      url: '/engine/finance/recycling-equipment/detailPage',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      getKey: 'id'
    },
    export: {
      title: '导出',
      url: '/engine/finance/recycling-equipment/exportDetail',
      isParams: true,
      isDisabled: false,
      type: 'primary',
      fetchKey: 'recyclingEquipmentInDetailIdList',
      filename: '回收设备-财务入库详情.xlsx'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode', search: true, width: 200 },
    { label: '客户名称', prop: 'clientName', search: true, width: 200 },
    { label: '签约主体', prop: 'orgName', width: 200 },
    { label: '财务合同状态', prop: 'financialContractStatus', width: 200 },
    { label: '入库日期', prop: 'inboundDate', width: 200 },
    { label: '应收租金', prop: 'receivableRentBalance', width: 200, render: ({ receivableRentBalance }) => toThousands(receivableRentBalance) },
    { label: '应收期末残值', prop: 'receivableResidualValueBalance', width: 200, render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance) },
    { label: '应收销项税', prop: 'receivableOuttaxBalance', width: 200, render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance) },
    { label: '未实现收益', prop: 'unrealizedRevenueBalance', width: 200, render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance) },
    { label: '承租人保证金', prop: 'lesseeMarginBalance', width: 200, render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance) },
    { label: '财务敞口', prop: 'financialExposure', width: 200, render: ({ financialExposure }) => toThousands(financialExposure) },
    { label: '回收设备成本', prop: 'recyclingEquipmentCost', width: 200, render: ({ recyclingEquipmentCost }) => toThousands(recyclingEquipmentCost) },
    { label: '入库时计提减值', prop: 'provisionForImpairment', width: 200, render: ({ provisionForImpairment }) => toThousands(provisionForImpairment) },
    {
      prop: 'operation',
      width: 100,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
