<template>
  <UTable ref="utableRef" :defaultParams="defaultParams"  :options="options"
   >
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small" :disabled="!row.voucherIds"
        @click="fromToRulePage(row)">查看凭证</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { gpsConfig } from '../config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const defaultParams = ref({
  expenseMainCategoryType: '1'
})
const { router } = useRouter()
const { setVoucherPage } = useVoucherPage()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = gpsConfig(router, dictData)

const fromToRulePage = ({ voucherIds }) => {
  setVoucherPage({ voucherIdList: voucherIds.split(',') })
}


</script>
