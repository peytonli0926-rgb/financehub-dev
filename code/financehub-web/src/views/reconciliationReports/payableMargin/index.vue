<template>
  <!-- 应付保证金 -->
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane
        v-for="(tab, inx) in tabList"
        :key="tab.name + inx"
        :label="tab.label"
        :name="tab.name"
      >
        <component :is="tab.components"></component>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { reactive, toRefs, markRaw } from 'vue'
import ContractDimension from './components/contractDimension.vue'
import CustomerDimension from './components/customerDimension.vue'

const state = reactive({
  activeName: 'contractDimension',
  tabList: [
    {
      label: '合同维度',
      name: 'contractDimension',
      components: markRaw(ContractDimension)
    },
    {
      label: '客户维度',
      name: 'customerDimension',
      components: markRaw(CustomerDimension)
    }
  ]
})

const { activeName, tabList } = toRefs(state)
</script>

<style lang="scss" scoped></style>
