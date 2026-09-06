
import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 批量查询-表头查询
export function getTableHeader () {
  return request({
    url: `${prefix}/engine/finance/batch-query-upload-record/header`,
    method: 'post'
  })
}
//生成批量查询数据excel
export function batchExport (data={}) {
  return request({
    url: `${prefix}/engine/finance/batch-query-upload-record/data/export`,
    method: 'post',
    data
  })
}

