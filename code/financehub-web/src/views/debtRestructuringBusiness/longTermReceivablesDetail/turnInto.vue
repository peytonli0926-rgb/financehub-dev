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
import { optionsConfigTurnInto } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigTurnInto(router, dictData)


defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const defaultParams = ref({})
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
