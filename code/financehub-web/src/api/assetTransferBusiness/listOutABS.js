
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 出表ABS 转让-i提交
export function submitListOutABS (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/submit`,
    method: 'post',
    data
  })
}

// 出表ABS 转让-撤回
export function withdrawListOutABS (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/withdraw`,
    method: 'post',
    data
  })
}
// 出表ABS 转让-删除
export function deleteListOutABS (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/deleteByIds`,
    method: 'post',
    data
  })
}
// 出表ABS 转让-生成凭证
export function voucherListOutABS (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/generateVoucher`,
    method: 'post',
    data
  })
}
// 出表ABS 转让-校验
export function checkPageListOutABS (data = {}) {
  return request({
    url: `${prefix}/engine/finance/out-table-abs/checkPage`,
    method: 'post',
    data
  })
}

//= ===赎回======

// 出表ABS 赎回-i提交
export function submitListOutABSRansom (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-redeem/submit`,
    method: 'post',
    data
  })
}

// 出表ABS 赎回-撤回
export function withdrawListOutABSRansom (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-redeem/withdraw`,
    method: 'post',
    data
  })
}
// 出表ABS 赎回-删除
export function deleteListOutABSRansom (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-redeem/deleteByIds`,
    method: 'post',
    data
  })
}
// 出表ABS 赎回-生成凭证
export function voucherListOutABSRansom (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-redeem/generateVoucher`,
    method: 'post',
    data
  })
}
// 出表ABS 赎回-校验
export function checkPageListOutABSRansom (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-redeem/checkPage`,
    method: 'post',
    data
  })
}

//= ===转付======

// 出表ABS 赎回-i提交TransfersPay
export function submitTransfersPay (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-transfer-payment/submit`,
    method: 'post',
    data
  })
}

// 出表ABS 赎回-撤回
export function withdrawTransfersPay (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-transfer-payment/withdraw`,
    method: 'post',
    data
  })
}

// 出表ABS 赎回-生成凭证
export function voucherTransfersPay (data = {}) {
  return request({
    url: `${prefix}/engine/finance/asset-abs-transfer-payment/generateVoucher`,
    method: 'post',
    data
  })
}
