<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-21 15:53:58
 * @LastEditTime: 2024-06-06 21:38:38
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <UTable ref="utableRef"  :options="options">
    <template #operationBtn="{ row }">
      <el-button type="primary" link size="small"
        @click="fromToRulePage(row)" v-permission="permission.viewVoucher">查看凭证</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigOutbound } from './config'
import { useRouter } from '@toystory/lotso'

import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigOutbound(router, dictData)

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId },"total")
}

</script>
