import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '单合同查询',
  icon: '',
  name: 'contractBusiness'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8,
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract/page',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      isDisabled: false,
      isParams: true,
      type: 'primary',
      url: '/engine/finance/contract/export',
      filename: '单合同查询.xlsx'
    }
  },
  columns: [
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      width: 150,
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'orgIds',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,
      width: 190
    },
    {
      prop: 'contractName',
      label: '合同名称',
      tooltip: true,
      width: 180
    },
    {
      prop: 'clientName',
      label: '客户名称',
      search: true,
      tooltip: true,
      width: 150
    },
    {
      label: '系统来源',
      prop: 'systemCodeList',
      width: 200,
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        filterable: true
      },
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_form_source')
    },
    {
      label: '系统来源',
      prop: 'systemCode',
      width: 160,
      render ({ systemCode, systemCodeName }) {
        return (systemCode && h(ElTag, () => systemCodeName || dictMappingLabel(dictData, 'sys_form_source', systemCode))) || ''
      }
    },
    {
      prop: 'businessName',
      label: '业务类型',
      tooltip: true,
      width: 140
    },
    {
      prop: 'businessPlate',
      label: '业务板块',
      width: 110
    },
    {
      prop: 'leaseType',
      label: '租赁类型',
      width: 120
    },
    {
      prop: 'returnType',
      label: '还租方式',
      width: 110
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      width: 130
    },
    {
      prop: 'contractAmount',
      label: '合同金额（元）',
      width: 140,
      align: 'right',
      render ({ contractAmount }) {
        return Number(contractAmount || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
      }
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 150
    },
    {
      prop: 'contractStatuses',
      label: '合同状态',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'business_contract_status'),

      width: 150
    },
    {
      prop: 'businessDate',
      label: '业务日期',
      hide: true,
      type: 'date',
      width: 150,
      search: true,

      cover: ['businessDateStart', 'businessDateEnd'],
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
      prop: 'leaseStartDate',
      label: '起租日',
      type: 'date',
      cover: ['leaseDateStartDate', 'leaseDateStartEndDate'],
      width: 110,
      search: true,
      attrs: {
        type: 'daterange',
        clearable: true,
        format: 'YYYY-MM-DD',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'leaseDateEnd',
      label: '到期日',
      search: true,
      cover: ['leaseDateEndStartDate', 'leaseDateEndEndDate'],
      width: 110,
      type: 'date',
      attrs: {
        type: 'daterange',
        clearable: true,
        format: 'YYYY-MM-DD',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 100,
      slot: true,
      display: false,
      fixed: 'right'

    }

  ]

})
