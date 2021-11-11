import request from '@/utils/request'

// 分页查询
export function listForPage(query) {
  return request({
    url: '/system/dict/data/listForPage',
    method: 'get',
    params: query
  })
}
// 添加
export function addDictData(data) {
  return request({
    url: '/system/dict/data/addDictData',
    method: 'post',
    params: data
  })
}
// 修改
export function updateDictData(data) {
  return request({
    url: '/system/dict/data/updateDictData',
    method: 'put',
    params: data
  })
}
// 删除
export function deleteDictDataByIds(dictCodeIds) {
  return request({
    url: '/system/dict/data/deleteDictDataByIds/' + dictCodeIds,
    method: 'delete'
  })
}
// 查询一个
export function getDictDataById(dictCode) {
  return request({
    url: '/system/dict/data/getOne/' + dictCode,
    method: 'get'
  })
}
// 根据字典类型查询字典信息【公共的】
export function getDataByType(dictType) {
  return request({
    url: '/system/dict/data/getDataByType/' + dictType,
    method: 'get'
  })
}
