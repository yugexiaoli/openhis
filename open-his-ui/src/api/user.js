import request from '@/utils/request'

// 登录后端接口
export function login(data) {
  return request({
    url: '/login/doLogin',
    method: 'post',
    data
  })
}
// 登陆成功得到用户信息接口
export function getInfo(token) {
  return request({
    url: '/login/getInfo',
    method: 'get',
    params: { token }
  })
}
// 退出接口
export function logout() {
  return request({
    url: '/login/logout',
    method: 'get'
  })
}
// 菜单接口
export function getMenus() {
  return request({
    url: '/login/getMenus',
    method: 'get'
  })
}
// 人脸登录后端接口
export function faceLogin(userId) {
  return request({
    url: '/login/faceLogin/' + userId,
    method: 'get'
  })
}
