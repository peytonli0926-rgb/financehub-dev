import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'

export default {
  hidden: false,
  title: '抵债房产',
  icon: '',
  name: 'debtOffsetProperty'
}

// 转入登记
export const optionsConfigTurnInto = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/transfer-register/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    import: {
      title: '上传',
      type: 'primary',
      level: 'F',
      isDisabled: false,
      params: {
        url: '/engine/finance/transfer-register/importFile',
        title: '转入登记',
        open: true,
        templateUrl: '/engine/finance/transfer-register/exportTemplate'
      }
    },
    export: {
      title: '导出',
      url: '/engine/finance/transfer-register/export',
      filename: '转入登记.xlsx',
      level: 'F',
      isParams: true,
      fetchKey: 'idList'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'F',
      url: '/engine/finance/transfer-register/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'F',
      url: '/engine/finance/transfer-register/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'F',
      url: '/engine/finance/transfer-register/delete'
    }
  },
  columns: [
    { label: '资产编号', prop: 'assetNumber', search: true, tooltip: true },
    {
      prop: 'orgIds',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      hide: true,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      width: 300
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      width: 300,
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    { label: '入账时间', type: 'date', prop: 'accountDate' },
    { label: '原合同号', prop: 'contractCode', search: true },
    { label: '房产地址', prop: 'propertyAddress' },
    { label: '抵债资产入账价值 ', prop: 'debtAssetValue', render: ({ debtAssetValue }) => toThousands(debtAssetValue) },
    {
      prop: 'processStatusList',
      label: '处理状态',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    }
  ]
})

// 转出-出售登记
export const optionsConfigTurnOutSale = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
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
      url: '/engine/finance/sell-register/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    import: {
      title: '上传',
      type: 'primary',
      level: 'S',
      isDisabled: false,
      params: {
        url: '/engine/finance/sell-register/importFile',
        title: '出售登记',
        open: true,
        templateUrl: '/engine/finance/sell-register/exportTemplate'
      }
    },
    export: {
      title: '导出',
      url: '/engine/finance/sell-register/export',
      filename: '出售登记.xlsx',
      level: 'S',
      isParams: true,
      fetchKey: 'idList'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'S',
      url: '/engine/finance/sell-register/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'S',
      url: '/engine/finance/sell-register/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'S',
      url: '/engine/finance/sell-register/delete'
    }
  },
  columns: [
    { label: '原合同号', prop: 'contractCode' },
    { label: '资产编号', prop: 'assetNumber', tooltip: true, search: true },

    { label: '转出时间', type: 'date', prop: 'transferOutDate' },
    { label: '买售人', prop: 'buyOrSellPerson' },
    { label: '售价 ', prop: 'sellPrice', render: ({ sellPrice }) => toThousands(sellPrice) },
    { label: '房产地址', prop: 'propertyAddress' },
    {
      prop: 'processStatusList',
      label: '处理状态',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    }
  ]
})

// 转出-出租登记
export const optionsConfigTurnOutRent = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isSearch: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  labelWidth:'150px',
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
      url: '/engine/finance/rent-register/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    each: {
      title: '分摊',
      level: 'T',
      url: '/engine/finance/rent-register/apportion',
      forward: '/debtRestructuringBusiness/rentConfirm',
      condition: (data) => (data.length !== 0)
    },
    import1: {
      title: '上传出租登记基本信息',
      type: 'primary',
      level: 'T',
      isDisabled: false,
      params: {
        url: '/engine/finance/rent-register/importFile',
        title: '出租登记-基本信息',
        open: true,
        templateUrl: '/engine/finance/rent-register/exportTemplate'
      }
    },

    import: {
      title: '上传租金计划表',
      type: 'primary',
      level: 'T',
      isDisabled: false,
      params: {
        url: '/engine/finance/rent-register/importFileForDetail',
        title: '上传租金计划表',
        open: true,
        templateUrl: '/engine/finance/rent-register/exportTemplateDetail'
      }
    },
    export: {
      title: '导出',
      url: '/engine/finance/rent-register/export',
      filename: '出租登记.xlsx',
      level: 'T',
      isParams: true,
      fetchKey: 'idList'
    },
    submit: {
      title: '提交',
      type: 'success',
      level: 'T',
      url: '/engine/finance/rent-register/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      level: 'T',
      url: '/engine/finance/rent-register/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      level: 'T',
      url: '/engine/finance/rent-register/delete'
    }
  },
  columns: [
    { label: '房产租赁合同编号', prop: 'contractCode', slot: true, search: true, redirectUrl: '' },
    { label: '资产编号', prop: 'assetNumber', search: true },
    { label: '转出时间', type: 'date', prop: 'transferOutDate' },
    { label: '客户名称', prop: 'clientName', search: true },
    { label: '起租日', type: 'date', prop: 'leaseDateStart' },
    { label: '到期日', type: 'date', prop: 'leaseDateEnd' },
    { label: '租金总额', prop: 'rentTotal', render: ({ rentTotal }) => toThousands(rentTotal) },
    { label: '租赁保证金', prop: 'rentBond', render: ({ rentBond }) => toThousands(rentBond) },
    { label: '版本号', prop: 'versionNum' },
    { label: '是否仅展示最新版本', hide:true,search:true, prop: 'isLatestVersion',type:'select',option:dictMappingToArray(dictData,'is_sys_bool') },
    {
      prop: 'processStatusList',
      label: '处理状态',
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      render (row) {
        return (row.processStatus && h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) || ''
      }
    }
  ]
})
