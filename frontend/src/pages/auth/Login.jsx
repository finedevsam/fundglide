import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { login } from '../../api/auth'
import { useAuth } from '../../contexts/AuthContext'
import toast from 'react-hot-toast'

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [loading, setLoading] = useState(false)
  const { loginCustomer } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const res = await login(form)
      // Response shape: { user: { accessToken }, profile: { firstName, ... } }
      const token = res.data.user?.accessToken
      const profile = res.data.profile || res.data
      loginCustomer(token, profile)
      toast.success('Welcome back!')
      navigate('/dashboard')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-blue-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-blue-700">FundGlide</h1>
          <p className="text-gray-500 mt-2">Sign in to your account</p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Username / Email</label>
            <input
              type="text"
              className="input-field"
              placeholder="Enter your username"
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input
              type="password"
              className="input-field"
              placeholder="Enter your password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              required
            />
          </div>
          <div className="flex items-center justify-between text-sm">
            <span />
            <Link to="/forgot-password" className="text-blue-600 hover:underline">Forgot password?</Link>
          </div>
          <button type="submit" className="btn-primary w-full py-3" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        <p className="text-center text-sm text-gray-500 mt-6">
          Don't have an account?{' '}
          <Link to="/register" className="text-blue-600 font-medium hover:underline">Create account</Link>
        </p>
        <p className="text-center text-sm text-gray-400 mt-2">
          <Link to="/admin/login" className="hover:text-gray-600">Admin login →</Link>
        </p>
      </div>
    </div>
  )
}
