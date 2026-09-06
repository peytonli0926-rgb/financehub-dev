<template>
  <!-- 转让 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"></UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { transferOptionCfg } from '../config'

const { router } = useRouter()
const route = useRoute()
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
  options: transferOptionCfg(router, dictData)
})

const { defaultParams, options } = toRefs(state)

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    // state.defaultParams = {
    //   idList: [query.id]
    // }
  }
})
</script>
