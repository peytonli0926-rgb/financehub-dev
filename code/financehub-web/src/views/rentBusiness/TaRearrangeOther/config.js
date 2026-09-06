// import { h } from 'vue'
// import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '重分类其他应付款详情',
  icon: '',
  name: 'TaRearrangeOther'
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/ta-other-payable-detail/page',
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
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      isParams: true,
      isDisabled: false,
      url: '/engine/finance/ta-other-payable/export',
      fetchKey: 'idList',
      filename: 'ta其他应付款.xlsx'
    }
  },
  columns: [
    {
      label: '业务系统',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      hide: true,
      search: true,
      option: dictMappingToArray(dictData, 'sys_form_source')
    },
    {
      label: '重分类月份',
      prop: 'reclassificationMonthStr',
      width: 200,
      type: 'date',
      attrs: {
        format: 'YYYY-MM'
      }
    },
    {
      label: '网银到账主体',
      prop: 'bankOrgName',
      width: 200
      // render: (row) => {
      //   return dictMappingLabel(dictData, 'company', row.bankOrgId)
      // }
    },
    { label: '业务系统', prop: 'systemName', width: 200 },
    { label: '业务系统网银编号/批次号', search: true, prop: 'ebankSerialNumber', width: 200 },
    { label: '业务系统批扣流水号', search: true, prop: 'ebankBatchNo', width: 200 },
    { label: '财务部初分类', prop: 'financialPrimaryClassic', width: 200 },
    { label: '运营部确认款项性质', prop: 'confirmAccountProperty', width: 200 },
    {
      label: '重分类金额',
      prop: 'reclassificationAmount',
      width: 200,
      render: ({ reclassificationAmount }) => toThousands(reclassificationAmount)
    },
    { label: '重分类科目', prop: 'accountCode', width: 200 },
    { label: '入账日期', prop: 'accountDate', width: 200, type: 'date' },
    { label: '账龄', prop: 'accountAge', width: 200 },
    {
      label: '账龄分类',
      prop: 'accountAgeClass',
      width: 200,
      search: true,
      type: 'select',
      option: []
    },
    { label: '付款客户', prop: 'payClientName', width: 200 },

    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
