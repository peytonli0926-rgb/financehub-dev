import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '业财核对-科目明细对账表',
  icon: '',
  name: 'subjectDeatil'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8, // 查询区域每一个表单要展示的宽度
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
      url: '/engine/finance/check-account-detail-record/resultDetailPage',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      type: 'select',
      display: false,
      isDefaultvalue: true,
      attrs: {
        filterable: true
      },
      width: 200,
      request: {
        // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    {
      prop: 'accountCode',
      label: '科目代码',
      search: true,
      type: 'select',
      attrs: {
        // filterable: true,
        // multiple: true,
        // 'collapse-tags': true,
        // 'collapse-tags-tooltip': true
      },
      request: { url: '/engine/scene/account/listAll', method: 'post' },
      keyValue: { label: 'accountCode', value: 'accountCode',formatLabel:['accountCode','accountName'] },
      width: 200
    },
    {
      prop: 'accountName',
      label: '科目名称',
      // search: true,
      type: 'select',
      attrs: {
        // filterable: true,
        // multiple: true,
        // 'collapse-tags': true,
        // 'collapse-tags-tooltip': true
      },
      request: { url: '/engine/scene/account/listAll', method: 'post' },
      keyValue: { label: 'accountName', value: 'accountName' },
      width: 200
    },
    { prop: 'clientCode', label: '客户代码', width: 200 },
    { prop: 'clientName', search: true, label: '客户名称', width: 200 },
    // { prop: 'contractCode', label: '合同编号', width: 200 },
    { // 展示数据
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      width: 200,
      attrs: {
        // multiple:true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post', params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      minWidth: 300,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'currencyType',
      label: '币种',
      search: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render (row) {
        return (row.currencyType && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) || ''
      }
    },
    {
      prop: 'accountRemainBalance',
      label: '科目余额',
      width: 200,
      render: ({ accountRemainBalance }) => toThousands(accountRemainBalance)
    },
    {
      prop: 'diffBalance',
      label: '差异金额',
      width: 200,
      render: ({ diffBalance }) => toThousands(diffBalance)
    },
    {
      prop: 'detailRemainBalance',
      label: '明细余额',
      width: 200,
      render: ({ detailRemainBalance }) => toThousands(detailRemainBalance)
    },
    {
      prop: 'diffFlagDesc',
      label: '差异状态描述',
      width: 200
      // render: ({ unrealizedRevenueBalance }) => toThousands(unrealizedRevenueBalance)
    }
  ]
})
