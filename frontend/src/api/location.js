import request from './request'

export function getLocationTracks() {
  return request({
    url: '/location-tracks',
    method: 'get'
  })
}

export function getLocationTracksByPetId(petId) {
  return request({
    url: `/location-tracks/pet/${petId}`,
    method: 'get'
  })
}

export function getLatestLocationByPetId(petId) {
  return request({
    url: `/location-tracks/pet/${petId}/latest`,
    method: 'get'
  })
}

export function createLocationTrack(data) {
  return request({
    url: '/location-tracks',
    method: 'post',
    data
  })
}

export function deleteLocationTrack(id) {
  return request({
    url: `/location-tracks/${id}`,
    method: 'delete'
  })
}
