import request from '@/utils/request'

const baseurl = '/erp/inventoryLog/'

// stock_inventory_log接口

export function listInventoryLogForPage(data) {
  return request({
    url: baseurl + 'listInventoryLogForPage',
    method: 'get',
    params: data
  })
}

