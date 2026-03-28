import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

export default function ProtectedRoute({ role }) {
  const { user, role: userRole, loading } = useAuth()

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600" />
      </div>
    )
  }

  if (!user) {
    return <Navigate to={role === 'admin' ? '/admin/login' : '/login'} replace />
  }

  if (userRole !== role) {
    return <Navigate to={userRole === 'admin' ? '/admin/dashboard' : '/dashboard'} replace />
  }

  return <Outlet />
}
