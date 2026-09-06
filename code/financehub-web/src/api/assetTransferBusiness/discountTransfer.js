import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 折价转让
export default class DiscountTransferAPI {
  // 生成支付信息
  static generatePayment(data = {}) {
    return request({
      url: `${prefix}/engine/finance/discounted-transfer-internal/generatePayment`,
      method: 'post',
      data
    })
  }
}
