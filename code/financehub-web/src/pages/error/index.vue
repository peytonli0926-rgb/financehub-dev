<template>
  <div class="page-exception">
    <div class="exception-text">{{exceptionText}}</div>
    <img v-if="exceptionType === '403'" src="@/assets/error/403.svg" alt="" class="exception-img">
    <img v-if="exceptionType === '404'" src="@/assets/error/404.svg" alt="" class="exception-img">
    <img v-if="exceptionType === '500'" src="@/assets/error/500.svg" alt="" class="exception-img">
    <el-button  class="pay-custom-btn exception-btn" primary @click="goHome">
      返回首页
    </el-button>
    <!-- <el-button v-else class="pay-custom-btn exception-btn" primary @click="goLogin">
      返回登录
    </el-button> -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from '@toystory/lotso'
// import { useStore } from 'vuex'
// import { ElMessage } from 'element-plus'

const { router } = useRouter()
const route = useRoute()
// const store = useStore()

const exceptionText = ref('服务器开小差了，请稍后再试')
const exceptionType = ref('500')

onMounted(() => {
  const { type } = route.value.query
  exceptionType.value = type || '500'
  if (type === '404') {
    exceptionText.value = '抱歉，您访问的页面不存在'
  }
  if (type === '403') {
    exceptionText.value = '抱歉，您没有权限访问该页面'
  }
  if (type === '500') {
    exceptionText.value = '服务器开小差了，请稍后再试'
  }
})

// const hasPermission = computed(() => {
//   const permissionData = store.getters['user/permissions']
//   if (permissionData && Object.keys(permissionData).length) {
//     return true
//   } else {
//     return false
//   }
// })

// const goLogin = () => {
//   store.dispatch('user/logout').then(() => {
//     router.push({ path: '/login' })
//   }).catch(err => {
//     ElMessage(
//       {
//         type: 'error',
//         message: '服务器开小差了，请稍后再试'
//       }
//     )
//     console.log(err)
//   })
// }

const goHome = () => {
  router.replace('/portalEntry/homePage')
}
</script>
<style lang="scss" scoped>
.page-exception {
  display: flex;
  margin: 0 auto;
  flex-direction: column;
  align-items: center;
  .exception-text {
    font-size: 24px;
    color: #333333;
    text-align: center;
    margin-top: 130px;
  }
  .exception-img {
    width: 560px;
    margin-top: 40px;
  }
  .exception-btn {
    margin-top: 88px;
  }
}
</style>
