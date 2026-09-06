<template>
  <div>
    <UTable
      v-if="options.isIndex"
      ref="utableRef"
      :defaultParams="defaultParams"
      :autoLoad="false"
      :options="options"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          :disabled="!row.voucherIds"
          v-permission="permission.viewVoucher"
          link
          size="small"
          @click="fromToRulePage(row, 'view')"
          >查看凭证</el-button
        >
      </template>
      <template #contractCode="{ row }">
        <div @click="fromToRulePage(row, 'contractCode')">
          <el-link type="primary">
            {{ row.contractCode }}
          </el-link>
        </div>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeMount } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage, useModifyRouteTitle } from '@/hooks'
import { ElMessage } from 'element-plus'
const { setCurrentTabbarTitle } = useModifyRouteTitle()
const defaultParams = ref({})
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const route = useRoute()
const { query } = route.value
const utableRef = ref()
const store = useStore()
const options = ref({})
const fromToRulePage = ({ voucherIds, contractId }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherIds })
  } else {
    if (!contractId) {
      ElMessage.error('合同编号不存在，请检查数据!')
    } else {
      router.push({
        name: 'contractBusinessDetail',
        query: {
          id: contractId
        }
      })
    }
  }
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.currentAmount
})

onBeforeMount(() => {
  const dictData = store.getters['useDictMapping/dictMapping']
  const { periodCodeStart, periodCodeEnd, contractCode, accountCode, orgId, targetType } = query
  defaultParams.value = {
    targetType,
    periodCodeStart: Number(periodCodeStart),
    periodCodeEnd: Number(periodCodeEnd),
    contractCode,
    accountCode,
    orgId,
    queryType: 'query'
  }
  const title = { assist: '本期发生额明细表', account: '本期明细', contract: '本期合同明细' }
  const name = title[targetType]
  setCurrentTabbarTitle(name)
  console.log(
    optionsConfig(router, dictData, targetType, 'optionsConfig(router, dictData,targetType)')
  )
  options.value = optionsConfig(router, dictData, targetType)
})
onMounted(() => {
  console.log(2)
  utableRef.value.requestBefore()
})
</script>

<style lang="scss" scoped></style>
