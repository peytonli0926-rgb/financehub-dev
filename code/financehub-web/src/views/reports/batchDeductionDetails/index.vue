<template>
  <!-- 批扣详情 -->
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" />
</template>

<script setup>
import { reactive, toRefs, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
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
      matchNumber: query.matchNumber,
      periodCode: Number(query.periodCode) // 202411
    }
  }
})
</script>
