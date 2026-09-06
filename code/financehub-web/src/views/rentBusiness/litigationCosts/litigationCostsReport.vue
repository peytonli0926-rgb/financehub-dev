<template>
  <UTable ref="utableRef"  :options="options">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" v-permission="permission.view" @click="fromToRulePage(row)">查看详情</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsReportConfig } from './config'
import { useRouter } from '@toystory/lotso'

defineProps({
  permission:{
    type:Object,
    default:()=>({})
  }
})
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsReportConfig(router, dictData)

const fromToRulePage = ({ contractCode, orgId }) => {
  const url = `/rentBusiness/litigationCostsReportDetail?contractCode=${contractCode}&orgId=${orgId}`
  router.push(url)
}


</script>
