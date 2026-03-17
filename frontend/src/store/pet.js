import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getPetList } from '@/api/pet'

export const usePetStore = defineStore('pet', () => {
  const petList = ref([])
  const selectedPet = ref(null)

  const fetchPetList = async (userId) => {
    try {
      const res = await getPetList(userId)
      petList.value = res.data || []
      if (petList.value.length > 0 && !selectedPet.value) {
        selectedPet.value = petList.value[0]
      }
    } catch (error) {
      console.error('获取宠物列表失败:', error)
    }
  }

  const setSelectedPet = (pet) => {
    selectedPet.value = pet
  }

  const addPet = (pet) => {
    petList.value.unshift(pet)
  }

  const updatePetInList = (pet) => {
    const index = petList.value.findIndex(p => p.id === pet.id)
    if (index !== -1) {
      petList.value[index] = pet
    }
    if (selectedPet.value?.id === pet.id) {
      selectedPet.value = pet
    }
  }

  const removePet = (id) => {
    const index = petList.value.findIndex(p => p.id === id)
    if (index !== -1) {
      petList.value.splice(index, 1)
    }
    if (selectedPet.value?.id === id) {
      selectedPet.value = petList.value.length > 0 ? petList.value[0] : null
    }
  }

  return {
    petList,
    selectedPet,
    fetchPetList,
    setSelectedPet,
    addPet,
    updatePetInList,
    removePet
  }
})
