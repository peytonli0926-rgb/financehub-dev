<template>
  <div>
    <UTable ref="utableRef" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          @click="validate(row)"
          v-permission="permission.validate"
          link
          size="small"
          type="warning"
          >校验</el-button
        >
        <el-button
          type="primary"
          v-permission="permission.viewVoucher"
          link
          size="small"
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
    <UDialog
      v-if="dialogVisible"
      @update:visible="dialogVisible = $event"
      title="校验结果"
      dialog-width="80%"
      :is-footer="false"
      :autoLoad="false"
      :visible="dialogVisible"
    >
      <UTable ref="utableDialogRef" :options="optionsDialog" :autoLoad="false" />
    </UDialog>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigInbound, optionsConfigDialog } from './config'
import { useRouter } from '@toystory/lotso'
import { confirmEl } from '@/utils'

import { useVoucherPage } from '@/hooks'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const dialogVisible = ref(false)
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const utableRef = ref()
const utableDialogRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigInbound(router, dictData)
const optionsDialog = optionsConfigDialog(router, dictData)

const fromToRulePage = ({ batchId, batchType, orgId, inboundDate }, type) => {
  const url = `/debtRestructuringBusiness/recallDeviceDetail?orgId=${orgId || ''}&inboundDate=${
    inboundDate || ''
  }`
  if (type === 'view') {
    setVoucherPage({ batchType, batchId }, 'total')
    return
  }
  router.push(url)
}

// 校验
const validate = ({ id }) => {
  confirmEl('您确定校验该条的数据吗?').then(async () => {
    dialogVisible.value = true
    setTimeout(() => {
      utableDialogRef.value.requestBefore({ id })
    }, 200)
  })
}
</script>
