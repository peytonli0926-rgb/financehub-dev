export default {
  hidden: true,
  title: '科目对账表',
  icon: '',
  name: 'accountReconciliation'
}

import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import { h } from 'vue'
import { ElText, ElTag } from 'element-plus'
import moment from 'moment'

const commonCfg = {
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
  }
}

// 汇总页面-应付账款报表
export const summaryOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/voucherReport/query',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'orgId'
    },
    export: {
      title: '导出',
      url: '/engine/finance/voucherReport/query/export',
      filename: '应付账款报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'periodCode',
      label: '会计期间',
      type: 'select',
      search: true,
      minWidth: 120,
      isSearchRequired: true,
      attrs: {
        filterable: true,
        'collapse-tags': true
      },
      request: {
        // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      },
      slot: true,
      rules: [
        {
          required: true,
          message: '请选择会计期间',
          trigger: 'change'
        }
      ]
    },
    {
      label: '签约主体',
      prop: 'orgIdList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '签约主体',
      prop: 'orgName',
      minWidth: 200,
      tooltip: true
    },
    {
      label: '科目代码',
      prop: 'accountCode',
      minWidth: 100
    },
    {
      label: '科目名称',
      prop: 'accountName',
      minWidth: 150,
      tooltip: true
    },
    {
      label: '借方',
      prop: 'debitAmount',
      minWidth: 150,
      render: ({ debitAmount }) => toThousands(debitAmount)
    },
    {
      label: '贷方',
      prop: 'creditAmount',
      minWidth: 150,
      render: ({ creditAmount }) => toThousands(creditAmount)
    },
    {
      prop: 'diffAmount',
      label: '差额',
      minWidth: 150,
      render: ({ diffAmount }) => toThousands(diffAmount)
    },
    {
      prop: 'isException',
      label: '是否异常',
      type: 'select',
      search: true,
      minWidth: 100,
      attrs: {},
      option: dictMappingToArray(dictData, 'sys_yes_no'),
      render({ isException }) {
        return (
          (isException &&
            h(ElText, { type: isException === 'Y' ? 'danger' : '' }, () =>
              dictMappingLabel(dictData, 'sys_yes_no', isException)
            )) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})

// 付款合同信息
export const paymentOrderOptionCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/voucherReport/contractInfoQuery',
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
      url: '/engine/finance/voucherReport/contractInfoQuery/export ',
      filename: '付款合同信息报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'periodCode',
      label: '会计期间',
      type: 'select',
      search: true,
      hide: true,
      minWidth: 120,
      isSearchRequired: true,
      attrs: {
        filterable: true,
        'collapse-tags': true
      },
      request: {
        // 请求参数
        url: '/engine/scene/account-period/queryAll',
        method: 'post'
      },
      keyValue: {
        label: 'periodName',
        value: 'periodCode'
      },
      rules: [
        {
          required: true,
          message: '请选择会计期间',
          trigger: 'change'
        }
      ]
    },
    {
      label: '签约主体',
      prop: 'orgIdList',
      width: 200,
      search: true,
      type: 'select',
      hide: true,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      label: '签约主体',
      prop: 'orgName',
      minWidth: 200,
      tooltip: true
    },
    {
      label: '交易日期',
      prop: 'businessDate',
      width: 170,
      render: ({ businessDate }) => {
        return moment(businessDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '系统来源',
      prop: 'systemCode',
      minWidth: 150,
      render({ systemCode }) {
        return (
          (systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', systemCode))) ||
          ''
        )
      }
    },
    {
      label: '付款编号',
      prop: 'paymentIdentifier',
      minWidth: 170,
      tooltip: true
    },
    {
      label: '业务场景',
      prop: 'sceneName',
      minWidth: 150
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 150
    },
    {
      label: '客户编码',
      prop: 'clientCode',
      minWidth: 150
    },
    {
      label: '客户名称',
      prop: 'clientName',
      minWidth: 150,
      tooltip: true
    },
    {
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
