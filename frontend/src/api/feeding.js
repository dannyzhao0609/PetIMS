import request from './request'

export function getFeedingRecordsByPetId(petId) {
  return request({
    url: `/feeding-records/pet/${petId}`,
    method: 'get'
  })
}

export function getFeedingRecordById(id) {
  return request({
    url: `/feeding-records/${id}`,
    method: 'get'
  })
}

export function createFeedingRecord(data) {
  return request({
    url: '/feeding-records',
    method: 'post',
    data
  })
}

export function updateFeedingRecord(data) {
  return request({
    url: '/feeding-records',
    method: 'put',
    data
  })
}

export function deleteFeedingRecord(id) {
  return request({
    url: `/feeding-records/${id}`,
    method: 'delete'
  })
}
