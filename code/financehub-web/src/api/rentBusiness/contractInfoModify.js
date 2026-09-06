
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 合同修改-提交
export function submitContractHis (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-his/submit`,
    method: 'post',
    data
  })
}

// 合同修改-撤回
export function withdrawContractHis (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-his/withdraw`,
    method: 'post',
    data
  })
}
// 合同修改-删除
export function deleteContractHis (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-his/delete`,
    method: 'post',
    data
  })
}
// 合同修改-生成偿还计划修改凭证
export function voucherContractHis (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-his/voucher`,
    method: 'post',
    data
  })
}
// 合同修改-详情-交易结构
export function detailsContractHis (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-his/detail/structure`,
    method: 'post',
    data
  })
}
