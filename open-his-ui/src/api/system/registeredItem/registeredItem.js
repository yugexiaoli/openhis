import request from '@/utils/request'

const baseurl = '/system/registeredItem/'

// 挂号费用接口

export function listRegisteredItemForPage(data) {
  return request({
    url: baseurl + 'listRegisteredItemForPage',
    method: 'get',
    params: data
  })
}
export function addRegisteredItem(data) {
  return request({
    url: baseurl + 'addRegisteredItem',
    method: 'post',
    params: data
  })
}
export function getRegisteredItemById(registeredItemId) {
  return request({
    url: baseurl + 'getRegisteredItemById/' + registeredItemId,
    method: 'get'
  })
}
export function updateRegisteredItem(data) {
  return request({
    url: baseurl + 'updateRegisteredItem',
    method: 'put',
    params: data
  })
}
export function deleteRegisteredItemByIds(registeredItemIds) {
  return request({
    url: baseurl + 'deleteRegisteredItemByIds/' + registeredItemIds,
    method: 'delete'
  })
}
export function selectAllRegisteredItem() {
  return request({
    url: baseurl + 'selectAllRegisteredItem',
    method: 'get'
  })
}
