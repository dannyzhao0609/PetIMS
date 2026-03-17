import request from './request'

export const getHealthList = (petId) => {
  return request({
    url: `/health-records/pet/${petId}`,
    method: 'get'
  })
}

export const getHealthById = (id) => {
  return request({
    url: `/health-records/${id}`,
    method: 'get'
  })
}

export const saveHealth = (data) => {
  return request({
    url: '/health-records',
    method: 'post',
    data
  })
}

export const updateHealth = (data) => {
  return request({
    url: '/health-records',
    method: 'put',
    data
  })
}

export const deleteHealth = (id) => {
  return request({
    url: `/health-records/${id}`,
    method: 'delete'
  })
}
