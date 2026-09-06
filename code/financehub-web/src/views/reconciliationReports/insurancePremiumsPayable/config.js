export default {
  hidden: false,
  title: '业业核对-应付保险费',
  icon: '',
  name: 'insurancePremiumsPayable'
}

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  span: 8, // 查询区域每一个表单要展示的宽度
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  }
}

import { h } from 'vue'
import { ElTag } from 'element-plus'
import moment from 'moment'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    list: {
      url: '/engine/finance/check-account-detail-record/yfbxf/resultPage',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/check-account-detail-record/yfbxf/export',
      filename: '应付保险费对账报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      label: '来源系统',
      prop: 'dbCode',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source')
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      search: true,
      hide: true,
      type: 'select-pagination',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'code',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      label: '签约主体',
      prop: 'orgId',
      type: 'select',
      tooltip: true,
      hide: true,
      display: false,
      search: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '异常情况',
      prop: 'exceptionFlag',
      search: true,
      minWidth: 130,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'check_account_exception_flag')
    },
    {
      label: '会计期间',
      prop: 'periodCode',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true
      },
      minWidth: 200,
      request: {
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    {
      label: '业务合同状态',
      prop: 'contractStatus',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'business_contract_status')
    },
    {
      label: '财务合同状态',
      prop: 'financialContractStatus',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status')
    },
    {
      prop: 'basicInfo',
      label: '基本信息',
      align: 'center',
      children: [
        {
          label: '来源系统',
          prop: 'dbCode',
          minWidth: 140,
          render({ dbCode }) {
            return (
              (dbCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', dbCode))) ||
              ''
            )
          }
        },
        {
          label: '签约主体',
          prop: 'orgId',
          minWidth: 150,
          tooltip: true,
          render(row) {
            return dictMappingLabel(dictData, 'company', row.orgId)
          }
        },
        {
          label: '合同编号',
          prop: 'contractCode',
          minWidth: 150
        },
        {
          label: '业务合同状态',
          prop: 'contractStatus',
          minWidth: 130,
          render({ contractStatus }) {
            return (contractStatus && h(ElTag, () => contractStatus)) || ''
          }
        },
        {
          label: '财务合同状态',
          prop: 'financialContractStatus',
          minWidth: 130,
          render({ financialContractStatus }) {
            return (financialContractStatus && h(ElTag, () => financialContractStatus)) || ''
          }
        },
        {
          label: '合同起租日',
          prop: 'leaseDateStart',
          minWidth: 120,
          render: ({ leaseDateStart }) => {
            return (leaseDateStart && moment(leaseDateStart).format('YYYY-MM-DD')) || ''
          }
        },
        {
          label: '合同到期日',
          prop: 'leaseDateEnd',
          minWidth: 120,
          render: ({ leaseDateEnd }) => {
            return (leaseDateEnd && moment(leaseDateEnd).format('YYYY-MM-DD')) || ''
          }
        }
      ]
    },
    {
      prop: 'businessSystem',
      label: '业务系统',
      align: 'center',
      children: [
        {
          label: '保险费实际支付(不含税)',
          prop: 'bizBalanceActual',
          minWidth: 180,
          render: ({ bizBalanceActual }) => toThousands(bizBalanceActual)
        },
        {
          label: '起租时点保险费金额(不含税)',
          prop: 'bizBalanceLease',
          minWidth: 210,
          render: ({ bizBalanceLease }) => toThousands(bizBalanceLease)
        },
        {
          label: '应付保险费-暂估期初余额',
          prop: 'estimateBalanceOpening',
          minWidth: 190,
          render: ({ estimateBalanceOpening }) => toThousands(estimateBalanceOpening)
        }
      ]
    },
    {
      prop: 'financialCenter',
      label: '财务中台',
      align: 'center',
      children: [
        {
          label: '应付保险费-暂估期末余额',
          prop: 'estimateBalanceEnding',
          minWidth: 190,
          render: ({ estimateBalanceEnding }) => toThousands(estimateBalanceEnding)
        },
        {
          label: '客户名称',
          prop: 'clientName',
          minWidth: 120,
          tooltip: true
        },
        {
          label: '记账日期',
          prop: 'accountDate',
          minWidth: 120,
          render: ({ accountDate }) => {
            return (accountDate && moment(accountDate).format('YYYY-MM-DD')) || ''
          }
        },
        {
          label: '结转金额',
          prop: 'carryoverAmount',
          minWidth: 140,
          render: ({ carryoverAmount }) => toThousands(carryoverAmount)
        },
        {
          label: '保险费交易结构调整(不含税)',
          prop: 'payableInsuranceBalanceStructure',
          minWidth: 210,
          render: ({ payableInsuranceBalanceStructure }) =>
            toThousands(payableInsuranceBalanceStructure)
        },
        {
          label: '合同撤销(不含税)',
          prop: 'payableInsuranceBalanceWithdrawal',
          minWidth: 150,
          render: ({ payableInsuranceBalanceWithdrawal }) =>
            toThousands(payableInsuranceBalanceWithdrawal)
        },
        {
          label: '保险费实际支付(不含税)',
          prop: 'balanceActual',
          minWidth: 180,
          render: ({ balanceActual }) => toThousands(balanceActual)
        },
        {
          label: '保险费实际支付差额(不含税)',
          prop: 'balanceActualDiff',
          minWidth: 210,
          render: ({ balanceActualDiff }) => toThousands(balanceActualDiff)
        },
        {
          label: '起租时点保险费金额(不含税)',
          prop: 'balanceLease',
          minWidth: 210,
          render: ({ balanceLease }) => toThousands(balanceLease)
        },
        {
          label: '起租时点保险费差额(不含税)',
          prop: 'balanceLeaseDiff',
          minWidth: 210,
          render: ({ balanceLeaseDiff }) => toThousands(balanceLeaseDiff)
        },
        {
          label: '应付保险费-暂估余额',
          prop: 'estimateBalance',
          minWidth: 170,
          render: ({ balanceLeaseDiff }) => toThousands(balanceLeaseDiff)
        },
        {
          label: '应付保险费暂估差额',
          prop: 'estimateBalanceDiff',
          minWidth: 170,
          render: ({ estimateBalanceDiff }) => toThousands(estimateBalanceDiff)
        },
        {
          label: '异常情况',
          prop: 'exceptionFlagStr',
          minWidth: 150,
          render({ exceptionFlagStr }) {
            return (exceptionFlagStr && h(ElTag, () => exceptionFlagStr)) || ''
          }
        }
      ]
    }
  ]
})
