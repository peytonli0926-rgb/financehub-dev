<template>
  <el-card shadow="never" :body-style="{padding:'10px 20px'}">
  <el-tabs v-model="activeName"  @tab-click="handleClick">
    <el-tab-pane label="偿还计划" name="turnInto"><TurnInto  :permission="permission.F"/></el-tab-pane>
    <el-tab-pane label="分摊表" name="turnOutSale"><TurnOutSale :permission="permission.S"/></el-tab-pane>
  </el-tabs>
</el-card>
</template>
<script  setup>
import { ref, computed } from 'vue'
import TurnInto from './turnInto.vue'
import TurnOutSale from './turnOutSale.vue'
import { useStore } from 'vuex'

const activeName = ref('turnInto')
const store = useStore()
const handleClick = (tab, event) => {
}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.longTermReceivablesDetail
})

</script>
<style>
</style>
