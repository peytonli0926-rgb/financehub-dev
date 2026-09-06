<template>
  <div>
    <UTable ref="utableRef"  @search-form-data="searchFormData" :listBefore="listBefore" :addBefore="listBefore" :options="options" :autoLoad="false"></UTable>

  </div>
</template>
<script setup>
import { ref, watchEffect } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const props = defineProps({
  scene: {
    type: Object,
    default: () => { }
  }
})

const { router } = useRouter()
const utableRef = ref()
const options = ref(optionsConfig(router, dictData))
const indexNum = ref(0)

const listBefore = (val) => {
  const { sceneCode, sceneName, id } = props.scene
  const params = { sceneCode, sceneName, sceneId: id, ...val }
  return params
}
const sceneKeyList = () => {
  const { sceneCode, sceneName, id } = props.scene
  const params = { sceneCode, sceneName, sceneId: id }
  // 避免重复请求
  if (indexNum.value === 0 && utableRef.value && sceneCode) {
    utableRef.value && utableRef.value.requestBefore(params)
    indexNum.value++
  }
}
const searchFormData = (value) => {
  utableRef.value && utableRef.value.requestBefore(value)
}
watchEffect(() => {
  sceneKeyList()
})

defineExpose({
  // sceneKeyList
})
</script>
