import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { adminLogin } from '../../api/auth'
import { useAuth } from '../../contexts/AuthContext'
import toast from 'react-hot-toast'
import { ShieldCheck } from 'lucide-react'

export default function AdminLogin() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [loading, setLoading] = useState(false)
  const { loginAdmin } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const res = await adminLogin(form)
      // Response shape: { user: { accessToken }, profile: { email, firstName, ... } }
      const token = res.data.user?.accessToken
      const profile = res.data.profile || res.data
      loginAdmin(token, profile)
      toast.success('Welcome, Admin!')
      navigate('/admin/dashboard')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-800 to-gray-900 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8">
        <div className="text-center mb-8">
          <div className="w-14 h-14 bg-gray-900 rounded-2xl flex items-center justify-center mx-auto mb-4">
            <ShieldCheck size={28} className="text-white" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900">Admin Portal</h1>
          <p className="text-gray-500 mt-1 text-sm">FundGlide Administration</p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              className="input-field"
              placeholder="admin@fintabsolution.com"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input
              type="password"
              className="input-field"
              placeholder="Admin password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              required
            />
          </div>
          <button type="submit" className="w-full py-3 bg-gray-900 hover:bg-gray-800 text-white font-medium rounded-lg transition-colors disabled:opacity-50" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        <p className="text-center text-sm text-gray-400 mt-6">
          <Link to="/login" className="hover:text-gray-600">← Customer login</Link>
        </p>
      </div>
    </div>
  )
}
