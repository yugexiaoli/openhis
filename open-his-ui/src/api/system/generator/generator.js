import request from '@/utils/request'

// 代码生成接口

export function generatecode(data) {
  return request({
    url: '/generate/code',
    method: 'post',
    data
  })
}
