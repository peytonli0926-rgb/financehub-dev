/**
 * @description 配置axios请求基础信息
 */
import { getToken } from '@/utils/auth'

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
  requestTimeout: 60000 * 5,
  // 请求拦截自定义函数，接收config参数
  handleRequest: config => {
    // 是否需要设置 token
    const needToken = (config.headers || {}).isToken !== false
    // 这里是在本系统内进行的添加
    if (getToken() && needToken) {
      config.headers.Authorization = /* 'Bearer ' + */getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers.token = getToken()
    }
    // 这里是为提供外链的比如OA的内嵌iframe提供的，开发过程中发现cookie等在外链iframe中表现有bug，故采用lostorage,后期可优化
    if (!getToken() && window.localStorage.getItem('tokenOutlinkOA')) {
      const token = window.localStorage.getItem('tokenOutlinkOA')
      config.headers.Authorization = 'Bearer ' + token // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers.token = token
    }
  },
  // 返回成功拦截自定义函数，接收response参数
  handleResSuccess: undefined,
  // 返回成功拦截自定义函数，接收response error参数
  handleResError: undefined
}

export const userApi = {
  // 登录接口
  login: {
    path: '/auth/login',
    method: 'post'
  },

  // 登出接口
  logout: {
    path: '/auth/sso/logout',
    method: 'post'
  },

  // 获取用户个人信息
  getUserInfo: {
    path: '/auth/sso/getUserInfo',
    method: 'get'
  },

  // 获取权限接口密钥
  getBpmToken: {
    path: '/bpmAuthTokenRegister/register',
    method: 'post'
  },

  // 获取权限数据接口
  getAuthData: {
    path: '/auth/sso/getAuthInfo',
    method: 'get'
  }
}
