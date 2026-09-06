
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 特殊合同状态-i提交
export function submitCourtCost (data = {}) {
  return request({
    url: `${prefix}/engine/court-cost/submit`,
    method: 'post',
    data
  })
}

// 特殊合同状态-撤回
export function withdrawCourtCost (data = {}) {
  return request({
    url: `${prefix}/engine/court-cost/withdraw`,
    method: 'post',
    data
  })
}
// 特殊合同状态-删除
export function deleteCourtCost (data = {}) {
  return request({
    url: `${prefix}/engine/court-cost/deleteByIds`,
    method: 'post',
    data
  })
}
// 特殊合同状态-生成凭证
export function voucherCourtCost (data = {}) {
  return request({
    url: `${prefix}/engine/court-cost/generateVoucher`,
    method: 'post',
    data
  })
}
