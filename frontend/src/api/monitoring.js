import request from './request'

export function getMonitoringRecordsByPetId(petId) {
  return request({
    url: `/monitoring-records/pet/${petId}`,
    method: 'get'
  })
}

export function getMonitoringRecordById(id) {
  return request({
    url: `/monitoring-records/${id}`,
    method: 'get'
  })
}

export function createMonitoringRecord(data) {
  return request({
    url: '/monitoring-records',
    method: 'post',
    data
  })
}

export function updateMonitoringRecord(data) {
  return request({
    url: '/monitoring-records',
    method: 'put',
    data
  })
}

export function deleteMonitoringRecord(id) {
  return request({
    url: `/monitoring-records/${id}`,
    method: 'delete'
  })
}
