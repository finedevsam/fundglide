import api from './axios'

export const register = (data) => api.post('/auth/register', data)
export const login = (data) => api.post('/auth/login', data)
export const adminLogin = (data) => api.post('/auth/admin/login', data)
export const getMe = () => api.get('/auth/me')
export const getAdminMe = () => api.get('/auth/admin/isme')
export const updateProfile = (data) => api.put('/auth/profile', data)
export const resetPassword = (data) => api.post('/auth/reset/password', data)
export const resetConfirm = (data) => api.post('/auth/reset/confirm', data)
export const changePassword = (data) => api.post('/auth/change_password', data)
export const activateQr = () => api.get('/auth/activate/qr')
