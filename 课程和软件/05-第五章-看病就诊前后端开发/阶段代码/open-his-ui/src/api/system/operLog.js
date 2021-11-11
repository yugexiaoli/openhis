import request from '@/utils/request'
// 分页查询
export function listForPage(query) {
  return request({
    url: '/system/operLog/listForPage',
    method: 'get',
    params: query
  })
}
// 删除
export function deleteOperLogByIds(ids) {
  return request({
    url: '/system/operLog/deleteOperLogByIds/' + ids,
    method: 'delete'
  })
}
// 清空
export function clearAllOperLog() {
  return request({
    url: '/system/operLog/clearAllOperLog',
    method: 'delete'
  })
}

