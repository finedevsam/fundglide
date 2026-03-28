import api from './axios'

export const createLoanType = (data) => api.post('/loan', data)
export const getLoanTypes = () => api.get('/loan')
export const updateLoanType = (id, data) => api.put(`/loan/${id}`, data)
export const applyForLoan = (data) => api.post('/loan/apply', data)
export const getMyLoans = () => api.get('/loan/customer')
export const getLoanBreakdown = (loanId) => api.get(`/loan/breakdown/${loanId}`)
export const adminGetAllLoans = () => api.get('/loan/admin/all')
export const adminGetLoanBreakdown = (loanId) => api.get(`/loan/admin/breakdown/${loanId}`)
export const adminApproveLoan = (loanId) => api.get(`/loan/admin/approve/${loanId}`)
