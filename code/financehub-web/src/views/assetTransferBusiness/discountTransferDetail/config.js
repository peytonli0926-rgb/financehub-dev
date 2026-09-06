export default {
  hidden: true,
  title: '折价转让详情',
  icon: '',
  name: 'discountTransferDetail'
}

import { h } from 'vue'
import { ElTag, ElButton } from 'element-plus'
import { changeCellValue } from '@/utils/format'

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
      url: '/engine/finance/convert-transfer-detail/page',
      method: 'post'
    }
  },
  columns: [
    { label: '转让批次', prop: 'batch', minWidth: 140 },
    {
      label: '原合同号',
      prop: 'contractCode',
      minWidth: 140,
      render: ({ contractCode, convertTransferId }) => {
        return h(
          ElButton,
          {
            'v-permission': 'discountDetail_btn_redirect',
            type: 'primary',
            link: true,
            onClick: () => {
              router.push({
                path: '/assetTransferBusiness/discountRentPlan',
                query: {
                  convertTransferId,
                  oldContractCode: contractCode
                }
              })
            }
          },
          () => contractCode
        )
      }
    },
    {
      label: '客户名称',
      prop: 'clientName',
      minWidth: 150,
      tooltip: true
    },
    {
      label: '签约主体',
      prop: 'orgId',
      minWidth: 150,
      tooltip: true,
      format: 'dict',
      dictKey: 'company',
      render: (row, item) => {
        return changeCellValue(row, item, dictData)
      }
    },
    {
      label: '财务合同状态',
      prop: 'financialContractStatus',
      minWidth: 150,
      format: 'dictTag',
      render: ({ financialContractStatus }) => {
        return (financialContractStatus && h(ElTag, () => financialContractStatus)) || ''
      }
    },
    { label: '税率', prop: 'taxRate', minWidth: 120 },
    {
      label: '应收租金',
      prop: 'receivableRent',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收期末残值',
      prop: 'receivableResidualValue',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收销项税',
      prop: 'receivableOuttax',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '未实现融资租赁收益',
      prop: 'unrealizedRevenue',
      minWidth: 170,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '承租人保证金',
      prop: 'lesseeMargin',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应收租赁款组合拨备',
      prop: 'depreciationReserves',
      minWidth: 170,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '评估价',
      prop: 'appraisedValue',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '转让时敞口',
      prop: 'transferOpen',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '补提拨备',
      prop: 'supplementaryProvision',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '收益确认',
      prop: 'revenueRecognition',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付经销商服务费-暂估',
      prop: 'payableAgencyEstimate',
      minWidth: 180,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付收车费-暂估',
      prop: 'payableVehicleEstimate',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付手环成本_暂估',
      prop: 'payableBandCostEstimate',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付抵押费_暂估',
      prop: 'payablePledgeEstimate',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付解抵押费_暂估',
      prop: 'payableUnpledgeEstimate',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '应付其他租赁成本-暂估',
      prop: 'payableOtherCostEstimate',
      minWidth: 180,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '基准日后计提收益',
      prop: 'baseDateAccruedIncome',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '基准日后计提拨备',
      prop: 'baseDateProvision',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    {
      label: '基准日后收款',
      prop: 'baseDateReceive',
      minWidth: 150,
      format: 'money',
      render: (row, item) => {
        return changeCellValue(row, item)
      }
    },
    // { label: '支付日期', prop: 'accountDate', minWidth: 170 },
    // { label: '银行账号', prop: '', minWidth: 150, tooltip: true },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
