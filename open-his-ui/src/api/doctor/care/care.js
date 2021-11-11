import request from '@/utils/request'

const baseurl = '/doctor/care/'
// 查询待就诊的挂号信息
export function queryToBeSeenRegistration(scheudlingType) {
  return request({
    url: baseurl + 'queryToBeSeenRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 查询就诊中的挂号信息
export function queryVisitingRegistration(scheudlingType) {
  return request({
    url: baseurl + 'queryVisitingRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 查询就诊完成的挂号信息
export function queryVisitCompletedRegistration(scheudlingType) {
  return request({
    url: baseurl + 'queryVisitCompletedRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 接诊
export function receivePatient(regId) {
  return request({
    url: baseurl + 'receivePatient/' + regId,
    method: 'post'
  })
}

// 根据患者ID获取患者信息、档案信息、病历信息
export function getPatientAllMessageByPatientId(patientId) {
  return request({
    url: baseurl + 'getPatientAllMessageByPatientId/' + patientId,
    method: 'get'
  })
}
// 保存病例信息
export function saveCareHistory(date) {
  return request({
    url: baseurl + 'saveCareHistory',
    method: 'post',
    data: date
  })
}

// 根据挂号ID查询病历信息
export function getCareHistoryByRegId(regId) {
  return request({
    url: baseurl + 'getCareHistoryByRegId/' + regId,
    method: 'get'
  })
}

// 根据病例ID查询处方列表及详情
export function queryCareOrdersByChId(chId) {
  return request({
    url: baseurl + 'queryCareOrdersByChId/' + chId,
    method: 'get'
  })
}
// 保存处方和详情信息
export function saveCareOrderItem(date) {
  return request({
    url: baseurl + 'saveCareOrderItem',
    method: 'post',
    data: date
  })
}
// 删除处方详情信息
export function deleteCareOrderItemById(itemId) {
  return request({
    url: baseurl + 'deleteCareOrderItemById/' + itemId,
    method: 'delete'
  })
}

// 完成就诊
export function visitComplete(regId) {
  return request({
    url: baseurl + 'visitComplete/' + regId,
    method: 'post'
  })
}
