import request from '@/utils/request'

const baseurl = '/doctor/scheduling/'

export function saveScheduling(data) {
  return request({
    url: baseurl + 'saveScheduling',
    method: 'post',
    data: data
  })
}
export function queryMyScheduling(data) {
  return request({
    url: baseurl + 'queryMyScheduling',
    method: 'get',
    params: data
  })
}
export function queryScheduling(data) {
  return request({
    url: baseurl + 'queryScheduling',
    method: 'get',
    params: data
  })
}
export function queryUsersNeedScheduling(userId, deptId) {
  return request({
    url: baseurl + 'queryUsersNeedScheduling',
    method: 'get',
    params: userId, deptId
  })
}
