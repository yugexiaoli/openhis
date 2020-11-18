import request from '@/utils/request'

// 数据源接口
export function datasourceadd(data) {
  return request({
    url: '/datasource/add',
    method: 'post',
    data
  })
}
export function datasourcelist() {
  return request({
    url: '/datasource/list',
    method: 'post'
  })
}
export function datasourcedbtype() {
  return request({
    url: '/datasource/dbtype',
    method: 'post'
  })
}

export function datasourcetable(datasourceConfigId) {
  return request({
    url: '/datasource/table/' + datasourceConfigId,
    method: 'post'
  })
}

export function datasourcedel(data) {
  return request({
    url: '/datasource/del',
    method: 'post',
    data
  })
}
export function datasourcetest(data) {
  return request({
    url: '/datasource/test',
    method: 'post',
    data
  })
}
export function datasourceupdate(data) {
  return request({
    url: '/datasource/update',
    method: 'post',
    data
  })
}

// 模板接口
export function templatelist() {
  return request({
    url: '/template/list',
    method: 'post'
  })
}
export function templateupdate(data) {
  return request({
    url: '/template/update',
    method: 'post',
    data
  })
}
export function templatedel(data) {
  return request({
    url: '/template/del',
    method: 'post',
    data
  })
}
export function templateadd(data) {
  return request({
    url: '/template/add',
    method: 'post',
    data
  })
}
export function templateget(id) {
  return request({
    url: '/template/get/' + id,
    method: 'post'
  })
}
