export default {
  hidden: false,
  title: '业业核对-咨询服务费',
  icon: '',
  name: 'consultServiceFee'
}

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  span: 8, // 查询区域每一个表单要展示的宽度
  leftCardName: '',
  addBtn: {
    isShow: false
  },
  editBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  }
}

import { h } from 'vue'
import { ElTag } from 'element-plus'
import moment from 'moment'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/check-account-detail-record/zxfwf/resultPage',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/check-account-detail-record/zxfwf/export',
      filename: '咨询服务费对账报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      label: '来源系统',
      prop: 'dbCode',
      minWidth: 140,
      type: 'select',
      search: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render({ dbCode }) {
        return (
          (dbCode && h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', dbCode))) || ''
        )
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      minWidth: 200,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      request: {
        url: '/engine/finance/kingdee/option/queryGeneralAsst',
        method: 'post',
        params: { asstType: '合同号' }
      },
      keyValue: {
        label: 'code',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      display: false,
      search: true,
      minWidth: 150,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'exceptionFlag',
      label: '异常情况',
      search: true,
      minWidth: 130,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'check_account_exception_flag')
    },
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        filterable: true
      },
      minWidth: 200,
      request: {
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      }
    },
    {
      prop: 'contractStatus',
      label: '业务合同状态',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'business_contract_status')
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      hide: true,
      search: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status')
    },
    {
      prop: 'contractCode',
      label: '服务费合同编号',
      minWidth: 150
    },
    {
      prop: 'accountCode',
      label: '科目代码',
      minWidth: 120
    },
    {
      prop: 'accountName',
      label: '科目名称',
      minWidth: 140,
      render({ accountName }) {
        return (accountName && h(ElTag, () => accountName)) || ''
      }
    },
    {
      prop: 'leaseDateStart',
      label: '合同起租日',
      minWidth: 120,
      render: ({ leaseDateStart }) => {
        return (leaseDateStart && moment(leaseDateStart).format('YYYY-MM-DD')) || ''
      }
    },
    {
      prop: 'leaseDateEnd',
      label: '合同到期日',
      minWidth: 120,
      render: ({ leaseDateEnd }) => {
        return (leaseDateEnd && moment(leaseDateEnd).format('YYYY-MM-DD')) || ''
      }
    },
    {
      prop: 'balanceFinance',
      label: '中台服务费金额',
      minWidth: 150,
      render: ({ balanceFinance }) => toThousands(balanceFinance)
    },
    {
      prop: 'contractCodeM',
      label: '主合同编号',
      minWidth: 120,
      tooltip: true
    },
    {
      prop: 'balance',
      label: '系统已收服务费金额',
      minWidth: 170,
      render: ({ balance }) => toThousands(balance)
    },
    {
      prop: 'systemReturnDate',
      label: '系统回笼日期',
      minWidth: 120,
      render: ({ systemReturnDate }) => {
        return (systemReturnDate && moment(systemReturnDate).format('YYYY-MM-DD')) || ''
      }
    },
    {
      prop: 'balanceDiff',
      label: '差异',
      minWidth: 120,
      render: ({ balanceDiff }) => toThousands(balanceDiff)
    },
    {
      prop: 'exceptionFlagStr',
      label: '异常情况',
      minWidth: 150,
      render({ exceptionFlagStr }) {
        return (exceptionFlagStr && h(ElTag, () => exceptionFlagStr)) || ''
      }
    }
  ]
})
