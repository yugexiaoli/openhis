import request from '@/utils/request'

const baseurl = '/system/face'

export function search(imgSrc) {
  return request({
    url: baseurl + '/search',
    method: 'post',
    data: imgSrc
  })
}
