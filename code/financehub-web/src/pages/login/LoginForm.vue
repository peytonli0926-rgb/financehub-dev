<template>
<div class="login-box u-p-b-20">
  <h3>登录</h3>
  <el-form :model="ruleForm" :rules="rules" ref="validateForm" class="login-ruleForm">
    <el-form-item prop="userId">
      <el-input placeholder="请输入账号" size="large" v-model="ruleForm.username">
        <template #prefix>
          <icon-user theme="outline" size="16" fill="#999"/>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        @keyup.enter="handleLogin"
        placeholder="请输入密码"
        size="large"
        type="password"
        v-model="ruleForm.password"
      >
        <template #prefix>
          <icon-lock theme="outline" size="16" fill="#999" />
        </template>
      </el-input>
    </el-form-item>
    <!-- <el-form-item v-if="showRandCode" prop="randCode">
      <el-row :gutter="10">
        <el-col :span="16">
          <el-input
            size="large"
            placeholder="请输入验证码"
            v-model="ruleForm.randCode"
          >
            <template #prefix>
              <icon-check-one theme="outline" size="16" fill="#999"/>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-tooltip effect="dark" content="点击图片刷新" placement="bottom">
            <img class="rand-code" :src="randCodeData" alt="验证码" @click="randCodeRefresh">
          </el-tooltip>
        </el-col>
      </el-row>
    </el-form-item> -->
    <el-form-item style="margin-bottom: 0;">
      <el-button
        type="primary"
        size="default"
        :loading="loading"
        class="login-btn"
        round
        @click="handleLogin"
        >
        登录
      </el-button>
    </el-form-item>
    <!-- <el-form-item>
      <el-button
        class="one-key"
        link
        block
        @click="oneKeyLogin">
        <el-icon :size="18">
          <refresh />
        </el-icon>免密一键登录
      </el-button>
    </el-form-item> -->
  </el-form>
</div>
</template>

<script setup>
import { reactive, ref, unref, watch, toRefs } from 'vue'
// import { Refresh } from '@element-plus/icons-vue'
// import { useStore } from 'vuex'
import { useRouter } from '@toystory/lotso'
import { ElMessage } from 'element-plus'
import { login } from '@/api/common'
import { setToken } from '@/utils/auth'
// const store = useStore()
const { router } = useRouter()
const validateForm = ref(null)
// const randCodeData = ref('')
const state = reactive({
  ruleForm: {
    username: '', // 'admin',
    password: ''// 'pU9fsAYX!U5@'
    // randCode: null,
    // requestId: null
  },
  loading: false,
  redirect: undefined,
  showRandCode: false,
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    // randCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
  }
})

watch(
  () => router.currentRoute,
  ({ _value }) => {
    const route = _value
    state.redirect = (route.query && route.query.redirect) ? decodeURIComponent(route.query.redirect) : '/'
  },
  {
    immediate: true
  }
)

const handleLogin = async () => {
  const form = unref(validateForm)
  if (!form) return
  await form.validate((valid) => {
    if (valid) {
      state.valid = true
      state.loading = true
      const {
        username,
        password
      } = state.ruleForm

      login({
        username,
        password
      })
      // store.dispatch('user/login', {
      //   username,
      //   password,
      //   requestId,
      //   validCode
      // })

        .then(response => {
          state.loading = false
          if (response.code === 200) {
            const routerPath = `/home/index?token=${response.data}` // state.redirect && state.redirect.startsWith('/error') ? '/' : state.redirect || '/'

            state.ruleForm.requestId = null
            state.ruleForm.randCode = null
            setToken(response.data)
            console.log(response.data)
            setTimeout(() => {
              location.href = routerPath
              // router.push(routerPath).catch(() => {})
            }, 200)
          } else if (response.code === 101300800) {
            state.showRandCode = true
            // randCodeRefresh()
            ElMessage({ // "5206955594014ef38071842efa82d4c1"
              message: response.msg || response.message,
              type: 'error',
              duration: 5 * 1000
            })
          } else {
            if (state.showRandCode) {
            // randCodeRefresh()
            }
            ElMessage({
              message: response.msg || response.message,
              type: 'error',
              duration: 5 * 1000
            })
          }
        }).catch(() => {
          state.loading = false
        })
    }
  })
}

// const oneKeyLogin = () => {
//   // TODO
// }

const { ruleForm, loading, rules } = toRefs(state)
</script>
<style lang="scss" scoped>
  .login-ruleForm {
    margin-top: 1rem;
    :deep(.el-input__prefix) {
      top: 2px;
    }
    :deep(.i-icon) {
      line-height: inherit;
    }
    .login-methods {
      display: flex;
      align-items: center;
      justify-content: space-around;
    }
    .login-btn {
      width: 100%;
      font-size: 18px;
      height: 40px;
    }
    .login-check {
      display: flex;
      align-content: center;
      justify-content: space-between;
    }
    .one-key {
      font-size: 16px;
      display: block;
      margin: 0 auto;
      :deep(.el-icon) {
        margin-right: 8px;
      }
    }
  }
  .login-box {
    h3 {
      font-size: 28px;
      margin-bottom: 20px;
      font-weight: normal;
    }
    .pacas-btns{
      font-size: 14px;
      color: #999999;
      line-height: 22px;
      padding-bottom: 30px;
      a {
        padding-left: 22px;
        display: inline-block;
        vertical-align: middle;
        margin-right: 28px;
        color: #999999;
        &:last-of-type {
          margin-right: 0;
        }
      }
      .pwd {
        background: url('@/assets/login/icon-pwd.png') left center no-repeat;
      }
      .act {
        background: url('@/assets/login/icon-act.png') left center no-repeat;
      }
      .change {
        background: url('@/assets/login/icon-change.png') left center no-repeat;
      }
      .diy {
        background: url('@/assets/login/icon-diy.png') left center no-repeat;
      }
    }
    :deep(.el-input-group__append) {
      vertical-align: middle;
    }
  }
  .rand-code {
    display: block;
    height: 100%;
    width: 100%;
  }
</style>
