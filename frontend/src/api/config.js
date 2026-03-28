import api from './axios'

export const getConfig = (service) => api.get(`/config/${service}`)
export const configureLoan = (data) => api.post('/config/loan', data)
export const configureCurrency = (data) => api.post('/config/currency', data)
export const getCurrencies = () => api.get('/config/currency')
export const setDefaultCurrency = (id) => api.get(`/config/currency/${id}`)
export const deleteCurrency = (id) => api.delete(`/config/currency/${id}`)

export const createPermission = (data) => api.post('/auth/permission', data)
export const getPermissions = () => api.get('/auth/permission')
export const updatePermission = (id, data) => api.put(`/auth/permission/${id}`, data)
export const deletePermission = (id) => api.delete(`/auth/permission/${id}`)
