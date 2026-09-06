<template>
  <div class="login-wrapper">
    <el-header class="header">
      <div class="logo">
        <img :src="`/static/image/${loginLogo || 'logo.jpg'}`" alt="logo" />
      </div>
    </el-header>
    <div class="login-container" :style="`background-image: url(/static/image/${loginBackground || 'login-bg.jpg'})`">
      <div class="login-left hidden-sm-and-down"></div>
      <div class="login-form">
        <div class="login-right-wrap">
          <h2 class="title">{{ title }}</h2>
          <LoginForm class="form-warp" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import LoginForm from './LoginForm.vue'
import setting from '@/config/setting'

const { title, loginBackground, loginLogo } = setting
</script>

<style lang="scss" scoped>
  .login-wrapper {
    position: relative;
    .header {
      position: absolute;
      top: 0;
      right: 0;
      left: 0;
      z-index: 99;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 40px;
      color: #fff;
      background: transparent;
      .logo {
        justify-content: start;
        :deep(.logo-title) {
          color: #fff !important;
        }
      }
      .lang:hover {
        background: transparent;
      }
    }
    .login-container {
      display: flex;
      width: 100%;
      height: 100vh;
      overflow: hidden;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      .login-left {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 50vw;
        height: 100%;
      }
      .login-form {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 50vw;
        height: 100vh;
        .form-warp {
          width: 400px;
          padding: 1rem 3rem 0 3rem;
          margin: auto;
          background-color: #fff;
          border-radius: 8px;
        }
      }
      .login-right-wrap {
        .title {
          font-weight: bold;
          color: #fff;
          letter-spacing: 1.2px;
          line-height:1.5;
          font-size: 36px;
          text-shadow: #FFF 3px 3px 22px;
          text-align: center;
          margin-bottom: 30px;
        }
      }
    }
  }
</style>
