<template>
  <UTable ref="utableRef" :default-params="defaultParams" :options="options"
   >
    <template #operationBtn="{ row }">
      <el-button type="primary" v-permission="permission.edit" link size="small" v-if="!excludes.includes(row.sceneName) && row.processStatus === '1'"
        @click="fromToRulePage(row, 'edit')">编辑</el-button>
      <el-button type="primary" v-permission="permission.view" link size="small" @click="fromToRulePage(row, 'detail')">查看详情</el-button>
    </template>
  </UTable>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { optionsConfig, excludes } from './config'
import { useRouter, useRoute } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

const route = useRoute()
const { query } = route.value
const defaultParams = ref({})
const fromToRulePage = ({ id }, type) => {
  let url = `/customVoucher/customVoucherDetail?manualId=${id}`
  if (type === 'edit') {
    url = `/customVoucher/customVoucherAdd?manualId=${id}`
  }
  router.push(url)
}
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.customVoucherIndex
})
onMounted(() => {
  const { id, idList: list } = query
  const idList = (list && list.split(',')) || undefined
  defaultParams.value = { idList, id }
  utableRef.value.requestBefore()
})
</script>
