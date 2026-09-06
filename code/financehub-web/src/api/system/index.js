import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 重新推送消息
export function rePushMessageError (data) {
  return request({
    url: `${prefix}/engine/rule/mq-error-message/rePush`,
    method: 'post',
    data
  })
}
