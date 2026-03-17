import request from './request'

export function getPetsByUserId(userId) {
  return request({
    url: `/pets/user/${userId}`,
    method: 'get'
  })
}

export function getPetById(id) {
  return request({
    url: `/pets/${id}`,
    method: 'get'
  })
}

export function createPet(data) {
  return request({
    url: '/pets',
    method: 'post',
    data
  })
}

export function updatePet(data) {
  return request({
    url: '/pets',
    method: 'put',
    data
  })
}

export function deletePet(id) {
  return request({
    url: `/pets/${id}`,
    method: 'delete'
  })
}
