import { useState } from 'react'
import { transfer, lookupAccount } from '../../api/account'
import toast from 'react-hot-toast'
import { Search, ArrowRight } from 'lucide-react'

export default function CustomerTransfer() {
  const [form, setForm] = useState({ accountNumber: '', amount: '', narration: '', pin: '' })
  const [lookup, setLookup] = useState(null)
  const [lookupLoading, setLookupLoading] = useState(false)
  const [loading, setLoading] = useState(false)
  const set = (f) => (e) => setForm({ ...form, [f]: e.target.value })

  const handleLookup = async () => {
    if (!form.accountNumber) return
    setLookupLoading(true)
    try {
      const res = await lookupAccount(form.accountNumber)
      setLookup(res.data)
    } catch {
      toast.error('Account not found')
      setLookup(null)
    } finally { setLookupLoading(false) }
  }

  const handleTransfer = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await transfer(form)
      toast.success('Transfer successful!')
      setForm({ accountNumber: '', amount: '', narration: '', pin: '' })
      setLookup(null)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Transfer failed')
    } finally { setLoading(false) }
  }

  return (
    <div className="max-w-lg mx-auto space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Fund Transfer</h2>
        <p className="text-gray-500 mt-1">Send money to any account</p>
      </div>

      <div className="card space-y-5">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Recipient Account Number</label>
          <div className="flex gap-2">
            <input type="text" className="input-field" placeholder="Enter account number" value={form.accountNumber} onChange={set('accountNumber')} />
            <button onClick={handleLookup} className="btn-secondary flex items-center gap-1 whitespace-nowrap" disabled={lookupLoading}>
              <Search size={16} /> {lookupLoading ? '...' : 'Lookup'}
            </button>
          </div>
          {lookup && (
            <div className="mt-2 p-3 bg-green-50 border border-green-200 rounded-lg">
              <p className="text-sm font-medium text-green-800">{lookup.accountName || lookup.name || 'Account found'}</p>
              <p className="text-xs text-green-600">{lookup.bank || lookup.bankName || ''}</p>
            </div>
          )}
        </div>

        <form onSubmit={handleTransfer} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Amount</label>
            <input type="number" className="input-field" placeholder="0.00" min="1" step="0.01" value={form.amount} onChange={set('amount')} required />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Narration</label>
            <input type="text" className="input-field" placeholder="Purpose of transfer" value={form.narration} onChange={set('narration')} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Transaction PIN</label>
            <input type="password" className="input-field" placeholder="Enter your PIN" maxLength={6} value={form.pin} onChange={set('pin')} required />
          </div>
          <button type="submit" className="btn-primary w-full py-3 flex items-center justify-center gap-2" disabled={loading}>
            {loading ? 'Processing...' : <><ArrowRight size={18} /> Send Money</>}
          </button>
        </form>
      </div>
    </div>
  )
}
