
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 手工表-保存
export function manualSave (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/save`,
    method: 'post',
    data
  })
}
// 删除
export function manualDelete (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/deleteByIds`,
    method: 'post',
    data
  })
}

// 撤回
export function manualWithdraw (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/withdraw`,
    method: 'post',
    data
  })
}
// 提交
export function manualSubmit (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/submit`,
    method: 'post',
    data
  })
}
// f复制
export function manualCopy (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/copy`,
    method: 'post',
    data
  })
}
// 冲销
export function manualWriteOff (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/writeOff`,
    method: 'post',
    data
  })
}
// 查询详情
export function manualDetails (id) {
  return request({
    url: `${prefix}/engine/finance/manual/get/${id}`,
    method: 'get'
  })
}
// 查询详情
export function manualUpdate (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/update/${data.id}`,
    method: 'post',
    data
  })
}

// 外部手工数据校验
export function manualExtenalDataCheck (data = {}) {
  return request({
    url: `${prefix}/engine/finance/manual/extenalDataCheck`,
    method: 'post',
    data
  })
}
