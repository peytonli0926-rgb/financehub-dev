
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 邮储手续费-i提交
export function submitPostalStorageFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/postal-storage-fee/submit`,
    method: 'post',
    data
  })
}

// 邮储手续费-撤回
export function withdrawPostalStorageFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/postal-storage-fee/withdraw`,
    method: 'post',
    data
  })
}
// 邮储手续费-生成分摊信息
export function generatePostalStorageFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/postal-storage-fee/generate`,
    method: 'post',
    data
  })
}
// 邮储手续费-生成凭证
export function voucherPostalStorageFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/postal-storage-fee/voucher`,
    method: 'post',
    data
  })
}
