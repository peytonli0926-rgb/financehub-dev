<template>
  <UTable ref="utableRef" :options="options">
    <template #operationBtn="{ row }">
      <!-- {{ permission }} -->
      <el-button
        type="primary"
        link
        size="small"
        v-permission="permission.view"
        :disabled="!row.url && row.documentType !== 'SYJT'"
        @click="fromToRulePage(row, 'detail')"
        >查看详情</el-button
      >
      <el-button
        type="primary"
        link
        size="small"
        @click="fromToRulePage(row, 'view')"
        v-permission="permission.voucher"
        v-if="row.documentType !== 'TSHT'"
        >查看凭证</el-button
      >

      <el-button
        @click="handleApprovePass([row.id])"
        link
        type="success"
        v-permission="permission.agree"
        >同意</el-button
      >
      <el-button
        @click="handleApproveRefuse([row.id])"
        link
        type="danger"
        v-permission="permission.reject"
        >拒绝</el-button
      >
    </template>
    <!-- <template #operateHeaderRight>
            <el-button @click="handleApprovePass()" v-permission="permission.agree" :disabled="isBoolReactive.exportDisabled" type="success">批量同意</el-button>
            <el-button @click="handleApproveRefuse()" v-permission="permission.reject" :disabled="isBoolReactive.exportDisabled" type="danger">批量拒绝</el-button>
        </template> -->
  </UTable>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { waitaApproveOptionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { confirmEl } from '@/utils'
import { portalApproveRefuse, portalApprovePass } from '@/api/portalEntry'
import { ElMessage } from 'element-plus'
import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
// const {billList} = inject('btnPermission')
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = waitaApproveOptionsConfig(router, dictData)

const fromToRulePage = ({ documentId, documentType, url, id }, type) => {
  if (type === 'view') {
    const isTotal = [
      'HZHX',
      'BZJ',
      'TACFL',
      'TAQTYFK',
      'YFBXF',
      'WCTZ',
      'FWFJT',
      'SYJT',
      'JZJT',
      'CBABS',
      'HSSBCWRK',
      'ZJZR',
      'ZJZRDB',
      'DSFZR',
      'ZRFY'
    ].includes(documentType)

    setVoucherPage({ batchId: documentId, batchType: documentType }, isTotal ? 'total' : null)
    return
  }
  const detailUrl = documentType === 'SYJT'
    ? `/measurementEngine/incomeProvisionDetail?leaseIncomeId=${documentId}`
    : url
  router.push(detailUrl)
}

// 批量拒绝
const handleApproveRefuse = (_ids) => {
  confirmEl('您确定进行拒绝操作吗？').then(async () => {
    utableRef.value.loading = true
    try {
      const { code, msg } = await portalApproveRefuse(_ids)
      if (code !== 200) throw new Error(msg || '审批拒绝失败')
      ElMessage.success('操作成功')
      await refreshList()
    } catch (error) {
      ElMessage.error(error?.message || '审批拒绝失败，请稍后重试')
    } finally {
      if (utableRef.value) utableRef.value.loading = false
    }
  })
}
// 批量同意
const handleApprovePass = (ids) => {
  confirmEl('您确定进行同意操作吗？').then(async () => {
    utableRef.value.loading = true
    try {
      const { code, msg } = await portalApprovePass(ids)
      if (code !== 200) throw new Error(msg || '审批同意失败')
      ElMessage.success('审批通过')
      await refreshList()
    } catch (error) {
      ElMessage.error(error?.message || '审批同意失败，请稍后重试')
    } finally {
      if (utableRef.value) utableRef.value.loading = false
    }
  })
}
const refreshList = () => {
  return utableRef.value.requestBefore()
}
</script>
