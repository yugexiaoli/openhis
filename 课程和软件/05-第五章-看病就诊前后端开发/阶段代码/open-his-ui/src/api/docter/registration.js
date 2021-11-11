import request from '@/utils/request'
// 查询挂号列表
export function listDeptForScheduling(query) {
  return request({
    url: '/doctor/registration/listDeptForScheduling',
    method: 'get',
    params: query
  })
}
// 根据身份证号查询患者信息
export function getPatientByIdCard(idCard) {
  return request({
    url: '/doctor/registration/getPatientByIdCard/' + idCard,
    method: 'get'
  })
}
// 保存挂号信息
export function addRegistration(data) {
  return request({
    url: '/doctor/registration/addRegistration',
    method: 'post',
    data: data
  })
}

// 收费
export function collectFee(registrationId) {
  return request({
    url: '/doctor/registration/collectFee/' + registrationId,
    method: 'post'
  })
}
// 分页查询挂号信息
export function queryRegistrationForPage(query) {
  return request({
    url: '/doctor/registration/queryRegistrationForPage',
    method: 'get',
    params: query
  })
}

// 作废
export function doInvalid(registrationId) {
  return request({
    url: '/doctor/registration/doInvalid/' + registrationId,
    method: 'post'
  })
}
// 退号
export function doReturn(registrationId) {
  return request({
    url: '/doctor/registration/doReturn/' + registrationId,
    method: 'post'
  })
}
