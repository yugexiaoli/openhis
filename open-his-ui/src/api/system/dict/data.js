import request from '@/utils/request'

const baseurl = '/system/dict/data/'

// 字典数据接口

export function listForPage(data) {
  return request({
    url: baseurl + 'listForPage',
    method: 'get',
    params: data
  })
}
export function addDictData(data) {
  return request({
    url: baseurl + 'addDictData',
    method: 'post',
    params: data
  })
}
export function getOne(dictCode) {
  return request({
    url: baseurl + 'getOne/' + dictCode,
    method: 'get'
  })
}
export function updateDictData(data) {
  return request({
    url: baseurl + 'updateDictData',
    method: 'put',
    params: data
  })
}
export function deleteDictDataByIds(dictCodeIds) {
  return request({
    url: baseurl + 'deleteDictDataByIds/' + dictCodeIds,
    method: 'delete'
  })
}
export function getDataByType(dictType) {
  return request({
    url: baseurl + 'getDataByType/' + dictType,
    method: 'get'
  })
}
export function selectDicDataBydictType(dictType) {
  return request({
    url: baseurl + 'selectDicDataBydictType/' + dictType,
    method: 'get'
  })
}
