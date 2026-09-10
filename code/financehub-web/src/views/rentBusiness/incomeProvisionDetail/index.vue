<template>
    <div>
        <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options"
            @search-form-data="searchFormData">
            <template #operationBtn="{ row }">
              <el-button type="primary" v-permission="permission.viewVoucher" link size="small" @click="fromToRulePage(row,'view')" :disabled="!row.voucherId">查看凭证</el-button>
                <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row)">查看详情</el-button>
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
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value
const { setVoucherPage } = useVoucherPage()
const defaultParams = ref({})

const fromToRulePage = ({ voucherId, contractCode }, type) => {
  const url = `/incomeProvisionAccess/incomeProvisionPlan?contractCode=${contractCode}`
  if (type === 'view') {
    setVoucherPage({ voucherIdList: [voucherId] })
    return
  }
  router.push(url)
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.incomeProvisionDetail
})

onBeforeMount(() => {
  const { leaseIncomeId } = query
  defaultParams.value = { leaseIncomeId }
})

</script>
