import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)
const prefix = import.meta.env.VITE_APP_SERVICE_API

export function pageRawDocuments (data) {
  return request({
    url: `${prefix}/engine/rule/interface/pageRawData`,
    method: 'post',
    data
  })
}

export function getRawDocument (id) {
  return request({
    url: `${prefix}/engine/rule/interface/getRawData/${id}`,
    method: 'get'
  })
}

export function retryRawDocument (id) {
  return request({
    url: `${prefix}/engine/rule/interface/retryRawData/${id}`,
    method: 'post'
  })
}

export function getVoucherInterfaceDataId (id) {
  return request({
    url: `${prefix}/engine/rule/interface/getVoucherInterfaceDataId/${id}`,
    method: 'get'
  })
}
