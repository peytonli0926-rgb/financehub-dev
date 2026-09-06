
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 特殊合同状态-i提交
export function submitOfflineContract (data = {}) {
  return request({
    url: `${prefix}/engine/finance/offline-contract/submit`,
    method: 'post',
    data
  })
}

// 特殊合同状态-撤回
export function withdrawOfflineContract (data = {}) {
  return request({
    url: `${prefix}/engine/finance/offline-contract/withdraw`,
    method: 'post',
    data
  })
}
// 特殊合同状态-删除
export function deleteOfflineContract (data = {}) {
  return request({
    url: `${prefix}/engine/finance/offline-contract/delete`,
    method: 'post',
    data
  })
}
// 特殊合同状态-生成凭证
export function voucherOfflineContract (data = {}) {
  return request({
    url: `${prefix}/engine/finance/offline-contract/voucher`,
    method: 'post',
    data
  })
}
