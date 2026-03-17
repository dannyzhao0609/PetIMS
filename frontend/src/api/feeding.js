import request from './request'

export const getFeedingList = (petId) => {
  return request({
    url: `/feeding-records/pet/${petId}`,
    method: 'get'
  })
}

export const getFeedingById = (id) => {
  return request({
    url: `/feeding-records/${id}`,
    method: 'get'
  })
}

export const saveFeeding = (data) => {
  return request({
    url: '/feeding-records',
    method: 'post',
    data
  })
}

export const updateFeeding = (data) => {
  return request({
    url: '/feeding-records',
    method: 'put',
    data
  })
}

export const deleteFeeding = (id) => {
  return request({
    url: `/feeding-records/${id}`,
    method: 'delete'
  })
}
