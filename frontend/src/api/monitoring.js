import request from './request'

export const getMonitoringList = (petId) => {
  return request({
    url: `/monitoring-records/pet/${petId}`,
    method: 'get'
  })
}

export const getMonitoringById = (id) => {
  return request({
    url: `/monitoring-records/${id}`,
    method: 'get'
  })
}

export const saveMonitoring = (data) => {
  return request({
    url: '/monitoring-records',
    method: 'post',
    data
  })
}

export const updateMonitoring = (data) => {
  return request({
    url: '/monitoring-records',
    method: 'put',
    data
  })
}

export const deleteMonitoring = (id) => {
  return request({
    url: `/monitoring-records/${id}`,
    method: 'delete'
  })
}
