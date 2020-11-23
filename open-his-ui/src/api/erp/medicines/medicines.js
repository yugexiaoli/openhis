import request from '@/utils/request'

const baseurl = '/erp/medicines/'

// 药品接口

export function listMedicinesForPage(data) {
  return request({
    url: baseurl + 'listMedicinesForPage',
    method: 'get',
    params: data
  })
}
export function addMedicines(data) {
  return request({
    url: baseurl + 'addMedicines',
    method: 'post',
    params: data
  })
}
export function getMedicinesById(medicinesId) {
  return request({
    url: baseurl + 'getMedicinesById/' + medicinesId,
    method: 'get'
  })
}
export function updateMedicines(data) {
  return request({
    url: baseurl + 'updateMedicines',
    method: 'put',
    params: data
  })
}
export function deleteMedicinesByIds(medicinesIds) {
  return request({
    url: baseurl + 'deleteMedicinesByIds/' + medicinesIds,
    method: 'delete'
  })
}
export function selectAllMedicines() {
  return request({
    url: baseurl + 'selectAllMedicines',
    method: 'get'
  })
}
export function updateMedicinesStorage(medicinesId, medicinesStockNum) {
  return request({
    url: baseurl + 'updateMedicinesStorage/' + medicinesId + '/' + medicinesStockNum,
    method: 'post'
  })
}
