<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="我的单据" name="first">
        <el-tabs v-model="documentSource" class="source-tabs">
          <el-tab-pane label="接口单据" name="interface"><MyDocuments /></el-tab-pane>
          <el-tab-pane label="手工单据" name="manual"><MyBill :permission="permission.F" /></el-tab-pane>
        </el-tabs>
      </el-tab-pane>

      <el-tab-pane label="待我审批" name="third">
        <WaitaApprove :permission="permission.S" />
      </el-tab-pane>
      <el-tab-pane label="我的审批" name="second"
        ><MyApprove :permission="permission.T"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import WaitaApprove from './waitaApprove.vue'
import MyApprove from './myApprove.vue'
import MyBill from './myBill.vue'
import MyDocuments from '../homePage/MyDocuments.vue'
import { useStore } from 'vuex'
const store = useStore()
const activeName = ref('first')
const documentSource = ref('interface')
const handleClick = (tab) => {
  console.log(tab)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.billList
})
</script>

<style lang="scss" scoped></style>
