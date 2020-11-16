import request from '@/utils/request'

const baseurl = '/system/user/'

// 角色接口

export function listUserForPage(data) {
  return request({
    url: baseurl + 'listUserForPage',
    method: 'get',
    params: data
  })
}
export function selectAllUser() {
  return request({
    url: baseurl + 'selectAllUser',
    method: 'get'
  })
}
export function addUser(data) {
  return request({
    url: baseurl + 'addUser',
    method: 'post',
    params: data
  })
}
export function getUserById(userId) {
  return request({
    url: baseurl + 'getUserById/' + userId,
    method: 'get'
  })
}
export function updateUser(data) {
  return request({
    url: baseurl + 'updateUser',
    method: 'put',
    params: data
  })
}
export function deleteUserByIds(userIds) {
  return request({
    url: baseurl + 'deleteUserByIds/' + userIds,
    method: 'delete'
  })
}

export function resetPwd(userIds) {
  return request({
    url: baseurl + 'resetPwd/' + userIds,
    method: 'put'
  })
}
export function getRoleIdsByUserId(userId) {
  return request({
    url: baseurl + 'getRoleIdsByUserId/' + userId,
    method: 'get'
  })
}
export function saveUserRole(userId, roleIds) {
  if (roleIds.length === 0) {
    roleIds = [-1]
  }
  return request({
    url: baseurl + 'saveUserRole/' + userId + '/' + roleIds,
    method: 'post'
  })
}

