<template>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" >
      <template #operationBtn="{ row }">
        <el-button type="primary" :disabled="row.voucherId === null"  v-permission="permission.viewVoucher" link size="small" @click="fromToRulePage(row, 'view')">查看凭证</el-button>
        <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row,'detail')">查看详情</el-button>
      </template>
    </UTable>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const route = useRoute()
const dictData = store.getters['useDictMapping/dictMapping']
const defaultParams = ref({})
const options = optionsConfig(router, dictData)
const { setVoucherPage } = useVoucherPage()
// const selectedData = ref([])
const fromToRulePage = ({ id, batchType, contractCode }, type) => {
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id })
    return
  }
  router.push(`/rentBusiness/offlineContractEnterDetail?contractCode=${contractCode}&id=${id}`)
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.offlineContractEnter
})
onBeforeMount(() => {
  const { query } = route.value
  defaultParams.value = {
    id: query.id
  }
})
</script>
