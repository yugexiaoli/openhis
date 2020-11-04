import request from '@/utils/request'

const baseurl = '/system/dict/type/'
// 字典类型接口
export function listForPage(data) {
  return request({
    url: baseurl + 'listForPage',
    method: 'get',
    params: data
  })
}

export function addDictType(data) {
  return request({
    url: baseurl + 'addDictType',
    method: 'post',
    params: data
  })
}
export function typegetOne(dictId) {
  return request({
    url: baseurl + 'getOne/' + dictId,
    method: 'get'
  })
}
export function updateDictType(data) {
  return request({
    url: baseurl + 'updateDictType',
    method: 'put',
    params: data
  })
}
export function deleteDictTypeByIds(dictIds) {
  return request({
    url: baseurl + 'deleteDictTypeByIds/' + dictIds,
    method: 'delete'
  })
}
export function selectAllDictType() {
  return request({
    url: baseurl + 'selectAllDictType',
    method: 'get'
  })
}
export function dictCacheAsync() {
  return request({
    url: baseurl + 'dictCacheAsync',
    method: 'get'
  })
}
