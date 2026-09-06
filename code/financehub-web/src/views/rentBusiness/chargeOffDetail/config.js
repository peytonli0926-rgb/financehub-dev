import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: 'chargeOff详情',
  icon: '',
  name: 'chargeOffDetail'
}

export const optionsConfig = (router, dictData = {}) => ({
  isIndex: true, // 是否需要序号
  isIndexFixed: true,
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
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
      url: '/engine/finance/charge-off/summaryDetailPage',
      method: 'post'
    }
  },
  columns: [
    {
      prop: 'contractCode',
      label: '合同编号',
      search: true,
      attrs: {
        SEARCH: true
      },
      width: 200
    },

    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      tooltip: true,
      search: true,
      attrs: {
        SEARCH: true,

        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company'),
      width: 300,
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },

    {
      prop: 'verificationStatus',
      label: '核销状态',
      width: 200,
      search: true,
      type: 'select',
      attrs: {
        SEARCH: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'charge_off_verification_status'),
      render({ verificationStatus }) {
        return (
          (verificationStatus &&
            h(ElTag, () =>
              dictMappingLabel(dictData, 'charge_off_verification_status', verificationStatus)
            )) ||
          ''
        )
      }
    },

    {
      prop: 'clientName',
      label: '客户名称',
      width: 200,
      search: true
    },

    {
      prop: 'verificationDate',
      label: '核销日期',
      width: 200,
      search: true,
      type: 'date',
      // cover: ['startVerificationDate', 'endVerificationDate'],
      attrs: {
        SEARCH: true,
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'provisionReversalYear',
      label: '拨备转回年份',
      width: 200,
      type: 'date',
      attrs: {
        format: 'YYYY'
      }
    },

    {
      prop: 'provisionReversalAmount',
      label: '拨备转回金额',
      width: 200,
      render({ provisionReversalAmount }) {
        return toThousands(provisionReversalAmount)
      }
    }
  ]
})
