
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 判断账期是否已存在对账数据
export function syncCheckExisted (data = {}) {
  return request({
    url: `${prefix}/engine/finance/check-account-detail-record/check/existed`,
    method: 'post',
    data
  })
}
// 科目余额与明细余额对账界面触发
export function syncSubject (periodCode) {
  return request({
    url: `${prefix}/engine/finance/check-account-detail-record/check/detail/${periodCode}`,
    method: 'post'
  })
}
// 金蝶科目余额与中台科目余额对账 界面触发
export function syncSubjectKingdee (periodCode) {
  return request({
    url: `${prefix}/engine/finance/check-account-detail-record/check/kingdee/${periodCode}`,
    method: 'post'
  })
}
// 金蝶中间表科目余额与中台科目余额对账 界面触发
export function syncSubjectMiddle (periodCode) {
  return request({
    url: `${prefix}/engine/finance/check-account-detail-record/check/middle/${periodCode}`,
    method: 'post'
  })
}
// 其他系统与中台数据统一 界面触发
export function syncSubjectCommon (data = {}) {
  return request({
    url: `${prefix}/engine/finance/check-account-detail-record/check/common`,
    method: 'post',
    data
  })
}
