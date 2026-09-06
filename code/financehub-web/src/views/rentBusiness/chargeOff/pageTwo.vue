<template>
  <div>
    <UTable
      ref="utableRef"
      :defaultParams="defaultParams"
      :options="options"
      @search-form-data="searchFormData"
    >
      <template #operationBtn="{ row }">
        <el-button
          type="primary"
          v-permission="permission?.S?.view ?? ''"
          link
          size="small"
          @click="fromToRulePage(row)"
          >查看详情</el-button
        >
      </template>
    </UTable>
  </div>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import { optionsConfigPageTwo } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import moment from 'moment'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfigPageTwo(router, dictData)
const utableRef = ref()
const formValue = ref({})
const defaultParams=ref()

const searchFormData = (formData) => {
  formValue.value = formData
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.chargeOff
})

// 跳转
const fromToRulePage = ({ contractCode, orgId, verificationDate, verificationStatus }) => {
  router.push(
    `/rentBusiness/chargeOffDetail?contractCode=${contractCode}&orgId=${orgId}&verificationDate=${verificationDate}&verificationStatus=${verificationStatus}`
  )
}

onBeforeMount(() => {
  defaultParams.value = {
    periodCode: parseInt(moment().format('YYYYMM'))
  }
})
</script>
