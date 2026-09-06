import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '实付保险费对账',
  icon: '',
  name: 'OutOfPocketPremiums'
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
      label: '系统来源',
      prop: 'systemCode',
      width: 200,
      type: 'select',
      search: true,
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render ({ systemCode }) {
        return (systemCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', systemCode))) || ''
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      display: false,
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
    { // 展示数据
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      width: 200,
      hide: true,
      attrs: {
        multiple: true
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
      label: '统一平台',
      prop: 'system',
      align: 'center',
      children: [
        { label: '保险费实际支付(不含税)', prop: '', width: 200 },
        { label: '起租时点保险费金额(不含税)', prop: '', width: 250 },
        { label: '保险费剩余支付金额', prop: '', width: 200 }
      ]
    },
    {
      label: '小微',
      prop: 'xiaowei',
      align: 'center',
      children: [
        { label: '保险费实际支付(不含税)', prop: '', width: 200 },
        { label: '起租时点保险费金额(不含税)', prop: '', width: 250 },
        { label: '保险费剩余支付金额', prop: '', width: 200 }
      ]
    },
    {
      label: '财务中台',
      prop: 'platform',
      align: 'center',
      children: [
        { label: '业务合同状态', prop: '', width: 200 },
        { label: '财务合同状态', prop: '', width: 200 },
        { label: '应付保险费-暂估期初余额', prop: '', width: 200 },
        { label: '应付保险费-暂估借方', prop: '', width: 200 },
        { label: '应付保险费-暂估贷方', prop: '', width: 200 },
        { label: '应付保险费-暂估期末余额', prop: '', width: 200 },
        { label: '应付保险费-暂估余额', prop: '', width: 200 },
        { label: '客户名称', prop: '', width: 200 },
        { label: '记账日期', prop: '', type: 'date', width: 200 },
        { label: '结转金额', prop: '', width: 200 },
        { label: '保险费支付报表余额', prop: '', width: 200 },
        { label: '保险费实际支付(不含税)', prop: '', width: 200 },
        { label: '起租时点保险费金额(不含税)', prop: '', width: 250 },
        { label: '保险费交易结构调整(不含税)', prop: '', width: 250 },
        { label: '合同撤销(不含税)', prop: '', width: 200 },
        { label: '保险费实际支付(不含税)核对状态', prop: '', width: 250 },
        { label: '起租时点保险费金额(不含税)核对状态', prop: '', width: 300 },
        { label: '保险费剩余支付金额核对状态', prop: '', width: 250 },
        { label: '保险费实际支付(不含税)差异金额', prop: '', width: 250 },
        { label: '起租时点保险费金额(不含税)差异金额', prop: '', width: 300 },
        { label: '保险费剩余支付金额差异金额', prop: '', width: 250 }
      ]
    }
  ]
})
