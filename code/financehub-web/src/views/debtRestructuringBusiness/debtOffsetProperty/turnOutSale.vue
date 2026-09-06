<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">

  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTurnOutSale } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const route = useRoute()
const utableRef = ref()
const store = useStore()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTurnOutSale(router, dictData)


onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
