<template>
  <!-- 内部调拨 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button
          :disabled="!row.voucherId"
          type="primary"
          link
          size="small"
          @click="onDetailFn(row, 'view')"
          v-permission="permission.S.viewVoucher"
        >
          查看凭证
        </el-button>
      </template>

      <template #operateHeaderRight>
        <el-button
          v-permission="permission.S.pay"
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
import { reactive, toRefs, ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from '@toystory/lotso'
import { internalOptionCfg, internalFormItems } from '../config'
import { useVoucherPage } from '@/hooks'
import { validateForm } from '@/utils'
import DiscountTransferAPI from '@/api/assetTransferBusiness/discountTransfer'

const { router } = useRouter()
const route = useRoute()
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
  financeDate: '',
  batchList: []
}

const formRef = ref()
const utableRef = ref()
const state = reactive({
  defaultParams: {},
  options: internalOptionCfg(router, dictData),
  dialogPayVisible: false,
  formData: {
    ...defaultForm
  },
  formItems: internalFormItems
})

const { defaultParams, options, dialogPayVisible, formData, formItems } = toRefs(state)

// 查看凭证
const onDetailFn = ({ voucherId }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId })
  }
}

// 生成支付信息
const handlePayConfirm = async () => {
  try {
    const valid = await validateForm(formRef.value)

    if (valid) {
      const { code, msg } = await DiscountTransferAPI.generatePayment(state.formData)
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

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    state.defaultParams = {
      idList: [query.id]
    }
  }
})
</script>
