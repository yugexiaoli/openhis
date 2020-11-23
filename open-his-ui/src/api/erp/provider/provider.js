import request from '@/utils/request'

const baseurl = '/erp/provider/'

// 供应商接口

export function listProviderForPage(data) {
  return request({
    url: baseurl + 'listProviderForPage',
    method: 'get',
    params: data
  })
}
export function addProvider(data) {
  return request({
    url: baseurl + 'addProvider',
    method: 'post',
    params: data
  })
}
export function getProviderById(providerId) {
  return request({
    url: baseurl + 'getProviderById/' + providerId,
    method: 'get'
  })
}
export function updateProvider(data) {
  return request({
    url: baseurl + 'updateProvider',
    method: 'put',
    params: data
  })
}
export function deleteProviderByIds(providerIds) {
  return request({
    url: baseurl + 'deleteProviderByIds/' + providerIds,
    method: 'delete'
  })
}
export function selectAllProvider() {
  return request({
    url: baseurl + 'selectAllProvider',
    method: 'get'
  })
}
