import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 更新场景配置数据
export function updateEngineScene ({ id, ...params }) {
  return request({
    url: `${prefix}/engine/scene/update/${id}`,
    method: 'post',
    data: params
  })
}
// 获取景规则配置数据
export function getSceneRuleDetail (sceneId) {
  return request({
    url: `${prefix}/engine/scene/rule/${sceneId}`,
    method: 'get'
  })
}
export function saveSceneRuleDetail (data) {
  return request({
    url: `${prefix}/engine/scene/rule/save`,
    method: 'post',
    data
  })
}
