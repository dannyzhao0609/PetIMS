import request from './request'

export const getPetList = (userId) => {
  return request({
    url: `/pets/user/${userId}`,
    method: 'get'
  })
}

export const getPetById = (id) => {
  return request({
    url: `/pets/${id}`,
    method: 'get'
  })
}

export const savePet = (data) => {
  return request({
    url: '/pets',
    method: 'post',
    data
  })
}

export const updatePet = (data) => {
  return request({
    url: '/pets',
    method: 'put',
    data
  })
}

export const deletePet = (id) => {
  return request({
    url: `/pets/${id}`,
    method: 'delete'
  })
}
