import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '重分类明细详情',
  icon: '',
  name: 'TaRearrangeDetail'
}

export const optionsConfig = (router, dictData = {}) => ({
  span: 8,
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: true,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/ta-reclassification-detail/page',
      method: 'post'
    }
  },
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  btnConfig: {
    export: {
      title: '下载明细表',
      url: '/engine/finance/ta-reclassification-detail/exportDetail',
      filename: '明细表.xlsx',
      isDisabled: false,
      isParams: true
    },
    // export1: {
    //   title: '下载统计表',
    //   url: '/engine/finance/ta-reclassification-detail/exportSummary',
    //   filename: '统计表.xlsx',
    //   isDisabled: false,
    //   isParams: true
    // }
  },
  columns: [
    {
      label: '重分类月份',
      prop: 'reclassificationMonth',
      type: 'date',
      attrs: {
        format: 'YYYY-MM'
      },
      width: 200
    },
    {
      label: '业务系统',
      prop: 'systemCodeList',
      width: 200,
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source')
    },
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      render: ({ systemCode }) => {
        return dictMappingLabel(dictData, 'sys_form_source', systemCode)
      }
    },
    {
      label: '合同签约主体',
      prop: 'orgIdList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      label: '合同签约主体',
      prop: 'orgId',
      width: 200,
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '合同编号', prop: 'contractCode', width: 200 },
    {
      label: '租赁大类',
      prop: 'businessCode',
      width: 200,
      option: dictMappingToArray(dictData, 'contract_business_type'),
      render: ({ businessCode }) => {
        return dictMappingLabel(dictData, 'contract_business_type', businessCode)
      }
    },
    {
      label: '合同状态',
      prop: 'contractStatus',
      width: 200,
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render: (row) => {
        return (
          (row.contractStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'business_contract_status', row.contractStatus)
            )) ||
          ''
        )
      }
    },
    {
      label: '网银到账主体',
      prop: 'bankOrgIdList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '客户名称', prop: 'clientName', width: 200 },
    { label: '网银到账主体', prop: 'bankOrgName', width: 200 },
    { label: '业务系统网银编号/批次号', prop: 'ebankSerialNumber', width: 200 },
    { label: '业务系统批扣流水号', prop: 'ebankBatchNo', width: 200 },
    { label: '网银付款人名称', prop: 'ebankClientName', width: 200 },
    { label: '运营部备注', prop: 'operationRemark', width: 200 },
    {
      label: 'TA/溢存款余额',
      prop: 'taExcessBalance',
      width: 200,
      render: ({ taExcessBalance }) => toThousands(taExcessBalance)
    },
    {
      label: 'TA重分类金额',
      prop: 'taReclassificationAmountStr',
      search: true,
      hide: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'ta_amount')
    },
    {
      label: 'TA重分类金额',
      prop: 'taReclassificationAmount',
      width: 200,
      render: ({ taReclassificationAmount }) => toThousands(taReclassificationAmount)
    },
    {
      prop: 'exceptionType',
      label: '异常类型',
      type: 'select',
      option: dictMappingToArray(dictData, 'ta_except_type'),
      render: (row) => {
        return (
          (row.exceptionType &&
            h(ElTag, () => dictMappingLabel(dictData, 'ta_except_type', row.exceptionType))) ||
          ''
        )
      },
      width: 200,
      search: true
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
