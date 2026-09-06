<template>
  <UTable ref="utableRef" :options="options">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" v-permission="permission.viewVoucher" :disabled="row.isGenerateVoucher==='0'" @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      <el-button type="primary" link size="small" v-permission="permission.view" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'


defineProps({
  permission:{
    type:Object,
    default:()=>({})
  }
})


const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const fromToRulePage = ({ batchType, id, accountDate }, type) => {
  const url = `/rentBusiness/litigationCostsByCostsDetail?courtCostId=${id}&accountDate=${accountDate}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id })
    return
  }
  router.push(url)
}


</script>
