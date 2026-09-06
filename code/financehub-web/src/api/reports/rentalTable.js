
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 根据日期检查
export function leaseTableCheck (data = {}) {
  return request({
    url: `${prefix}/engine/finance/report/leaseTable/check`,
    method: 'post',
    data
  })
}

// 根据日期检查
export function leaseTableExport (data = {}) {
  return request({
    url: `${prefix}/engine/finance/report/leaseTable/export`,
    method: 'post',
    data
  })
}
// 租赁大表-上传文件查询
export function leaseTableFileList (data = {}) {
  return request({
    url: `${prefix}/engine/finance/report/leaseTable/fileList`,
    method: 'post',
    data
  })
}

//生成批量查询数据excel
export function batchExport (data={}) {
  return request({
    url: `${prefix}/engine/finance/report/inbound-outbound/export`,
    method: 'post',
    data
  })
}
