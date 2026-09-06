<template>
  <!-- 资金占用成本明细 -->
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
import moment from 'moment'
import { ElMessage } from 'element-plus'
import { reactive, toRefs, ref, onBeforeMount } from 'vue'
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
  options: {
    ..._optDefault
  }
})

const { defaultParams, options } = toRefs(state)

const dictObj = {
  orgId: 'company',
  currencyType: 'sys_currency_type',
  includeTax: 'tax_yes_no'
}

// 动态获取表头
const getDynamicsHeader = async (params) => {
  try {
    const { code, data, msg } = await RemainRentalDetailsAPI.getHeaderTitle(params)

    if (+code === 200) {
      const tableColumns = data || []
      const _options = Object.assign({}, _optDefault)

      if (tableColumns.length > 0) {
        tableColumns.forEach((item) => {
          if (['clientName', 'dept', 'orgId'].includes(item.prop)) {
            item.tooltip = true
            item.minWidth = 180
          }
          if (['orgId', 'currencyType', 'includeTax', 'businessName'].includes(item.prop)) {
            item.format = ['orgId'].includes(item.prop) ? 'dict' : 'dictTag'
            item.dictKey = dictObj[item.prop]
            item.render = (row) => changeCellValue(row, item, dictData)
          }
          if (item.format !== 'string' && item.prop !== 'leaseDateStart') {
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
      label: '签约主体',
      prop: 'orgId',
      minWidth: 150,
      format: '',
      tooltip: true
    },
    {
      label: '所属部门',
      prop: 'dept',
      minWidth: 150,
      format: '',
      tooltip: true
    },
    {
      label: '合同编号',
      prop: 'contractCode',
      minWidth: 150,
      format: ''
    },
    {
      label: '承租人',
      prop: 'clientName',
      minWidth: 150,
      format: '',
      tooltip: true
    },
    {
      label: '会计起租日',
      prop: 'leaseDateStart',
      minWidth: 150,
      format: 'date'
    },
    {
      label: '业务类型',
      prop: 'businessName',
      minWidth: 150,
      format: '',
      dictKey: null
    },
    {
      label: '税率',
      prop: 'taxRate',
      minWidth: 150,
      format: 'money'
    },
    {
      label: '币种',
      prop: 'overdueDays',
      minWidth: 150,
      format: 'dictTag',
      dictKey: 'sys_currency_type'
    },
    {
      label: '资金成本',
      prop: 'zjzycb',
      minWidth: 150,
      format: 'money'
    },
    {
      label: '是否含税',
      prop: 'includeTax',
      minWidth: 150,
      format: 'dictTag',
      dictKey: 'tax_yes_no'
    }
    // {
    //   label: '逾期天数',
    //   prop: 'overdueDays',
    //   minWidth: 150,
    //   format: ''
    // }
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
    includeTax: 'true'
    // contractCodeList: ['L23B106263'] // 暂时先给个合同号
  }
  getStaticHeader()
})

// 搜索
const isNumberArr = ['overdueDays', 'decZijcb']
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
    clientName: _val.clientName ? _val.clientName.join(',') : '',
    bussinessDate: moment(_val.bussinessDate.toString()).format('YYYY-MM')
  }

  getDynamicsHeader(params)
  utableRef.value.requestBefore(params)
}
</script>
