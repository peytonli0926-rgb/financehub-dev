
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 认领查询
export function nonConfirmCollectionSumQuery (data = {}) {
  return request({
    url: `${prefix}/engine/finance/non-confirm-collection-sum/claimQuery`,
    method: 'post',
    data
  })
}
// 认领确认
export function nonConfirmCollectionSumConfirm (data = {}) {
  return request({
    url: `${prefix}/engine/finance/non-confirm-collection-sum/claimConfirm`,
    method: 'post',
    data
  })
}

// 修改网银编号页面查询
export function nonConfirmCollectionSumModifyEbankNoQuery (data = {}) {
  return request({
    url: `${prefix}/engine/finance/non-confirm-collection-sum/modifyEbankNoQuery`,
    method: 'post',
    data
  })
}

// 修改网银编号确认
export function nonConfirmCollectionSumModifyEbankNoConfirm (data = {}) {
  return request({
    url: `${prefix}/engine/finance/non-confirm-collection-sum/modifyEbankNoConfirm`,
    method: 'post',
    data
  })
}

// 冲销确认
export function nonConfirmCollectionSumWriteOffConfirm (data = {}) {
  return request({
    url: `${prefix}/engine/finance/non-confirm-collection-sum/writeOffConfirm`,
    method: 'post',
    data
  })
}
