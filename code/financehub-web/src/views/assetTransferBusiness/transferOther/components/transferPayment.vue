<template>
  <!-- 转付 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operateHeaderRight>
        <el-button v-permission="permission.P.importPlan" type="primary" @click="uploadPlan">
          上传计划
        </el-button>
      </template>

      <template #operationBtn="{ row }">
        <el-button
          v-permission="permission.P.viewVoucher"
          :disabled="!row.voucherId"
          type="primary"
          link
          size="small"
          @click="onDetailFn(row, 'view')"
        >
          查看凭证
        </el-button>
      </template>
    </UTable>
  </div>
</template>

<script setup>
import { reactive, toRefs } from 'vue'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { TransferPaymentOptionCfg } from '../config'
import { useVoucherPage, useExport } from '@/hooks'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const { setVoucherPage } = useVoucherPage()

const { commonUploadDialog } = useExport()

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})

const state = reactive({
  defaultParams: {},
  options: TransferPaymentOptionCfg(router, dictData)
})

const { defaultParams, options } = toRefs(state)

// 查看凭证
const onDetailFn = ({ voucherId, id }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId })
  }
}

// 上传计划
const uploadPlan = () => {
  commonUploadDialog({
    url: '/engine/finance/converter-transfer-other-payment/import-plan',
    title: '其他-转付-上传计划',
    open: true,
    templateUrl: '/engine/finance/converter-transfer-other-payment/download-template'
  })
}
</script>
