<template>
  <!-- 已勾稽数据 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <!-- :disabled="!row.url" -->
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.detail"
          @click="onDetailFn(row)"
        >
          查看批扣详情
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import moment from 'moment'
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { verifiedOptionsCfg } from '../config'

const { router } = useRouter()
const store = useStore()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: verifiedOptionsCfg(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.batchDeductionMapping
})

// 查看批扣详情
const onDetailFn = ({ matchNumber }) => {
  router.push({
    path: '/reports/batchDeductionDetails',
    query: {
      matchNumber,
      periodCode: state.defaultParams.periodCode
    }
  })
}

onBeforeMount(() => {
  state.defaultParams = {
    periodCode: parseInt(moment().format('YYYYMM'))
  }
})
</script>
