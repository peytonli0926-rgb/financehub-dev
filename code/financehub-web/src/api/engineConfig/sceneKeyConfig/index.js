
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 更新场景配置数据
export function getSceneFieldsList (sceneId) {
  return request({
    url: `${prefix}/engine/scene/fields/tree/${sceneId}`,
    method: 'get'
  })
}

export const getEditorFieldsList = (sceneId) => {
  return request({
    url: `${prefix}/engine/scene/fields/listEditorOption/${sceneId}`,
    method: 'get'
  })
}
