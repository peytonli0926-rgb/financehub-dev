<template>
  <el-card shadow="never" :body-style="{ padding: '0px 12px  12px' }">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="转让" name="inbound"><Inbound :permission="permission.F" /></el-tab-pane>
      <el-tab-pane label="内部调拨" name="outbound"
        ><Outbound :permission="permission.S"
      /></el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script setup>
import { ref, computed, onBeforeMount } from 'vue'
import Inbound from './inbound.vue'
import Outbound from './outbound.vue'
import { useStore } from 'vuex'
import { useRoute } from '@toystory/lotso'

const route = useRoute()
const activeName = ref('inbound')
const store = useStore()
const handleClick = (tab, event) => {
  // console.log(tab, event)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.normalTransfer
})
onBeforeMount(() => {
  const { tab } = route.value.query
  activeName.value = tab || 'inbound'
})
</script>
<style></style>
.
