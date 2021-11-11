import request from '@/utils/request'

// 分页查询
export function listForPage(query) {
  return request({
    url: '/system/dict/type/listForPage',
    method: 'get',
    params: query
  })
}
// 不分页查询
export function selectAllDictType() {
  return request({
    url: '/system/dict/type/selectAllDictType',
    method: 'get'
  })
}
// 添加
export function addDictType(data) {
  return request({
    url: '/system/dict/type/addDictType',
    method: 'post',
    params: data
  })
}

// 修改
export function updateDictType(data) {
  return request({
    url: '/system/dict/type/updateDictType',
    method: 'put',
    params: data
  })
}
// 删除
export function deleteDictTypeByIds(dictId) {
  return request({
    url: '/system/dict/type/deleteDictTypeByIds/' + dictId,
    method: 'delete'
  })
}
// 查询一个
export function getDictTypeById(dictId) {
  return request({
    url: '/system/dict/type/getOne/' + dictId,
    method: 'get'
  })
}
// 缓存同步接口
export function dictCacheAsync() {
  return request({
    url: '/system/dict/type/dictCacheAsync',
    method: 'get'
  })
}
