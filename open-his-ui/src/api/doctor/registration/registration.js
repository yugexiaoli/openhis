import request from '@/utils/request'

const baseurl = '/doctor/registration/'

export function listDeptForScheduling(data) {
  return request({
    url: baseurl + 'listDeptForScheduling',
    method: 'get',
    params: data
  })
}
export function getPatientByIdCard(idCard) {
  return request({
    url: baseurl + 'getPatientByIdCard/' + idCard,
    method: 'get'
  })
}
export function addRegistration(data) {
  return request({
    url: baseurl + 'addRegistration',
    method: 'post',
    data: data
  })
}
export function collectFee(regId) {
  return request({
    url: baseurl + 'collectFee/' + regId,
    method: 'post'
  })
}
export function queryRegistrationForPage(data) {
  return request({
    url: baseurl + 'queryRegistrationForPage',
    method: 'get',
    params: data
  })
}
export function doInvalid(regId) {
  return request({
    url: baseurl + 'doInvalid/' + regId,
    method: 'post'
  })
}
export function doReturn(regId) {
  return request({
    url: baseurl + 'doReturn/' + regId,
    method: 'post'
  })
}
