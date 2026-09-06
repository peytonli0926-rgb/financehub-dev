<!--
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-14 20:35:36
 * @LastEditTime: 2024-06-06 21:35:58
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
-->
<template>
  <div>
    <UTable ref="utableRef" :defaultParams="defaultParams" :options="options" @selection-change="selectionChange"
     >
      <template #operationBtn="{ row }">
        <el-button type="primary"  v-permission="permission.viewVoucher" link size="small" @click="fromToRulePage(row,'view')" :disabled="row.isGenerateVoucher==='0'">查看凭证</el-button>
        <el-button type="primary"  v-permission="permission.view" link size="small" @click="fromToRulePage(row,'detail')">查看详情</el-button>
      </template>
    </UTable>

  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useVoucherPage } from '@/hooks'

const { setVoucherPage } = useVoucherPage()
const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()

const selectedData = ref([])
const route = useRoute()
const { query } = route.value

const defaultParams = {
  id: query?.id
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.verification
})

// 多选数据
const selectionChange = (data) => {
  selectedData.value = data
}

// 跳转
const fromToRulePage = ({ batchType, id }, type) => {
  if (type === 'view') {
    setVoucherPage({ batchType, batchId: id },"total")
    return
  }
  router.push(`/rentBusiness/verificationDetail?verificationId=${id}`)
}

</script>
