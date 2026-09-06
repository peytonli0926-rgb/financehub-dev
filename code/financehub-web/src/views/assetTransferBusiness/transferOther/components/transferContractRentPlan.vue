<template>
  <!-- 转让合同租金计划 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"> </UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { TransferContractRentPlanOptionCfg } from '../config'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const state = reactive({
  defaultParams: {},
  options: TransferContractRentPlanOptionCfg(router, dictData)
})

const { defaultParams, options } = toRefs(state)
</script>
