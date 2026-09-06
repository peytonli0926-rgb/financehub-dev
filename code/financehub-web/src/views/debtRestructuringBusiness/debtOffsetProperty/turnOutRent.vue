<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #contractCode="{ row }">
        <el-button type="primary"  link size="small"
          @click="fromToRulePage(row)">{{row.contractCode}}</el-button>
      </template>
  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTurnOutRent } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const route = useRoute()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTurnOutRent(router, dictData)
defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const fromToRulePage = ({ id }) => {
  router.push({
    name: 'singleContract',
    query: {
      rentRegisterId:id
    }
  })
}

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    isLatestVersion:'1',
    id
  }
})
</script>
