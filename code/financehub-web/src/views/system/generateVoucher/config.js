import { h, markRaw } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { DArrowRight } from '@element-plus/icons-vue'

export default {
  hidden: false,
  title: '规则测试',
  icon: '',
  name: 'generateVoucher'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: false, // 是否需要翻页
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
      url: '/engine/rule/generateVoucher',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'voucherNum',
      label: '凭证号',
      search: true,
      width: 200,
      fixed: 'left'
    },
    {
      prop: 'voucherType',
      label: '凭证类型',
      search: true,
      width: 200,
      option: dictMappingToArray(dictData, 'sys_voucher_type'),
      render (row) {
        return (row.voucherType && h(ElTag, () => dictMappingLabel(dictData, 'sys_voucher_type', row.voucherType))) || ''
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      minWidth: 250,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      type: 'date',
      cover: ['businessDateStart', 'businessDateEnd'],
      width: 200,
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'voucherDate',
      label: '记账日期',
      search: true,
      type: 'date',
      cover: ['voucherDateStart', 'voucherDateEnd'],
      width: 200,
      attrs: {
        type: 'daterange',
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    }, {
      prop: 'sceneName',
      label: '业务场景',
      search: true,
      width: 200
    }, {
      prop: 'accountCode',
      label: '科目代码',
      width: 200
    }, {
      prop: 'accountName',
      label: '科目名称',
      width: 200
    },

    {
      prop: 'currency',
      label: '币种',
      type: 'select',
      width: 200
      // option: dictMappingToArray(dictData, 'sys_currency_type'),
      // rules: [{
      //   required: true,
      //   message: '请选择币种',
      //   trigger: ['change']
      // }],
      // render (row) {
      //   return (row.invoiceType && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currency))) || ''
      // }
    },
    {
      prop: 'debitAmount',
      label: '借方发生额',
      width: 200,
      render (row) {
        return toThousands(row.debitAmount)
      }
    }, {
      prop: 'creditAmount',
      label: '贷方发生额',
      width: 200,
      render (row) {
        return toThousands(row.creditAmount)
      }
    }, {
      prop: 'voucherSummary',
      label: '凭证头摘要',
      width: 200
    }, {
      prop: 'voucherEntrySummary',
      label: '凭证行摘要',
      width: 200,
      icon: markRaw(DArrowRight),
      tips: '点击【展示/隐藏】项',
      hideColumns: ['clientName', 'contractCode', 'bankAccount']
    }, {
      prop: 'clientName',
      label: '客户名称',
      search: true,
      hide: false,
      width: 300
    }, {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      hide: false,
      width: 200
    }, {
      prop: 'bankAccount',
      label: '银行账号',
      hide: false,
      search: true,
      width: 200
    }, {
      prop: 'createUserName',
      label: '制单人',
      search: true,
      width: 200
    }, {
      prop: 'recheckUserName',
      label: '复核人',
      search: true,
      width: 200
    }, {
      prop: 'voucherStatus',
      label: '处理状态',
      width: 100,
      search: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status'),
      render (row) {
        return (row.voucherStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.voucherStatus))) || ''
      }
    }]
})
