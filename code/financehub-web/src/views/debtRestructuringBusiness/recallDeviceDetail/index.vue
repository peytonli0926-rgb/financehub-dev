<template>
    <UTable ref="utableRef" :autoLoad="false" @selection-change="selectionChange" :defaultParams="defaultParams"
      :options="options" @search-form-data="searchFormData">
      <template #operationBtn="{ row }">
        <el-button type="primary" link size="small" @click="fromToRulePage(row)"
          :disabled="!row.voucherId" v-permission="permission.viewVoucher">查看凭证</el-button>
      </template>
    </UTable>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
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
const { setVoucherPage } = useVoucherPage()
const defaultParams = ref({})
const selectedData = ref([])

const searchFormData = (formData) => {
  utableRef.value.requestBefore(formData)
}

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId })
}
const selectionChange = (data) => {
  selectedData.value = data
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.recallDeviceDetail
})

onActivated(() => {
  const { orgId, inboundDate } = query
  defaultParams.value = { orgId, inboundDate }
  utableRef.value.requestBefore()
})

</script>
