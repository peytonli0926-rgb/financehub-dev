
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 应付保险费
// 撤回
export function withdrawPayableInsurance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/payable-insurance/withdraw`,
    method: 'post',
    data
  })
}
// 提交
export function submitPayableInsurance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/payable-insurance/submit`,
    method: 'post',
    data
  })
}
// 生成凭证
export function generatePayableInsurance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/payable-insurance/voucher`,
    method: 'post',
    data
  })
}
// 生成信息
export function generatePayableInsuranceInfo (data = {}) {
  return request({
    url: `${prefix}/engine/finance/payable-insurance/generate`,
    method: 'post',
    data
  })
}
