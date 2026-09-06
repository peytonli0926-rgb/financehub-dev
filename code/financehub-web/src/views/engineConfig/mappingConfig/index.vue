<template>
  <div>
    <UTable  ref="utableRef"  :options="options" >
    </UTable>

  </div>
</template>
<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

</script>

<style scoped lang="scss">
:deep(.el-input__wrapper) {
    width: 95.5% !important;
}
</style>
