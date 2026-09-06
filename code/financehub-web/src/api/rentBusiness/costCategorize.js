
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// -生成凭证
export function voucherCostChannelFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/cost-channel-fee/generateVoucher`,
    method: 'post',
    data
  })
}
// 提交
export function submitCostChannelFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/cost-channel-fee/submit`,
    method: 'post',
    data
  })
}

// 撤回
export function withdrawCostChannelFee (data = {}) {
  return request({
    url: `${prefix}/engine/finance/cost-channel-fee/withdraw`,
    method: 'post',
    data
  })
}
