<template>
  <!-- 转付 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <!-- 查看凭证  -->
        <el-button
          type="primary"
          v-permission="permission.P.viewVoucher"
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
          v-permission="permission.P.view"
          link
          size="small"
          @click="onDetailFn(row, 'detail')"
        >
          查看详情
        </el-button>
      </template>

      <template #operateHeaderRight>
        <el-button
          v-permission="permission.P.pay"
          type="success"
          @click="
            ;(dialogPayVisible = true),
              (formData = {
                ...defaultForm
              })
          "
        >
          生成支付信息
        </el-button>
      </template>
    </UTable>

    <!-- 生成支付信息-弹窗 -->
    <UDialog
      v-if="dialogPayVisible"
      title="生成支付信息"
      :visible="dialogPayVisible"
      @update:visible="dialogPayVisible = $event"
      @handle-submit="handlePayConfirm"
    >
      <el-form :model="formData" ref="formRef" label-width="120px">
        <el-form-item
          v-for="(item, inx) in formItems"
          :key="item.prop + inx"
          :label="item.label"
          :prop="item.prop"
          :rules="item.rules"
        >
          <component :is="item.fieldType" v-model="formData[item.prop]" v-bind="item.props" />
        </el-form-item>
      </el-form>
    </UDialog>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { reactive, toRefs, ref } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { TransferPaymentOptionCfg, paymentFormItems } from '../config'
import { useVoucherPage } from '@/hooks'
import { validateForm } from '@/utils'
import ThirdTransmitionAPI from '@/api/assetTransferBusiness/thirdTransmition'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const { setVoucherPage } = useVoucherPage()

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const defaultForm = {
  paymentDate: '',
  batch: ''
}
const utableRef = ref()
const formRef = ref()
const state = reactive({
  defaultParams: {},
  options: TransferPaymentOptionCfg(router, dictData),
  dialogPayVisible: false,
  formData: {
    ...defaultForm
  },
  formItems: paymentFormItems
})

const { defaultParams, options, dialogPayVisible, formData, formItems } = toRefs(state)

// 查看凭证、查看详情
const onDetailFn = ({ voucherId, id }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId }, 'total')
  }
  router.push({
    path: '/assetTransferBusiness/thirdPaymentDetail',
    query: { paymentId: id }
  })
}

// 生成支付信息
const handlePayConfirm = async () => {
  try {
    const valid = await validateForm(formRef.value)
    if (valid) {
      const { code, msg } = await ThirdTransmitionAPI.generatePayment(state.formData)
      if (code === 200) {
        ElMessage.success('生成成功')
        state.dialogPayVisible = false
        utableRef.value.requestBefore()
        return
      }
      ElMessage.error(msg)
    }
  } catch (error) {
    console.log(error)
  }
}
</script>
