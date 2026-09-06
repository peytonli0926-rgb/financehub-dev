<template>
  <!-- 收益全生命周期明细 -->
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :autoLoad="false"
      :options="options"
      @search-form-data="refreshList"
    ></UTable>
  </div>
</template>

<script setup>
import { ElMessage, ElTag } from 'element-plus'
import moment from 'moment'
import { reactive, toRefs, ref, onBeforeMount, h } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { changeCellValue } from '@/utils/format'
import RemainRentalDetailsAPI from '@/api/reports/remainRentalDetails'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']
const _optDefault = optionsConfig(router, dictData)

const utableRef = ref(null)
const state = reactive({
  defaultParams: {},
  options: { ..._optDefault }
})

const { defaultParams, options } = toRefs(state)

const isNumberArr = [
  'overdueDays',
  'plannedTotalAmount',
  'apportionTotalAmount',
  'diffAmount',
  'xirr'
]

// 基本表头
const getStaticHeader = () => {
  const _options = { ..._optDefault }
  const tableColumns = [
    {
      label: '签约主体',
      prop: 'orgId',
      minWidth: 200,
      tooltip: true,
      format: 'dict',
      dictKey: 'company'
    },
    {
      prop: 'contractCode',
      label: '合同编号',
      minWidth: 150,
      tooltip: true
    },
    {
      prop: 'incomeType',
      label: '收入类型',
      minWidth: 150,
      format: 'dictTag',
      dictKey: 'allocation_method'
    },
    {
      prop: 'xirr',
      label: '财务XIRR',
      minWidth: 150,
      format: 'money'
    },
    {
      prop: 'currency',
      label: '币种',
      minWidth: 150
    },
    {
      prop: 'includeTax',
      label: '是否含税',
      minWidth: 150
    },
    {
      prop: 'taxRate',
      label: '税率',
      minWidth: 150
    },
    {
      prop: 'overdueDays',
      label: '逾期天数',
      minWidth: 150
    },
    {
      prop: 'plannedTotalAmount',
      label: '计划总额',
      minWidth: 150
    },
    {
      prop: 'apportionTotalAmount',
      label: '分摊总额',
      minWidth: 150
    },
    {
      prop: 'diffAmount',
      label: '差额',
      minWidth: 150
    },
    {
      prop: 'contractStatus',
      label: '合同状态',
      minWidth: 150
    },
    {
      prop: 'financialContractStatus',
      label: '特殊合同状态',
      minWidth: 150
    },
    {
      prop: 'incomeProvisionMethod',
      label: '计提方式',
      minWidth: 150
    },
    {
      prop: 'beforeEarnings',
      label: '所选期间之前收益',
      minWidth: 180
    },
    {
      prop: 'afterEarnings',
      label: '所选期间之后收益',
      minWidth: 180
    }
  ]

  tableColumns.forEach((item) => {
    if (item.format) {
      item.render = (row) => changeCellValue(row, item, dictData)
    }
  })

  Reflect.set(state.options, 'columns', [..._options.columns, ...tableColumns])
}

onBeforeMount(() => {
  state.defaultParams = {
    periodCode: parseInt(moment().format('YYYYMM'))
  }
  getStaticHeader()
})

const tagArr = [
  'includeTax',
  'contractStatus',
  'financialContractStatus',
  'incomeProvisionMethod',
  'incomeType'
]

// 动态获取表头
const getDynamicsHeader = async (params) => {
  try {
    const { code, data, msg } = await RemainRentalDetailsAPI.getFullLifeTitle(params)

    if (+code === 200) {
      const tableColumns = data || []
      const _options = Object.assign({}, _optDefault)
      if (tableColumns.length > 0) {
        tableColumns.forEach((item) => {
          if (item.format !== 'string' && item.prop !== 'incomeType') {
            item.render = (row) => (row[item.prop] && changeCellValue(row, item, dictData)) || ''
          } else if (tagArr.includes(item.prop)) {
            item.render = (row) => (row[item.prop] && h(ElTag, () => row[item.prop])) || ''
          }
          if (['clientName', 'orgId'].includes(item.prop)) {
            item.tooltip = true
          }
        })
      }

      Reflect.set(state.options, 'columns', [..._options.columns, ...tableColumns])
    } else {
      ElMessage.error(msg)
    }
  } catch (error) {
    throw new Error('动态获取表头', error)
  }
}

// 刷新列表
const refreshList = (val = {}) => {
  const _val = Object.assign({}, val)

  Object.keys(_val).forEach((key) => {
    if (isNumberArr.includes(key)) {
      _val[key + 'Start'] = Number(_val[key][0])
      _val[key + 'End'] = Number(_val[key][1])
    }
  })

  const params = {
    ..._val
  }

  getDynamicsHeader(params)
  utableRef.value.requestBefore(params)
}
</script>
