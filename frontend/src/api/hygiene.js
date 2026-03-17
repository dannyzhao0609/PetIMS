import request from './request'

export const getHygieneList = (petId) => {
  return request({
    url: `/hygiene-records/pet/${petId}`,
    method: 'get'
  })
}

export const getHygieneById = (id) => {
  return request({
    url: `/hygiene-records/${id}`,
    method: 'get'
  })
}

export const saveHygiene = (data) => {
  return request({
    url: '/hygiene-records',
    method: 'post',
    data
  })
}

export const updateHygiene = (data) => {
  return request({
    url: '/hygiene-records',
    method: 'put',
    data
  })
}

export const deleteHygiene = (id) => {
  return request({
    url: `/hygiene-records/${id}`,
    method: 'delete'
  })
}
