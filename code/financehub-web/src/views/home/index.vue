<template>
    <div class="home-content" v-loading="true">

    </div>
</template>
<script setup>
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const { router } = useRouter()
const store = useStore()
const goHome = () => {
  store.dispatch('tabsBar/delVisitedRoute', router.currentRoute.value)
  router.replace('/portalEntry/homePage')
}
goHome()

</script>

<style scoped lang="scss">
.home-content {
    height: 70vh;
}
</style>
