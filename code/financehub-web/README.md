# financehub-web系统
基于Vue3.0，使用Element Plus框架，Vite构建

## 安装
```bash
yarn add @toystory/lotso -S
# OR
npm i @toystory/lotso -S
```

## 使用

### 注册
在main.js中引入
```js
import options from '@/config/setting'
import requestConfig, { userApi } from '@/config/request.config'

import { createFrameApp } from '@toystory/lotso'
import '@toystory/lotso/dist/style.css'

import App from './App.vue'

import store from '@/store'

const constantFile = import.meta.glob('./pages/**/index.vue')
const constantConfig = import.meta.globEager('./pages/**/config.js')
const asyncFile = import.meta.glob('./views/**/index.vue')
const asyncConfig = import.meta.globEager('./views/**/config.js')

// 注册框架，传入项目配置和store，页面路径
const app = createFrameApp(App, {
  options, // 框架配置
  requestConfig, // axios配置
  userApi,  // 登录、权限接口地址
  store, // 项目Vuex仓库
  constantFile, // 通用页面文件集合（不参与权限控制）
  constantConfig, // 通用页面配置集合
  asyncFile, // 动态页面文件集合（权限控制）
  asyncConfig // 动态页面配置集合
})
app.mount('#app')
```

setting.js
```js
export default {
  // 是否显示顶部进度条
  progressBar: true,
  // 菜单栏默认打开路由
  defaultOpeneds: [],
  // vertical布局时是否只保持一个子菜单的展开
  uniqueOpened: false,
  // token名称
  tokenName: 'accessToken',
  // 是否跳过登录
  skipLogin: true,
  // default language
  lang: 'zh-cn',
  // 标题
  title: '会计引擎后台管理系统',
  // 版权信息
  copyrightZh: '版权所有 © 德勤华庆商务服务有限公司 未经许可不得复制、转载或摘编，违者必究！',
  copyrightEn: 'Copyright © Haitong Unitrust International Leasing CO.,LTD.All Rights Reserved',
  // 默认主题颜色
  themeColor: '#1890ff',
  // 默认左侧菜单背景颜色
  leftMenuBgColor: '#0d153c',
  // 默认顶部菜单背景颜色
  topMenuBgColor: '#ffffff',
  // 默认左侧菜单文字颜色
  leftMenuTextColor: '#FFF',
  // 默认顶部菜单文字颜色
  topMenuTextColor: '#fff',
  // 是否显示用户名
  showName: false,
  // 头部工具栏布局
  showHeaderBar: false,
  // 是否显示页面底部自定义版权信息
  footerCopyright: true,
  // token失效回退到登录页时是否记录本次的路由
  recordRoute: true,
  // 是否显示全屏
  fullScreen: true,
  // 是否显示刷新
  refresh: true,
  // 是否显示通知
  notice: true,
  // 是否显示面包导航
  isBreadcrumb: true,
  // 是否显示logo
  isLogo: true,
  // logo图片 相对于public/static/image的路径
  logo: 'logo.png',
  // 登录页logo图片
  loginLogo: 'logo.png',
  // 登录页面背景图片
  loginBackground: 'login-bg3.jpg',
  // 菜单栏logo
  sideLogo: 'side-logo.png',
  // 是否显示标签
  tag: true,
  // 是否展开菜单
  collapse: false,
  // 路由白名单不经过token校验的路由
  routesWhiteList: ['/login', '/register', '/404', '/401'],
  // 权限信息白名单
  whiteAuthData: [
    {
      children: [
        {
          children: [],
          elements: [],
          menuType: 3,
          sort: 0,
          menu: 'homeIndex'
        }
      ],
      elements: [],
      menuType: 3,
      sort: 0,
      menu: 'home'
    }
  ],
  // 默认首页页面
  defaultPath: '/home',
  // 默认添加进路由表中的路由
  defaultRoutes: [],
  // 是否使用hash模式
  useHash: false,
  // base路径
  baseURL: '',
  // 登录页面路径，默认为'/login'，可配置SSO路径
  loginPath: '',
  // 接管权限路由拦截，若routerBeforeEach不为undefined，则跳过默认权限拦截，(to, from, next, store, router) => {}
  routerBeforeEach: undefined
}
```
request.config.js
```js
export default {
  // axios 基础url地址
  baseURL: import.meta.env.VITE_APP_BASE_API,
  // bpm 请求地址前缀：
  bpmPrefix: import.meta.env.VITE_APP_BPM_API,
  // 网关请求地址前缀:
  gatewayPrefix: import.meta.env.VITE_APP_GATEWAY,
  // 为开发服务器配置 CORS。默认启用并允许任何源，传递一个 选项对象 来调整行为或设为 false 表示禁用
  cors: true,
  // 根据后端定义配置
  contentType: 'application/json;charset=UTF-8',
  // 消息框消失时间
  messageDuration: 3000,
  // 最长请求时间
  requestTimeout: 60000,
  // 请求拦截自定义函数，接收config参数
  handleRequest: config => {
    // do something
  },
  // 返回成功拦截自定义函数，接收response参数
  handleResSuccess: undefined,
  // 返回成功拦截自定义函数，接收response error参数
  handleResError: undefined
}

export const userApi = {
  // 登录接口
  login: {
    path: '/login',
    method: 'post'
  },

  // 登出接口
  logout: {
    path: '/logout',
    method: 'post'
  },

  // 获取用户个人信息
  getUserInfo: {
    path: '/getUserInfo',
    method: 'get'
  },

  // 获取权限接口密钥
  getBpmToken: {
    path: '/bpmAuthTokenRegister/register',
    method: 'post'
  },

  // 获取权限数据接口
  getAuthData: {
    path: '/authDataOutput/frontData',
    method: 'post'
  }
}
```
>项目使用store，因脚手架中已为项目注册Vuex，无需重复注册，只需将modules和getters暴露即可，脚手架会进行merge

