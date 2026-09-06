<template>
  <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
    @search-form-data="searchFormData">

  </UTable>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { recollectPlanOptionsConfig } from '../config'

const formValue = ref({})

const utableRef = ref()
const props = defineProps({
  query: {
    type: Object,
    default: () => {}
  }
})
const options = recollectPlanOptionsConfig(props.query)
const defaultParams = ref({})
const searchFormData = (formData) => {
  formValue.value = formData
  utableRef.value.requestBefore(formData)
}

onMounted(() => {
  const { id } = props.query
  defaultParams.value = { id }
  utableRef.value.requestBefore()
})

</script>
