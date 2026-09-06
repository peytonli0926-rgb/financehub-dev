import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '核销详情',
  icon: '',
  name: 'verificationDetail'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
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
    isShow: true
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      routeQuery: ['verificationId'],
      params: {
        url: '/engine/verification/details/importDetailInfos',
        title: '核销详情上传',
        open: true,
        templateUrl: '/engine/verification/details/exportTemplate'
      }
    },
    export: {
      title: '导出',
      type: '',
      isParams: true,
      isDisabled: false,
      routeQuery: ['verificationId'],
      filename: '核销详情.xlsx',
      url: '/engine/verification/details/exportDetailInfos'
    }
    // validate: {
    //   title: '校验',
    //   type: 'success',
    //   url: '/engine/verification/details/verify'
    // }
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/verification/details/page',
      method: 'post'
    },
    del: {
      url: '/engine/verification/details/delete/$[id]',
      method: 'post'
    }
  },

  columns: [
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      width: 200
    },
    {
      prop: 'clientName',
      label: '客户名称',
      search: true,
      width: 200
    },

    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      },
      width: 200
    },

    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'verification_financial_status'),
      width: 200,
      render: (row) => {
        return (
          (row.financialContractStatus &&
            h(ElTag, () =>
              dictMappingLabel(
                dictData,
                'verification_financial_status',
                row.financialContractStatus
              )
            )) ||
          ''
        )
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      width: 200
    },
    {
      prop: 'receivableRent',
      label: '应收租金',
      render: (row) => {
        return toThousands(row.receivableRent)
      },
      width: 200
    },

    {
      prop: 'receivableResidualValue',
      label: '应收期末残值',
      render: (row) => {
        return toThousands(row.receivableResidualValue)
      },
      width: 200
    },
    {
      prop: 'receivableDownpayment',
      label: '应收首付款',
      render: (row) => {
        return toThousands(row.receivableDownpayment)
      },
      width: 200
    },
    {
      prop: 'receivableCommission',
      label: '应收手续费',
      render: (row) => {
        return toThousands(row.receivableCommission)
      },
      width: 200
    },
    {
      prop: 'receivableInsurance',
      label: '应收保险费',
      render: (row) => {
        return toThousands(row.receivableInsurance)
      },
      width: 200
    },
    {
      prop: 'receivableOtherincome',
      label: '应收其他收入',
      render: (row) => {
        return toThousands(row.receivableOtherincome)
      },
      width: 200
    },
    {
      prop: 'receivableOuttax',
      label: '应收销项税',
      width: 200,
      render: (row) => {
        return toThousands(row.receivableOuttax)
      }
    },
    {
      prop: 'unrealizedRevenue',
      label: '未实现收益',
      render: (row) => {
        return toThousands(row.unrealizedRevenue)
      },
      width: 200
    },
    {
      prop: 'payableDeviceEstimate',
      label: '应付设备款-暂估',
      render: (row) => {
        return toThousands(row.payableDeviceEstimate)
      },
      width: 200
    },
    {
      prop: 'payableAgencyEstimate',
      label: '应付经销商服务费-暂估',
      render: (row) => {
        return toThousands(row.payableAgencyEstimate)
      },
      width: 200
    },
    {
      prop: 'payableVehicleEstimate',
      label: '应付收车费-暂估',
      render: (row) => {
        return toThousands(row.payableVehicleEstimate)
      },
      width: 200
    },
    {
      prop: 'payableBandCostEstimate',
      label: '应付手环成本-暂估',
      render: (row) => {
        return toThousands(row.payableBandCostEstimate)
      },
      width: 200
    },
    {
      prop: 'payablePledgeEstimate',
      label: '应付抵押费-暂估',
      render: (row) => {
        return toThousands(row.payablePledgeEstimate)
      },
      width: 200
    },
    {
      prop: 'payableUnpledgeEstimate',
      label: '应付解抵押费-暂估',
      render: (row) => {
        return toThousands(row.payableUnpledgeEstimate)
      },
      width: 200
    },
    {
      prop: 'payableOtherCostEstimate',
      label: '应付其他租赁成本-暂估',
      render: (row) => {
        return toThousands(row.payableOtherCostEstimate)
      },
      width: 200
    },

    {
      prop: 'financialExpenseAmount',
      label: '财务核销敞口',
      render: (row) => {
        return toThousands(row.financialExpenseAmount)
      },
      width: 200
    },

    {
      prop: 'depreciationReserves',
      label: '应收租赁款组合拨备',
      width: 200,
      render: (row) => {
        return toThousands(row.depreciationReserves)
      }
    },
    {
      prop: 'compensationProvisionAmount',
      label: '补提拨备',
      width: 200,
      render: (row) => {
        return toThousands(row.compensationProvisionAmount)
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
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: false,
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: true
  },

  request: {
    // 请求参数
    list: {
      url: '/engine/verification/checkPage',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      width: 200
    },
    {
      prop: 'clientName',
      label: '客户名称',
      search: true,
      width: 200
    },

    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      },
      width: 200
    },

    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'verification_financial_status'),
      width: 200,
      render: (row) => {
        return (
          (row.financialContractStatus &&
            h(ElTag, () =>
              dictMappingLabel(
                dictData,
                'verification_financial_status',
                row.financialContractStatus
              )
            )) ||
          ''
        )
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      width: 200
    },
    {
      prop: 'receivableRent',
      label: '应收租金',
      render: ({ receivableRent }) => toThousands(receivableRent),
      width: 200
    },

    {
      prop: 'receivableResidualValue',
      label: '应收期末残值',
      render: ({ receivableResidualValue }) => toThousands(receivableResidualValue),
      width: 200
    },
    {
      prop: 'receivableDownpayment',
      label: '应收首付款',
      render: ({ receivableDownpayment }) => toThousands(receivableDownpayment),
      width: 200
    },
    {
      prop: 'receivableCommission',
      label: '应收手续费',
      render: ({ receivableCommission }) => toThousands(receivableCommission),
      width: 200
    },
    {
      prop: 'receivableInsurance',
      label: '应收保险费',
      render: ({ receivableInsurance }) => toThousands(receivableInsurance),
      width: 200
    },
    {
      prop: 'receivableOtherincome',
      label: '应收其他收入',
      render: ({ receivableOtherincome }) => toThousands(receivableOtherincome),
      width: 200
    },
    {
      prop: 'receivableOuttax',
      label: '应收销项税',
      width: 200,
      render: ({ receivableOuttax }) => toThousands(receivableOuttax)
    },
    {
      label: '未实现收益',
      prop: 'unrealizedRevenue',
      width: 200,
      render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue)
    },
    {
      label: '应付设备款-暂估',
      prop: 'payableDeviceEstimate',
      width: 200,
      render: ({ payableDeviceEstimate }) => toThousands(payableDeviceEstimate)
    },
    {
      label: '应付经销商服务费-暂估',
      prop: 'payableAgencyEstimate',
      width: 200,
      render: ({ payableAgencyEstimate }) => toThousands(payableAgencyEstimate)
    },
    {
      label: '应付收车费-暂估',
      prop: 'payableVehicleEstimate',
      width: 200,
      render: ({ payableVehicleEstimate }) => toThousands(payableVehicleEstimate)
    },
    {
      label: '应付手环成本_暂估',
      prop: 'payableBandCostEstimate',
      width: 200,
      render: ({ payableBandCostEstimate }) => toThousands(payableBandCostEstimate)
    },
    {
      label: '应付抵押费_暂估',
      prop: 'payablePledgeEstimate',
      width: 200,
      render: ({ payablePledgeEstimate }) => toThousands(payablePledgeEstimate)
    },
    {
      label: '应付解抵押费_暂估',
      prop: 'payableUnpledgeEstimate',
      width: 200,
      render: ({ payableUnpledgeEstimate }) => toThousands(payableUnpledgeEstimate)
    },
    {
      label: '应付其他租赁成本-暂估',
      prop: 'payableOtherCostEstimate',
      width: 200,
      render: ({ payableOtherCostEstimate }) => toThousands(payableOtherCostEstimate)
    },
    {
      label: '应收租赁款组合拨备',
      prop: 'depreciationReserves',
      width: 200,
      render: ({ depreciationReserves }) => toThousands(depreciationReserves)
    }
  ]
})
