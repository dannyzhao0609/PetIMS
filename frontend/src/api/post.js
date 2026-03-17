import request from './request'

export const getRecentPosts = () => {
  return request({
    url: '/posts/recent',
    method: 'get'
  })
}

export const getPostList = (userId) => {
  return request({
    url: `/posts/user/${userId}`,
    method: 'get'
  })
}

export const getPostById = (id) => {
  return request({
    url: `/posts/${id}`,
    method: 'get'
  })
}

export const savePost = (data) => {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

export const updatePost = (data) => {
  return request({
    url: '/posts',
    method: 'put',
    data
  })
}

export const deletePost = (id) => {
  return request({
    url: `/posts/${id}`,
    method: 'delete'
  })
}
