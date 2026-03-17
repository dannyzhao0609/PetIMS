import request from './request'

export function getSensors() {
  return request({
    url: '/sensors',
    method: 'get'
  })
}

export function getSensorById(id) {
  return request({
    url: `/sensors/${id}`,
    method: 'get'
  })
}

export function createSensor(data) {
  return request({
    url: '/sensors',
    method: 'post',
    data
  })
}

export function updateSensor(data) {
  return request({
    url: '/sensors',
    method: 'put',
    data
  })
}

export function deleteSensor(id) {
  return request({
    url: `/sensors/${id}`,
    method: 'delete'
  })
}

export function getBindings() {
  return request({
    url: '/pet-sensor-bindings',
    method: 'get'
  })
}

export function createBinding(data) {
  return request({
    url: '/pet-sensor-bindings',
    method: 'post',
    data
  })
}

export function deleteBinding(id) {
  return request({
    url: `/pet-sensor-bindings/${id}`,
    method: 'delete'
  })
}

export function getBindingsByPetId(petId) {
  return request({
    url: `/pet-sensor-bindings/pet/${petId}`,
    method: 'get'
  })
}
