<template>
  <UTable ref="utableRef" :defaultParams=defaultParams :options="options" @search-form-data="searchFormData">
    <template #operationBtn="{ row }">
        <el-button type="primary"  link size="small" v-permission="permission.viewVoucher" :disabled="!row.voucherId"
          @click="fromToRulePage(row, 'view')">查看凭证</el-button>
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
const route = useRoute()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const formValue = ref({})
const { setVoucherPage } = useVoucherPage()
const searchFormData = (formData) => {
  formValue.value = formData
}
const fromToRulePage = ({ voucherId }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId })
  }
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.singleContract
})

const defaultParams = ref({})

onBeforeMount(() => {
  const { rentRegisterId} = route.value.query
  defaultParams.value = {
    rentRegisterIdList: [rentRegisterId],
  }

  // refreshList()
})
</script>
