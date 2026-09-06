import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

const commonUrl = `${prefix}/engine/finance/voucher-rule-check`

// 凭证规则自动测试
export default class VoucherRulesAutoTestAPI {
  // 运行按钮-提交事件
  static runRulesSubmit(data = {}) {
    return request({
      url: commonUrl + '/runRulesSubmit',
      method: 'post',
      data
    })
  }

  // 更新凭证比对样本
  static updateCompareVoucher(data = {}) {
    return request({
      url: commonUrl + '/updateCompareVoucher',
      method: 'post',
      data
    })
  }
}
