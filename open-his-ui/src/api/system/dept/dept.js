import request from '@/utils/request'

const baseurl = '/system/dept/'

// 部门科室接口

export function listDeptForPage(data) {
  return request({
    url: baseurl + 'listDeptForPage',
    method: 'get',
    params: data
  })
}
export function addDept(data) {
  return request({
    url: baseurl + 'addDept',
    method: 'post',
    params: data
  })
}
export function getDeptById(deptId) {
  return request({
    url: baseurl + 'getDeptById/' + deptId,
    method: 'get'
  })
}
export function updateDept(data) {
  return request({
    url: baseurl + 'updateDept',
    method: 'put',
    params: data
  })
}
export function deleteDeptByIds(deptIds) {
  return request({
    url: baseurl + 'deleteDeptByIds/' + deptIds,
    method: 'delete'
  })
}
export function selectAllDept() {
  return request({
    url: baseurl + 'selectAllDept',
    method: 'get'
  })
}
