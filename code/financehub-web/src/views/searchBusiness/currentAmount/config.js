import { h } from 'vue'
import { ElTag } from 'element-plus'
import { dictMappingToArray, dictMappingLabel } from '@/utils'

export default {
  title: '本期发生额',
  hidden: true,
  icon: '',
  name: 'currentAmount'
}

export const optionsConfig = (router, dictData, targetType) => {
  const _column = [
    {
      // 展示数据
      prop: 'clientName',
      label: '客户名称',
      width: 200
    },
    {
      // 展示数据
      prop: 'contractCode',
      label: '合同编号',
      width: 200,
      slot: true
    }
  ]
  const insertColumn = {
    assist: _column,
    account: [],
    contract: [_column[1]]
  }

  return {
    isIndex: true, // 是否需要序号
    isSelection: false, // 是否需要多选
    isPagination: true, // 是否需要翻页
    isBorder: true,
    isOperateHeader: true, // 是否需要table头部操作区域，
    rowKey: 'id', // 表格唯一id
    leftCardName: '',
    isSearch: false,
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
        url: '/engine/finance/account-assist-balance/assist/currentBalancePage',
        method: 'post'
      }
    },
    btnConfig: {
      export: {
        title: '导出',
        type: 'primary',
        url: '/engine/finance/account-assist-balance/exportCurrentBalance',
        filename: '核算项目余额表-本期发生额.xlsx',
        isDisabled: false,
        params: {
          queryType: 'export',
          targetType
        },
        isParams: true
      }
    },
    columns: [
      {
        // 展示数据1
        prop: 'accountCode',
        label: '科目代码',
        width: 200
      },
      {
        // 展示数据1
        prop: 'accountName',
        label: '科目名称',
        width: 200
      },
      {
        // 展示数据
        prop: 'businessCode',
        label: '业务类型',
        width: 200,
        type: 'select',
        option: dictMappingToArray(dictData, 'contract_business_type'),
        render(row) {
          return (
            (row.systemCode &&
              h(ElTag, () =>
                dictMappingLabel(dictData, 'contract_business_type', row.businessCode)
              )) ||
            ''
          )
        }
      },
      {
        // 展示数据
        prop: 'voucherDate',
        label: '记账日期',
        width: 200
      },
      {
        prop: 'orgId',
        label: '签约主体',
        type: 'select',
        tooltip: true,
        minWidth: 250,
        option: dictMappingToArray(dictData, 'company'),
        render(row) {
          return dictMappingLabel(dictData, 'company', row.orgId)
        }
      },
      {
        prop: 'voucherSummary',
        label: '凭证摘要',
        tooltip: true,
        minWidth: 250
      },

      ...insertColumn[targetType],

      {
        prop: 'fundType3',
        label: '本期发生额',
        align: 'center',
        children: [
          {
            prop: 'debitAmount',
            label: '借方',
            width: 200
          },
          {
            prop: 'creditAmount',
            label: '贷方',
            width: 200
          }
        ]
      },
      {
        prop: 'operation',
        label: '操作',
        align: 'center',
        width: 120,
        display: false,
        fixed: 'right'
      }
    ]
  }
}
