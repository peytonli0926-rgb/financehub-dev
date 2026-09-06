
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 校验
export function verifyVerification (data = {}) {
  return request({
    url: `${prefix}/engine/verification/payback/verification`,
    method: 'post',
    data
  })
}
