
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 合同查询详情-基础信息
export function getContractDetail (id) {
  return request({
    url: `${prefix}/engine/finance/contract/getContractDetail/${id}`,
    method: 'get'
  })
}

// 乘用车合同全生命周期
export function getVehicleLifecycle (id) {
  return request({
    url: `${prefix}/engine/finance/contract/vehicleLifecycle/${id}`,
    method: 'get'
  })
}
