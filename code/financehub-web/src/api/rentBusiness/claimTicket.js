
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 开票认领同步数据
export function syncInvoicingSystem (data = {}) {
  return request({
    url: `${prefix}/etl/financial/invoice-claim/syncInvoicingSystem`,
    method: 'post',
    data
  })
}
