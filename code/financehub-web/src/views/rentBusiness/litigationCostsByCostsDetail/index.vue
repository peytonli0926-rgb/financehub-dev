<template>
    <el-card shadow="never">
    <UTable ref="utableRef" :default-params="defaultParams" :options="options" ></UTable>

  </el-card>
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

const defaultParams = ref({})


onBeforeMount(() => {
  const { courtCostId } = query
  defaultParams.value = { courtCostId }
})
</script>
