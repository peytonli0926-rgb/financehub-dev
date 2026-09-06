<template>
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="ChargeOff手工" name="first">
        <PageOne />
      </el-tab-pane>
      <el-tab-pane label="ChargeOff汇总" name="second">
        <PageTwo />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import PageOne from './pageOne.vue'
import PageTwo from './pageTwo.vue'

const activeName = ref('first')

const handleClick = (tab, event) => {
  console.log(tab, event)
}
</script>

<style lang="scss" scoped></style>
