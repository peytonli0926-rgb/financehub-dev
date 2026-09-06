import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

//
export default class RemainRentalDetailsAPI {
  // 租金剩余本金、保证金明细表-获取表头
  static getHeader(data = {}) {
    return request({
      url: `${prefix}/engine/finance/report/remain-rent-principal-deposit-detail/getHeader`,
      method: 'post',
      data
    })
  }

  // 资金占用成本明细-分页查询-表头
  static getHeaderTitle(data = {}) {
    return request({
      url: `${prefix}/engine/finance/report/fund-occupation-cost/title`,
      method: 'post',
      data
    })
  }

  // 收益全生命周期明细表-表头查询
  static getFullLifeTitle(data = {}) {
    return request({
      url: `${prefix}/engine/finance/report/full-life-cycle-income-detail/title`,
      method: 'post',
      data
    })
  }
}
