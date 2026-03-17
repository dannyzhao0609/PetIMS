import request from './request'

export function getHygieneRecordsByPetId(petId) {
  return request({
    url: `/hygiene-records/pet/${petId}`,
    method: 'get'
  })
}

export function getHygieneRecordById(id) {
  return request({
    url: `/hygiene-records/${id}`,
    method: 'get'
  })
}

export function createHygieneRecord(data) {
  return request({
    url: '/hygiene-records',
    method: 'post',
    data
  })
}

export function updateHygieneRecord(data) {
  return request({
    url: '/hygiene-records',
    method: 'put',
    data
  })
}

export function deleteHygieneRecord(id) {
  return request({
    url: `/hygiene-records/${id}`,
    method: 'delete'
  })
}
