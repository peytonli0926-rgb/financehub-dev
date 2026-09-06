<template>
  <UTable ref="utableRef"  :options="options" @search-form-data="searchFormData">

  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigRentPlan } from './config'
import { useRouter } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigRentPlan(router, dictData)

</script>
