<template>
  <!-- 未勾稽数据 -->
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" />
</template>

<script setup>
import { reactive, toRefs } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { unrelatedOptionsCfg } from '../config'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: unrelatedOptionsCfg(router, dictData)
})

const { defaultParams, options } = toRefs(state)
</script>
