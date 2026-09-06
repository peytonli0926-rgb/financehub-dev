<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="应交增值税对账" name="transfer"
        ><Transfer :permission="permission.F"
      /></el-tab-pane>
      <el-tab-pane label="应交销项税对账" name="ransom"
        ><Ransom :permission="permission.S"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import Transfer from './transfer.vue'
import Ransom from './ransom.vue'
import { useStore } from 'vuex'
import { useRoute } from '@toystory/lotso'

const activeName = ref('transfer')
const store = useStore()
const route = useRoute()
const handleClick = (tab, event) => {
  // console.log(tab, event)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.valueAddedTax
})
onBeforeMount(() => {
  const { tab } = route.value.query
  activeName.value = tab || 'transfer'
})
</script>
<style></style>
