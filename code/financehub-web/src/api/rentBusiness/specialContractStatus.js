
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 特殊合同状态-i提交
export function submitContractStatus (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-status-record/submit`,
    method: 'post',
    data
  })
}

export function exportContractStatus (data = {}) {
  return request({
    url: `${prefix}/engine/finance/contract-status-record/export`,
    method: 'post',
    data
  })
}
