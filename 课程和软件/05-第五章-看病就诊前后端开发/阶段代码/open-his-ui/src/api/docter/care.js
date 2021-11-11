import request from '@/utils/request'
// 加载挂号列表[待就诊]
export function queryToBeSeenRegistration(scheudlingType) {
  return request({
    url: '/doctor/care/queryToBeSeenRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 加载挂号列表[就诊中]
export function queryVisitingRegistration(scheudlingType) {
  return request({
    url: '/doctor/care/queryVisitingRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 加载挂号列表[就诊完成]
export function queryVisitCompletedRegistration(scheudlingType) {
  return request({
    url: '/doctor/care/queryVisitCompletedRegistration/' + scheudlingType,
    method: 'get'
  })
}
// 接诊
export function receivePatient(regId) {
  return request({
    url: '/doctor/care/receivePatient/' + regId,
    method: 'post'
  })
}
// 根据患者ID查询患者信息 患者档案信息  历史病例
export function getPatientAllMessageByPatientId(patientId) {
  return request({
    url: '/doctor/care/getPatientAllMessageByPatientId/' + patientId,
    method: 'get'
  })
}
// 保存病历的接口
export function saveCareHistory(data) {
  return request({
    url: '/doctor/care/saveCareHistory',
    method: 'post',
    data: data
  })
}
// 根据挂号ID查询病历信息
export function getCareHistoryByRegId(regId) {
  return request({
    url: '/doctor/care/getCareHistoryByRegId/' + regId,
    method: 'get'
  })
}
// 根据病例ID查询处方列表及详情
export function queryCareOrdersByChId(chId) {
  return request({
    url: '/doctor/care/queryCareOrdersByChId/' + chId,
    method: 'get'
  })
}
// 保存处方及详情信息
export function saveCareOrderItem(data) {
  return request({
    url: '/doctor/care/saveCareOrderItem',
    method: 'post',
    data: data
  })
}
// 根据处方详情ID删除处方详情【只能删除未支付的】
export function deleteCareOrderItemById(itemId) {
  return request({
    url: '/doctor/care/deleteCareOrderItemById/' + itemId,
    method: 'delete'
  })
}
// 根据挂号单号完成就诊
export function visitComplete(regId) {
  return request({
    url: '/doctor/care/visitComplete/' + regId,
    method: 'post'
  })
}
