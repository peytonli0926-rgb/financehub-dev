<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
      @search-form-data="handlerChange">

      </UTable>

  </div>
</template>
<script setup>
import { ref, onActivated } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'

const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const { router } = useRouter()
const route = useRoute()
const { query } = route.value
const utableRef = ref()
const defaultParams = ref({})
const options = optionsConfig(router, dictData)

const handlerChange = (value) => {
  refreshList(value)
}
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
onActivated(() => {
  const { roleId, roleName } = query
  defaultParams.value = { roleId, roleName }
  refreshList()
})

</script>
