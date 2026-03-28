import { useEffect, useState } from 'react'
import { getAccounts, setPin, createInternalAccount, getInternalAccounts } from '../../api/account'
import toast from 'react-hot-toast'
import { Plus, Key } from 'lucide-react'

export default function CustomerAccounts() {
  const [accounts, setAccounts] = useState([])
  const [internal, setInternal] = useState([])
  const [showPinModal, setShowPinModal] = useState(false)
  const [showInternalModal, setShowInternalModal] = useState(false)
  const [pin, setPin_] = useState('')
  const [internalForm, setInternalForm] = useState({ accountName: '', currency: 'USD' })
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getAccounts().then(r => setAccounts(r.data || [])).catch(() => {})
    getInternalAccounts().then(r => setInternal(r.data || [])).catch(() => {})
  }, [])

  const handleSetPin = async () => {
    setLoading(true)
    try {
      await setPin({ pin })
      toast.success('PIN set successfully')
      setShowPinModal(false)
      setPin_('')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to set PIN')
    } finally { setLoading(false) }
  }

  const handleCreateInternal = async () => {
    setLoading(true)
    try {
      await createInternalAccount(internalForm)
      toast.success('Internal account created')
      setShowInternalModal(false)
      getInternalAccounts().then(r => setInternal(r.data || []))
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to create account')
    } finally { setLoading(false) }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Accounts</h2>
          <p className="text-gray-500 mt-1">Manage your bank accounts</p>
        </div>
        <div className="flex gap-2">
          <button onClick={() => setShowPinModal(true)} className="btn-secondary flex items-center gap-2">
            <Key size={16} /> Set PIN
          </button>
          <button onClick={() => setShowInternalModal(true)} className="btn-primary flex items-center gap-2">
            <Plus size={16} /> Internal Account
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {accounts.map((acc) => (
          <div key={acc.id} className="card bg-gradient-to-br from-blue-600 to-blue-800 text-white">
            <p className="text-sm opacity-75 mb-4 capitalize">{acc.accountType || 'Savings Account'}</p>
            <p className="text-2xl font-bold mb-2">${parseFloat(acc.balance || 0).toLocaleString('en', { minimumFractionDigits: 2 })}</p>
            <p className="text-base font-mono opacity-90">{acc.accountNumber || acc.accountNo}</p>
            <p className="text-sm opacity-60 mt-1">{acc.currency || 'USD'}</p>
          </div>
        ))}
      </div>

      {internal.length > 0 && (
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Internal Accounts</h3>
          <div className="space-y-3">
            {internal.map((acc) => (
              <div key={acc.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <p className="font-medium">{acc.accountName || acc.name}</p>
                  <p className="text-sm text-gray-400">{acc.accountNumber || acc.accountNo}</p>
                </div>
                <p className="font-bold text-gray-800">${parseFloat(acc.balance || 0).toLocaleString('en', { minimumFractionDigits: 2 })}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {showPinModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-sm">
            <h3 className="text-lg font-semibold mb-4">Set Transaction PIN</h3>
            <input type="password" className="input-field mb-4" placeholder="Enter 4-6 digit PIN" maxLength={6} value={pin} onChange={(e) => setPin_(e.target.value)} />
            <div className="flex gap-3">
              <button onClick={() => setShowPinModal(false)} className="btn-secondary flex-1">Cancel</button>
              <button onClick={handleSetPin} className="btn-primary flex-1" disabled={loading}>{loading ? '...' : 'Set PIN'}</button>
            </div>
          </div>
        </div>
      )}

      {showInternalModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-sm">
            <h3 className="text-lg font-semibold mb-4">Create Internal Account</h3>
            <div className="space-y-3 mb-4">
              <input type="text" className="input-field" placeholder="Account Name" value={internalForm.accountName} onChange={(e) => setInternalForm({ ...internalForm, accountName: e.target.value })} />
              <input type="text" className="input-field" placeholder="Currency (e.g. USD)" value={internalForm.currency} onChange={(e) => setInternalForm({ ...internalForm, currency: e.target.value })} />
            </div>
            <div className="flex gap-3">
              <button onClick={() => setShowInternalModal(false)} className="btn-secondary flex-1">Cancel</button>
              <button onClick={handleCreateInternal} className="btn-primary flex-1" disabled={loading}>{loading ? '...' : 'Create'}</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
