
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 更新场景配置数据
export function getBusinessList (data = {}) {
  return request({
    url: `${prefix}/engine/scene/business/list`,
    method: 'post',
    data
  })
}

// 查询业务下的子表
export function getSceneList (id) {
  return request({
    url: `${prefix}/engine/scene/business/get/${id}`,
    method: 'get'

  })
}

// 保存业务下的子表
export function saveBusinessSceneItem (data = {}) {
  return request({
    url: `${prefix}/engine/scene/business-scene/save`,
    method: 'post',
    data
  })
}

// 批量删除业务下的子表
export function deleteBusinessSceneItem (data = {}) {
  return request({
    url: `${prefix}/engine/scene/business-scene/delete/batch`,
    method: 'post',
    data
  })
}
// 重名名
export function renameBusinessSceneItem (data = {}) {
  return request({
    url: `${prefix}/engine/scene/business/update/${data.id}`,
    method: 'post',
    data
  })
}
