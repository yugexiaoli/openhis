import request from '@/utils/request'

const baseurl = '/system/notice/'

// 通知公告接口

export function listNoticeForPage(data) {
  return request({
    url: baseurl + 'listNoticeForPage',
    method: 'get',
    params: data
  })
}
export function addNotice(data) {
  return request({
    url: baseurl + 'addNotice',
    method: 'post',
    params: data
  })
}
export function getNoticeById(noticeId) {
  return request({
    url: baseurl + 'getNoticeById/' + noticeId,
    method: 'get'
  })
}
export function updateNotice(data) {
  return request({
    url: baseurl + 'updateNotice',
    method: 'put',
    params: data
  })
}
export function deleteNoticeByIds(noticeIds) {
  return request({
    url: baseurl + 'deleteNoticeByIds/' + noticeIds,
    method: 'delete'
  })
}
