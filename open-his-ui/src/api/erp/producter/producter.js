import request from '@/utils/request'

const baseurl = '/erp/producter/'

// stock_producter接口

export function listProducterForPage(data) {
  return request({
    url: baseurl + 'listProducterForPage',
    method: 'get',
    params: data
  })
}
export function addProducter(data) {
  return request({
    url: baseurl + 'addProducter',
    method: 'post',
    params: data
  })
}
export function getProducterById(producterId) {
  return request({
    url: baseurl + 'getProducterById/' + producterId,
    method: 'get'
  })
}
export function updateProducter(data) {
  return request({
    url: baseurl + 'updateProducter',
    method: 'put',
    params: data
  })
}
export function deleteProducterByIds(producterIds) {
  return request({
    url: baseurl + 'deleteProducterByIds/' + producterIds,
    method: 'delete'
  })
}
export function selectAllProducter() {
  return request({
    url: baseurl + 'selectAllProducter',
    method: 'get'
  })
}
