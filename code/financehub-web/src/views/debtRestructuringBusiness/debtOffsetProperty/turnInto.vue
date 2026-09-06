<template>
  <UTable ref="utableRef" :defaultParams="defaultParams"  :options="options" >
  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTurnInto } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const route = useRoute()
const defaultParams = ref({})
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTurnInto(router, dictData)



onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
