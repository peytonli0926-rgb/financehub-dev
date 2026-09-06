
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 核销-删除
export function deleteVerificationByIds (data = {}) {
  return request({
    url: `${prefix}/engine/verification/deleteByIds`,
    method: 'post',
    data
  })
}
// 校验
export function verifyVerification (data = {}) {
  return request({
    url: `${prefix}/engine/verification/details/verify`,
    method: 'post',
    data
  })
}
// 撤回
export function withdrawVerification (data = {}) {
  return request({
    url: `${prefix}/engine/verification/withdraw`,
    method: 'post',
    data
  })
}
// 提交
export function submitVerification (data = {}) {
  return request({
    url: `${prefix}/engine/verification/submit`,
    method: 'post',
    data
  })
}
// 生成凭证
export function generateVoucher (data = {}) {
  return request({
    url: `${prefix}/engine/verification/generateVoucher`,
    method: 'post',
    data
  })
}
