<template>
  <el-card shadow="never" :body-style="{ padding: '0px 12px  12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="转入登记" name="turnInto"
        ><TurnInto :permission="permission.F"
      /></el-tab-pane>
      <el-tab-pane label="转出-出售登记" name="turnOutSale"><TurnOutSale /></el-tab-pane>
      <el-tab-pane label="转出-出租登记" name="turnOutRent"
        ><TurnOutRent :permission="permission.T"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import TurnInto from './turnInto.vue'
import TurnOutSale from './turnOutSale.vue'
import TurnOutRent from './turnOutRent.vue'
import { useStore } from 'vuex'
import { useRoute } from '@toystory/lotso'

const activeName = ref('turnInto')
const store = useStore()
const route = useRoute()
const handleClick = (tab, event) => {}

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.debtOffsetProperty
})
onBeforeMount(() => {
  const { tab } = route.value.query
  activeName.value = tab || 'turnInto'
})
</script>
<style></style>
