<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="明细表" name="taDetail"
        ><TaDetail :permission="permission.F"
      /></el-tab-pane>
      <el-tab-pane label="其他应付款" name="otherPay"
        ><OtherPay :permission="permission.S"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import TaDetail from './taDetail.vue'
import OtherPay from './otherPay.vue'
import { useStore } from 'vuex'
import { useRoute } from '@toystory/lotso'

const activeName = ref('taDetail')
const store = useStore()
const route = useRoute()
const handleClick = (tab, event) => {
  // console.log(tab, event)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.TaRearrange
})
onBeforeMount(() => {
  const { tab } = route.value.query
  activeName.value = tab || 'taDetail'
})
</script>
<style></style>
