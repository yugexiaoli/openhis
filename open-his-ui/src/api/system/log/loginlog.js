import request from '@/utils/request'

const baseurl = '/system/loginInfo/'

// 登录日志接口

export function listForPage(data) {
  return request({
    url: baseurl + 'listForPage',
    method: 'get',
    params: data
  })
}
export function deleteLoginInfoByIds(infoIds) {
  return request({
    url: baseurl + 'deleteLoginInfoByIds/' + infoIds,
    method: 'delete'
  })
}
export function clearLoginInfo() {
  return request({
    url: baseurl + 'clearLoginInfo',
    method: 'delete'
  })
}
