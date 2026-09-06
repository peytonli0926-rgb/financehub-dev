<template>
  <div class="middle-content">
    <div class="u-text-center">
      <h1>
        欢迎使用会计引擎管理系统
        <!-- <span>通</span>
      <span>恒</span>
      <span>信</span>
      <span>财</span>
      <span>务</span> -->
      </h1>
      <h3>数据加载中，请稍后...</h3>
    </div>
    <!-- 中台管理系统 -->
    <div class="google-loader">
      <span></span>
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from '@toystory/lotso'
import { onBeforeMount } from 'vue'
import { ElMessage } from 'element-plus'
import { setToken } from '@/utils/auth'
import Cookies from 'js-cookie'

const { router } = useRouter()
const route = useRoute()

onBeforeMount(() => {
  // eslint-disable-next-line camelcase
  const { token, redirect_url = '/' } = route.value.query
  if (token) {
    setToken(token)
    Cookies.set('toystoryUserToken', token)
    setTimeout(() => {
      router.push(redirect_url)
    }, 300)
  } else {
    ElMessage.error('未携带系统统一标识[token]。跳转失败')
    setTimeout(() => {
      router.push('/')
    }, 300)
  }
})
</script>
<!--  -->
<style lang="scss" scoped>
$font: 'Montserrat', sans-serif;

$blue: #4285f4;
$red: #db4437;
$yellow: #f4b400;
$green: #0f9d58;
$colors-list: $blue $red $yellow $green;

.middle-content {
  display: flex;
  height: 70vh;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.google-loader {
  display: block;
  margin-top: 20px;
  span {
    display: inline-block;
    margin-top: 10px;
    height: 20px;
    width: 20px;
    border-radius: 50%;
    &:not(:first-child) {
      margin-left: 10px;
    }
  }
  @each $current-color in $colors-list {
    $i: index($colors-list, $current-color);
    $t: $i * -0.25;
    span:nth-child(#{$i}) {
      background: $current-color;
      animation: move 1s ease-in-out (#{$t}s) infinite alternate;
    }
  }
}

@keyframes move {
  from {
    transform: translateY(-10px);
  }
  to {
    transform: translateY(5px);
  }
}

h1 {
  font-family: $font;
  font-size: 2em;
  text-align: center;
  letter-spacing: 5px;
  margin-top: 0;
  span {
    &:first-child {
      color: $blue;
    }
    &:nth-child(2) {
      color: $red;
    }
    &:nth-child(3) {
      color: $yellow;
    }
    &:nth-child(4) {
      color: $blue;
    }
    &:nth-child(5) {
      color: $green;
    }
    &:last-child {
      color: $red;
      transform: rotate(-20deg);
      display: inline-block;
    }
  }
}

html,
body {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
