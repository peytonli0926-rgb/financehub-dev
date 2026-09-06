import { h } from 'vue'
import { ElTag } from 'element-plus'
import { toThousands, dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: false,
  title: '科目余额表',
  icon: '',
  name: 'subjectBusiness'
}
export const optionsConfig = (router, dictData) => ({
  // isIndex: false, // 是否需要序号
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
      url: '/engine/finance/account-assist-balance/accountBalancePage',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      type: 'primary',
      url: '/engine/finance/account-assist-balance/exportAccountBalance',
      filename: '核算余额表.xlsx',
      isDisabled: false,
      params: {
        queryType: 'export'
      },
      isParams: true
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
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      search: true,
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      width: 300,
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
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
      prop: 'currencyCode',
      label: '币种',
      width: 100,
      render(row) {
        return (
          (row.currencyCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyCode))) ||
          ''
        )
      }
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
    {
      // 展示数据
      prop: 'accountCode',
      label: '科目代码',
      search: true,
      width: 200,
      hide: true,
      type: 'inputrange',
      cover: ['accountCodeStart', 'accountCodeEnd'],
      attrs: {
        'range-separator': '-',
        'start-placeholder': '开始范围',
        'end-placeholder': '结束范围'
      }
    },
    {
      // 展示数据
      prop: 'accountCode',
      label: '科目代码',
      width: 200,
      type: 'inputrange',
      cover: ['accountCodeStart', 'accountCodeEnd'],
      attrs: {
        'range-separator': '-',
        'start-placeholder': '开始范围',
        'end-placeholder': '结束范围'
      }
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
      display: false,
      width: 200
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
          render: ({ monthDebitAmount }) => toThousands(monthDebitAmount)
        },
        {
          prop: 'monthCreditAmount',
          label: '贷方',
          width: 200,
          render: ({ monthCreditAmount }) => toThousands(monthCreditAmount)
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
    },
    {
      prop: 'operation',
      label: '操作',
      align: 'center',
      width: 200,
      display: false,
      fixed: 'right'
    }
  ]
})
