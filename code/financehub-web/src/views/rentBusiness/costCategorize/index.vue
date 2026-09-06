<template>
  <div class="h-100">
    <el-card shadow="never" class="h-100" :body-style="{ padding: '0 12px 12px' }">
      <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="GPS" name="gps"></el-tab-pane>
        <el-tab-pane label="手环设备款" name="device"></el-tab-pane>
        <el-tab-pane label="经销商服务费&外部渠道费&海通渠道费" name="tabService"></el-tab-pane>
        <el-tab-pane label="收车费&抵押费&解抵押费" name="businessStract"></el-tab-pane>
      </el-tabs>

      <component :is="getComponent"></component>
    </el-card>
  </div>
</template>

<script setup>
import { ref, shallowRef, onMounted } from 'vue'
import componentList from './components/index.js'

const getComponent = shallowRef(null)
const activeName = ref('gps')
const handleClick = ({ index }) => {
  getComponent.value = componentList[index]
}

onMounted(() => {
  handleClick({ index: 0 })
})
</script>

<style lang="scss" scoped></style>
