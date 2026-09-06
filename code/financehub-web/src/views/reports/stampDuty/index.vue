<template>
  <!-- 印花税 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"></UTable>
  </div>
</template>

<script setup>
import { toRefs, reactive } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})
const { defaultParams, options } = toRefs(state)
</script>

<style lang="scss" scoped></style>
