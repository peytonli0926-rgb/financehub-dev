<template>
  <div>
    <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options"
     @search-form-data="searchFormData">

     <template #operationBtn="{ row }">
          <el-button type="primary" v-permission="permission.viewVoucher" :disabled="!row.voucherIds" link size="small" @click="fromToRulePage(row)">查看凭证</el-button>
        </template>

    </UTable>

  </div>
</template>
<script setup>
import { ref,onBeforeMount ,computed} from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'
const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
const route = useRoute()
const { query } = route.value

const defaultParams = ref({})

const { setVoucherPage } = useVoucherPage()



const fromToRulePage = ({ voucherIds }) => {
  setVoucherPage({ voucherIdList: voucherIds })
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.endAdjustDetail
})
onBeforeMount(() => {
  const { tailDifferenceAdjustmentId } = query
  defaultParams.value = { tailDifferenceAdjustmentId }
})

</script>
