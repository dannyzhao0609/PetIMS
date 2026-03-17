import request from './request'

export function getHealthRecordsByPetId(petId) {
  return request({
    url: `/health-records/pet/${petId}`,
    method: 'get'
  })
}

export function getHealthRecordById(id) {
  return request({
    url: `/health-records/${id}`,
    method: 'get'
  })
}

export function createHealthRecord(data) {
  return request({
    url: '/health-records',
    method: 'post',
    data
  })
}

export function updateHealthRecord(data) {
  return request({
    url: '/health-records',
    method: 'put',
    data
  })
}

export function deleteHealthRecord(id) {
  return request({
    url: `/health-records/${id}`,
    method: 'delete'
  })
}
