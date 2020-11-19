import request from '@/utils/request'

const baseurl = '/system/checkItem/'

// 检查费用接口

export function listCheckItemForPage(data) {
  return request({
    url: baseurl + 'listCheckItemForPage',
    method: 'get',
    params: data
  })
}
export function addCheckItem(data) {
  return request({
    url: baseurl + 'addCheckItem',
    method: 'post',
    params: data
  })
}
export function getCheckItemById(checkItemId) {
  return request({
    url: baseurl + 'getCheckItemById/' + checkItemId,
    method: 'get'
  })
}
export function updateCheckItem(data) {
  return request({
    url: baseurl + 'updateCheckItem',
    method: 'put',
    params: data
  })
}
export function deleteCheckItemByIds(checkItemIds) {
  return request({
    url: baseurl + 'deleteCheckItemByIds/' + checkItemIds,
    method: 'delete'
  })
}
export function selectAllCheckItem() {
  return request({
    url: baseurl + 'selectAllCheckItem',
    method: 'get'
  })
}
