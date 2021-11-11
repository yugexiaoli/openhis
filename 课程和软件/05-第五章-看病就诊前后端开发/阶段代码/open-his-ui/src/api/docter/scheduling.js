import request from '@/utils/request'
// 查询需要排班的用户列表 可以传部门ID 如果部门ID为空，则直接显示所有
export function queryUsersNeedScheduling(deptId) {
  return request({
    url: '/doctor/scheduling/queryUsersNeedScheduling',
    params: { deptId: deptId }
  })
}
// 查询排班
export function queryScheduling(query) {
  return request({
    url: '/doctor/scheduling/queryScheduling',
    method: 'get',
    params: query
  })
}
// 保存排班数据
export function saveScheduling(data) {
  return request({
    url: '/doctor/scheduling/saveScheduling',
    method: 'post',
    data: data
  })
}
// 查询我的排班
export function queryMyScheduling(params) {
  return request({
    url: '/doctor/scheduling/queryMyScheduling',
    method: 'get',
    params: params
  })
}
