import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '保证金单月汇总',
  icon: '',
  name: 'depositDetail'
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
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/margin-contract-balance/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'marginStatus',
      getKey: 'batchId'
    },
    fileListBtn: {
      title: '查看列表',
      isDisabled: false,
      params: {
        moduleName: 'margin_contract',
        businessScene: 'marginContract'
      }
    },
    export: {
      title: '导出',
      isDisabled: false,
      url: '/engine/finance/margin-contract-balance/export',
      isParams: true,
      filename: '保证金.xlsx',
      isAsyncFile: true
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      width: 200
    },
    {
      prop: 'financeDate',
      label: '财务日期',
      width: 200
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      minWidth: 250,
      attrs: {
        disabled: true
      },

      option: dictMappingToArray(dictData, 'company'),
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'accountCodeList',
      label: '科目名称',
      type: 'select',
      search: true,
      hide: true,
      attrs: {
        disabled: true,
        multiple: true
      },
      option: dictMappingToArray(dictData, 'sys_margin_subject')
    },
    {
      prop: 'contractCode',
      label: '合同编码',
      width: 200
    },
    {
      prop: 'contractName',
      label: '合同名称',
      width: 200
    },
    {
      prop: 'accountCode',
      label: '科目编码',
      width: 200
    },
    {
      prop: 'accountName',
      label: '保证金类别',
      width: 200
    },
    {
      prop: 'currencyType',
      label: '币种',
      search: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      rules: [
        {
          required: true,
          message: '请选择币种',
          trigger: ['change']
        }
      ],
      render: (row) => {
        return (
          (row.currencyType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', row.currencyType))) ||
          ''
        )
      }
    },
    {
      prop: 'contractBalance',
      label: '保证金余额',
      width: 200,
      render: ({ contractBalance }) => {
        const _style = `color:${contractBalance < 0 ? 'red' : ''}`
        return toThousands(contractBalance, _style)
        // h(ElTag, () => dictMappingLabel(dictData, 'margin_status', label))
      }
    },
    {
      prop: 'leaseDateStart',
      label: '财务起租日',
      type: 'date',
      width: 200
    },
    {
      prop: 'leaseDateEnd',
      label: '约定到期日',
      type: 'date',
      width: 200
    },
    {
      prop: 'withinOneYear',
      label: '是否一年内到期',
      width: 200,
      render: (row) => {
        const label = row.withinOneYear === '0' ? '否' : '是'
        return h(ElTag, () => dictMappingLabel(dictData, 'margin_status', label))
      }
    },
    {
      prop: 'withinOneYearDeposit',
      label: 'Future Value',
      width: 200,
      // render (row) {
      //   return h(ElTag, () => dictMappingLabel(dictData, 'sys_margin_subject_reclassified', row.withinOneYearDeposit))
      // }
      render: (row) => {
        return toThousands(row.withinOneYearDeposit)
      }
    },
    {
      prop: 'lpr',
      label: '贷款利率',
      width: 200
    },
    {
      prop: 'pv',
      label: '现值',
      width: 200,
      render: (row) => {
        return toThousands(row.pv)
      }
    },
    {
      prop: 'principalAmount',
      label: '本金',
      width: 200,
      render: (row) => {
        return toThousands(row.principalAmount)
      }
    },
    {
      prop: 'currentEnterPl',
      label: '本期计提金额',
      width: 200,
      render: (row) => {
        return toThousands(row.currentEnterPl)
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 100,
      fixed: 'right'
    }
  ]
})
