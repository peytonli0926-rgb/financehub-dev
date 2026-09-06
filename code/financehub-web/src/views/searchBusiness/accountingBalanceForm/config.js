import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '核算项目余额表',
  icon: '',
  name: 'accountingBalanceForm'
}
export const optionsConfig = (router, dictData) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8,
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
      url: '/engine/finance/account-assist-balance/assistBalancePage',
      method: 'post'
    }
  },
  btnConfig: {
    fileListBtn: {
      title: '查看列表',
      isDisabled: false,
      params: {
        moduleName: 'query_module',
        businessScene: 'assist_balance'
      }
    },
    export: {
      title: '导出',
      type: 'primary',
      url: '/engine/finance/account-assist-balance/assistBalance/excel',
      filename: '核算项目余额表.xlsx',
      isDisabled: false,
      params: {
        queryType: 'export'
      },
      isParams: true,
      isAsyncFile: true
    }
  },
  columns: [
    {
      prop: 'periodCode',
      label: '会计期间',
      search: true,
      type: 'selectrange',
      width: 200,
      isDefaultvalue: true,
      cover: ['periodCodeStart', 'periodCodeEnd']
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      minWidth: 250,
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      minWidth: 250,
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      // 展示数据
      prop: 'contractCode',
      label: '合同编号',
      width: 200,
      slot: true
    },
    {
      // 展示数据
      prop: 'clientName',
      label: '客户名称',
      // search: true,
      width: 200
    },
    {
      // 展示数据
      prop: 'billContractCode',
      label: '借款合同编号',
      width: 200
    },
    {
      prop: 'leaseType',
      label: '租赁类型',
      width: 100,
      render({ leaseType }) {
        return (
          (leaseType && h(ElTag, () => dictMappingLabel(dictData, 'lease_type', leaseType))) || ''
        )
      }
    },
    {
      prop: 'currencyCode',
      label: '币种',
      width: 100,
      render({ currencyCode }) {
        return (
          (currencyCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', currencyCode))) ||
          ''
        )
      }
    },
    {
      // 展示数据
      prop: 'accountCode',
      label: '科目代码',
      search: true,
      width: 200,
      type: 'inputrange',
      cover: ['accountCodeStart', 'accountCodeEnd'],
      attrs: {
        'range-separator': '-',
        'start-placeholder': '开始范围',
        'end-placeholder': '结束范围'
      }
    },
    {
      prop: 'accountName',
      label: '科目名称',
      display: false,
      width: 200
    },
    {
      // 展示数据
      prop: 'contractCodes',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
      hide: true,
      width: 200,
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
      },
      slot: true
    },
    {
      // 展示数据
      prop: 'billContractCodeList',
      label: '借款合同编号',
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
      keyValue: {
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
      // slot:true,
    },

    {
      // 展示数据
      prop: 'exceptList',
      label: '排除范围',
      search: true,
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'exclusion_scope')
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
    //   keyValue: { label: 'accountName', value: 'accountName', formatLable: [''] }
    // },

    {
      prop: 'currencyCodeList',
      label: '币种',
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type')
    },
    {
      prop: 'fundType5',
      label: '年初余额',
      align: 'center',
      children: [
        {
          prop: 'yearBeginDebitBalance',
          label: '借方',
          width: 200,
          render: ({ yearBeginDebitBalance }) => toThousands(yearBeginDebitBalance)
        },
        {
          prop: 'yearBeginCreditBalance',
          label: '贷方',
          width: 200,
          render: ({ yearBeginCreditBalance }) => toThousands(yearBeginCreditBalance)
        }
      ]
    },
    {
      prop: 'fundType4',
      label: '期初余额',
      align: 'center',
      children: [
        {
          prop: 'monthBeginDebitBalance',
          label: '借方',
          width: 200,
          render: ({ monthBeginDebitBalance }) => toThousands(monthBeginDebitBalance)
        },
        {
          prop: 'monthBeginCreditBalance',
          label: '贷方',
          width: 200,
          render: ({ monthBeginCreditBalance }) => toThousands(monthBeginCreditBalance)
        }
      ]
    },
    {
      prop: 'fundType3',
      label: '本期发生额',
      align: 'center',
      children: [
        {
          prop: 'monthDebitAmount',
          label: '借方',
          width: 200,
          slot: true
          // render: ({ monthDebitAmount }) => toThousands(monthDebitAmount)
        },
        {
          prop: 'monthCreditAmount',
          label: '贷方',
          width: 200,
          slot: true
          // render: ({ monthCreditAmount }) => toThousands(monthCreditAmount)
        }
      ]
    },
    {
      prop: 'fundType2',
      label: '本年累计',
      align: 'center',
      children: [
        {
          prop: 'yearDebitAmount',
          label: '借方',
          width: 200,
          render: ({ yearDebitAmount }) => toThousands(yearDebitAmount)
        },
        {
          prop: 'yearCreditAmount',
          label: '贷方',
          width: 200,
          render: ({ yearCreditAmount }) => toThousands(yearCreditAmount)
        }
      ]
    },
    {
      prop: 'fundType1',
      label: '期末余额',
      align: 'center',
      children: [
        {
          prop: 'monthEndDebitBalance',
          label: '借方',
          width: 200,
          render: ({ monthEndDebitBalance }) => toThousands(monthEndDebitBalance)
        },
        {
          prop: 'monthEndCreditBalance',
          label: '贷方',
          width: 200,
          render: ({ monthEndCreditBalance }) => toThousands(monthEndCreditBalance)
        }
      ]
    },
    {
      // 展示数据
      prop: 'processStatusList',
      label: '取值范围',
      search: true,
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    }
    // {
    //   prop: 'operation',
    //   label: '操作',
    //   align: 'center',
    //   width: 200,
    //   display: false,
    //   fixed: 'right'
    // }
  ]
})
