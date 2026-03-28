import api from './axios'

export const createTargetSavings = (data) => api.post('/target-savings', data)
export const getTargetSavings = () => api.get('/target-savings')
export const getSavingsHistory = (id) => api.get(`/target-savings/${id}`)
export const quickSave = (id, data) => api.post(`/target-savings/${id}/quicksave`, data)
