import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '尾差调整',
  icon: '',
  name: 'endAdjust'
}

const btnConfig = {
  common: {
    disabled: 'processStatus',
    getKey: 'id'
  },

  initInfo: {
    title: '生成尾差调整信息',
    isDisabled: false,
    isParams: true,
    type: 'primary',
    url: '/engine/finance/tail-difference-adjustment/initData'
  },
  export: {
    title: '导出',
    type: 'primary',
    url: '/engine/finance/tail-difference-adjustment/export',
    filename: '尾差调整.xlsx'
  },
  voucher: {
    title: '生成凭证',
    type: 'warning',
    url: '/engine/finance/tail-difference-adjustment/generateVoucher'
  },
  submit: {
    title: '提交',
    type: 'success',
    url: '/engine/finance/tail-difference-adjustment/submit'
  },
  withdraw: {
    title: '撤回',
    type: 'warning',
    url: '/engine/finance/tail-difference-adjustment/withdraw'
  },
  delete: {
    title: '删除',
    type: 'danger',
    url: '/engine/finance/tail-difference-adjustment/deleteByIds'
  }
}
// 手工
export const optionsConfigPageOne = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8,
  btnConfig: btnConfig,
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
      url: '/engine/finance/tail-difference-adjustment/page',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '业务日期',
      search: true,
      type: 'date',
      // cover: ['startVerificationDate', 'endVerificationDate'],
      attrs: {
        type: 'date',
        // 'range-separator': '-',
        // 'start-placeholder': '开始时间',
        // 'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'accountDate',
      label: '记账日期',
      type: 'date',
      cover: ['startVerificationDate', 'endVerificationDate'],
      attrs: {
        type: 'date',
        // 'range-separator': '-',
        // 'start-placeholder': '开始时间',
        // 'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },

    {
      prop: 'orgIdList',
      label: '签约主体',

      search: true,
      hide: true,
      type: 'select',
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
      width: 300,
      render: (row) => {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      prop: 'accountCodeList',
      label: '科目代码',
      type: 'select',
      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'mantissa_adjust_account'),
      search: true
    },

    {
      prop: 'accountCode',
      label: '科目代码'
    },
    {
      prop: 'accountName',
      label: '科目名称'
    },
    {
      prop: 'accountBalance',
      label: '科目余额',
      hide: true,
      search: true,
      type: 'inputrange',
      cover: ['minAccountBalance', 'maxAccountBalance'],
      attrs: {
        type: 'number',
        'range-separator': '-',
        'start-placeholder': '开始范围',
        'end-placeholder': '结束范围'
      }
    },

    {
      // 展示数据
      prop: 'tailDifferenceBalance',
      label: '尾差金额',
      width: 200,
      render: ({ tailDifferenceBalance }) => {
        return toThousands(tailDifferenceBalance)
      }
    },

    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render: (row) => {
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
