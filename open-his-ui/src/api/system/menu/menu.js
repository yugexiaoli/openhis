import request from '@/utils/request'

const baseurl = '/system/menu/'

// 菜单接口

export function listAllMenus(data) {
  return request({
    url: baseurl + 'listAllMenus',
    method: 'get',
    params: data
  })
}
export function selectMenuTree() {
  return request({
    url: baseurl + 'selectMenuTree',
    method: 'get'
  })
}
export function addMenu(data) {
  return request({
    url: baseurl + 'addMenu',
    method: 'post',
    params: data
  })
}
export function getMenuById(menuId) {
  return request({
    url: baseurl + 'getMenuById/' + menuId,
    method: 'get'
  })
}
export function updateMenu(data) {
  return request({
    url: baseurl + 'updateMenu',
    method: 'put',
    params: data
  })
}
export function deleteMenuById(menuId) {
  return request({
    url: baseurl + 'deleteMenuById/' + menuId,
    method: 'delete'
  })
}
