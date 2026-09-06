import { useRequest } from '@toystory/lotso'
import requestConfig from '@/config/request.config'

const request = useRequest(requestConfig)

const prefix = import.meta.env.VITE_APP_SERVICE_API

// 财务入库-提交
export function submitRecyclingEquipmentInbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment/submit`,
    method: 'post',
    data
  })
}

// 财务入库-撤回
export function withdrawRecyclingEquipmentInbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment/withdraw`,
    method: 'post',
    data
  })
}
// 财务入库-删除
export function deleteRecyclingEquipmentInbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment/deleteByIds`,
    method: 'post',
    data
  })
}
// 财务入库-生成凭证
export function voucherRecyclingEquipmentInbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment/generateVoucher`,
    method: 'post',
    data
  })
}
// 财务入库-校验
export function checkPageRecyclingEquipmentInbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment/remainBalance/check`,
    method: 'post',
    data
  })
}

//= ========
// 财务出库-提交
export function submitRecyclingEquipmentOutbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment-out/submit`,
    method: 'post',
    data
  })
}

// 财务出库-撤回
export function withdrawRecyclingEquipmentOutbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment-out/withdraw`,
    method: 'post',
    data
  })
}
// 财务出库-删除
export function deleteRecyclingEquipmentOutbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment-out/deleteByIds`,
    method: 'post',
    data
  })
}
// 财务出库-生成凭证
export function voucherRecyclingEquipmentOutbound (data = {}) {
  return request({
    url: `${prefix}/engine/finance/recycling-equipment-out/generateVoucher`,
    method: 'post',
    data
  })
}
