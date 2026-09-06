<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName">
      <el-tab-pane :label="item.label" :name="item.name" v-for="item of tabList" :key="item.name">
        <component :is="item.component" :permission="permission[item.permission]" />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import LitigationCostsByCosts from './litigationCostsByCosts.vue'
import LitigationCostsReport from './litigationCostsReport.vue'

const store = useStore()
const activeName = ref('first')
const tabList = [
  {
    label: '诉讼费转费用',
    name: 'first',
    permission: 'F',
    component: LitigationCostsByCosts
  },
  {
    label: '诉讼费报表',
    name: 'second',
    permission: 'S',
    component: LitigationCostsReport
  }
]

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.litigationCosts
})
</script>
