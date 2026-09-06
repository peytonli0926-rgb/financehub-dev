import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel, dictMappingToArray, toThousands } from '@/utils'

export default {
  hidden: true,
  title: '手工处理',
  icon: '',
  name: 'nonConfirmCustom'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: true, // 是否需要多选
  isPagination: false, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  isSearch: true,
  labelWidth: '180px',
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
      url: '/engine/finance/non-confirm-collection-second-detail/queryManualProcess',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      getKey: 'id',
      disabled: 'processStatus'
    },
    withdraw: {
      title: '撤回',
      type: 'warning',
      url: '/engine/finance/non-confirm-collection-second-detail/recall',
      isParams: true,
      fetchKey: 'detailIds'
    },
    delete: {
      title: '删除',
      type: 'danger',
      url: '/engine/finance/non-confirm-collection-second-detail/delete',
      isParams: true,
      fetchKey: 'detailIds'
    }
  },
  columns: [
    {
      label: '操作类型',
      type: 'select',
      prop: 'operationType',
      search: true,
      option: dictMappingToArray(dictData, 'unconfirmed_amount_operation_type'),
      render: ({ operationType }) => {
        return (
          (operationType &&
            h(ElTag, () => dictMappingLabel(dictData, 'unconfirmed_amount_operation_type', operationType))) ||
          ''
        )
      }
    },
    { label: '记账日期', type: 'date', prop: 'businessHappenDate' },
    { label: '业务系统网银编号/批次号', search: true, width: 200, prop: 'ebankSerialNumber' },
    { label: '业务系统批扣流水号', search: true, prop: 'ebankSerialNumber' },
    { label: '新业务系统批扣流水号', prop: 'newEbankSerialNumber' },
    {
      label: '认领/冲销/调整金额',
      prop: 'claimAmount',
      render: ({ claimAmount }) => toThousands(claimAmount)
    },
    { label: '原入账月份', prop: 'incomeYmOld' },

    {
      prop: 'processStatus',
      label: '处理状态',
      width: 100,
      search: true,
      type: 'select',
      attrs: {
        // multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status'),
      render (row) {
        return (
          (row.processStatus &&
            h(ElTag, () => dictMappingLabel(dictData, 'process_status', row.processStatus))) ||
          ''
        )
      }
    },
    {
      prop: 'operation',
      label: '操作',
      width: 180,
      display: false,
      fixed: 'right'
    }
  ]
})
