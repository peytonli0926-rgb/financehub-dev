import { dictMappingToArray } from '@/utils'
export default {
  hidden: false,
  title: '批量查询',
  icon: '',
  name: 'batchSearch'
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
  isShowSummary: true, // 合并计算表格
  span: 8,
  addBtn: {
    isShow: false
  },
  delBtn: {
    isShow: false
  },
  editBtn: {
    isShow: true,
    condition: ({ editFlag }) => {
      return editFlag === '1'
    }
  },
  request: {
    // 请求参数
    list: {
      url: '/engine/finance/batch-query-upload-record/page',
      method: 'post'
    }
  },
  btnConfig: {
    import: {
      title: '上传',
      type: 'primary',
      isDisabled: false,
      params: {
        url: '/engine/finance/batch-query-upload-record/importTemplate',
        title: '上传',
        open: true,
        templateUrl: '/engine/finance/batch-query-upload-record/exportTemplate'
      }
    }
  },
  columns: [
    {
      label: '查询日期',
      prop: 'queryDate',
      type: 'date',
      search: true,
      hide: true,
      attrs: {
        type: 'date',
        format: 'YYYY-MM-DD',
        'value-format': 'YYYY-MM-DD'
      }
    },
    {
      prop: 'orgIdList',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      display: false,
      hide: true,
      minWidth: 300,
      attrs: {
        filterable: true,
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'company')
    },
    {
      prop: 'currencyCode',
      label: '币种',
      search: true,
      display: false,
      hide: true,
      type: 'select',
      width: 100,
      option: dictMappingToArray(dictData, 'sys_currency_type')
    },
    {
      // 展示数据
      prop: 'exceptList',
      label: '排除范围',
      search: true,
      width: 200,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        filterable: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'plcx_pcfw_bq')
    },
    {
      prop: 'processStatusList',
      label: '处理状态',
      display: false,
      width: 100,
      search: true,
      hide: true,
      type: 'select',
      attrs: {
        multiple: true,
        'collapse-tags': true,
        'collapse-tags-tooltip': true
      },
      option: dictMappingToArray(dictData, 'process_status_bq')
    }
  ]
})
