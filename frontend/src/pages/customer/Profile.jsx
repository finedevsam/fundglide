import { useState } from 'react'
import { useAuth } from '../../contexts/AuthContext'
import { updateProfile, changePassword, activateQr } from '../../api/auth'
import toast from 'react-hot-toast'
import { QrCode } from 'lucide-react'

export default function CustomerProfile() {
  const { user, setUser } = useAuth()
  const [profileForm, setProfileForm] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    phone: user?.phone || '',
  })
  const [pwForm, setPwForm] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' })
  const [loadingProfile, setLoadingProfile] = useState(false)
  const [loadingPw, setLoadingPw] = useState(false)
  const [qrCode, setQrCode] = useState(null)

  const handleProfile = async (e) => {
    e.preventDefault()
    setLoadingProfile(true)
    try {
      const res = await updateProfile(profileForm)
      setUser(res.data?.user || res.data)
      toast.success('Profile updated!')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Update failed')
    } finally { setLoadingProfile(false) }
  }

  const handlePw = async (e) => {
    e.preventDefault()
    if (pwForm.newPassword !== pwForm.confirmPassword) return toast.error('Passwords do not match')
    setLoadingPw(true)
    try {
      await changePassword({ oldPassword: pwForm.oldPassword, newPassword: pwForm.newPassword })
      toast.success('Password changed!')
      setPwForm({ oldPassword: '', newPassword: '', confirmPassword: '' })
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to change password')
    } finally { setLoadingPw(false) }
  }

  const handleQr = async () => {
    try {
      const res = await activateQr()
      setQrCode(res.data)
      toast.success('QR code activated!')
    } catch (err) {
      toast.error(err.response?.data?.message || 'QR activation failed')
    }
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Profile</h2>
        <p className="text-gray-500 mt-1">Manage your account details</p>
      </div>

      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Personal Information</h3>
        <form onSubmit={handleProfile} className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
              <input type="text" className="input-field" value={profileForm.firstName} onChange={(e) => setProfileForm({ ...profileForm, firstName: e.target.value })} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
              <input type="text" className="input-field" value={profileForm.lastName} onChange={(e) => setProfileForm({ ...profileForm, lastName: e.target.value })} />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" className="input-field" value={profileForm.email} onChange={(e) => setProfileForm({ ...profileForm, email: e.target.value })} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input type="tel" className="input-field" value={profileForm.phone} onChange={(e) => setProfileForm({ ...profileForm, phone: e.target.value })} />
          </div>
          <button type="submit" className="btn-primary" disabled={loadingProfile}>{loadingProfile ? 'Saving...' : 'Save Changes'}</button>
        </form>
      </div>

      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Change Password</h3>
        <form onSubmit={handlePw} className="space-y-4">
          <input type="password" className="input-field" placeholder="Current password" value={pwForm.oldPassword} onChange={(e) => setPwForm({ ...pwForm, oldPassword: e.target.value })} required />
          <input type="password" className="input-field" placeholder="New password" value={pwForm.newPassword} onChange={(e) => setPwForm({ ...pwForm, newPassword: e.target.value })} required />
          <input type="password" className="input-field" placeholder="Confirm new password" value={pwForm.confirmPassword} onChange={(e) => setPwForm({ ...pwForm, confirmPassword: e.target.value })} required />
          <button type="submit" className="btn-primary" disabled={loadingPw}>{loadingPw ? 'Updating...' : 'Update Password'}</button>
        </form>
      </div>

      <div className="card">
        <h3 className="text-lg font-semibold mb-2">QR Code Payment</h3>
        <p className="text-sm text-gray-500 mb-4">Activate your QR code to receive payments</p>
        <button onClick={handleQr} className="btn-secondary flex items-center gap-2">
          <QrCode size={16} /> Activate QR Code
        </button>
        {qrCode && (
          <div className="mt-4 p-4 bg-gray-50 rounded-lg">
            {typeof qrCode === 'string' && qrCode.startsWith('data:image') ? (
              <img src={qrCode} alt="QR Code" className="w-40 h-40 mx-auto" />
            ) : (
              <pre className="text-xs text-gray-600">{JSON.stringify(qrCode, null, 2)}</pre>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
