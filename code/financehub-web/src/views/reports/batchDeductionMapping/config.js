export default {
  hidden: false,
  title: '批扣映射表',
  icon: '',
  name: 'batchDeductionMapping'
}

import { dictMappingToArray, dictMappingLabel, toThousands, parseTime } from '@/utils'

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

const searchCfg = (dictData) => {
  return [
    {
      prop: 'periodCode',
      label: '会计期间',
      type: 'select',
      search: true,
      minWidth: 300,
      hide: true,
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
      }
    },
    {
      prop: 'abnormal',
      label: '是否异常',
      type: 'select',
      search: true,
      hide: true,
      minWidth: 300,
      attrs: {},
      option: dictMappingToArray(dictData, 'sys_yes_no')
    },
    {
      prop: 'businessDate',
      label: '交易日期',
      width: 110,
      render: ({ businessDate }) => businessDate && businessDate.split(' ')[0]
    }
  ]
}

// 已勾稽数据
export const verifiedOptionsCfg = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/system-bank-mapping/filter',
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
      url: '/engine/finance/system-bank-mapping/filter/export',
      filename: '已勾稽报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    ...searchCfg(dictData),
    { prop: 'matchNumber', label: '勾稽编号', minWidth: 150 },
    { prop: 'ebankNumber', label: '资金编号', minWidth: 150, tooltip: true },
    { prop: 'ebankSerialNumber', label: '大网银编号', minWidth: 150, tooltip: true },
    {
      prop: 'matchAmount',
      label: '到账金额',
      minWidth: 150,
      render: ({ matchAmount }) => toThousands(matchAmount)
    },
    {
      prop: 'collectAmount',
      label: '批扣总金额',
      minWidth: 150,
      render: ({ collectAmount }) => toThousands(collectAmount)
    },
    {
      prop: 'diffAmount',
      label: '差额',
      minWidth: 150,
      render: ({ diffAmount }) => toThousands(diffAmount)
    },
    {
      prop: 'sendKingdee',
      label: '是否推送金蝶',
      minWidth: 150,
      render(row) {
        return dictMappingLabel(dictData, 'abs_redeem', row.sendKingdee)
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

// 未勾稽数据
export const unrelatedOptionsCfg = (router, dictData = {}) => ({
  ...commonCfg,
  isSearch: false,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/system-bank-mapping/filter/no',
      method: 'post'
    }
  },
  btnConfig: {
    export: {
      title: '导出',
      url: '/engine/finance/system-bank-mapping/filter/no/export',
      filename: '未勾稽报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'createTime',
      label: '交易日期',
      width: 110,
      render: ({ createTime }) => parseTime(createTime, '{y}-{m}-{d}')
    },
    { prop: 'onlineBankNo', label: '大网银编号', minWidth: 150 },
    { prop: 'deductBatchNo', label: '小网银编号', minWidth: 150 },
    {
      prop: 'collectAmount',
      label: '批扣金额',
      minWidth: 150,
      render: ({ collectAmount }) => toThousands(collectAmount)
    }
  ]
})
