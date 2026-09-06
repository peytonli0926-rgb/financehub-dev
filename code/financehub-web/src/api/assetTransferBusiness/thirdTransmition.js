import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 第三方转让
export default class ThirdTransmitionAPI {
  // 生成支付信息
  static generatePayment(data = {}) {
    return request({
      url: `${prefix}/engine/finance/convert-transfer-third-payment/generate-payment`,
      method: 'post',
      data
    })
  }
}
