<template>
  <!-- 租金剩余本金、保证金明细表 -->
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
import { ElMessage } from 'element-plus'
import { reactive, toRefs, onBeforeMount, ref } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import moment from 'moment'
import { changeCellValue } from '@/utils/format'
import RemainRentalDetailsAPI from '@/api/reports/remainRentalDetails'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']
const _optDefault = optionsConfig(router, dictData)

const utableRef = ref(null)
const state = reactive({
  defaultParams: {
    isPaging: 'true',
    bussinessDate: moment().format('YYYY-MM')
  },
  options: { ..._optDefault }
})

const { defaultParams, options } = toRefs(state)

// 动态获取表头
const getDynamicsHeader = async (params) => {
  try {
    const { code, data, msg } = await RemainRentalDetailsAPI.getHeader(params)

    if (+code === 200) {
      const tableColumns = data || []
      const _options = Object.assign({}, _optDefault)

      if (tableColumns.length > 0) {
        tableColumns.forEach((item) => {
          if (item.format !== 'string') {
            item.render = (row) => changeCellValue(row, item, dictData)
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

// 基本表头
const getStaticHeader = () => {
  const _options = { ..._optDefault }
  const tableColumns = [
    {
      label: '会计期间',
      prop: '',
      minWidth: 150,
      format: ''
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 150,
      format: ''
    },
    {
      label: '金额类型',
      prop: 'amountType',
      minWidth: 150,
      format: 'dictTag',
      dictKey: 'tjbb_cash_type'
    },
    {
      label: '会计起租日',
      prop: 'leaseDateStart',
      minWidth: 150,
      format: ''
    },
    {
      label: '起租时金额',
      prop: 'leaseBeforeReceviedAmount',
      minWidth: 150,
      format: 'money'
    },
    {
      label: '币种',
      prop: 'currencyType',
      minWidth: 150,
      format: 'dictTag',
      dictKey: 'sys_currency_type'
    },
    {
      label: '逾期天数',
      prop: 'overdueDays',
      minWidth: 150,
      format: ''
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
    bussinessDate: parseInt(moment().format('YYYYMM')),
    includeTax: 'true',
    amountType: 'remain_principal'
  }
  getStaticHeader()
})

// 刷新列表
const isNumberArr = ['overdueDays']
const refreshList = (val = {}) => {
  const _val = Object.assign({}, val)

  Object.keys(_val).forEach((key) => {
    if (isNumberArr.includes(key)) {
      _val[key + 'Start'] = Number(_val[key][0])
      _val[key + 'End'] = Number(_val[key][1])
    }
  })

  const params = {
    ..._val,
    bussinessDate: moment(_val.bussinessDate.toString()).format('YYYY-MM')
  }
  getDynamicsHeader(params)
  utableRef.value.requestBefore(params)
}
</script>
