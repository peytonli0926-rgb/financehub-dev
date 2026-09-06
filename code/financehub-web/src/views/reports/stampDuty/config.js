export default {
  hidden: false,
  title: '印花税',
  icon: '',
  name: 'stampDuty'
}

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

import { dictMappingToArray, dictMappingLabel, toThousands } from '@/utils'
import moment from 'moment'
import { h } from 'vue'
import { ElTag } from 'element-plus'

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/report/stamp-tax/page',
      method: 'post'
    }
  },
  btnConfig: {},
  columns: [
    {
      label: '签约主体',
      prop: 'orgIdList',
      minWidth: 150,
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
      render: ({ orgId }) => {
        return (orgId && dictMappingLabel(dictData, 'company', orgId)) || ''
      }
    },
    {
      label: '系统来源',
      prop: 'systemCodeList',
      minWidth: 120,
      search: true,
      type: 'select',
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'sys_form_source'),
      render({ systemCode }) {
        return (
          (systemCode &&
            h(ElTag, () => dictMappingLabel(dictData, 'sys_form_source', systemCode))) ||
          ''
        )
      }
    },
    {
      // 展示数据
      prop: 'contractCodeList',
      label: '合同编号',
      search: true,
      type: 'select-pagination',
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
        label: 'name',
        value: 'code',
        formatLabel: ['code', 'name']
      }
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 150,
      tooltip: true
    },
    {
      label: '客户名称',
      prop: 'clientName',
      minWidth: 150,
      tooltip: true
    },
    {
      label: '租赁类型',
      prop: 'businessName',
      search: true,
      hide: true,
      type: 'select',
      attrs: {},
      option: dictMappingToArray(dictData, 'lease_type')
    },
    {
      label: '业务类型',
      prop: 'leaseType',
      minWidth: 120,
      render({ leaseType }) {
        return (leaseType && h(ElTag, () => leaseType)) || ''
      }
    },
    {
      prop: 'leaseDateStart',
      label: '会计起租日',
      search: true,
      type: 'date',
      hide: true,
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'leaseDateStart',
      label: '财务起租日',
      minWidth: 150,
      render: ({ leaseDateStart }) => moment(leaseDateStart).format('YYYY-MM-DD')
    },
    {
      prop: 'rentTotalAmount',
      label: '租金总额（含税）',
      minWidth: 150,
      render: ({ rentTotalAmount }) => toThousands(rentTotalAmount)
    },
    {
      prop: 'receivableFirstAmount',
      label: '首付款（含税）',
      minWidth: 150,
      render: ({ receivableFirstAmount }) => toThousands(receivableFirstAmount)
    },
    {
      prop: 'payableDeviceAmount',
      label: '设备金额（不含税）',
      minWidth: 160,
      render: ({ payableDeviceAmount }) => toThousands(payableDeviceAmount)
    },
    {
      prop: 'taxFinancingLeaseContract',
      label: '融资租赁合同计税依据',
      minWidth: 170,
      render: ({ taxFinancingLeaseContract }) => toThousands(taxFinancingLeaseContract)
    },
    {
      prop: 'stampTaxFinancingLeaseContract',
      label: '融资租赁合同印花税',
      minWidth: 160,
      render: ({ stampTaxFinancingLeaseContract }) => toThousands(stampTaxFinancingLeaseContract)
    },
    {
      prop: 'stampTaxPurchaseSaleContract',
      label: '购销合同印花税',
      minWidth: 160,
      render: ({ stampTaxPurchaseSaleContract }) => toThousands(stampTaxPurchaseSaleContract)
    }
  ]
})
