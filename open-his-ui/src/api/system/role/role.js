import request from '@/utils/request'

const baseurl = '/system/role/'

// 角色接口

export function listRoleForPage(data) {
  return request({
    url: baseurl + 'listRoleForPage',
    method: 'get',
    params: data
  })
}
export function selectAllRole() {
  return request({
    url: baseurl + 'selectAllRole',
    method: 'get'
  })
}
export function addRole(data) {
  return request({
    url: baseurl + 'addRole',
    method: 'post',
    params: data
  })
}
export function getRoleById(roleId) {
  return request({
    url: baseurl + 'getRoleById/' + roleId,
    method: 'get'
  })
}
export function updateRole(data) {
  return request({
    url: baseurl + 'updateRole',
    method: 'put',
    params: data
  })
}
export function deleteRoleByIds(roleIds) {
  return request({
    url: baseurl + 'deleteRoleByIds/' + roleIds,
    method: 'delete'
  })
}
export function saveRoleMenu(roleId, menuIds) {
  if (menuIds.length === 0) {
    menuIds = [-1]
  }
  return request({
    url: baseurl + 'saveRoleMenu/' + roleId + '/' + menuIds,
    method: 'post'
  })
}

