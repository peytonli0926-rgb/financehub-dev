// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '平价转让详情',
  icon: '',
  name: 'normalTransferDetail'
}

// 详情
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: false,
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
      url: '/engine/finance/parity-transfer/detailPage',
      method: 'post'
    }
  },
  // btnConfig: {
  //   common: {
  //     getKey: 'id'
  //   },
  //   export: {
  //     title: '导出',
  //     url: '/engine/finance/recycling-equipment/exportDetail',
  //     isParams: true,
  //     type: 'primary',
  //     fetchKey: 'recyclingEquipmentInDetailIdList',
  //     filename: '回收设备-财务入库详情.xlsx'
  //   }
  // },
  columns: [
    { label: '转让批次', prop: 'batch', width: 200 },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '客户名称', prop: 'clientName', width: 200 },
    { label: '签约主体', prop: 'orgIdName', width: 200 },
    { label: '财务合同状态', prop: 'financialContractStatus', width: 200 },
    { label: '税率', prop: 'taxRate', width: 200 },
    { label: '应收租金', prop: 'receivableRent', width: 200, render: ({ receivableRent }) => toThousands(receivableRent) },
    { label: '应收期末残值', prop: 'receivableResidualValue', width: 200, render: ({ receivableResidualValue }) => toThousands(receivableResidualValue) },
    { label: '应收销项税', prop: 'receivableOuttax', width: 200, render: ({ receivableOuttax }) => toThousands(receivableOuttax) },
    { label: '未实现融资租赁收益', prop: 'unrealizedRevenue', width: 200, render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue) },
    { label: '承租人保证金', prop: 'lesseeMargin', width: 200, render: ({ lesseeMargin }) => toThousands(lesseeMargin) },
    { label: '应付经销商服务费-暂估', prop: 'payableAgencyEstimate', width: 200, render: ({ payableAgencyEstimate }) => toThousands(payableAgencyEstimate) },
    { label: '应付收车费-暂估', prop: 'payableVehicleEstimate', width: 200, render: ({ payableVehicleEstimate }) => toThousands(payableVehicleEstimate) },
    { label: '应付手环成本_暂估', prop: 'payableBandCostEstimate', width: 200, render: ({ payableBandCostEstimate }) => toThousands(payableBandCostEstimate) },
    { label: '应付抵押费_暂估', prop: 'payablePledgeEstimate', width: 200, render: ({ payablePledgeEstimate }) => toThousands(payablePledgeEstimate) },
    { label: '应付解抵押费_暂估', prop: 'payableUnpledgeEstimate', width: 200, render: ({ payableUnpledgeEstimate }) => toThousands(payableUnpledgeEstimate) },
    { label: '应付其他租赁成本-暂估', prop: 'payableOtherCostEstimate', width: 200, render: ({ payableOtherCostEstimate }) => toThousands(payableOtherCostEstimate) },
    { label: '应收租赁款组合拨备', prop: 'depreciationReserves', width: 200, render: ({ depreciationReserves }) => toThousands(depreciationReserves) },
    { label: '评估价', prop: 'appraisedValue', width: 200, render: ({ appraisedValue }) => toThousands(appraisedValue) },

    {
      prop: 'operation',
      width: 150,
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
  isPagination: true, // 是否需要翻页
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
      url: '/engine/finance/parity-transfer/checkPage',
      method: 'post'
    }
  },
  columns: [
    { label: '合同编号', prop: 'contractCode' },
    { label: '客户名称', prop: 'clientName' },
    {
      prop: 'orgId',
      label: '签约主体',
      width: 200,
      type: 'select',
      tooltip: true,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '应收租金', prop: 'receivableRent', render: ({ receivableRent }) => toThousands(receivableRent) },
    { label: '应收期末残值', prop: 'receivableResidualValue', render: ({ receivableResidualValue }) => toThousands(receivableResidualValue) },
    { label: '应收销项税', prop: 'receivableOuttax', render: ({ receivableOuttax }) => toThousands(receivableOuttax) },
    { label: '未实现融资租赁收益', prop: 'unrealizedRevenue', render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue) },
    { label: '承租人保证金', prop: 'lesseeMargin', render: ({ lesseeMargin }) => toThousands(lesseeMargin) }

  ]
})
