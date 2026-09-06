export default {
  hidden: true,
  title: '第三方转让详情',
  icon: '',
  name: 'thirdTransmitionDetail'
}

import { toThousands, dictMappingLabel } from '@/utils'
import { ElTag } from 'element-plus'
import { h } from 'vue'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'convertTransferId', // 表格唯一id
  span: 6, // 查询区域每一个表单要展示的宽度
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
      url: '/engine/finance/convert-transfer-third-detail/page',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-third-detail/export',
      filename: '转让合同费用详情.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    { label: '转让批次', prop: 'batch', minWidth: 120 },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 140
    },
    {
      label: '客户名称',
      prop: 'clientName',
      minWidth: 140,
      tooltip: true
    },
    {
      label: '签约主体',
      prop: 'orgId',
      minWidth: 140,
      tooltip: true,
      render({ orgId }) {
        return dictMappingLabel(dictData, 'company', orgId)
      }
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      minWidth: 130,
      render({ financialContractStatus }) {
        return (financialContractStatus && h(ElTag, () => financialContractStatus)) || ''
      }
    },
    {
      prop: 'receivableRent',
      label: '应收租金',
      minWidth: 130,
      render: ({ receivableRent }) => toThousands(receivableRent)
    },
    {
      prop: 'receivableResidualValue',
      label: '应收期末残值',
      minWidth: 130,
      render: ({ receivableResidualValue }) => toThousands(receivableResidualValue)
    },
    {
      prop: 'receivableOuttax',
      label: '应收销项税',
      minWidth: 130,
      render: ({ receivableOuttax }) => toThousands(receivableOuttax)
    },
    {
      prop: 'unrealizedRevenue',
      label: '未实现融资租赁收益',
      minWidth: 160,
      render: ({ unrealizedRevenue }) => toThousands(unrealizedRevenue)
    },
    {
      prop: 'lesseeMargin',
      label: '承租人保证金',
      minWidth: 130,
      render: ({ lesseeMargin }) => toThousands(lesseeMargin)
    },
    {
      prop: 'depreciationReserves',
      label: '应收租赁款组合拨备',
      minWidth: 160,
      render: ({ depreciationReserves }) => toThousands(depreciationReserves)
    },
    {
      prop: 'appraisedValue',
      label: '评估价',
      minWidth: 140,
      render: ({ appraisedValue }) => toThousands(appraisedValue)
    },
    {
      prop: 'transferOpen',
      label: '转让时敞口',
      minWidth: 140,
      render: ({ transferOpen }) => toThousands(transferOpen)
    },
    {
      prop: 'supplementaryProvision',
      label: '补提拨备',
      minWidth: 140,
      render: ({ supplementaryProvision }) => toThousands(supplementaryProvision)
    },
    {
      prop: 'baseDateAccruedIncome',
      label: '基准日后计提收益',
      minWidth: 140,
      render: ({ baseDateAccruedIncome }) => toThousands(baseDateAccruedIncome)
    },
    {
      prop: 'baseDateProvision',
      label: '基准日后计提拨备',
      minWidth: 140,
      render: ({ baseDateProvision }) => toThousands(baseDateProvision)
    },
    {
      prop: 'baseDateReceive',
      label: '基准日后收款',
      minWidth: 140,
      render: ({ baseDateReceive }) => toThousands(baseDateReceive)
    },
    {
      prop: 'baseDateInvoices',
      label: '基准日后开票',
      minWidth: 140,
      render: ({ baseDateInvoices }) => toThousands(baseDateInvoices)
    },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
