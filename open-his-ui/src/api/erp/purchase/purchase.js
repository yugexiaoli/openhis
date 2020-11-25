import request from '@/utils/request'

const baseurl = '/erp/purchase/'

// 采购单据接口

export function listPurchaseForPage(data) {
  return request({
    url: baseurl + 'listPurchaseForPage',
    method: 'get',
    params: data
  })
}

export function listPurchasePendingForPage(data) {
  return request({
    url: baseurl + 'listPurchasePendingForPage',
    method: 'get',
    params: data
  })
}
export function doAudit(purchaseId) {
  return request({
    url: baseurl + 'doAudit/' + purchaseId,
    method: 'post'
  })
}
export function doInvalid(purchaseId) {
  return request({
    url: baseurl + 'doInvalid/' + purchaseId,
    method: 'post'
  })
}
export function auditPass(purchaseId) {
  return request({
    url: baseurl + 'auditPass/' + purchaseId,
    method: 'post'
  })
}
export function auditNoPass(purchaseId, auditMsg) {
  return request({
    url: baseurl + 'auditNoPass/' + purchaseId + '/' + auditMsg,
    method: 'post'
  })
}

export function getPurchaseItemById(purchaseId) {
  return request({
    url: baseurl + 'getPurchaseItemById/' + purchaseId,
    method: 'get'
  })
}
