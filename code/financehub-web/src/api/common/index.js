import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 列表获取
export function getCommonTableList ({ url, method = 'get', params = {}, config = {} }) {
  const _params = method === 'get' ? { params } : { data: params }
  return request({
    url: `${prefix}${url}`,
    method,
    ..._params,
    ...config
  })
}

export const getAllNMappingDict = () => {
  return request({
    url: `${prefix}/admin/dict/data/map`,
    method: 'get'
  })
}

// 根据字典type获取值
export const getDictItem = (dictType) => {
  return request({
    url: `${prefix}/admin/dict/data/type/${dictType}`,
    method: 'get'
  })
}
// 语法校验
export const validateSyntax = (data) => {
  return request({
    url: `${prefix}/engine/rule/validateSyntax`,
    method: 'post',
    data
  })
}
// 语法校验
export const login = (data) => {
  return request({
    url: `${prefix}/auth/sso/login`,
    method: 'post',
    data,
    headers: {
      isToken: false

    }
  })
}

// 根据合同编码和机构id查询合同信息
export const queryContractInfo = (data) => {
  return request({
    url: `${prefix}/engine/finance/contract/queryContractInfo`,
    method: 'post',
    data
  })
}
