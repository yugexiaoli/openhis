import request from '@/utils/request'
// 分页查询所有患者信息
export function listPatientForPage(params) {
  return request({
    url: '/doctor/patient/listPatientForPage',
    params: params
  })
}
// 根据患者ID查询患者信息
export function getPatientById(patientId) {
  return request({
    url: '/doctor/patient/getPatientById/' + patientId,
    method: 'get'
  })
}
// 根据患者ID查询患者的档案信息
export function getPatientFileById(patientId) {
  return request({
    url: '/doctor/patient/getPatientFileById/' + patientId,
    method: 'get'
  })
}
// 根据患者ID查询患者所有信息【基础，档案，病例】
export function getPatientAllMessageByPatientId(patientId) {
  return request({
    url: '/doctor/patient/getPatientAllMessageByPatientId/' + patientId,
    method: 'get'
  })
}

