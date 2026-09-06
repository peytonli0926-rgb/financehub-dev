<template>
    <div>
      <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
        <template #operationBtn="{ row }">
          <el-button type="primary" v-permission="permission.viewVoucher" link size="small" @click="fromToRulePage(row,'view')"  :disabled="!row.voucherId">查看凭证</el-button>
          <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row)">查看详情</el-button>
        </template>
      </UTable>
    </div>
  </template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const defaultParams = ref({

})
const fromToRulePage = (row, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: [row.voucherId] })
    return
  }
  router.push(`/rentBusiness/contractInfoModifyDetail?id=${row.id}`)
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.contractInfoModify
})
onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