```js
import getters from './getters'

const modulesFiles = import.meta.globEager('./modules/*.js')

const modules = {}
for (const path in modulesFiles) {
  const moduleName = path.replace(/(.*\/)*([^.]+).*/gi, '$2')
  modules[moduleName] = modulesFiles[path].default
}

Object.keys(modules).forEach((key) => {
  modules[key].namespaced = true
})

export default {
  modules,
  getters
}

```
### dispatch公共方法
> 例：调用方式：store.dispatch('user/method')
* user
  |方法|说明|参数|
  |--------|-------|:------:|
  | login | 登录 | |
  | getUserInfo | 获取用户信息 | - |
  | logout | 登出 | - |
  | resetInfo | 重置并删除当前用户信息 | - |

* tabsBar
  |方法|说明|参数|
  |--------|-------|:------:|
  | addVisitedRoute | 添加tab | route |
  | delVisitedRoute | 移除tab | route |
  | delOthersVisitedRoute | 移除其他tab | route |
  | delLeftVisitedRoute | 移除左边的tab | route |
  | delRightVisitedRoute | 移除右边的tab | route |
  | delAllVisitedRoutes | 移除所有tab | - |
  | updateVisitedRoute | 更新tab | route |

* routes
  |方法|说明|参数|
  |--------|-------|:------:|
  | setRoutes | 注册路由 | bpmData（权限数据） |
  | setAllRoutes | 注册所有路由（*将跳过权限管控） | - |
  
* notice
  |方法|说明|参数|
  |--------|-------|:------:|
  | setNoticeList | 设置消息中心列表 | Array: { id: number \| string, title: string, time: string, icon: string}|
  | setNoticeCallback | 消息中心回调 | Function: (Object?: \<NoticeItem\> ) => void |
  | emitNoticeCallback | 手动触发回调 | NoticeItem |
  | setBadge | 设置消息中心角标 | String|

## ！使用注意：
### 因store，request，router为框架统一注册，引用时请注意从包内引用，示例：

```js
import { useRouter, useRoute, useRequest } from '@toystory/lotso'
import { useStore } from 'vuex'
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const { router } = useRouter()
const route = useRoute()
const { query } = route.value

const store = useStore()
```
