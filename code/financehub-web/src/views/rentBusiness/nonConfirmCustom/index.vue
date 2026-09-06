<template>
  <div>
    <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options">

      <template #operationBtn="{ row }">
        <el-button type="primary" :disabled="!row.manualVoucherIds" v-permission="permission.customVoucher" link size="small" @click="fromToRulePage(row,'custom')">查看手工凭证</el-button>
        <el-button type="primary" :disabled="!row.writeOffVoucherId" v-permission="permission.viewVoucher" link size="small" @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      </template>

    </UTable>

  </div>
</template>
<script setup>
import { ref, onBeforeMount, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value
const defaultParams = ref({})

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.nonConfirmCustom
})
const fromToRulePage = ({ manualVoucherIds, writeOffVoucherId }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: writeOffVoucherId })
    return
  }
  router.push(`/customVoucher/index?idList=${manualVoucherIds || ''}`)
}
onBeforeMount(() => {
  const { sumId, detailId } = query
  defaultParams.value = {
    sumId,
    detailId
  }
})

</script>
