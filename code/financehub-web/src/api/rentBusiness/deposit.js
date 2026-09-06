
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 保证金-录入信息列表查询
export function getMarginContractBalanceList (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/enter-list`,
    method: 'post',
    data
  })
}
// 保证金-录入批量新增
export function saveMarginContractBalance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/save/batch`,
    method: 'post',
    data
  })
}
// 保证金-撤回
export function withdrawMarginContractBalance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/withdraw`,
    method: 'post',
    data
  })
}
// 保证金-提交
export function submitMarginContractBalance (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/submmit`,
    method: 'post',
    data
  })
}
// 保证金-生成重分类凭证
export function submitVoucherReclassification (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/voucher/reclassification`,
    method: 'post',
    data
  })
}

// 保证金-生成利息计提凭证
export function submitVoucherBalanceProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/voucher/interest-provision`,
    method: 'post',
    data
  })
}
// 保证金-生成重分类信息
export function submitMarginContractBalanceReclassification (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/generate/reclassification`,
    method: 'post',
    data
  })
}

// 保证金-生成利息计提信息
export function submitMarginContractBalanceProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/margin-contract-balance/generate/interest-provision`,
    method: 'post',
    data
  })
}
