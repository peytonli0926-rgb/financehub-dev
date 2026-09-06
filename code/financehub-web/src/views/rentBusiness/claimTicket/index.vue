<template>
    <UTable ref="utableRef" :options="options" >
        <template #operationBtn="{ row }">
            <el-button type="primary" :disabled="row.processStatus==='1'" v-permission="permission.inputVoucher" link size="small" @click="fromToRulePage(row, 'detail')">录入凭证</el-button>
            <el-button type="primary" v-permission="permission.viewVoucher" link size="small" :disabled="!row.manualId" @click="fromToRulePage(row, 'view')">查看凭证</el-button>
        </template>
    </UTable>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
// import { useVoucherPage } from '@/hooks'
import moment from 'moment'

// const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.claimTicket
})
const fromToRulePage = ({ manualId, orgId, documentDate, id }, type) => {
  let url = `/customVoucher/customVoucherAdd?businessDate=${moment(documentDate).format('YYYY-MM-DD')}&orgId=${orgId||''}&externalId=${id||''}&sourceFrom=KJFP`
  if (type === 'view') {
    // setVoucherPage({ voucherIdList: [manualId] })url
    url=`/customVoucher/index?id=${manualId}`
  }
  router.push(url)
}
</script>
