<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button @click="validate(row)" v-permission="permission.validate" link size="small"
          type="warning">校验</el-button>
        <el-button type="primary" :disabled="!row.voucherIds" v-permission="permission.viewVoucher" link size="small"
          @click="fromToRulePage(row, 'view')">查看凭证</el-button>
        <el-button type="primary" v-permission="permission.view" link size="small"
          @click="fromToRulePage(row, 'detail')">查看详情</el-button>
      </template>
    </UTable>

  </div>
</template>
<script setup>
import { ref, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { optionsConfigInbound } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
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
const route = useRoute()
const utableRef = ref()
const utableDialogRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigInbound(router, dictData)
const defaultParams = ref({})


const fromToRulePage = ({ voucherIds, id }, type) => {
  const url = `/assetTransferBusiness/normalTransferDetail?parityTransferId=${id}`
  if (type === 'view') {
    setVoucherPage({ voucherIdList: voucherIds },"total")
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

onBeforeMount(() => {
  const { id } = route.value.query
  defaultParams.value = {
    id
  }
})
</script>
