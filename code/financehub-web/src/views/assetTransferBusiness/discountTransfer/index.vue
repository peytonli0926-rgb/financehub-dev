<template>
  <!-- 折价转让 -->
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
import { reactive, toRefs, markRaw, computed, onBeforeMount } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from '@toystory/lotso'
import TransferPossession from './components/transferPossession.vue'
import InternalAllocation from './components/internalAllocation.vue'

const store = useStore()
const route = useRoute()

// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']

  return btnPermissions.discountTransfer
})

const state = reactive({
  activeName: 'transferPossession',
  tabList: [
    {
      label: '转让',
      name: 'transferPossession',
      components: markRaw(TransferPossession)
    },
    {
      label: '内部调拨',
      name: 'internalAllocation',
      components: markRaw(InternalAllocation)
    }
  ]
})

const { activeName, tabList } = toRefs(state)

onBeforeMount(() => {
  const { query } = route.value
  const obj = {
    ZJZRDB: 'internalAllocation',
    ZJZR: 'transferPossession'
  }
  if (Object.keys(query).length > 0 && query.type) {
    state.activeName = obj[query.type]
  }
})
</script>

<style lang="scss" scoped></style>
