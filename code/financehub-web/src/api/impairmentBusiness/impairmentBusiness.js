
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 减值计提-i提交
export function submitImpairmentProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/submit`,
    method: 'post',
    data
  })
}

// 减值计提-撤回
export function withdrawImpairmentProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/withdraw`,
    method: 'post',
    data
  })
}
// 减值计提-上传列表
export function excelUploadListImpairmentProvision () {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/getUploadExcelList`,
    method: 'get'
  })
}
// 减值计提-获取导出减值清单excel列表
export function excelExportListImpairmentProvision () {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/getExportExcelList`,
    method: 'get'
  })
}

// 减值计提-生成凭证
export function voucherImpairmentProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/generateVoucher`,
    method: 'post',
    data
  })
}
// 减值计提-查看本月减值报告
export function reportImpairmentProvision () {
  return request({
    url: `${prefix}/engine/finance/impairment-provision/getImpairmentReport`,
    method: 'get'
  })
}
// 减值计提-详情数据汇总
export function summaryImpairmentProvision (data = {}) {
  return request({
    url: `${prefix}/engine/finance/impairment-provision-detail/summary`,
    method: 'post',
    data
  })
}
