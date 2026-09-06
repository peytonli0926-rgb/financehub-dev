import { h, markRaw } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { DArrowRight } from '@element-plus/icons-vue'

// 凭证查询首屏按“凭证标识 → 核算内容 → 金额 → 辅助核算 → 状态”展示。
// 查询专用字段仍保留在筛选区，低频信息通过表头的展示/隐藏功能按需查看。
const firstScreenOrder = [
  'voucherNum', 'voucherDate', 'sceneName', 'voucherEntrySummary',
  'accountCode', 'accountName', 'debitAmount', 'creditAmount',
  'clientName', 'contractCode', 'voucherStatus'
]
const firstScreenColumns = {
  voucherNum: { width: 64, align: 'center' },
  voucherDate: { width: 120, align: 'center' },
  sceneName: { width: 82, tooltip: true, align: 'center' },
  voucherEntrySummary: { minWidth: 190, width: 190, tooltip: true },
  accountCode: { width: 100, tooltip: true },
  accountName: { minWidth: 220, width: 220, tooltip: true },
  debitAmount: { width: 125, align: 'right' },
  creditAmount: { width: 125, align: 'right' },
  clientName: { width: 105, tooltip: true },
  contractCode: { width: 170, tooltip: true },
  voucherStatus: { width: 110, align: 'center' }
}
const defaultHiddenColumns = new Set([
  'orgId', 'periodCode', 'businessDate', 'voucherType', 'subSceneType',
  'voucherSummary', 'currency', 'taxRate', 'billContractCode', 'bankAccount',
  'createUserName', 'recheckUserName', ' isRelatedOtherCustomer',
  'easVoucherNumber', 'operation'
])

// 凭证状态采用财务中台统一中文口径，不依赖原引擎可能缺失的状态字典，
// 页面不得向业务用户回显 1、2、3 等内部代码。
const voucherProcessStatusOptions = () => [
  { value: '0', label: '未录入' },
  { value: '1', label: '已录入' },
  { value: '2', label: '已提交' },
  { value: '3', label: '待传送金蝶' },
  { value: '4', label: '已传至金蝶' },
  { value: '5', label: '复核失败' },
  { value: '6', label: '已冲销' }
]

const voucherProcessStatusLabel = (status) => {
  const item = voucherProcessStatusOptions().find(option => option.value === String(status))
  return item ? item.label : '未知状态'
}

const voucherProcessStatusType = (status) => ({
  0: 'info',
  1: 'warning',
  2: 'primary',
  3: 'warning',
  4: 'success',
  5: 'danger',
  6: 'info'
}[String(status)] || 'info')

const prioritizeVoucherColumns = (columns) => columns
  .map((column, originalIndex) => ({
    ...column,
    ...(firstScreenColumns[column.prop] || {}),
    ...(defaultHiddenColumns.has(column.prop) ? { hide: true } : {}),
    _firstScreenOrder: firstScreenOrder.includes(column.prop)
      ? firstScreenOrder.indexOf(column.prop)
      : firstScreenOrder.length + originalIndex
  }))
  .sort((a, b) => a._firstScreenOrder - b._firstScreenOrder)
  .map(({ _firstScreenOrder, ...column }) => column)

