import request from './request'

export function getPosts() {
  return request({
    url: '/posts',
    method: 'get'
  })
}

export function getPostById(id) {
  return request({
    url: `/posts/${id}`,
    method: 'get'
  })
}

export function createPost(data) {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

export function updatePost(data) {
  return request({
    url: '/posts',
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: `/posts/${id}`,
    method: 'delete'
  })
}
