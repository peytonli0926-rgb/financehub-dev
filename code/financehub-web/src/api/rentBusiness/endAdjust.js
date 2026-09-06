
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 核销-删除
export function deleteEndAdjustByIds (data = {}) {
  return request({
    url: `${prefix}/engine/finance/tail-difference-adjustment/deleteByIds`,
    method: 'post',
    data
  })
}
// 生成尾差调整信息
export function verifyEndAdjust (data = {}) {
  return request({
    url: `${prefix}/engine/finance/tail-difference-adjustment/initData`,
    method: 'post',
    data
  })
}
// 撤回
export function withdrawEndAdjust (data = {}) {
  return request({
    url: `${prefix}/engine/finance/tail-difference-adjustment/withdraw`,
    method: 'post',
    data
  })
}
// 提交
export function submitEndAdjust (data = {}) {
  return request({
    url: `${prefix}/engine/finance/tail-difference-adjustment/submit`,
    method: 'post',
    data
  })
}
// 生成凭证
export function generateVoucher (data = {}) {
  return request({
    url: `${prefix}/engine/finance/tail-difference-adjustment/generateVoucher`,
    method: 'post',
    data
  })
}
