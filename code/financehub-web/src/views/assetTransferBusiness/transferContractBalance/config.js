export default {
  hidden: false,
  title: '转让合同费用',
  icon: '',
  name: 'transferContractBalance'
}

import moment from 'moment'
import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

const commonCfg = {
  isIndex: true, // 是否需要序号
  isSelection: true, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearchBtn: true,
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

export const optionsConfig = (router, dictData = {}) => ({
  ...commonCfg,
  request: {
    list: {
      url: '/engine/finance/convert-transfer-contract-fee/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    export: {
      title: '导出',
      url: '/engine/finance/convert-transfer-contract-fee/export',
      fetchKey: 'idList',
      filename: '转让合同余额.xlsx',
      isParams: true
    },
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      params: {
        url: '/engine/finance/convert-transfer-contract-fee/importFile',
        title: '转让合同余额',
        open: true,
        templateUrl: '/engine/finance/convert-transfer-contract-fee/template'
      }
    },
    voucher: {
      title: '生成凭证',
      type: 'warning',
      url: '/engine/finance/convert-transfer-contract-fee/generateVoucher'
    },
    submit: {
      title: '提交',
      type: 'success',
      url: '/engine/finance/convert-transfer-contract-fee/submit'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/convert-transfer-contract-fee/withdraw'
    },
    delete: {
      title: '删除',
      type: 'danger',
      url: '/engine/finance/convert-transfer-contract-fee/delete'
    }
  },
  columns: [
    {
      prop: 'uploadDate',
      label: '业务日期',
      minWidth: 140,
      render: ({ uploadDate }) => {
        return uploadDate && moment(uploadDate).format('YYYY-MM-DD')
      }
    },
    {
      label: '记账日期',
      type: 'date',
      prop: 'accountDate',
      minWidth: 140,
      search: true,
      cover: ['accountStartDate', 'accountEndDate'],
      attrs: {
        type: 'daterange',
        clearable: true,
        'range-separator': '-',
        'start-placeholder': '开始时间',
        'end-placeholder': '结束时间',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      label: '签约主体',
      prop: 'orgIdList',
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
      label: '签约主体',
      prop: 'orgId',
      minWidth: 200,
      tooltip: true
    },
    {
      label: '金额',
      prop: 'transferFeeAmount',
      minWidth: 140,
      render: ({ transferFeeAmount }) => toThousands(transferFeeAmount)
    },
    {
      label: '处理状态',
      prop: 'processStatus',
      width: 140,
      render({ processStatus }) {
        return (
          (processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', processStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      width: 200,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
