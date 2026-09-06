<template>
  <!-- 租金计划 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"></UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from '@toystory/lotso'
import { optionsConfig } from './config'

const { router } = useRouter()
const route = useRoute()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})

const { defaultParams, options } = toRefs(state)

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    state.defaultParams = {
      convertTransferId: query.convertTransferId,
      oldContractCode: query.oldContractCode
    }
  }
})
</script>
