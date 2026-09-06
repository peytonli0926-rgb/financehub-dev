import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '财财核对-科目与金蝶余额对账',
  icon: '',
  name: 'subjectKingdee'
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
      url: '/engine/finance/check-account-detail-record/resultKingdeePage',
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
        filterable: true,
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
     
      type: 'select',
      attrs: {
        filterable: true,
        // multiple: true,
        // 'collapse-tags': true,
        // 'collapse-tags-tooltip': true
      },
      request: { url: '/engine/scene/account/listAll', method: 'post' },
      keyValue: { label: 'accountName', value: 'accountName' },
      width: 200
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
      prop: 'diffFlag',
      label: '差异类型',
      search: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'diff_flag'),
      render ({ diffFlag }) {
        return (diffFlag && h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', diffFlag))) || ''
      }
    },
    {
      prop: 'accountRemainBalance',
      label: '中台科目余额',
      width: 200,
      render: ({ accountRemainBalance }) => toThousands(accountRemainBalance)
    },
    {
      prop: 'kingdeeRemainBalance',
      label: '金蝶科目余额',
      width: 200,
      render: ({ kingdeeRemainBalance }) => toThousands(kingdeeRemainBalance)
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
