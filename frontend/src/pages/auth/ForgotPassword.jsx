import { useState } from 'react'
import { Link } from 'react-router-dom'
import { resetPassword, resetConfirm } from '../../api/auth'
import toast from 'react-hot-toast'

export default function ForgotPassword() {
  const [step, setStep] = useState(1)
  const [email, setEmail] = useState('')
  const [form, setForm] = useState({ token: '', newPassword: '' })
  const [loading, setLoading] = useState(false)

  const handleReset = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await resetPassword({ email })
      toast.success('Reset code sent to your email')
      setStep(2)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to send reset email')
    } finally {
      setLoading(false)
    }
  }

  const handleConfirm = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await resetConfirm({ ...form, email })
      toast.success('Password reset successfully!')
      setStep(3)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to reset password')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-blue-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-lg w-full max-w-md p-8">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">Reset Password</h1>
          <p className="text-gray-500 mt-1 text-sm">
            {step === 1 ? 'Enter your email to receive a reset code' : step === 2 ? 'Enter the code and new password' : 'Password reset successful'}
          </p>
        </div>

        {step === 1 && (
          <form onSubmit={handleReset} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input type="email" className="input-field" placeholder="your@email.com" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </div>
            <button type="submit" className="btn-primary w-full py-3" disabled={loading}>
              {loading ? 'Sending...' : 'Send Reset Code'}
            </button>
          </form>
        )}

        {step === 2 && (
          <form onSubmit={handleConfirm} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Reset Token</label>
              <input type="text" className="input-field" placeholder="Enter token from email" value={form.token} onChange={(e) => setForm({ ...form, token: e.target.value })} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">New Password</label>
              <input type="password" className="input-field" placeholder="New password" value={form.newPassword} onChange={(e) => setForm({ ...form, newPassword: e.target.value })} required />
            </div>
            <button type="submit" className="btn-primary w-full py-3" disabled={loading}>
              {loading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        )}

        {step === 3 && (
          <div className="text-center">
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <p className="text-gray-600 mb-4">Your password has been reset successfully.</p>
            <Link to="/login" className="btn-primary inline-block">Back to Login</Link>
          </div>
        )}

        {step !== 3 && (
          <p className="text-center text-sm text-gray-500 mt-6">
            <Link to="/login" className="text-blue-600 hover:underline">← Back to login</Link>
          </p>
        )}
      </div>
    </div>
  )
}
