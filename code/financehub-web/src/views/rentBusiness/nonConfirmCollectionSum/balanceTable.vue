<template>
  <div>
    <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options">
      <!-- <template #operationBtn="{ row }">
        <el-button type="primary" link size="small" @click="fromToRulePage(row)">查看明细</el-button>
      </template> -->
    </UTable>

  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigBalanceTable } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigBalanceTable(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({
})
onMounted(() => {
  const { id } = query
  defaultParams.value = {
    id
  }
  // utableRef.value.requestBefore()
})

</script>
