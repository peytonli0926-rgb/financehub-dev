
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 增值税-i提交
export function submitPayVat (data = {}) {
  return request({
    url: `${prefix}/engine/finance/pay-vat/submit`,
    method: 'post',
    data
  })
}

// 增值税-撤回
export function withdrawPayVat (data = {}) {
  return request({
    url: `${prefix}/engine/finance/pay-vat/withdraw`,
    method: 'post',
    data
  })
}
// 增值税-删除
export function deletePayVat (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/deleteByIds`,
    method: 'post',
    data
  })
}
// 增值税-生成凭证
export function voucherPayVat (data = {}) {
  return request({
    url: `${prefix}/engine/finance/pay-vat/generateVoucher`,
    method: 'post',
    data
  })
}
// // 增值税-校验
// export function measurementPayVat (data = {}) {
//   return request({
//     url: `${prefix}/engine/finance/out-table-abs/generate`,
//     method: 'post',
//     data
//   })
// }
