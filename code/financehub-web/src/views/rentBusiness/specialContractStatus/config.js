import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '特殊合同状态',
  icon: '',
  name: 'specialContractStatus'
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
  dialogSpan: 12,
  span: 8,
  dialogLabelWidth: '150px',
  labelWidth: '100px',
  dialogWidth: '50%',
  addBtn: {
    isShow: true
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
      url: '/engine/finance/contract-status-record/page',
      method: 'post'
    },
    add: {
      url: '/engine/finance/contract-status-record/save',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'recordStatus',
      getKey: 'id'
    },
    add: {
      title: '新增',
      type: 'primary',
      isDisabled: false
    },
    import: {
      title: '上传',
      isDisabled: false,
      type: 'primary',
      params: {
        url: '/engine/finance/contract-status-record/importData',
        title: '特殊合同状态',
        open: true,
        templateUrl: '/engine/finance/contract-status-record/importTemplate'
      }
    },
    // export: {
    //   title: '导出',
    //   url: '/engine/finance/contract-status-record/export',
    //   filename: '特殊合同状态.xlsx',
    //   // fetchKey: 'idList',
    //   isDialog:true,
    //   tooltips:''
    // },

    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/contract-status-record/submit'
    },
    delete: {
      title: '删除',
      type: 'danger',
      url: '/engine/finance/contract-status-record/deleteByIds'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/contract-status-record/withdraw'
    }
  },
  columns: [
    {
      prop: 'endDate',
      label: '截止日期',
      search: true,
      width: 200,
      display: false,
      hide: true,
      type: 'date',
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'contractCode',
      label: '合同编码',
      search: true,

      rules: [
        {
          required: true,
          message: '请输入合同编码',
          trigger: 'change'
        }
      ],
      width: 200
    },
    {
      prop: 'clientName',
      label: '客户名称',
      display: false,
      tooltip: true,
      width: 200
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      hide: true,
      display: false,
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      minWidth: 250,
      tooltip: true,
      rules: [
        {
          required: true,
          message: '请选择签约主体',
          trigger: 'change'
        }
      ],
      option: dictMappingToArray(dictData, 'company'),
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'financialContractStatusUpdateTime',
      label: '财务合同状态更新时间',
      alias: '财务合同日期',
      search: true,
      width: 200,
      display: false,
      type: 'date',
      cover: ['startFinancialContractStatusUpdateTime', 'endFinancialContractStatusUpdateTime'],
      attrs: {
        type: 'daterange',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'financialContractStatusUpdateTime',
      label: '财务合同更新时间',
      alias: '财务合同日期',
      width: 200,
      hide: true,
      type: 'date',
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'financialContractStatusList',
      label: '财务合同状态',
      type: 'select',
      search: true,
      hide: true,
      display: false,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      width: 120,
      option: dictMappingToArray(dictData, 'financial_contract_status')
    },
    {
      prop: 'financialContractStatus',
      label: '财务合同状态',
      rules: [
        {
          required: true,
          message: '请选择财务合同状态',
          trigger: 'change'
        }
      ],
      width: 200,
      type: 'select',
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'financial_contract_status'),
      render(row) {
        return dictMappingLabel(dictData, 'financial_contract_status', row.financialContractStatus)
      }
    },

    {
      prop: 'contractStatus',
      label: '业务合同状态',
      type: 'select',
      option: dictMappingToArray(dictData, 'business_contract_status'),
      display: false,

      render(row) {
        return (
          (row.contractStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'business_contract_status', row.contractStatus)
            )) ||
          ''
        )
      },
      width: 200
    },

    {
      prop: 'transferOrgId',
      label: '转入公司(针对内部转让)',
      display: false,
      width: 200
    },
    {
      prop: 'transferContractCode',
      label: '转入合同号(针对内部转让)',
      display: false,
      width: 200
    },
    {
      prop: 'transferContractStatus',
      label: '转入合同系统合同状态(针对内部转让)',
      type: 'select',

      option: dictMappingToArray(dictData, 'business_contract_status'),
      display: false,
      render(row) {
        return (
          (row.transferContractStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'business_contract_status', row.transferContractStatus)
            )) ||
          ''
        )
      },
      width: 170
    },
    {
      prop: 'recordStatusList',
      label: '处理状态',
      display: false,
      width: 100,
      hide: true,
      search: true,
      type: 'select',
      attrs: {
        'collapse-tags': true,
        'collapse-tags-tooltip': true,
        multiple: true
      },
      option: dictMappingToArray(dictData, 'process_status')
    },
    {
      prop: 'recordStatus',
      label: '处理状态',
      display: false,
      width: 100,
      render(row) {
        return (
          (row.recordStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.recordStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 100,
      display: false,
      fixed: 'right'
    }
  ]
})

export const optionsFileConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
  isOperateHeader: false, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  span: 8, // 查询区域每一个表单要展示的宽度
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
      url: '/engine/finance/contract-status-record/fileList',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'fileName',
      label: '文件名称',
      minWidth: 180
    },
    {
      prop: 'fileUploadBy',
      label: '下载人',
      minWidth: 130
    },
    {
      prop: 'fileUploadTime',
      label: '下载时间',
      width: 180
    },
    {
      prop: 'operation',
      label: '操作',
      display: false,
      width: 100
    }
  ]
})
