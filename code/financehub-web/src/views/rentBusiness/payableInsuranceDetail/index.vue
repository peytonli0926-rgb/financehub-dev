<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
      @selection-change="selectionChange" @search-form-data="searchFormData">
      <template #operationBtn="{ row }">
        <el-button type="primary" link size="small" :disabled="!row.voucherId" v-permission="permission.viewVoucher" @click="viewVoucher(row)">查看凭证</el-button>
      </template>

    </UTable>

  </div>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()

const defaultParams = ref({})
const selectedData = ref([])
const route = useRoute()
const { query } = route.value
const { setVoucherPage } = useVoucherPage()

const searchFormData = (formData) => {
  utableRef.value.requestBefore({ ...formData })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.payableInsuranceDetail
})

// 多选数据
const selectionChange = (data) => {
  selectedData.value = data
}
const viewVoucher = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId })
}
onActivated(() => {
  defaultParams.value = { ...query }
  utableRef.value.requestBefore(query)
})
</script>
