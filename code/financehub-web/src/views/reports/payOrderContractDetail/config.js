export default {
  hidden: true,
  title: '付款单合同详情',
  icon: '',
  name: 'payOrderContractDetail'
}

import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingLabel } from '@/utils'
import moment from 'moment'

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isIndexOrder: true,
  isSearch: false,
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
      url: '/engine/finance/voucherReport/contractDetailQuery',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      getKey: 'paymentIdentifier'
    },
    export: {
      title: '导出',
      url: '/engine/finance/voucherReport/contractDetailQuery/export',
      filename: '合同详情报表.xlsx',
      isDisabled: false,
      isParams: true
    }
  },
  columns: [
    {
      prop: 'orgName',
      label: '签约主体',
      minWidth: 200,
      tooltip: true
    },
    {
      prop: 'businessDate',
      label: '交易日期',
      width: 170,
      render: ({ businessDate }) => {
        return moment(businessDate).format('YYYY-MM-DD')
      }
    },
    {
      prop: 'systemCode',
      label: '系统来源',
      minWidth: 120,
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
      minWidth: 120,
      render({ sceneName }) {
        return (sceneName && h(ElTag, () => sceneName)) || ''
      }
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 150,
      tooltip: true
    },
    {
      label: '客户编码',
      prop: 'clientCode',
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
      prop: 'operation',
      width: 120,
      label: '操作',
      fixed: 'right',
      display: false
    }
  ]
})
