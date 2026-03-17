import { defineStore } from 'pinia'
import { petApi } from '../api/pet'

export const usePetStore = defineStore('pet', {
  state: () => ({
    pets: [],
    currentPet: null,
    loading: false,
    error: null
  }),
  getters: {
    getPets: (state) => state.pets,
    getCurrentPet: (state) => state.currentPet,
    getLoading: (state) => state.loading,
    getError: (state) => state.error
  },
  actions: {
    async fetchPets() {
      this.loading = true
      this.error = null
      try {
        const response = await petApi.getPets()
        this.pets = response.data
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },
    async fetchPetById(id) {
      this.loading = true
      this.error = null
      try {
        const response = await petApi.getPetById(id)
        this.currentPet = response.data
        return response.data
      } catch (error) {
        this.error = error.message
        return null
      } finally {
        this.loading = false
      }
    },
    async addPet(petData) {
      this.loading = true
      this.error = null
      try {
        const response = await petApi.addPet(petData)
        this.pets.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        return null
      } finally {
        this.loading = false
      }
    },
    async updatePet(id, petData) {
      this.loading = true
      this.error = null
      try {
        const response = await petApi.updatePet(id, petData)
        const index = this.pets.findIndex(pet => pet.id === id)
        if (index !== -1) {
          this.pets[index] = response.data
        }
        if (this.currentPet && this.currentPet.id === id) {
          this.currentPet = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        return null
      } finally {
        this.loading = false
      }
    },
    async deletePet(id) {
      this.loading = true
      this.error = null
      try {
        await petApi.deletePet(id)
        this.pets = this.pets.filter(pet => pet.id !== id)
        if (this.currentPet && this.currentPet.id === id) {
          this.currentPet = null
        }
        return true
      } catch (error) {
        this.error = error.message
        return false
      } finally {
        this.loading = false
      }
    },
    setCurrentPet(pet) {
      this.currentPet = pet
    }
  }
})