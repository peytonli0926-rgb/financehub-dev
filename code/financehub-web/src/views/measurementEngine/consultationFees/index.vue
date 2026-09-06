<template>
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
        link
        size="small"
        v-permission="permission.view"
        @click="fromToRulePage(row, 'detail')"
        >查看详情</el-button
      >
    </template>
    <template #operateHeaderLeft>
      <div class="u-flex u-col-center">
        <span style="flex: 0 0 60px" class="u-font-12"> 分摊比例:</span>
        <el-input
          size="small"
          type="number"
          placeholder="请输入分摊比例"
          v-model="allocationRatio"
          style="width: 200px"
        >
          <template #suffix> %</template></el-input
        >
      </div>
    </template>
    <template #operateHeaderRight>
      <el-button
        @click="handleMeasurement"
        :icon="Document"
        v-permission="permission.cesuan"
        type="success"
        >测算</el-button
      >
    </template>
  </UTable>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { confirmEl } from '@/utils'
import { measurementServiceFee } from '@/api/rentBusiness/consultationFees'
import { ElMessage } from 'element-plus'
import { useVoucherPage } from '@/hooks'
import { Document } from '@element-plus/icons-vue'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const allocationRatio = ref('')

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.consultationFees
})

const fromToRulePage = ({ batchType, id }, type) => {
  const url = `/rentBusiness/consultationFeesDetail?serviceFeeId=${id}`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id })
    return
  }
  router.push(url)
}

// 测算
const handleMeasurement = () => {
  if (allocationRatio.value) {
    confirmEl('您确定进行分摊比例操作吗？').then(async () => {
      const _params = await utableRef.value.getSearchParams()
      const { code, msg } = await measurementServiceFee({
        allocationRatio: allocationRatio.value,
        ..._params
      })
      if (code === 200) {
        ElMessage.success('操作成功')
        refreshList()
      } else {
        ElMessage.success(msg)
      }
    })
    return
  }
  ElMessage.error('分摊比例不能为空！')
}
// 刷新列表
const refreshList = () => {
  utableRef.value.requestBefore()
}
</script>
