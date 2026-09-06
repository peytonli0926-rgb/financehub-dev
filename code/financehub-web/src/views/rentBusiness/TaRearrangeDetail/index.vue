<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        link
        size="small"
         :disabled="!row.voucherId"
        v-permission="permission.viewVourcher"
        @click="fromToRulePage(row)"
        >查看凭证</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({
  // balanceDate: '2023-09-25'
})
const { setVoucherPage } = useVoucherPage()
const fromToRulePage = ({ contractId, voucherId }, type) => {
  setVoucherPage({ voucherIdList: voucherId })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.TaRearrangeDetail
})
onBeforeMount(() => {
  const { id } = query
  defaultParams.value = { taReclassificationIdList: [id] }
})
</script>
