import { createContext, useContext, useState, useEffect } from 'react'
import { getMe, getAdminMe } from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [role, setRole] = useState(null) // 'customer' | 'admin'
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const savedRole = localStorage.getItem('role')
    if (token && savedRole) {
      setRole(savedRole)
      const fetchUser = savedRole === 'admin' ? getAdminMe : getMe
      fetchUser()
        .then((res) => setUser(res.data.profile || res.data))
        .catch(() => logout())
        .finally(() => setLoading(false))
    } else {
      setLoading(false)
    }
  }, [])

  const loginCustomer = (token, userData) => {
    localStorage.setItem('token', token)
    localStorage.setItem('role', 'customer')
    setRole('customer')
    setUser(userData)
  }

  const loginAdmin = (token, userData) => {
    localStorage.setItem('token', token)
    localStorage.setItem('role', 'admin')
    setRole('admin')
    setUser(userData)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    setUser(null)
    setRole(null)
  }

  return (
    <AuthContext.Provider value={{ user, role, loading, loginCustomer, loginAdmin, logout, setUser }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
