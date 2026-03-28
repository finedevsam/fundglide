import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      const role = localStorage.getItem('role')
      localStorage.removeItem('token')
      localStorage.removeItem('role')
      window.location.href = role === 'admin' ? '/admin/login' : '/login'
    }
    return Promise.reject(err)
  }
)

export default api
