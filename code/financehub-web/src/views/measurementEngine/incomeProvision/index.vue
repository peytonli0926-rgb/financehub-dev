<template>
  <div class="income-provision-page">
    <el-alert
      class="process-tip"
      title="收益计提处理链路"
      description="选择计提月份并生成本月收益计提，系统根据回收计划、逾期状态、减值阶段和特殊状态确认表内、表外收益；选择已生成的计提记录生成凭证，提交后进入审核流程。"
      type="info"
      :closable="false"
      show-icon
    />
  <UTable ref="utableRef" :options="options">
    <template #operationBtn="{ row }">
      <el-button
        type="primary"
        v-permission="permission.viewVoucher"
        link
        size="small"
        :disabled="row.isGenerateVoucher === '0'"
        @click="fromToRulePage(row, 'view')"
        >查看凭证</el-button
      >
      <el-button
        type="primary"
        v-permission="permission.view"
        link
        size="small"
        @click="fromToRulePage(row, 'detail')"
        >查看详情</el-button
      >
    </template>
  </UTable>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const fromToRulePage = ({ id, batchType }, type) => {
  const url = `/measurementEngine/incomeProvisionDetail?leaseIncomeId=${id}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id })
    return
  }
  router.push(url)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.incomeProvision
})
</script>
<style scoped>
.process-tip {
  margin-bottom: 12px;
  border-color: #f2b6bc;
  background: #fff7f7;
}
</style>
