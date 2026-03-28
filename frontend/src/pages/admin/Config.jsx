import { useEffect, useState } from 'react'
import { getConfig, configureLoan, getCurrencies, configureCurrency, setDefaultCurrency, deleteCurrency } from '../../api/config'
import { getInternalAccounts } from '../../api/account'
import toast from 'react-hot-toast'
import { Plus, Star, Trash2 } from 'lucide-react'

export default function AdminConfig() {
  const [currencies, setCurrencies] = useState([])
  const [internalAccounts, setInternalAccounts] = useState([])
  // LoanConfigDto: { name, type (disburse|interest), sourceId }
  const [loanForm, setLoanForm] = useState({ name: '', type: '', sourceId: '' })
  const [currencyForm, setCurrencyForm] = useState({ code: '', currencyName: '' })
  const [activeTab, setActiveTab] = useState('loan')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getConfig('loan').then(r => {
      const d = r.data || {}
      setLoanForm({ name: d.name || '', type: d.type || '', sourceId: d.sourceId || '' })
    }).catch(() => {})
    getCurrencies().then(r => setCurrencies(r.data || [])).catch(() => {})
    getInternalAccounts().then(r => setInternalAccounts(r.data || [])).catch(() => {})
  }, [])

  const handleLoanConfig = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await configureLoan(loanForm)
      toast.success('Loan configuration saved!')
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleAddCurrency = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await configureCurrency(currencyForm)
      toast.success('Currency added!')
      setCurrencyForm({ code: '', currencyName: '' })
      getCurrencies().then(r => setCurrencies(r.data || []))
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleSetDefault = async (id) => {
    try {
      await setDefaultCurrency(id)
      toast.success('Default currency updated!')
      getCurrencies().then(r => setCurrencies(r.data || []))
    } catch { toast.error('Failed') }
  }

  const handleDeleteCurrency = async (id) => {
    if (!window.confirm('Remove this currency?')) return
    try {
      await deleteCurrency(id)
      toast.success('Currency removed!')
      getCurrencies().then(r => setCurrencies(r.data || []))
    } catch { toast.error('Failed') }
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">System Configuration</h2>
        <p className="text-gray-500 mt-1">Configure platform settings</p>
      </div>

      <div className="flex gap-2">
        <button onClick={() => setActiveTab('loan')} className={`px-4 py-2 rounded-lg text-sm font-medium ${activeTab === 'loan' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>Loan Config</button>
        <button onClick={() => setActiveTab('currency')} className={`px-4 py-2 rounded-lg text-sm font-medium ${activeTab === 'currency' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>Currencies</button>
      </div>

      {activeTab === 'loan' && (
        <div className="card max-w-lg">
          <h3 className="text-lg font-semibold mb-1">Loan Configuration</h3>
          <p className="text-sm text-gray-500 mb-4">Configure loan disbursement and interest source accounts</p>
          <form onSubmit={handleLoanConfig} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Configuration Name</label>
              <input type="text" className="input-field" placeholder="e.g. Disbursement Config" value={loanForm.name} onChange={(e) => setLoanForm({ ...loanForm, name: e.target.value })} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
              <select className="input-field" value={loanForm.type} onChange={(e) => setLoanForm({ ...loanForm, type: e.target.value })} required>
                <option value="">Select type</option>
                <option value="disburse">Disburse (loan payout account)</option>
                <option value="interest">Interest (interest collection account)</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Source Account (Internal)</label>
              <select className="input-field" value={loanForm.sourceId} onChange={(e) => setLoanForm({ ...loanForm, sourceId: e.target.value })} required>
                <option value="">Select internal account</option>
                {internalAccounts.map(a => (
                  <option key={a.id} value={a.id}>{a.accountName || a.name} — {a.accountNumber || a.accountNo}</option>
                ))}
              </select>
              {internalAccounts.length === 0 && (
                <p className="text-xs text-orange-500 mt-1">No internal accounts found. Create one first under Accounts → Internal Account.</p>
              )}
            </div>
            <button type="submit" className="btn-primary" disabled={loading}>{loading ? 'Saving...' : 'Save Configuration'}</button>
          </form>
        </div>
      )}

      {activeTab === 'currency' && (
        <div className="space-y-4">
          <div className="card max-w-lg">
            <h3 className="text-lg font-semibold mb-4">Add Currency</h3>
            <form onSubmit={handleAddCurrency} className="space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <input type="text" className="input-field" placeholder="Code (e.g. USD)" value={currencyForm.code} onChange={(e) => setCurrencyForm({ ...currencyForm, code: e.target.value })} required maxLength={3} />
                <input type="text" className="input-field" placeholder="Currency Name (e.g. US Dollar)" value={currencyForm.currencyName} onChange={(e) => setCurrencyForm({ ...currencyForm, currencyName: e.target.value })} required />
              </div>
              <button type="submit" className="btn-primary flex items-center gap-2" disabled={loading}><Plus size={16} /> {loading ? '...' : 'Add'}</button>
            </form>
          </div>

          <div className="card">
            <h3 className="text-lg font-semibold mb-4">Currencies</h3>
            <div className="space-y-2">
              {currencies.map((c) => (
                <div key={c.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-blue-700 font-bold text-sm">
                      {c.code}
                    </div>
                    <div>
                      <p className="font-medium text-sm">{c.currency}</p>
                      <p className="text-xs text-gray-400">{c.code}</p>
                    </div>
                    {c.isDefault && <span className="badge-success">Default</span>}
                  </div>
                  <div className="flex gap-2">
                    {!c.isDefault && (
                      <button onClick={() => handleSetDefault(c.id)} className="text-yellow-500 hover:text-yellow-700" title="Set as default"><Star size={16} /></button>
                    )}
                    <button onClick={() => handleDeleteCurrency(c.id)} className="text-red-500 hover:text-red-700"><Trash2 size={16} /></button>
                  </div>
                </div>
              ))}
              {currencies.length === 0 && <p className="text-gray-400 text-sm">No currencies configured.</p>}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
