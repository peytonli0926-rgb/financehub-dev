<template>
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :options="options"
      @search-form-data="searchFormDataChange"
      :autoLoad="false"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.view"
          @click="fromToRulePage(row, 'detail')"
          >查看详情</el-button
        >
        <el-button
          type="primary"
          link
          size="small"
          v-permission="permission.viewVoucher"
          @click="fromToRulePage(row, 'view')"
          >查看凭证</el-button
        >
      </template>
      <template #operateHeaderRight>
        <el-button @click="exportExcel" v-permission="permission.viewFile" type="primary"
          >查看下载</el-button
        >
      </template>
    </UTable>
    <UDialog
      :visible="uploadVisible"
      dialogWidth="75%"
      @update:visible="(val) => (uploadVisible = val)"
      title="下载列表"
      :is-footer="false"
    >
      <UTable ref="utableUploadRef" :autoLoad="false" :options="optionsFile">
        <template #operationBtn="{ row }">
          <el-button
            type="primary"
            link
            size="small"
            :disabled="row.executeStatus !== 'Finish'"
            @click="
              handleExport({ url: `/engine/finance/report/downloadFile/${row.id}` }, row.fileName)
            "
            >下载</el-button
          >
        </template>
      </UTable>
    </UDialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { leaseTableCheck, leaseTableExport } from '@/api/reports/rentalTable'
import { alertEl } from '@/utils'
import { useVoucherPage, useExport } from '@/hooks'
import moment from 'moment'
import { optionsFileConfig } from '@/utils/fileListConfig'

const { handleExport } = useExport()
const utableUploadRef = ref()
const uploadVisible = ref(false)
const { router } = useRouter()
const defaultParams = ref({
  queryDate: moment().format('YYYY-MM-DD')
})
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const optionsFile = optionsFileConfig('/engine/finance/report/leaseTable/fileList')
const { setVoucherPage } = useVoucherPage()
const fromToRulePage = ({ contractId, voucherId }, type) => {
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherId })
    return
  }
  router.push(`/searchBusiness/contractBusinessDetail?id=${contractId}`)
}

const searchFormDataChange = async (val) => {
  const { data } = await leaseTableCheck({ queryDate: val.queryDate })
  if (data.type === 'contract') {
    const { data: d } = await leaseTableExport({ ...val, queryType: data.type })
    alertEl(`文件【${d.fileName}】正在生成中，请于30分钟之后，在页面点击【查看下载】查看生成的文件`)
    return
  }
  defaultParams.value = {
    ...val,
    queryType: data.type || 'month'
  }
  utableRef.value.requestBefore()
}
const exportExcel = async () => {
  uploadVisible.value = true
  setTimeout(() => {
    utableUploadRef.value.requestBefore()
  }, 300)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.rentalTable
})
</script>

<style lang="scss" scoped></style>
