<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="汇总表" name="transfer"
        ><SumTable :permission="permission.F"
      /></el-tab-pane>
      <el-tab-pane label="明细表" name="ransom"><Details :permission="permission.S" /></el-tab-pane>
      <el-tab-pane label="对账表" name="rentPlan"
        ><BalanceTable :permission="permission"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed } from 'vue'
import SumTable from './sumTable.vue'
import Details from './details.vue'
import BalanceTable from './balanceTable.vue'
import { useStore } from 'vuex'

const store = useStore()
const activeName = ref('transfer')

const handleClick = (tab, event) => {
  console.log(tab, event)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.nonConfirmCollectionSum
})
</script>
<style></style>
