
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 收益计提-i提交
export function submitLeaseIncome (data = {}) {
  return request({
    url: `${prefix}/engine/finance/lease-income/submit`,
    method: 'post',
    data
  })
}

// 收益计提-撤回
export function withdrawLeaseIncome (data = {}) {
  return request({
    url: `${prefix}/engine/finance/lease-income/withdraw`,
    method: 'post',
    data
  })
}
// 收益计提-删除
export function deleteLeaseIncome (data = {}) {
  return request({
    url: `${prefix}/engine/finance/lease-income/delete`,
    method: 'post',
    data
  })
}
// 收益计提-生成凭证
export function voucherLeaseIncome (data = {}) {
  return request({
    url: `${prefix}/engine/finance/lease-income/voucher`,
    method: 'post',
    data
  })
}
// 收益计提-生成本月收益计提
export function measurementLeaseIncome (data = {}) {
  return request({
    url: `${prefix}/engine/finance/lease-income/generate`,
    method: 'post',
    data
  })
}
