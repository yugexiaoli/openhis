import request from '@/utils/request'
// 登陆的后端接口配置
export function login(data) {
  return request({
    url: '/login/doLogin',
    method: 'post',
    data
  })
}
// 登陆成功之后得到用户信息接口配置
export function getInfo(token) {
  return request({
    url: '/login/getInfo',
    method: 'get',
    params: { token }
  })
}
// 退出
export function logout() {
  return request({
    url: '/login/logout',
    method: 'post'
  })
}
// 加载菜单信息
export function getMenus() {
  return request({
    url: '/login/getMenus',
    method: 'get'
  })
}
