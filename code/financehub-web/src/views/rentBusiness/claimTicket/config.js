import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '开票认领',
  icon: '',
  name: 'claimTicket'
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
  // span:8,
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
      url: '/etl/financial/invoice-claim/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },

    export: {
      title: '导出',
      isDisabled: false,
      isParams: true,
      url: '/etl/financial/invoice-claim/export',
      filename: '开票认领.xlsx'
    },
    dataAsync: {
      title: '数据同步',
      type: 'success',
      isDisabled: false,
      isParams: true,
      url: '/etl/financial/invoice-claim/syncInvoicingSystem'
    }
  },
  columns: [
    { label: '单据编号', prop: 'documentNum', width: 150 },
    {
      label: '业务来源',
      search: true,
      prop: 'businessSource',
      width: 240,
      type: 'select',
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'invoice_business_source'),
      render (row) {
        return (row.businessSource && h(ElTag, () => dictMappingLabel(dictData, 'invoice_business_source', row.businessSource))) || ''
      }
    },
    { label: '销方税号', prop: 'sellerTaxCode', width: 240 },
    { label: '销方名称', search: true, prop: 'sellerName', width: 200 },
    {
      prop: 'invoiceType',
      label: '发票类型',
      type: 'select',
      width: 100,
      search: true,
      option: dictMappingToArray(dictData, 'invoice_claim_type'),
      rules: [{
        required: true,
        message: '请选择发票类型',
        trigger: ['change']
      }],
      render (row) {
        return (row.invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'invoice_claim_type', row.invoiceType))) || ''
      }
    },
    // { label: '付款人', prop: 'payer', width: 200 },
    // { label: '客户行业', prop: 'clientIndustry', width: 100 },
    {
      label: '单据日期',
      search: true,
      prop: 'documentDate',
      width: 200,
      type: 'date',
      cover: ['startDocumentDate', 'endDocumentDate'],
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    { label: '商品名称', prop: 'productName', width: 200 },
    { label: '价税合计（含税金额）', prop: 'taxAmount', width: 200, render: (row) => toThousands(row.taxAmount) },
    { label: '税率(%)', alias: '税率', search: true, prop: 'taxRate', width: 100, type: 'inputNumber' },
    { label: '金额 (不含税)', prop: 'noTaxAmount', width: 200, render: (row) => toThousands(row.noTaxAmount) },
    { label: '税额', prop: 'taxValue', width: 200, render: (row) => toThousands(row.taxValue) },
    { label: '购方识别号(税号)', prop: 'purchaserTaxCode', width: 200 },
    { label: '购方名称', search: true, prop: 'purchaserName', width: 200 },
    { label: '备注', prop: 'comments', width: 200 },

    { label: '合同编号', prop: 'contractCode', width: 200 },

    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 200
    },

    { label: '期数', prop: 'periodNum', width: 100 },

    {
      label: '状态',
      prop: 'processStatus',
      width: 100,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'claim_status'),
      render ({ processStatus }) {
        return (processStatus && h(ElTag, () => dictMappingLabel(dictData, 'claim_status', processStatus))) || ''
      }
    },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false

    }]
})
