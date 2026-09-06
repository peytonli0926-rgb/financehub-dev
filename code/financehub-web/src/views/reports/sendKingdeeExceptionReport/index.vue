<template>
  <!-- 传送金蝶异常报表 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <!-- <template #accountNumber="{ row }">
        <div>{{ changePeriodCodeLabel(row.accountNumber) + '(' + row.accountNumber + ')' }}</div>
      </template> -->
    </UTable>
  </div>
</template>

<script setup>
import moment from 'moment'
// import { ElMessage } from 'element-plus'
import { reactive, toRefs, onBeforeMount } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
// import { getCommonTableList } from '@/api/common'
import { optionsConfig } from './config'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData),
  accountNumberOption: []
})

const { defaultParams, options } = toRefs(state)

// 科目代码
// const getAccountNumber = async () => {
//   try {
//     const { data, code } = await getCommonTableList({
//       url: '/engine/scene/account/listAll',
//       method: 'post'
//     })
//     if (code === 200) {
//       state.accountNumberOption = data
//     }
//   } catch (error) {
//     ElMessage.error('系统错误')
//   }
// }

// // 处理表格会计期间回显
// const changePeriodCodeLabel = (prop) => {
//   let _periodName = ''

//   if (prop) {
//     _periodName = state.accountNumberOption.filter((item) => item.accountCode === prop)[0]
//       .accountName
//   }

//   return _periodName
// }

onBeforeMount(() => {
  state.defaultParams = {
    periodNumber: [moment().format('MM')],
    periodYear: moment().format('YYYY')
  }
})
</script>
