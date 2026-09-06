<template>
  <!-- 付款单合同详情 -->
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
      <!-- :disabled="!row.url" -->
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.view"
        @click="onDetailFn(row, 'view')"
      >
        查看凭证
      </el-button>
    </template>
  </UTable>
</template>

<script setup>
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useVoucherPage } from '@/hooks'

const { router } = useRouter()
const route = useRoute()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']
const { setVoucherPage } = useVoucherPage()

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.payOrderContractDetail
})

// 查看凭证
const onDetailFn = ({ voucherId }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId, periodCode: route.value.query.periodCode })
  }
  // router.push({
  //   path: '/reports/voucherInquiryReport',
  //   query: {
  //     periodCode: route.value.query.periodCode,
  //     voucherId
  //   }
  // })
}

onBeforeMount(() => {
  const { query } = route.value

  if (Object.keys(query).length > 0) {
    state.defaultParams = {
      paymentIdentifierList: [query.paymentIdentifier]
      // periodCode: query.periodCode
    }
  }
})
</script>
