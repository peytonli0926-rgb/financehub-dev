
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 线下合同-详情
export function offlineContractDetail (data = {}) {
  return request({
    url: `${prefix}/engine/finance/offline-contract/detail/structure`,
    method: 'post',
    data
  })
}
