<template>
  <UTable ref="utableRef" :defaultParams="defaultParams"  :options="options" />
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsReportConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsReportConfig(router, dictData)
const route = useRoute()
const { query } = route.value
const defaultParams=ref({})

onBeforeMount(() => {
  const { contractCode, orgId } = query
  defaultParams.value={
    contractCode, orgId
  }
})
</script>
