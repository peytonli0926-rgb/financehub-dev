<template>
    <div>
      <UTable v-if="dictReady" ref="utableRef" :options="options"></UTable>

    </div>
  </template>
<script setup>
import { onBeforeMount, ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictReady = ref(false)
const options = ref(optionsConfig(router, {}))

onBeforeMount(async () => {
  const dictData = await store.dispatch('useDictMapping/refreshDictMapping')
  options.value = optionsConfig(router, dictData)
  dictReady.value = true
})

</script>
