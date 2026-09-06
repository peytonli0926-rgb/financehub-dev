<template>
  <!-- 第三方转让详情 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <!-- 查看凭证  -->
        <el-button
          type="primary"
          v-permission="permission.viewVoucher"
          link
          size="small"
          :disabled="!row.voucherId"
          @click="onDetailFn(row, 'view')"
        >
          查看凭证
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { onBeforeMount, reactive, toRefs, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from '@toystory/lotso'
import { optionsConfig } from './config'
import { useVoucherPage } from '@/hooks'

const route = useRoute()
const { router } = useRouter()
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

  return btnPermissions.thirdTransmitionDetail
})

// 查看凭证
const onDetailFn = ({ voucherId, id }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId })
  }
}

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    state.defaultParams = {
      transferId: query.transferId
    }
  }
})
</script>
