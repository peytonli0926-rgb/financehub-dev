<template>
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
        <el-button type="primary"  link size="small" v-permission="permission.viewVoucher" :disabled="!row.voucherId"
          @click="fromToRulePage(row)">查看凭证</el-button>
      </template>

  </UTable>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigTurnOutSale } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTurnOutSale(router, dictData)

const route = useRoute()
const defaultParams = ref({})
const { setVoucherPage } = useVoucherPage()
defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId })
}

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    longRegisterIdList: [id]
  }
})
</script>
