import { Menu, Bell, LogOut, User } from 'lucide-react'
import { useAuth } from '../../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

export default function Navbar({ onMenuClick }) {
  const { user, logout, role } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate(role === 'admin' ? '/admin/login' : '/login')
  }

  return (
    <header className="bg-white border-b border-gray-200 px-4 py-3 flex items-center justify-between">
      <div className="flex items-center gap-3">
        <button onClick={onMenuClick} className="lg:hidden p-1 rounded-md hover:bg-gray-100">
          <Menu size={20} />
        </button>
        <span className="text-blue-700 font-bold text-lg">FundGlide</span>
      </div>
      <div className="flex items-center gap-3">
        <button className="p-2 rounded-full hover:bg-gray-100 relative">
          <Bell size={18} />
        </button>
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
            <User size={16} className="text-blue-600" />
          </div>
          <span className="hidden sm:block text-sm font-medium text-gray-700">
            {user?.firstName || user?.username || 'User'}
          </span>
        </div>
        <button onClick={handleLogout} className="p-2 rounded-full hover:bg-red-50 text-gray-500 hover:text-red-600">
          <LogOut size={18} />
        </button>
      </div>
    </header>
  )
}
