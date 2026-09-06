import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  hidden: true,
  title: '角色数据权限',
  icon: '',
  name: 'rolesDataAuth'
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
  request: { // 请求参数
    list: {
      url: '/admin/sys-internal-role-org/page',
      method: 'post'
    },
    add: {
      url: '/admin/sys-internal-role-org/save',
      method: 'post'
    },
    edit: {
      url: '/admin/sys-internal-role-org/update/$[id]',
      method: 'post'
    },
    del: {
      url: '/admin/sys-internal-role-org/delete/$[id]',
      method: 'post'
    }
  },
  btnConfig: {
    add: {
      isDisabled: false,
      type: 'primary',
      title: '新增'
    }
  },
  columns: [

    { // 展示数据
      prop: 'roleName',
      label: '角色名称',
      attrs: {
        ADD: true,
        EDIT: true
      },
      rules: [{
        required: true,
        message: '请输入角色名称',
        trigger: 'change'
      }]
    }, {
      prop: 'orgId',
      label: '签约主体',
      type: 'select',
      search: true,
      tooltip: true,
      hide: true,
      relation: [{
        prop: 'orgName',
        selectKey: 'label'
      }],
      attrs: {
        filterable: true
      },
      rules: [{
        required: true,
        message: '请选择签约主体',
        trigger: 'change'
      }],
      option: dictMappingToArray(dictData, 'company'),
      render (row) {
        return dictMappingLabel(dictData, 'company', row.orgId)
      }
    }, {
      prop: 'orgName',
      label: '签约主体名称',
      display: false,
      tooltip: true,
      attrs: {
        disabled: true
      }
    }, {
      prop: 'operation',
      width: 220,
      label: '操作',
      display: false
    }]
})
