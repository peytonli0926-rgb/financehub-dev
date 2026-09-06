<template>
  <div>
    <UTable ref="utableRef"  :defaultParams="defaultParams" :options="options">
      <template #operationBtn="{ row }">
        <el-button type="primary" link size="small" v-permission="permission.view" @click="fromToRulePage(row)">查看明细</el-button>
      </template>
      <!-- <template #operateHeaderRight>
        <el-button @click="exportExcel" type="primary">导出</el-button>
      </template> -->

    </UTable>

  </div>
</template>
<script setup>
import { ref, onBeforeMount ,computed} from 'vue'
import { useStore } from 'vuex'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'


const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const route = useRoute()
const { query } = route.value

const defaultParams = ref({
})

const fromToRulePage = () => {
  router.push({
    name: 'specialContractStatusDetail',
    query: {
      sumId: query.sumId
    }
  })
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.nonConfirmThirdSumDetail
})

onBeforeMount(() => {
  const { sumId } = query
  defaultParams.value = {
    sumId
  }
  // utableRef.value.requestBefore()
})

</script>
