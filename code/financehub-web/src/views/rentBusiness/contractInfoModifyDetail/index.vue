<template>
  <el-card shadow="never">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="交易结构" name="structure">
        <Structure :permission="permission.F"/>
      </el-tab-pane>
      <el-tab-pane label="租金计划" name="plan">
        <Plan :permission="permission.S"/>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import Structure from './structure.vue'
import Plan from './plan.vue'
import { useStore } from 'vuex'

const activeName = ref('structure')
const handleClick = () => {}
const store = useStore()

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.contractInfoModifyDetail
})

</script>

<style lang="scss" scoped>
.export-btn {
  text-align: right;
}
</style>
