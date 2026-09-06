import { h } from 'vue'
import { ElButton } from 'element-plus'

export default {
  hidden: false,
  title: '指标字典',
  icon: '',
  name: 'indicatorDict',
  sort: 1
}

export const optionsConfig = (router) => ({
  isIndex: true, // 是否需要序号
  isSelection: false, // 是否需要多选
  isPagination: true, // 是否需要翻页
  isBorder: true,
  isOperateHeader: true, // 是否需要table头部操作区域，
  rowKey: 'id', // 表格唯一id
  leftCardName: '',
  // delBtn:{
  //   label:'删除',
  //   isShow:false
  // },
  request: { // 请求参数
    list: {
      url: '/moji/business-indicators/page',
      method: 'post'
    }
    /*add: {
      url: '/admin/dict/type',
      method: 'post'
    },
    edit: {
      url: '/admin/dict/type/$[dictId]',
      method: 'put'
    },
    del: {
      url: '/admin/dict/type/$[dictId]',
      method: 'delete'
    }*/
  },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '创建指标'
    },
    edit: {
      isDisabled: false,
      type: 'primary',
      title: '编辑指标'
    }
  },
  columns: [
    {
      prop: 'firstLevelCategory',
      label: '一级分类',
      search: true
    },
    {
      prop: 'secondLevelCategory',
      label: '二级分类',
      search: true

    },
    {
      prop: 'thirdLevelCategory',
      label: '三级分类',
      search: true
    },
    {
      prop: 'indicatorLevel',
      label: '指标级别',
      search: true
    },
    {
      prop: 'indicatorCode',
      label: '指标编码',
      search: true
    },
    {
      prop: 'indicatorName',
      label: '指标名称',
      search: true
    },
    {
      prop: 'fieldType',
      label: '字段类型',
      search: true
    },
    {
      prop: 'indicatorDefinition',
      label: '指标定义',
      tooltip:true
    },
    {
      prop: 'basicCalculatedIndicator',
      label: '基础/计算指标'
    },
    {
      prop: 'indicatorCalculationRules',
      label: '指标口径/计算规则',
      tooltip:true
    },
    {
      prop: 'statisticalFrequency',
      label: '统计频率'
    },
    {
      prop: 'periodPointValue',
      label: '期间值/时点值'
    },
    {
      prop: 'isEffective',
      label: '是否生效'
    },
    {
      prop: 'effectiveDate',
      label: '生效/废止时间'
    },
    {
      prop: 'businessType',
      label: '业务类型'
    },
    {
      prop: 'customer',
      label: '客户'
    },
    {
      prop: 'team',
      label: '团队'
    },
    {
      prop: 'employee',
      label: '员工'
    },
    {
      prop: 'businessLine',
      label: '业务线'
    },
    {
      prop: 'project',
      label: '项目'
    },
    {
      prop: 'costType',
      label: '成本费用类型'
    },
    {
      prop: 'industry',
      label: '行业'
    },
    {
      prop: 'visitType',
      label: '拜访种类'
    },
    {
      prop: 'salesStage',
      label: '销售阶段'
    },
    {
      prop: 'channel',
      label: '渠道'
    },
    {
      prop: 'resourcePosition',
      label: '资源位'
    },
    {
      prop: 'memberType',
      label: '会员类型'
    },
    {
      prop: 'product',
      label: '产品'
    },
    {
      prop: 'region',
      label: '地域'
    },
    {
      prop: 'platform',
      label: '平台'
    },
    {
      prop: 'adType',
      label: '广告类型',
      type: 'input'
    },
    {
      prop: 'responsibleDepartment',
      label: '归口管理部门',

    },
    {
      prop: 'responsiblePerson',
      label: '归口管理人'
    },
    {
      prop: 'dataSourceMethod',
      label: '数据来源方式'
    },
    {
      prop: 'dataSourceSystem',
      label: '数据来源系统'
    },
    {
      prop: 'remark',
      label: '备注',
      attrs: {
        type: 'textarea'
      }
    }
  ]
})
