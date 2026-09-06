<template>
    <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options"
     />
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({
  // balanceDate: '2023-09-25'
})

onBeforeMount(() => {
  const { id } = query
  defaultParams.value = { id }
})

</script>
