<template>
  <!-- 折价转让详情 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          :disabled="!row.voucherId"
          v-permission="permission.viewVoucher"
          link
          size="small"
          @click="onDetailFn(row, 'view')"
        >
          查看凭证
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs, onBeforeMount, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from '@toystory/lotso'
import { optionsConfig } from './config'
import { useVoucherPage } from '@/hooks'

const { router } = useRouter()
const route = useRoute()
const store = useStore()
const { setVoucherPage } = useVoucherPage()

const dictData = store.getters['useDictMapping/dictMapping']

const state = reactive({
  defaultParams: {},
  options: optionsConfig(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.discountTransferDetail
})

// 查看凭证
const onDetailFn = ({ voucherId }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId })
  }
}

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    state.defaultParams = {
      convertTransferId: query.id
    }
  }
})
</script>
