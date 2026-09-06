<template>
  <component :is="currentView" />
</template>

<script setup>
import { shallowRef, onActivated } from 'vue'
import { useRoute } from '@toystory/lotso'
import TransferDetail from './transferDetail.vue'
import RansomDetail from './ransomDetail.vue'
import TransfersPayDetail from './transfersPayDetail.vue'
import { useModifyRouteTitle } from '@/hooks'

const { setCurrentTabbarTitle } = useModifyRouteTitle()
const componentT = {
  ransom: {
    name: '出表ABS-赎回详情',
    component: RansomDetail
  },
  transfer: {
    name: '出表ABS-转让详情',
    component: TransferDetail
  },
  transfersPay: {
    name: '出表ABS-转付详情',
    component: TransfersPayDetail
  }
}
const route = useRoute()
const currentView = shallowRef()
onActivated(() => {
  const { query } = route.value
  const { component, name } = componentT[query.type]
  currentView.value = component
  setCurrentTabbarTitle(name)
})

</script>

<style lang="scss" scoped></style>
