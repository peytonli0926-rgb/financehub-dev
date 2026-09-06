<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" >
    <template #operationBtn="{ row }">
        <el-button type="primary"  link size="small" v-permission="permission.view"
          @click="fromToRulePage(row,'detail')">查看详情</el-button>
          <el-button type="primary"  link size="small" v-permission="permission.viewVoucher" :disabled="!row.voucherId"
          @click="fromToRulePage(row,'view')">查看凭证</el-button>
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
const route = useRoute()
const defaultParams = ref({})
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const { setVoucherPage } = useVoucherPage()

const fromToRulePage = ({ voucherId, id }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId })
    return
  }
  router.push(`/debtRestructuringBusiness/longTermReceivablesDetail?id=${id}`)
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.longTermReceivables
})

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    isLatestVersion:'1',
    id
  }
})
</script>
