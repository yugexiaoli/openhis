import request from '@/utils/request'
// 分页查询
export function listForPage(query) {
  return request({
    url: '/system/loginInfo/listForPage',
    method: 'get',
    params: query
  })
}
// 删除
export function deleteLoginInfoByIds(ids) {
  return request({
    url: '/system/loginInfo/deleteLoginInfoByIds/' + ids,
    method: 'delete'
  })
}
// 清空
export function clearLoginInfo() {
  return request({
    url: '/system/loginInfo/clearLoginInfo',
    method: 'delete'
  })
}

