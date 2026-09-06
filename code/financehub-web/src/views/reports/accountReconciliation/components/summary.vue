<template>
  <!-- 汇总页面 -->
  <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
    <template #periodCode="{ row }">
      <div>{{ changePeriodCodeLabel(row.periodCode) }}</div>
    </template>

    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.pay.view"
        @click="onDetailFn(row)"
      >
        查看付款详情
      </el-button>
    </template>
  </UTable>
</template>

<script setup>
import moment from 'moment'
import { ElMessage } from 'element-plus'
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { getCommonTableList } from '@/api/common'
import { summaryOptionCfg } from '../config'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: summaryOptionCfg(router, dictData),
  periodCodeOption: []
})
const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.accountReconciliation
})

// 查看付款详情
const onDetailFn = ({ orgId, periodCode }) => {
  router.push({
    path: '/reports/paymentOrderDetail',
    query: {
      orgId,
      periodCode
    }
  })
}

// 获取会计期间字典
const customRequest = async () => {
  try {
    const { data, code } = await getCommonTableList({
      url: '/engine/scene/account-period/queryAll',
      method: 'post'
    })
    if (code === 200) {
      state.periodCodeOption = data
    }
  } catch (error) {
    ElMessage.error('系统错误')
  }
}

// 处理表格会计期间回显
const changePeriodCodeLabel = (prop) => {
  let _periodName = ''
  if (prop) {
    _periodName = state.periodCodeOption.filter((item) => item.periodCode === Number(prop))[0]
      .periodName
  }

  return _periodName
}

onBeforeMount(() => {
  state.defaultParams = {
    periodCode: parseInt(moment().format('YYYYMM'))
  }
  customRequest()
})
</script>
