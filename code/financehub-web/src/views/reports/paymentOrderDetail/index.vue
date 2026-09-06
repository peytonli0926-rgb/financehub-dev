<template>
  <!-- 付款单详情 -->
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #operationBtn="{ row }">
      <!-- :disabled="!row.url" -->
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.view"
        @click="onDetailFn(row)"
      >
        查看合同详情
      </el-button>
    </template>
  </UTable>
</template>

<script setup>
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { optionsConfig } from './config'

const { router } = useRouter()
const route = useRoute()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.paymentOrderDetail
})

// 查看合同详情
const onDetailFn = ({ paymentIdentifier }) => {
  router.push({
    path: '/reports/payOrderContractDetail',
    query: {
      paymentIdentifier,
      periodCode: route.value.query.periodCode
    }
  })
}

onBeforeMount(() => {
  const { query } = route.value

  state.defaultParams = {
    orgId: query.orgId,
    periodCode: query.periodCode
  }
})
</script>
