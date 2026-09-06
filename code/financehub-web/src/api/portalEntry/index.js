
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 审批拒绝接口
export function portalApproveRefuse (data = {}) {
  return request({
    url: `${prefix}/engine/approve/approve/refuse`,
    method: 'post',
    data
  })
}

export function portalApproveReturn (data = {}) {
  return request({
    url: `${prefix}/engine/approve/approve/returnReviewed`,
    method: 'post',
    data
  })
}
// 审批通过接口
export function portalApprovePass (data = {}) {
  return request({
    url: `${prefix}/engine/approve/approve/pass`,
    method: 'post',
    data
  })
}

// 批量撤回
export function portalApproveWithdraw (data = {}) {
  return request({
    url: `${prefix}/engine/approve/approve/withdraw`,
    method: 'post',
    data
  })
}
// 批量提交
export function portalApproveSubmit (data = {}) {
  return request({
    url: `${prefix}/engine/approve/approve/submit`,
    method: 'post',
    data
  })
}
