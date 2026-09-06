
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 服务费分摊表-i提交
export function submitServiceFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/service-fee/submit`,
    method: 'post',
    data
  })
}

// 服务费分摊表-撤回
export function withdrawServiceFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/service-fee/withdraw`,
    method: 'post',
    data
  })
}
// 服务费分摊表-删除
export function deleteServiceFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/service-fee/delete`,
    method: 'post',
    data
  })
}
// 服务费分摊表-生成凭证
export function voucherServiceFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/service-fee/voucher`,
    method: 'post',
    data
  })
}
// 服务费分摊表-测算
export function measurementServiceFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/service-fee/measurement`,
    method: 'post',
    data
  })
}