export default {
  hidden: false,
  title: '凭证查询',
  icon: '',
  name: 'voucherBusiness'
}
export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  indexWidth: 48,
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isShowSummary: true, // 合并计算表格
  span: 8,
  addBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  editBtn: {
    isShow: true,
    condition: ({ editFlag }) => {
      return editFlag === '1'
    }
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/voucher/query',
      method: 'post'
    },
    edit: {
      url: '/engine/finance/voucher-entry/modify/$[id]',
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
      type: 'primary',
      isDisabled: false,
      isParams: true,
      url: '/engine/finance/voucher/export',
      filename: '凭证查询.xlsx'
    },
    copy: {
      title: '复制',
      type: 'success',
      // isDisabled: false,
      isParams: false,
      url: '/engine/finance/voucher/copy',
      beforeParam: (params, selectRow) => {
        console.log(params, selectRow)
        return selectRow.map(({ sourceFromType, voucherId }) => ({
          sourceFromType,
          voucherId
        }))
      }
    },
    writeOff: {
      title: '冲销',
      type: 'warning',
      isParams: false,
      url: '/engine/finance/voucher/writeOff',
      condition: (item) => item.isWriteOff === '0' && item.voucherStatus === '3',
      beforeParam: (params, selectRow) => {
        return selectRow.map(({ sourceFromType, voucherId }) => ({
          sourceFromType,
          voucherId
        }))
      }
    }
  },
  columns: prioritizeVoucherColumns([
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      display: false,
      hide: true,
      minWidth: 300,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      display: false,
      minWidth: 300,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      type: 'select',
      display: false,
      width: 100,
      isDefaultvalue: true,
      attrs: {
        filterable: true
      },
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
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      display: false,
      type: 'date',
      cover: ['businessDateStart', 'businessDateEnd'],
      width: 110,
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
      display: false,
      type: 'date',
      cover: ['voucherDateStart', 'voucherDateEnd'],
      width: 110,
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
      prop: 'voucherTypeList',
      label: '凭证类型',
      search: true,
      type: 'select',
      hide: true,
      display: false,
      width: 120,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_voucher_type')
    },

    {
      prop: 'voucherType',
      label: '凭证类型',
      type: 'select',
      display: false,
      width: 120,
      option: dictMappingToArray(dictData, 'sys_voucher_type'),
      render: (row) => {
        return (
          (row.voucherType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_voucher_type', row.voucherType))) ||
          ''
        )
      }
    },

    {
      prop: 'voucherNumList',
      label: '凭证号',
      search: true,
      type: 'select-pagination',
      display: false,
      hide: true,
      width: 100,
      attrs: {
        multiple: true
      },
      request: {
        // 请求参数
        url: '/engine/finance/voucher/voucherNumberPage',
        method: 'post',
        formKey: 'periodCode'
      },
      keyValue: {
        label: 'voucherNum',
        value: 'voucherNum'
      }
    },
    {
      prop: 'voucherNum',
      label: '凭证号',
      display: false,
      width: 100
    },

    {
      prop: 'sceneCodeList',
      label: '业务场景',
      search: true,
      type: 'select',
      display: false,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/scene/list',
        method: 'post'
      },
      keyValue: {
        label: 'sceneName',
        value: 'sceneCode'
      }
    },
    {
      prop: 'sceneName',
      label: '业务场景',
      display: false,
      width: 200
    },

    {
      prop: 'subSceneTypeList',
      label: '细分场景',
      width: 200,
      display: false,
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_sub_scene_type')
    },
    {
      prop: 'subSceneType',
      label: '细分场景',
      width: 200,
      display: false,
      type: 'select',
      option: dictMappingToArray(dictData, 'sys_sub_scene_type'),
      render: (row) => {
        return (
          (row.subSceneType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_sub_scene_type', row.subSceneType))) ||
          ''
        )
      }
    },

    {
      prop: 'voucherSummary',
      label: '凭证头摘要',
      display: false,
      search: true,
      width: 200
    },

    {
      prop: 'accountCodeList',
      label: '科目代码',
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: { url: '/engine/scene/account/listAll', method: 'post' },
      keyValue: {
        label: 'accountCode',
        value: 'accountCode',
        formatLable: ['accountCode', 'accountName']
      },
      width: 200
    },
    {
      prop: 'accountCode',
      label: '科目代码',
      width: 200
    },

    // {
    //   prop: 'accountNameList',
    //   label: '科目名称',
    //   search: true,
    //   type: 'select',
    //   hide: true,
    //   display: false,
    //   attrs: {
    //     filterable: true,
    //     multiple: true,
    //     'collapse-tags': true,
    //     'collapse-tags-tooltip': true
    //   },
    //   width: 200,
    //   request: { url: '/engine/scene/account/listAll', method: 'post' },
    //   keyValue: { label: 'accountName', value: 'accountName' }
    // },
    {
      prop: 'accountName',
      label: '科目名称',
      tooltip: true,
      width: 220
    },
    {
      prop: 'currency',
      label: '币种',
      search: true,
      display: false,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render: (row) => {
        return (
          (row.currency &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currency))) ||
          ''
        )
      }
    },
    { width: 200, prop: 'taxRate', label: '汇率' },
    {
      prop: 'debitAmount',
      label: '借方发生额',
      display: false,
      width: 200,
      render: (row) => {
        return toThousands(row.debitAmount)
      }
    },
    {
      prop: 'creditAmount',
      label: '贷方发生额',
      display: false,
      width: 200,
      render: (row) => {
        return toThousands(row.creditAmount)
      }
    },
    {
      prop: 'voucherEntrySummary',
      label: '凭证行摘要',
      display: false,
      search: true,
      width: 200,
      icon: markRaw(DArrowRight),
      arrow: 'left',
      tips: '点击【展示/隐藏】项',
      hideColumns: ['clientName', 'contractCode', 'billContractCode', 'costCentre', 'bankAccount']
    },
    {
      // 展示数据
      prop: 'contractCodeList',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      width: 200,
      hide: true,
      attrs: {
        multiple: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      display: false,
      hide: false,
      width: 200
    },
    {
      prop: 'clientNameList',
      label: '客户名称',
      type: 'select-pagination',
      hide: true,
      search: true,
      display: false,
      attrs: {
        multiple: true
      },
      searchName: 'clientCodeOrName',
      request: { url: '/engine/finance/client/page', method: 'post' },
      keyValue: {
        label: 'clientName',
        value: 'clientName',
        formatLabel: ['clientName', 'clientCode']
      }
    },
    {
      prop: 'clientName',
      label: '客户名称',
      display: false,
      width: 300
    },

    {
      prop: 'billContractCodeList',
      label: '借款合同编号',
      display: false,
      search: true,
      hide: true,
      type: 'select-pagination',
      width: 200,
      attrs: {
        multiple: true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '借款合同编号' }
      },
      keyValue: { label: 'name', value: 'code', formatLabel: ['code', 'name'] }
    },

    {
      prop: 'billContractCode',
      label: '借款合同编号',
      display: false,
      hide: false,
      width: 200
    },

    {
      prop: 'bankAccount',
      label: '银行账号',
      hide: false,
      search: true,
      width: 200
    },
    {
      // 展示数据
      prop: 'createUserNameList',
      label: '制单人',
      search: true,
      type: 'select-pagination',
      hide: true,
      width: 200,
      request: {
        url: '/admin/sys-internal-user/page',
        method: 'post'
      },
      attrs: {
        multiple: true
      },
      searchName: 'userName',
      keyValue: {
        label: 'userName',
        value: 'userCode',
        formatLabel: ['userName', 'userCode']
      }
    },

    {
      prop: 'createUserName',
      label: '制单人',
      display: false,
      width: 200
    },
    {
      prop: 'recheckUserName',
      label: '复核人',
      display: false,
      search: true,
      width: 200
    },
    {
      prop: 'processStatusList',
      label: '处理状态',
      display: false,
      width: 100,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: voucherProcessStatusOptions()
    },
    {
      prop: 'voucherStatus',
      label: '处理状态',
      display: false,
      width: 100,
      render: (row) => {
        return (
          (row.voucherStatus &&
            h(ElTag, {
              class: 'voucher-status-tag',
              type: voucherProcessStatusType(row.voucherStatus),
              effect: 'light',
              round: true
            }, () => voucherProcessStatusLabel(row.voucherStatus))) ||
          ''
        )
      }
    },
    {
      label: '是否涉及其他客户及辅助账',
      prop: ' isRelatedOtherCustomer',
      width: 200,
      search: true,
      type: 'select',
      option: dictMappingToArray(dictData, 'is_sys_bool'),
      render: (row) => {
        return (
          (row.isRelatedOtherCustomer &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'is_sys_bool', row.isRelatedOtherCustomer)
            )) ||
          ''
        )
      }
    },

    {
      prop: 'easVoucherNumber',
      label: 'EAS凭证编号',
      display: false,
      type: 'input',
      search: true,
      width: 200,
      hide: true
    },
    {
      prop: 'operation',
      width: 180,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ])
})
