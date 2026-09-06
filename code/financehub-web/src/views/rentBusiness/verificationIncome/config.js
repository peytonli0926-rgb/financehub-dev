import { dictMappingToArray, toThousands, dictMappingLabel } from '@/utils'
import moment from 'moment'
export default {
  hidden: true,
  title: '核销回款',
  icon: '',
  name: 'verificationIncome'
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
      url: '/engine/verification/payback/page',
      method: 'post'
    }
  },
  btnConfig: {
    common: {
      disabled: 'processStatus',
      getKey: 'id'
    },
    fileListBtn: {
      title: '查看列表',
      isDisabled: false,
      params: {
        moduleName: 'verification_summary',
        businessScene: 'verification_summary'
      }
    },
    export: {
      title: '导出',
      isDisabled: false,
      type: 'primary',
      url: '/engine/verification/payback/export',
      isParams: true,
      filename: '核销回款.xlsx',
      isAsyncFile: true
    }
  },
  columns: [
    {
      prop: 'businessDate',
      label: '回款月份',
      search: true,
      type: 'date',
      cover: ['startBusinessDate', 'endBusinessDate'],
      attrs: {
        type: 'monthrange',
        clearable: false,
        'range-separator': '-',
        'start-placeholder': '开始月份',
        'end-placeholder': '结束月份',
        format: 'YYYY-MM',
        'value-format': 'YYYY-MM'
      },
      render({ businessDate }) {
        return moment(businessDate).format('YYYY-MM')
      }
    },

    {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      attrs: {
        filterable: true
      },
      option: dictMappingToArray(dictData, 'company'),
      render(row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    },
    {
      prop: 'reversalProvisionAmount',
      label: '拨备转回金额',
      render(row) {
        return toThousands(row.reversalProvisionAmount)
      }
    },
    {
      prop: 'revenueFinance',
      label: '确认收入金融',
      render(row) {
        return toThousands(row.revenueFinance)
      }
    },
    {
      prop: 'operation',
      width: 150,
      label: '操作',
      display: false
    }
  ]
})
