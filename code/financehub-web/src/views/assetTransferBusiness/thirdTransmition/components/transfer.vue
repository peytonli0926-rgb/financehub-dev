<template>
  <!-- 转让 -->
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <!-- 校验 -->
        <el-button
          @click="onValidateFn(row)"
          v-permission="permission.T.validate"
          link
          size="small"
          type="warning"
        >
          校验
        </el-button>
        <!-- 查看凭证  -->
        <el-button
          type="primary"
          v-permission="permission.T.viewVoucher"
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
          v-permission="permission.T.view"
          link
          size="small"
          @click="onDetailFn(row, 'detail')"
        >
          查看详情
        </el-button>
      </template>
    </UTable>
    <!-- 科目校验 -->
    <UDialog
      v-if="dialogVisible"
      @update:visible="dialogVisible = $event"
      title="科目校验"
      dialog-width="80%"
      :is-footer="false"
      :visible="dialogVisible"
    >
      <UTable
        ref="utableDialogRef"
        :defaultParams="dialogParams"
        :options="optionsDialog"
        :autoLoad="false"
      />
    </UDialog>
  </div>
</template>

<script setup>
import { reactive, toRefs, ref, onBeforeMount } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'
import { transferOptionCfg, optionsCfgDialog } from '../config'
import { confirmEl } from '@/utils'

const { router } = useRouter()
const route = useRoute()
const store = useStore()
const { setVoucherPage } = useVoucherPage()
const dictData = store.getters['useDictMapping/dictMapping']

defineProps({
  permission: {
    type: Object,
    default: () => ({})
  }
})
const utableDialogRef = ref()
const state = reactive({
  defaultParams: {},
  options: transferOptionCfg(router, dictData),
  dialogVisible: false,
  dialogParams: {},
  optionsDialog: optionsCfgDialog(router, dictData)
})

const { defaultParams, options, dialogVisible, dialogParams, optionsDialog } = toRefs(state)

// 查看凭证、查看详情
const onDetailFn = ({ voucherId, id }, type) => {
  if (type === 'view') {
    return setVoucherPage({ voucherIdList: voucherId }, 'total')
  }
  router.push({
    path: '/assetTransferBusiness/thirdTransmitionDetail',
    query: { transferId: id }
  })
}

// 校验
const onValidateFn = ({ id }) => {
  confirmEl('您确定进行校验操作吗？').then(async () => {
    try {
      state.dialogVisible = true
      state.dialogParams = {
        transferId: id
      }
      setTimeout(() => {
        utableDialogRef.value.requestBefore({ id })
      }, 200)
    } catch (error) {
      console.log(error)
    }
  })
}

onBeforeMount(() => {
  const { query } = route.value
  if (Object.keys(query).length > 0) {
    // state.defaultParams = {
    //   idList: [query.id]
    // }
  }
})
</script>
