import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 获取验证码
export function getValidCode (params) {
  return request({
    url: `${prefix}/validCode/getValidCode`,
    method: 'get',
    params
  })
}

// 获取股权结构原始数据
export function getCompanyUnAuthReport (data) {
  return request({
    url: `${prefix}/create/report/getCompanyUnAuthReport`,
    method: 'post',
    data
  })
}

// 获取股权结构原始数据
export function getCompanyUnAuthReportReduce (data) {
  return request({
    url: `${prefix}/create/report/getCompanyUnAuthReportRedece`,
    method: 'post',
    data
  })
}

// 获取版本列表
export function getSelectList (params) {
  return request({
    url: `${prefix}/create/report/selectList`,
    method: 'get',
    params
  })
}

// 根据版本号查询股权结构
export function getCompanyByVersion (data) {
  return request({
    url: `${prefix}/create/report/getCompanyByVersion`,
    method: 'post',
    data
  })
}

// 更新版本
export function updateResponseParam (data) {
  return request({
    url: `${prefix}/create/report/updateResponseParam`,
    method: 'post',
    data
  })
}
