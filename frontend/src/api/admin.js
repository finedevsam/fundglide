import api from './axios'

// Staff
export const createStaff = (data) => api.post('/admin/staff', data)
export const getStaff = () => api.get('/admin/staff')
export const updateStaff = (id, data) => api.put(`/admin/staff/${id}`, data)
export const assignPermission = (id, data) => api.put(`/admin/staff/permission/${id}/assign`, data)
export const revokePermission = (staffId, permissionId) => api.get(`/admin/staff/permission/${staffId}/revoke/${permissionId}`)

// Customers
export const getAllCustomers = () => api.get('/admin/customer')
export const getCustomerAccounts = (id) => api.get(`/admin/customer/${id}/account`)
export const getAllTransactions = () => api.get('/admin/customer/transactions')
export const getAccountLogs = () => api.get('/admin/customer/account/logs')

// Departments
export const createDepartment = (data) => api.post('/admin/department', data)
export const getDepartments = () => api.get('/admin/department')
export const updateDepartment = (id, data) => api.put(`/admin/department/${id}`, data)
export const deleteDepartment = (id) => api.delete(`/admin/department/${id}`)
