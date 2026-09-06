<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
      @search-form-data="searchFormData">

      <template #operationBtn="{ row }">
        <el-button type="primary" v-permission="permission.T.viewVoucher" link size="small" @click="viewVoucher(row)" :disabled="!row.voucherId">查看凭证</el-button>
      </template>

    </UTable>

  </div>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { ticketAndJitiOptionsConfig } from '../config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = ticketAndJitiOptionsConfig(router, dictData)
const props = defineProps({
  query: {
    type: Object,
    default: () => {}
  }
})

const defaultParams = ref({})

const searchFormData = (formData) => {
  utableRef.value.requestBefore(formData)
}
const viewVoucher = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: [voucherId] })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.contractBusinessDetail
})
onMounted(() => {
  const { id } = props.query
  defaultParams.value = { id }
  utableRef.value.requestBefore()
})

</script>
