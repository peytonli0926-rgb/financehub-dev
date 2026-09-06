<template>
  <el-card shadow="never" :body-style="{ padding: '0px 12px  12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="转让" name="transfer"
        ><Transfer :permission="permission.F"
      /></el-tab-pane>
      <el-tab-pane label="赎回" name="ransom"><Ransom :permission="permission.S" /></el-tab-pane>
      <el-tab-pane label="出表合同租金计划" name="rentPlan"><RentPlan /></el-tab-pane>
      <el-tab-pane label="转付" name="transfersPay"
        ><TransfersPay :permission="permission.Fo"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed } from 'vue'
import Transfer from './transfer.vue'
import Ransom from './ransom.vue'
import RentPlan from './rentPlan.vue'
import TransfersPay from './transfersPay.vue'
import { useStore } from 'vuex'

const activeName = ref('transfer')
const store = useStore()
const handleClick = (tab, event) => {}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.listOutABS
})
</script>
<style></style>
