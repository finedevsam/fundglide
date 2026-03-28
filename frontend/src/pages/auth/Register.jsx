import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { register } from '../../api/auth'
import toast from 'react-hot-toast'

export default function Register() {
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', username: '', password: '', phone: ''
  })
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const set = (field) => (e) => setForm({ ...form, [field]: e.target.value })

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await register(form)
      toast.success('Account created! Please log in.')
      navigate('/login')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-blue-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-lg p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-blue-700">FundGlide</h1>
          <p className="text-gray-500 mt-2">Create your account</p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
              <input type="text" className="input-field" placeholder="John" value={form.firstName} onChange={set('firstName')} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
              <input type="text" className="input-field" placeholder="Doe" value={form.lastName} onChange={set('lastName')} required />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" className="input-field" placeholder="john@example.com" value={form.email} onChange={set('email')} required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
            <input type="text" className="input-field" placeholder="johndoe" value={form.username} onChange={set('username')} required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input type="tel" className="input-field" placeholder="+1234567890" value={form.phone} onChange={set('phone')} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input type="password" className="input-field" placeholder="Create a password" value={form.password} onChange={set('password')} required />
          </div>
          <button type="submit" className="btn-primary w-full py-3 mt-2" disabled={loading}>
            {loading ? 'Creating account...' : 'Create Account'}
          </button>
        </form>
        <p className="text-center text-sm text-gray-500 mt-6">
          Already have an account?{' '}
          <Link to="/login" className="text-blue-600 font-medium hover:underline">Sign in</Link>
        </p>
      </div>
    </div>
  )
}
