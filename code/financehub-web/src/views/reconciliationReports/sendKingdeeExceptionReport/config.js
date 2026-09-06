export default {
  hidden: false,
  title: '传送总帐异常',
  icon: '',
  name: 'sendKingdeeExceptionReport'
}

import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  span: 6, // 查询区域每一个表单要展示的宽度
  leftCardName: '',
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
      url: '/engine/finance/voucherToEasReport/query',
      method: 'post'
    }
  },
  btnConfig: {
    // common: {
    //   disabled: 'processStatus',
    //   getKey: 'id'
    // },
    export: {
      title: '导出',
      // type: 'primary',
      url: '/engine/finance/voucherToEasReport/export',
      filename: '核算项目余额表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'transactionDate',
      label: '传送日期',
      minWidth: 120,
      search: true,
      type: 'date',
      cover: ['transactionDateStart', 'transactionDateEnd'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'successFlag',
      search: true,
      label: '传送结果',
      width: 110,
      type: 'select',
      option: [
        { label: '成功', value: 'sucs' },
        { label: '失败', value: 'errs' }
      ],
      render({ successFlag }, item) {
        let _label = ''
        if (successFlag) {
          _label = item.option.filter((temp) => temp.value === successFlag)[0].label
        }

        return _label
      }
    },
    { prop: 'errMsg', search: true, label: '失败原因', minWidth: 140, tooltip: true },
    {
      label: '主体',
      prop: 'companyNumber',
      minWidth: 200,
      search: true,
      type: 'select',
      tooltip: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      render({ companyNumber }) {
        return (companyNumber && dictMappingLabel(dictData, 'company', companyNumber)) || ''
      }
    },
    {
      prop: 'bizDate',
      label: '业务日期',
      minWidth: 150,
      search: true,
      type: 'date',
      cover: ['bizDateStart', 'bizDateEnd'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'bookedDate',
      label: '记账日期',
      minWidth: 120,
      search: true,
      type: 'date',
      cover: ['bookedDateStart', 'bookedDateEnd'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始日期',
        'end-placeholder': '结束日期',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'periodYear',
      label: '年度',
      alias: '会计年度',
      search: true,
      type: 'date',
      isSearchRequired: true,
      minWidth: 120,
      attrs: {
        type: 'year',
        'value-format': 'YYYY',
        format: 'YYYY'
        // 'disabled-date': (data) => {
        //   return data.getTime() < new Date('2024').getTime()
        // }
      },
      rules: [
        {
          required: true,
          message: '请选择会计年度',
          trigger: 'change'
        }
      ],
      render({ periodYear }) {
        return periodYear.toString()
      }
    },
    {
      prop: 'periodNumber',
      label: '会计期间',
      type: 'select',
      search: true,
      minWidth: 120,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true
      },
      option: [
        { label: '1期', value: '1' },
        { label: '2期', value: '2' },
        { label: '3期', value: '3' },
        { label: '4期', value: '4' },
        { label: '5期', value: '5' },
        { label: '6期', value: '6' },
        { label: '7期', value: '7' },
        { label: '8期', value: '8' },
        { label: '9期', value: '9' },
        { label: '10期', value: '10' },
        { label: '11期', value: '11' },
        { label: '12期', value: '12' }
      ],
      render({ periodNumber }, item) {
        let _label = ''
        if (periodNumber) {
          _label = item.option.filter((temp) => Number(temp.value) === Number(periodNumber))[0]
            .label
        }

        return _label
      }
    },
    {
      label: '凭证类型',
      prop: 'voucherType',
      minWidth: 120,
      search: true,
      type: 'select',
      display: false,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_voucher_type'),
      render({ voucherType }) {
        return (
          (voucherType &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_voucher_type', voucherType))) ||
          ''
        )
      }
    },
    { prop: 'description', search: true, label: '参考信息', minWidth: 120, tooltip: true },
    {
      label: '凭证号',
      prop: 'voucherNumber',
      minWidth: 150,
      search: true,
      // type: 'select-pagination',
      // display: false,
      // attrs: {
      //   multiple: true
      // },
      // request: {
      //   url: '/engine/finance/voucher/voucherNumberPage',
      //   method: 'post',
      //   formKey: 'periodCode'
      // },
      // keyValue: {
      //   label: 'voucherNum',
      //   value: 'voucherNum'
      // },
      tooltip: true
    },
    {
      label: '制单人',
      prop: 'creator',
      minWidth: 120,
      search: true // /admin/sys-internal-user/page
    },
    {
      label: '过账人',
      prop: 'poster',
      minWidth: 120,
      search: true // /engine/finance/kingdee/option/queryPerson
    },
    {
      label: '审核人',
      prop: 'auditor',
      minWidth: 120,
      search: true // /engine/finance/kingdee/option/queryPerson
    },
    {
      label: '科目代码',
      prop: 'accountNumber',
      minWidth: 150,
      search: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        filterable: true
      },
      request: {
        url: '/engine/scene/account/listAll',
        method: 'post'
      },
      keyValue: {
        label: 'accountCode',
        value: 'accountCode',
        formatLabel: ['accountCode', 'accountName']
      }
    },
    {
      prop: 'currencyNumber',
      label: '币种',
      search: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type'),
      render({ currencyNumber }) {
        return (
          (currencyNumber &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_currency_type', currencyNumber))) ||
          ''
        )
      }
    },
    {
      prop: 'debitAmount',
      label: '借方金额',
      search: true,
      type: 'inputrange',
      minWidth: 150,
      cover: ['debitAmountMin', 'debitAmountMax'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小金额',
        'end-placeholder': '最大金额'
      },
      render: ({ debitAmount }) => toThousands(debitAmount)
    },
    {
      prop: 'creditAmount',
      label: '贷方金额',
      search: true,
      type: 'inputrange',
      minWidth: 150,
      cover: ['creditAmountMin', 'creditAmountMax'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '最小金额',
        'end-placeholder': '最大金额'
      },
      render: ({ creditAmount }) => toThousands(creditAmount)
    },
    {
      prop: 'asstActName1',
      label: '物料',
      minWidth: 150,
      search: true,
      tooltip: true
    },

    {
      prop: 'asstActName2',
      label: '客户',
      minWidth: 150,
      search: true,
      tooltip: true
    },
    { prop: 'asstActName3', label: '银行账户', minWidth: 140, search: true, tooltip: true },
    { prop: 'asstActName6', label: '成本中心', minWidth: 140, search: true, tooltip: true },
    { prop: 'asstActName7', label: '职员', minWidth: 140, search: true, tooltip: true },
    {
      prop: 'asstActName8',
      label: '费用类型',
      minWidth: 140,
      tooltip: true,
      search: true,
      render: ({ asstActName8 }) => {
        return (asstActName8 && h(ElTag, () => asstActName8)) || ''
      }
    },
    { prop: 'assistAbstract', search: true, label: '辅助账摘要', minWidth: 140, tooltip: true }
  ]
})
