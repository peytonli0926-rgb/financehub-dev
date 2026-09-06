<template>
  <!-- 第三方转让 -->
  <el-card shadow="never" :body-style="{ padding: '0 12px 12px' }">
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane
        v-for="(tab, inx) in tabList"
        :key="tab.name + inx"
        :label="tab.label"
        :name="tab.name"
      >
        <component :is="tab.components" :permission="permission"></component>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup>
import { reactive, toRefs, markRaw, computed } from 'vue'
import { useStore } from 'vuex'
import Transfer from './components/transfer.vue'
import TransferPayment from './components/transferPayment.vue'

const store = useStore()

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.thirdTransmition
})

const state = reactive({
  activeName: 'transfer',
  tabList: [
    {
      label: '转让',
      name: 'transfer',
      components: markRaw(Transfer)
    },
    {
      label: '转付',
      name: 'transferPayment',
      components: markRaw(TransferPayment)
    }
  ]
})

const { activeName, tabList } = toRefs(state)
</script>

<style lang="scss" scoped></style>
