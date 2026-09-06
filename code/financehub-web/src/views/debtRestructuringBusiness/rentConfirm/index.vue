<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
        <el-button type="primary"  link size="small" v-permission="permission.viewVoucher" :disabled="!row.voucherId"
          @click="fromToRulePage(row, 'view')">查看凭证</el-button>
      </template>

  </UTable>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import moment from 'moment'

const { router } = useRouter()
const utableRef = ref()
const route = useRoute()
const store = useStore()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const { setVoucherPage } = useVoucherPage()

const fromToRulePage = ({ voucherId }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId })
  }
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.rentConfirm
})


onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id,
    accountMonth: moment().format('YYYY-MM')
  }
})
</script>
