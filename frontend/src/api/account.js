import api from './axios'

export const getAccounts = () => api.get('/account')
export const lookupAccount = (accountNo) => api.get(`/account/lookup/${accountNo}`)
export const setPin = (data) => api.post('/account/set-pin', data)
export const transfer = (data) => api.post('/account/transfer', data)
export const validateChannel = (data) => api.post('/account/channel/validate', data)
export const bulkPayment = (data) => api.post('/account/payment/bulk', data)
export const getBulkPayments = () => api.get('/account/payment/bulk')
export const getBulkPaymentById = (batchId) => api.get(`/account/payment/bulk/${batchId}`)
export const getTransactionLogs = () => api.get('/account/logs')
export const createInternalAccount = (data) => api.post('/account/internal', data)
export const getInternalAccounts = () => api.get('/account/internal')
