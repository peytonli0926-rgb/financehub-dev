import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '租赁大表',
  icon: '',
  name: 'rentalTable'
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
      url: '/engine/finance/report/leaseTable/report',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'queryDate',
      label: '日期',
      search: true,
      hide: true,
      type: 'date',
      isSearchRequired: true,
      width: 200,
      rules: [
        {
          required: true,
          message: '请选择查询日期',
          trigger: 'change'
        }
      ],
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      minWidth: 300,
      search: true,
      hide: true,
      rules: [
        {
          required: true,
          message: '请选择签约主体',
          trigger: 'change'
        }
      ],
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 300,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      // 展示数据
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      width: 200,
      attrs: {
        multiple: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    // { width: 200, prop: 'clientCode', label: '客户编码' },
    { width: 200, prop: 'clientName', search: true, label: '客户名称' },
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render (row) {
        return (
          (row.systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', row.systemCode))) ||
          ''
        )
      }
    },
    { width: 200, prop: 'contractStatus', label: '合同状态' },
    { width: 200, prop: 'financialContractStatus', label: '财务合同状态' },
    { width: 200, prop: 'leaseDateStart', label: '会计起租日', type: 'date' },
    { width: 200, prop: 'leaseDateEnd', label: '合同约定到期日', type: 'date' },
    {
      prop: 'leaseType',
      label: '租赁类型',
      type: 'select',
      search: true,
      width: 200,
      option: dictMappingToArray(dictData, 'lease_type'),
      render (row) {
        return (
          (row.leaseType &&
            h(ElTag, () => dictMappingLabel(dictData, 'lease_type', row.leaseType))) ||
          ''
        )
      }
    },
    {
      width: 200,
      prop: 'receivableRentBalance',
      label: '应收租金',
      render: ({ receivableRentBalance }) => toThousands(receivableRentBalance)
    },
    {
      width: 200,
      prop: 'receivableDownpaymentBalance',
      label: '应收首付款',
      render: ({ receivableDownpaymentBalance }) => toThousands(receivableDownpaymentBalance)
    },

    {
      width: 200,
      prop: 'receivableResidualValueBalance',
      label: '应收期末残值',
      render: ({ receivableResidualValueBalance }) => toThousands(receivableResidualValueBalance)
    },
    {
      width: 200,
      prop: 'receivableCommissionBalance',
      label: '应收手续费',
      render: ({ receivableCommissionBalance }) => toThousands(receivableCommissionBalance)
    },

    {
      width: 200,
      prop: 'receivableInsuranceBalance',
      label: '应收保险费',
      render: ({ receivableInsuranceBalance }) => toThousands(receivableInsuranceBalance)
    },
    {
      width: 200,
      prop: 'receivableOtherincomeBalance',
      label: '应收其他收入',
      render: ({ receivableOtherincomeBalance }) => toThousands(receivableOtherincomeBalance)
    },
    {
      width: 200,
      prop: 'receivableRebateBalance',
      label: '应收返利',
      render: ({ receivableRebateBalance }) => toThousands(receivableRebateBalance)
    },
    {
      width: 200,
      prop: 'receivableOuttaxBalance',
      label: '应收销项税',
      render: ({ receivableOuttaxBalance }) => toThousands(receivableOuttaxBalance)
    },

    {
      width: 200,
      prop: 'receivableOutputtaxBaseBalance',
      label: '应收销项税-本金',
      render: ({ receivableOutputtaxBaseBalance }) => toThousands(receivableOutputtaxBaseBalance)
    },

    {
      width: 200,
      prop: 'receivableTotalBalance',
      label: '应收融资租赁款总额',
      render: ({ receivableTotalBalance }) => toThousands(receivableTotalBalance)
    },

    {
      width: 200,
      prop: 'receivableServiceFeeBalance',
      label: '未实现收益-咨询服务费分摊',
      render: ({ receivableServiceFeeBalance }) => toThousands(receivableServiceFeeBalance)
    },

    {
      width: 200,
      prop: 'rentalIncomeAfterTotal',
      label: '未实现收益-不含服务费',
      render: ({ rentalIncomeAfterTotal }) => toThousands(rentalIncomeAfterTotal)
    },

    {
      width: 200,
      prop: 'unrealizedRevenueBalance',
      label: '未实现收益-总',
      render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance)
    },
    {
      width: 200,
      prop: 'leaseRevenueBalance',
      label: '应收融资租赁款余额',
      render: ({ leaseRevenueBalance }) => toThousands(leaseRevenueBalance)
    },

    {
      width: 200,
      prop: 'depreciationReservesBalance',
      label: '减值准备余额',
      render: ({ depreciationReservesBalance }) => toThousands(depreciationReservesBalance)
    },

    {
      width: 200,
      prop: 'receivableNetBalance',
      label: '应收融资租赁款净值',
      render: ({ receivableNetBalance }) => toThousands(receivableNetBalance)
    },

    {
      width: 200,
      prop: 'lesseeMarginBalance',
      label: '承租人保证金',
      render: ({ lesseeMarginBalance }) => toThousands(lesseeMarginBalance)
    },
    {
      width: 200,
      prop: 'taAmount',
      label: 'TA重分类',
      render: ({ taAmount }) => toThousands(taAmount)
    },
    {
      width: 200,
      prop: 'overdueEarnings',
      label: '逾期收益',
      render: ({ overdueEarnings }) => toThousands(overdueEarnings)
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
