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
import moment from 'moment'
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'
import { useStore } from 'vuex'
import { paymentOrderOptionCfg } from '../config'

const { router } = useRouter()
const store = useStore()
const { setVoucherPage } = useVoucherPage()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: paymentOrderOptionCfg(router, dictData)
})
const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.accountReconciliation
})

// 查看凭证
const onDetailFn = ({ voucherId }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId, periodCode: state.defaultParams.periodCode })
  }
  // router.push({
  //   path: '/reports/voucherInquiryReport',
  //   query: {
  //     periodCode: state.defaultParams.periodCode,
  //     voucherId
  //   }
  // })
}

onBeforeMount(() => {
  state.defaultParams = {
    periodCode: parseInt(moment().format('YYYYMM'))
  }
})
</script>
