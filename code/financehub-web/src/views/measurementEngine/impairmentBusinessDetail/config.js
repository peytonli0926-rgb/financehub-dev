import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '减值计提详情',
  icon: '',
  name: 'impairmentBusinessDetail',
  sort: 4
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
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
    isShow: false
  },
  request: { // 请求参数
    list: {
      url: '/engine/finance/impairment-provision-detail/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      getKey: 'id'
    },
    export: {
      title: '导出',
      type: 'primary',
      fetchKey: 'idList',
      isParams: true,
      url: '/engine/finance/impairment-provision-detail/export',
      filename: '减值计提详情.xlsx'
    }
  },
  columns: [

    { label: '核算项目', alias: '核算编号', search: true, prop: 'contractCode', width: 200 },
    { label: '合同名称', prop: 'contractName', width: 200 },
    { label: '客户编号', prop: 'clientCode', width: 200 },
    { label: '客户名称', prop: 'clientName', width: 200 },
    {
      prop: 'impairmentType',
      label: '减值类型',
      width: 200,
      type: 'select',
      option: dictMappingToArray(dictData, 'provision_type')
    },
    { label: '业务类型', prop: 'businessType', search: true, width: 200 },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      minWidth: 300,
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '五级分类', prop: 'fiveClass', search: true, width: 200, type: 'select', option: dictMappingToArray(dictData, 'three_phases') },
    { label: '三阶段', prop: 'threeStep', search: true, width: 200, type: 'select', option: dictMappingToArray(dictData, 'five_level_classification') },
    { label: '风险敞口', prop: 'riskExposure', width: 200, render: ({ riskExposure }) => toThousands(riskExposure) },
    { label: '拨备合计', prop: 'provisionTotal', width: 200, render: ({ provisionTotal }) => toThousands(provisionTotal) },
    { label: '上月余额', prop: 'lastMonthBalance', width: 200, render: ({ lastMonthBalance }) => toThousands(lastMonthBalance) },
    { label: '本月计提', prop: 'thisMonthProvision', width: 200, render: ({ thisMonthProvision }) => toThousands(thisMonthProvision) },

    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      search: true,
      width: 200,
      type: 'select',
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status'),
      render (row) {
        return (row.financialContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus))) || ''
      }
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      search: true,
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 200
    },

    { label: '是否核销', prop: 'isVerification', width: 200 },
    { label: '应收租金余额', prop: 'rentReceivableBalance', width: 200, render: ({ rentReceivableBalance }) => toThousands(rentReceivableBalance) },
    { label: '中台应收租金余额', prop: 'hubRentReceivableBalance', width: 200, render: ({ hubRentReceivableBalance }) => toThousands(hubRentReceivableBalance) },

    {
      prop: 'operation',
      width: 150,
      fixed: 'right',
      label: '操作',
      display: false

    }]
})
