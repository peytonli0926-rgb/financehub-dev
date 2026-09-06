<template>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" 
      />
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { optionsConfigPageOne } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigPageOne(router, dictData)
const utableRef = ref()

const route = useRoute()
const { query } = route.value

const defaultParams = ref({})


onBeforeMount(() => {
  defaultParams.value = {
    ...query
  }
})
</script>
