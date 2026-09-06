<template>
  <!-- 批扣映射表 -->
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
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
import UnrelatedComp from './components/unrelated.vue'
import VerifiedComp from './components/verified.vue'

const state = reactive({
  activeName: 'verifiedData',
  tabList: [
    {
      label: '已勾稽数据',
      name: 'verifiedData',
      components: markRaw(VerifiedComp)
    },
    {
      label: '未勾稽数据',
      name: 'unrelatedData',
      components: markRaw(UnrelatedComp)
    }
  ]
})

const { activeName, tabList } = toRefs(state)
const handleClick = (tab) => {
  console.log(tab)
}
</script>
