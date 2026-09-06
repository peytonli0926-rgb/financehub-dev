import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '特殊合同状态详情',
  icon: '',
  name: 'specialContractStatusDetail'
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
  request: { // 请求参数
    list: {
      url: '/engine/finance/contract-status-record/detail',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'recordStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      isDisabled: false,
      url: '/engine/finance/contract-status-record/detail/export',
      filename: '特殊合同状态详情.xlsx',
      routeQuery: ['contractCode', 'orgId'],
      isParams: true
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,

      rules: [{
        required: true,
        message: '请输入合同编码',
        trigger: 'change'
      }],
      width: 200
    },
    {
      prop: 'clientName',
      label: '客户名称',
      display: false,
      tooltip: true,
      width: 200
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      minWidth: 250,
      tooltip: true,
      rules: [{
        required: true,
        message: '请选择签约主体',
        trigger: 'change'
      }],
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'financialContractStatusUpdateTime',
      label: '财务合同状态更新时间',
      alias: '日期',
      search: true,
      width: 200,
      type: 'date',
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'financialContractStatusList',
      label: '财务合同状态',
      type: 'select',
      search: true,
      hide: true,
      display: false,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      width: 120,
      option: dictMappingToArray(dictData, 'financial_contract_status')
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      rules: [{
        required: true,
        message: '请选择财务合同状态',
        trigger: 'change'
      }],
      width: 120,
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
      label: '业务合同状态',
      type: 'select',
      option: dictMappingToArray(dictData, 'business_contract_status'),
      display: false,

      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 200
    },

    {
      prop: 'transferOrgId',
      label: '转入公司(针对内部转让)',
      display: false,
      width: 200
    }, {
      prop: 'transferContractCode',
      label: '转入合同号(针对内部转让)',
      display: false,
      width: 200

    }, {
      prop: 'transferContractStatus',
      label: '转入合同系统合同状态(针对内部转让)',
      type: 'select',

      option: dictMappingToArray(dictData, 'business_contract_status'),
      display: false,
      render (row) {
        return (row.transferContractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.transferContractStatus))) || ''
      },
      width: 170
    }, /* {
      prop: 'operator',
      label: '操作人',
      display: false,
      width: 100
    }, */
    {
      prop: 'recordStatus',
      label: '处理状态',
      display: false,
      width: 100,
      type: 'select',
      attrs: {
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        multiple: true
      },
      option: dictMappingToArray(dictData, 'process_status'),
      render (row) {
        return (row.recordStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.recordStatus))) || ''
      }
    }]
})
