<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options"/>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigRansom } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const defaultParams = ref({})
const route = useRoute()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigRansom(router, dictData)

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
