<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-21 15:50:42
 * @LastEditTime: 2024-06-06 21:38:09
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          :disabled="!row.voucherId"
          type="primary"
          link
          size="small"
          @click="fromToRulePage(row)"
          v-permission="permission.viewVoucher"
          >查看凭证</el-button
        >
      </template>
      <template #operateHeaderRight>
        <el-button @click="dialogPayVisible = true" v-permission="permission.pay" type="success"
          >生成支付信息</el-button
        >
      </template>
    </UTable>
    <UDialog
      v-if="dialogVisible"
      @update:visible="dialogVisible = $event"
      title="校验结果"
      dialog-width="80%"
      :is-footer="false"
      :visible="dialogVisible"
    >
      <UTable ref="utableDialogRef" :options="optionsDialog" :autoLoad="false" />
    </UDialog>
    <UDialog
      v-if="dialogPayVisible"
      @handle-submit="handlePayConfirm"
      @update:visible="dialogPayVisible = $event"
      title="生成支付信息"
      :visible="dialogPayVisible"
    >
      <el-form :model="formData" ref="formRef" label-width="120px">
        <el-form-item
          label="财务日期"
          prop="financeDate"
          :rules="{ required: true, message: '请选择财务日期', trigger: ['blur', 'change'] }"
        >
          <el-date-picker
            class="w-100"
            type="date"
            value-format="YYYY-MM-DD"
            format="YYYY-MM-DD"
            v-model="formData.financeDate"
            placeholder="请选择财务日期"
          />
        </el-form-item>
        <el-form-item
          label="转让批次"
          prop="batchList"
          :rules="{ required: true, message: '请选择转让批次', trigger: ['blur', 'change'] }"
        >
          <USelect
            v-model="formData.batchList"
            :request="{ url: '/engine/finance/parity-transfer/batchList', method: 'post' }"
            :attrs="{ filterable: true, multiple: true }"
            placeholder="请选择转让批次"
          ></USelect>
        </el-form-item>
      </el-form>
    </UDialog>
  </div>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigOutbound, optionsConfigDialog } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { validateForm } from '@/utils'
import { useVoucherPage } from '@/hooks'
import { generatePayment } from '@/api/assetTransferBusiness/normalTransfer'
import { ElMessage } from 'element-plus'

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const dialogVisible = ref(false)
const dialogPayVisible = ref(false)
const formData = ref({
  financeDate: '',
  batchList: []
})
const route = useRoute()
const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const defaultParams = ref({})
const utableRef = ref()
const formRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigOutbound(router, dictData)
const optionsDialog = optionsConfigDialog(router, dictData)

const fromToRulePage = ({ voucherId }) => {
  setVoucherPage({ voucherIdList: voucherId }, 'total')
}

const handlePayConfirm = async () => {
  const valid = await validateForm(formRef.value)
  if (valid) {
    const { code, msg } = await generatePayment(formData.value)
    if (code === 200) {
      ElMessage.success('生成成功')
      dialogPayVisible.value = false
      refreshList()
      return
    }
    ElMessage.error(msg)
  }
}

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
const refreshList = () => {
  utableRef.value.requestBefore()
}
</script>
