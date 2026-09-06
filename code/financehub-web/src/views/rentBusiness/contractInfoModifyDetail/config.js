import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '合同信息修改详情',
  icon: '',
  name: 'contractInfoModifyDetail'
}
export const optionsStructureConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
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
    isShow: false
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/contract-his/detail/structure',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      level: 'F',
      url: '/engine/finance/contract-his/detail/structure/export',
      type: 'primary',
      isDisabled: false,
      isParams: true,
      filename: '合同信息修改-交易结构.xlsx'
    }
  },
  columns: [
    {
      prop: 'contractCodeM',
      label: '主合同编号',
      width: 200
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      width: 200
    },
    {
      prop: 'payableDeviceAmount',
      label: '设备款',
      width: 200,
      render: ({ payableDeviceAmount }) => toThousands(payableDeviceAmount)
    },
    {
      prop: 'receivableFirstAmount',
      label: '首付款',
      width: 200,
      render: ({ receivableFirstAmount }) => toThousands(receivableFirstAmount)
    },
    {
      prop: 'lessorInsuranceAmount',
      label: '出租人保险费',
      width: 200,
      render: ({ lessorInsuranceAmount }) => toThousands(lessorInsuranceAmount)
    },
    {
      prop: 'receivableMarginAmount',
      label: '承租人履行保证金',
      width: 200,
      render: ({ receivableMarginAmount }) => toThousands(receivableMarginAmount)
    },
    {
      prop: 'channelFee',
      label: '渠道费用',
      width: 200,
      render: ({ channelFee }) => toThousands(channelFee)
    },

    {
      prop: 'receivableProcedureAmount',
      label: '手续费收入(含增值税)',
      width: 200,
      render: ({ receivableProcedureAmount }) => toThousands(receivableProcedureAmount)
    },

    {
      prop: 'lessorOtherCosts',
      label: '出租人其他成本',
      width: 200,
      render: ({ lessorOtherCosts }) => toThousands(lessorOtherCosts)
    },
    {
      prop: 'receivableFirmRebate',
      label: '厂商返利',
      width: 200,
      render: ({ receivableFirmRebate }) => toThousands(receivableFirmRebate)
    },
    {
      prop: 'receivableInsuranceAmount',
      label: '承租人保险费',
      width: 200,
      render: ({ receivableInsuranceAmount }) => toThousands(receivableInsuranceAmount)
    },
    {
      prop: 'retainedPrice',
      label: '期末残值',
      width: 200,
      render: ({ retainedPrice }) => toThousands(retainedPrice)
    },
    {
      prop: 'receivableOther',
      label: '其他收入(含增值税)',
      width: 200,
      render: ({ receivableOther }) => toThousands(receivableOther)
    },
    {
      prop: 'receivableServiceAmount',
      label: '咨询服务收入(含增值税)',
      width: 200,
      render: ({ receivableServiceAmount }) => toThousands(receivableServiceAmount)
    },
    {
      prop: 'vendorMarginAmount',
      label: '供应商履约保证金',
      width: 200,
      render: ({ vendorMarginAmount }) => toThousands(vendorMarginAmount)
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
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'
    }
  ]
})

export const optionsPlanConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
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
    isShow: false
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/contract-his/detail/plan',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      level: 'S',
      url: '/engine/finance/contract-his/detail/plan/export',
      type: 'primary',
      isDisabled: false,
      isParams: true,
      filename: '合同信息修改-租金计划.xlsx'
    }
  },
  columns: [
    {
      prop: 'contractCodeM',
      label: '主合同编号',
      width: 200
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      width: 200
    },
    {
      prop: 'periods',
      label: '期数',
      width: 200
    },
    {
      prop: 'planDate',
      label: '还款日',
      type: 'date',
      width: 150
    },
    {
      prop: 'rentAmount',
      label: '应收租金',
      render: ({ rentAmount }) => toThousands(rentAmount)
    },
    {
      prop: 'principalAmount',
      label: '本金',
      render: ({ principalAmount }) => toThousands(principalAmount)
    },
    {
      prop: 'interestAmount',
      label: '利息',
      render: ({ interestAmount }) => toThousands(interestAmount)
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
      label: '操作',
      width: 150,
      display: false,
      fixed: 'right'
    }
  ]
})
