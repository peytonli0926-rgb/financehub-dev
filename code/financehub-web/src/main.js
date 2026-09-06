import options from '@/config/setting'
import requestConfig, { userApi } from '@/config/request.config'
import { EagleEye } from 'eagle-eye-track-sdk'
// element
import 'element-plus/dist/index.css'
import { ElLoading } from 'element-plus'

// framework
import { createFrameApp, Layout } from '@toystory/lotso'
import '@toystory/lotso/dist/style.css'

import App from './App.vue'
import ContractBusinessDetail from '@/views/searchBusiness/contractBusinessDetail/index.vue'

// vuex
import store from '@/store'

// 注册字节跳动图标
import iconPark from './plugin/icon-park'
// 注册全局按钮权限
// import btnPermission from '@/utils/btnPermission.js'

// svg-icon
import 'virtual:svg-icons-register'
// 公共组件
import UHeader from '@/components/UHeader/index.vue'
import UTable from '@/components/UTable/index.vue'
import UTableColumn from '@/components/UTable/UTableColumn.vue'

import USearch from '@/components/USearch/index.vue'
import UDialog from '@/components/UDialog/index.vue'
import USelect from '@/components/USelect/index.vue'
import USelectPagination from '@/components/USelectPagination/index.vue'
import UButtonOperate from '@/components/UButtonOperate/index.vue'

// 华夏金租品牌色。同步覆盖浏览器中保存的旧主题，避免按钮继续显示默认蓝色。
const HUAXIA_THEME_COLOR = '#D70D18'
const HUAXIA_THEME_KEY = 'lotsoTheme'
const huaxiaThemeVariables = {
  '--el-color-primary': HUAXIA_THEME_COLOR,
  '--el-color-primary-light-1': '#DB252F',
  '--el-color-primary-light-2': '#DF3D46',
  '--el-color-primary-light-3': '#E3555D',
  '--el-color-primary-light-4': '#E76D74',
  '--el-color-primary-light-5': '#EB868C',
  '--el-color-primary-light-6': '#EF9EA3',
  '--el-color-primary-light-7': '#F3B6BA',
  '--el-color-primary-light-8': '#F7CED1',
  '--el-color-primary-light-9': '#FBE7E8',
  '--el-color-primary-dark-2': '#C20C16'
}

const applyHuaxiaTheme = () => {
  Object.entries(huaxiaThemeVariables).forEach(([name, value]) => {
    document.documentElement.style.setProperty(name, value)
  })
}

try {
  const savedTheme = JSON.parse(window.localStorage.getItem(HUAXIA_THEME_KEY) || '{}')
  window.localStorage.setItem(
    HUAXIA_THEME_KEY,
    JSON.stringify({ ...savedTheme, themeColor: HUAXIA_THEME_COLOR })
  )
} catch (error) {
  window.localStorage.removeItem(HUAXIA_THEME_KEY)
}

applyHuaxiaTheme()

// 合同详情是隐藏的权限路由。浏览器直接刷新详情地址时，权限路由尚未从
// 后端恢复，Vue Router 会先把它解析成未匹配路由，随后被框架守卫误判为 404。
// 先注册同名的启动路由；权限加载完成后，框架会用正式的异步路由替换它。
const frameOptions = {
  ...options,
  defaultRoutes: [
    ...(options.defaultRoutes || []),
    {
      path: '/searchBusiness',
      name: 'contractBusinessDetailBootstrap',
      component: Layout,
      hidden: true,
      children: [
        {
          path: '/searchBusiness/contractBusinessDetail',
          name: 'contractBusinessDetail',
          component: ContractBusinessDetail,
          hidden: true,
          meta: { title: '合同详情' }
        }
      ]
    }
  ]
}

// 注册框架，传入项目配置和store，页面路径
const app = createFrameApp(App, {
  options: frameOptions,
  requestConfig,
  userApi,
  store,
  constantFile: import.meta.glob('./pages/**/index.vue'),
  constantConfig: import.meta.globEager('./pages/**/config.js'),
  // 若配置接管权限路由，无需引入asyncFile
  asyncFile: import.meta.glob('./views/**/index.vue'),
  asyncConfig: import.meta.globEager('./views/**/config.js')
})

app.use(ElLoading)
iconPark(app)
app.component('UHeader', UHeader)
app.component('UTable', UTable)
app.component('USearch', USearch)
app.component('UDialog', UDialog)
app.component('USelect', USelect)
app.component('UTableColumn', UTableColumn)
app.component('USelectPagination', USelectPagination)
app.component('UButtonOperate', UButtonOperate)
// app.provide('btnPermission',btnPermission)

// 注册埋点
setTimeout(() => {
  window.eagleEye = new EagleEye({
    appKey: import.meta.env.VITE_APP_TRACK_ID,
    baseUrl: import.meta.env.VITE_APP_TRACK_API
  })
}, 500)
// 监控错误
const _oldError = app.config.errorHandler
app.config.errorHandler = (errMsg, vm, info) => {
  window.eagleEye && window.eagleEye.trackError(errMsg)
  if (typeof _oldError === 'function') {
    _oldError.call(this, errMsg, vm, info)
  }
}
app.mount('#app')
// 主题组件挂载后再次校准，确保所有主按钮使用华夏红。
applyHuaxiaTheme()
