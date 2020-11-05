import request from '@/utils/request'

const baseurl = '/system/operLog/'

// 操作日志接口

export function listForPage(data) {
  return request({
    url: baseurl + 'listForPage',
    method: 'get',
    params: data
  })
}
export function deleteOperLogByIds(infoIds) {
  return request({
    url: baseurl + 'deleteOperLogByIds/' + infoIds,
    method: 'delete'
  })
}
export function clearAllOperLog() {
  return request({
    url: baseurl + 'clearAllOperLog',
    method: 'delete'
  })
}
