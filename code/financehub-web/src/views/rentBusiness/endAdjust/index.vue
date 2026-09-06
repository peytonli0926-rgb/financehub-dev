<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.view"
          @click="fromToRulePage(row, 'detail')"
          >查看详情</el-button
        >
        <el-button
          type="primary"
          v-permission="permission.viewVoucher"
          link
          size="small"
          @click="fromToRulePage(row, 'view')"
          >查看凭证</el-button
        >
      </template>
    </UTable>
  </div>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import { optionsConfigPageOne } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigPageOne(router, dictData)
const utableRef = ref()

const route = useRoute()

const defaultParams = ref({})

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.endAdjust
})

// 跳转
const fromToRulePage = ({ id, batchType }, type) => {
  let url = `/rentBusiness/endAdjustDetail?tailDifferenceAdjustmentId=${id}`
  if (type === 'view') {
    url = `/searchBusiness/voucherSummary?batchType=${batchType}&batchId=${id}`
  }
  router.push(url)
}
onBeforeMount(() => {
  const { query } = route.value
  defaultParams.value = {
    id: query?.id,
    businessDate: moment().format('YYYY-MM-DD')
  }
})
</script>
