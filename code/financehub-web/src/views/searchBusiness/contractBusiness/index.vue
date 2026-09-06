<template>
    <UTable ref="utableRef" :options="options" >
      <template #operation="{ row }">
        <el-button type="primary" v-permission="permission.view" size="small" link @click="detail(row)">查看详情</el-button>
      </template>
    </UTable>
</template>

<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const { router } = useRouter()
const utableRef = ref()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)

// 导出
// const exportExcel = () => {
//   handleExport({
//     url: '/engine/finance/contract/export',
//     params: {}
//   }, '合同查询.xlsx')
// }

const detail = (row) => {
  router.push({
    name: 'contractBusinessDetail',
    query: { id: row.id }
  })
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.contractBusiness
})

</script>

<style lang="scss" scoped></style>
