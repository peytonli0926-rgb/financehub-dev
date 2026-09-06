<template>
  <!-- 科目对账表 -->
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
import SummaryComp from './components/summary.vue'
import PaymentOrderContractComp from './components/paymentOrderContract.vue'

const state = reactive({
  activeName: 'summary',
  tabList: [
    {
      label: '应付账款报表',
      name: 'summary',
      components: markRaw(SummaryComp)
    },
    {
      label: '付款合同信息',
      name: 'paymentOrderContract',
      components: markRaw(PaymentOrderContractComp)
    }
  ]
})

const { activeName, tabList } = toRefs(state)
</script>
