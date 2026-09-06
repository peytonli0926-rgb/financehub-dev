<template>
    <div>
        <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
            @search-form-data="searchFormData">
        </UTable>
    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { contractSubjectOptionsConfig } from '../config'
import { useRouter } from '@toystory/lotso'
// import { useVoucherPage } from '@/hooks'

// const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = contractSubjectOptionsConfig(router, dictData)
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
// const viewVoucher = ({ voucherId }) => {
//   setVoucherPage({ voucherIdList: [voucherId] })
// }
// 获取按钮权限
// const permission = computed(() => {
//   const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
//   return btnPermissions.contractBusinessDetail
// })

onMounted(() => {
  const { id } = props.query
  defaultParams.value = { id }
  utableRef.value.requestBefore()
})

</script>
