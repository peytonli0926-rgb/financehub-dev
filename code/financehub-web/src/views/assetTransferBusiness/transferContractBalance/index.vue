<template>
  <!-- 转让合同余额 -->
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

        <!-- 查看详情  -->
        <el-button
          type="primary"
          v-permission="permission.view"
          link
          size="small"
          @click="onDetailFn(row, 'detail')"
        >
          查看详情
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs, computed, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'
import { optionsConfig } from './config'

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

  return btnPermissions.transferContractBalance
})

// 查看凭证、查看详情
const onDetailFn = ({ voucherId, id }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId }, 'total')
  }
  router.push({
    path: '/assetTransferBusiness/transContractBalanceDetail',
    query: { id }
  })
}

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    // state.defaultParams = {
    //   idList: [query.id]
    // }
  }
})
</script>

<style lang="scss" scoped></style>
