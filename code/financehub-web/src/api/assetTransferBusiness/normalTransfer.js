
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 生成支付信息
export function generatePayment (data = {}) {
  return request({
    url: `${prefix}/engine/finance/internal-transfer/generatePayment`,
    method: 'post',
    data
  })
}

// 校验
export function checkPage (data = {}) {
  return request({
    url: `${prefix}/engine/finance/parity-transfer/checkPage`,
    method: 'post',
    data
  })
}
