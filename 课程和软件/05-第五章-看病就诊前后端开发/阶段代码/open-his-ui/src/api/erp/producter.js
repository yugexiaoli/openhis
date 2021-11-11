import request from '@/utils/request'

// 分页查询
export function listProducterForPage(query) {
  return request({
    url: '/erp/producter/listProducterForPage',
    method: 'get',
    params: query
  })
}
// 添加
export function addProducter(data) {
  return request({
    url: '/erp/producter/addProducter',
    method: 'post',
    params: data
  })
}
// 修改
export function updateProducter(data) {
  return request({
    url: '/erp/producter/updateProducter',
    method: 'put',
    params: data
  })
}
// 删除
export function deleteProducterByIds(id) {
  return request({
    url: '/erp/producter/deleteProducterByIds/' + id,
    method: 'delete'
  })
}
// 查询一个
export function getProducterById(id) {
  return request({
    url: '/erp/producter/getProducterById/' + id,
    method: 'get'
  })
}
// 查询所有有效的生产厂家信息
export function selectAllProducter() {
  return request({
    url: '/erp/producter/selectAllProducter',
    method: 'get'
  })
}
