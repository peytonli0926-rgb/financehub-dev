
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { h } from 'vue'
import { ElTag } from 'element-plus'
export default {
  hidden: true,
  title: '邮储手续费收入详情',
  icon: '',
  name: 'postalChargesIncomeDetail'
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
  span: 8,
  addBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  request: { // 请求参数
    list: {
      url: '/engine/finance/postal-storage-fee/detail/page',
      method: 'post'
    },
    edit: {
      url: '/engine/finance/postal-storage-fee/detail/update',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      type: 'date',
      width: 150,
      display: false,
      // cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'date',
        // type: 'monthrange',
        // clearable: false,
        // 'range-separator': '-',
        // 'start-placeholder': '开始月份',
        // 'end-placeholder': '结束月份',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      }
    },
    {
      prop: 'accountDate',
      label: '财务日期',
      search: true,
      width: 150,
      display: false,
      type: 'date',
      // cover: ['startAccountDate', 'endAccountDate'],
      attrs: {
        type: 'date',
        // type: 'monthrange',
        // clearable: false,
        // 'range-separator': '-',
        // 'start-placeholder': '开始月份',
        // 'end-placeholder': '结束月份',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      minWidth: 250,
      display: false,
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      width: 150,
      display: false
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      type: 'select',
      display: false,
      option: dictMappingToArray(dictData, 'business_contract_status'),
      render (row) {
        return (row.contractStatus && h(ElTag, () => dictMappingLabel(dictData, 'business_contract_status', row.contractStatus))) || ''
      },
      width: 150
    },
    {
      prop: 'leaseDateStart',
      label: '合同起租日期',
      width: 150,
      display: false
    },
    {
      prop: 'leaseTerm',
      label: '租赁期限',
      width: 150,
      display: false
    },
    {
      prop: 'payableProcedureCost',
      label: '支付手续费金额',
      width: 150,
      display: false,
      render (row) {
        return toThousands(row.payableProcedureCost)
      }
    },

    {
      prop: 'allocationAmount',
      label: '当期分摊金额',
      width: 150,
      render (row) {
        return toThousands(row.allocationAmount)
      }
    },
    {
      prop: 'allocatedPeriods',
      label: '已分摊期数',
      width: 150,
      display: false
    },
    {
      prop: 'postalStorageProjectType',
      label: '邮储项目类型',
      width: 150,
      type: 'select',
      option: dictMappingToArray(dictData, 'postal_savings_project_type'),
      render ({ postalStorageProjectType }) {
        return dictMappingLabel(dictData, 'postal_savings_project_type', postalStorageProjectType)
      }
    },
    {
      prop: 'allocationBalance',
      label: '分摊余额',
      width: 150,
      display: false,
      render (row) {
        return toThousands(row.allocationBalance)
      }
    }, {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false

    }]
})
