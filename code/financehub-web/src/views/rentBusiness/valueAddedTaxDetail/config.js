import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '增值税-应交增值税对账详情',
  icon: '',
  name: 'valueAddedTaxDetail'
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
  isSearch: false,
  request: { // 请求参数
    list: {
      url: '/etl/financial/invoice-claim/selectContractInvoiceByPage',
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
  columns: [
    { label: '合同编号', prop: 'contractCode', width: 200 },
    { label: '合同名称', prop: 'contractName', width: 200 },
    {
      label: '租赁类型',
      prop: 'leaseType',
      width: 200,
      render ({ leaseType }) {
        return (leaseType && h(ElTag, () => dictMappingLabel(dictData, 'lease_type', leaseType))) || ''
      }
    },
    { label: '开票/计提税率', prop: 'backTaxRate', width: 200 },
    { label: '开票/计提项目', prop: 'productName', width: 200 },
    {
      label: '提前开票类型',
      prop: 'invoiceType',
      width: 200,
      render ({ invoiceType }) {
        return (invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'invoice_type', invoiceType))) || ''
      }
    },
    { label: '对应期数', prop: 'periodNum', width: 200 },
    {
      label: '应开票主体',
      prop: 'orgIdName',
      width: 200
      // render ({ sellerTaxCode }) {
      //   return dictMappingLabel(dictData, 'company', sellerTaxCode)
      // }
    },
    { label: '应开票对象', prop: 'clientName', width: 200 },
    { label: '应收日期', prop: 'planRepayDate', type: 'date', width: 200 },
    { label: '收款日期', prop: 'planDate', type: 'date', width: 200 },
    { label: '应收本金', prop: 'principalAmount', width: 200, render: ({ principalAmount }) => toThousands(principalAmount) },
    { label: '应收利息', prop: 'interestAmount', width: 200, render: ({ interestAmount }) => toThousands(interestAmount) },
    { label: '应收租金/其他款项', prop: 'rentReceivableAmount', width: 200 },
    { label: '应开票/计提金额', prop: 'backTaxAmount', width: 200, render: ({ backTaxAmount }) => toThousands(backTaxAmount) },
    { label: '应开票/计提税额', prop: 'backTaxValue', width: 200, render: ({ backTaxValue }) => toThousands(backTaxValue) },
    {
      label: '实际开票主体',
      prop: 'sellerTaxCode',
      width: 200,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.sellerTaxCode)
      }
    },
    { label: '实际开票对象', prop: 'payer', width: 200 },
    { label: '实际开票/计提金额', prop: 'taxAmount', width: 200, render: ({ taxAmount }) => toThousands(taxAmount) },
    { label: '实际开票/计提税率', prop: 'taxRate', width: 200 },
    { label: '实际开票/计提税额', prop: 'taxValue', width: 200, render: ({ taxValue }) => toThousands(taxValue) },
    { label: '开票/计提日期', prop: 'documentDate', type: 'date', width: 200 },
    { label: '发票号码', prop: 'invoiceNumber', width: 200 },
    { label: '异常类型', prop: 'exceptionType', width: 200 },
    { label: '备注', prop: 'comments', width: 200 },

    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false

    }
  ]
})
