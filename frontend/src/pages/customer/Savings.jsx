import { useEffect, useState } from 'react'
import { getTargetSavings, createTargetSavings, getSavingsHistory, quickSave } from '../../api/savings'
import toast from 'react-hot-toast'
import { Plus, Eye } from 'lucide-react'

export default function CustomerSavings() {
  const [savings, setSavings] = useState([])
  const [showCreate, setShowCreate] = useState(false)
  const [history, setHistory] = useState(null)
  const [quickSaveModal, setQuickSaveModal] = useState(null)
  const [form, setForm] = useState({ name: '', targetAmount: '', duration: '' })
  const [quickAmount, setQuickAmount] = useState('')
  const [loading, setLoading] = useState(false)

  const load = () => getTargetSavings().then(r => setSavings(r.data || [])).catch(() => {})
  useEffect(() => { load() }, [])

  const handleCreate = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await createTargetSavings(form)
      toast.success('Savings goal created!')
      setShowCreate(false)
      load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed')
    } finally { setLoading(false) }
  }

  const handleQuickSave = async () => {
    setLoading(true)
    try {
      await quickSave(quickSaveModal.id, { amount: quickAmount })
      toast.success('Quick save successful!')
      setQuickSaveModal(null)
      load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed')
    } finally { setLoading(false) }
  }

  const viewHistory = async (id) => {
    try {
      const res = await getSavingsHistory(id)
      setHistory(res.data)
    } catch { toast.error('Could not load history') }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Target Savings</h2>
          <p className="text-gray-500 mt-1">Save towards your goals</p>
        </div>
        <button onClick={() => setShowCreate(true)} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> New Goal
        </button>
      </div>

      {savings.length === 0 ? (
        <div className="card text-center py-12">
          <p className="text-gray-400">No savings goals yet. Create your first goal!</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {savings.map((s) => {
            const pct = Math.min(100, Math.round((parseFloat(s.currentAmount || s.savedAmount || 0) / parseFloat(s.targetAmount || 1)) * 100))
            return (
              <div key={s.id} className="card">
                <div className="flex items-start justify-between mb-3">
                  <h4 className="font-semibold text-gray-900">{s.name || s.savingsName}</h4>
                  <span className="text-sm font-bold text-blue-600">{pct}%</span>
                </div>
                <div className="mb-3">
                  <div className="flex justify-between text-sm text-gray-500 mb-1">
                    <span>${parseFloat(s.currentAmount || s.savedAmount || 0).toLocaleString()}</span>
                    <span>${parseFloat(s.targetAmount || 0).toLocaleString()}</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div className="bg-blue-600 h-2 rounded-full transition-all" style={{ width: `${pct}%` }} />
                  </div>
                </div>
                <p className="text-xs text-gray-400 mb-3">{s.duration || ''} months goal</p>
                <div className="flex gap-2">
                  <button onClick={() => setQuickSaveModal(s)} className="btn-primary text-sm py-1.5 flex-1">Quick Save</button>
                  <button onClick={() => viewHistory(s.id)} className="btn-secondary text-sm py-1.5 flex items-center gap-1">
                    <Eye size={14} />
                  </button>
                </div>
              </div>
            )
          })}
        </div>
      )}

      {showCreate && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">Create Savings Goal</h3>
            <form onSubmit={handleCreate} className="space-y-4">
              <input type="text" className="input-field" placeholder="Goal name (e.g. New Car)" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
              <input type="number" className="input-field" placeholder="Target amount" value={form.targetAmount} onChange={(e) => setForm({ ...form, targetAmount: e.target.value })} required />
              <input type="number" className="input-field" placeholder="Duration (months)" value={form.duration} onChange={(e) => setForm({ ...form, duration: e.target.value })} required />
              <div className="flex gap-3">
                <button type="button" onClick={() => setShowCreate(false)} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? '...' : 'Create'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {quickSaveModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-sm">
            <h3 className="text-lg font-semibold mb-1">Quick Save</h3>
            <p className="text-sm text-gray-500 mb-4">{quickSaveModal.name || quickSaveModal.savingsName}</p>
            <input type="number" className="input-field mb-4" placeholder="Amount to save" value={quickAmount} onChange={(e) => setQuickAmount(e.target.value)} />
            <div className="flex gap-3">
              <button onClick={() => setQuickSaveModal(null)} className="btn-secondary flex-1">Cancel</button>
              <button onClick={handleQuickSave} className="btn-primary flex-1" disabled={loading}>{loading ? '...' : 'Save'}</button>
            </div>
          </div>
        </div>
      )}

      {history && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md max-h-[80vh] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">Savings History</h3>
            {Array.isArray(history) && history.length > 0 ? history.map((h, i) => (
              <div key={i} className="flex justify-between py-2 border-b border-gray-50 text-sm">
                <span className="text-gray-500">{h.date || h.createdAt || `Entry ${i + 1}`}</span>
                <span className="font-medium text-green-600">+${parseFloat(h.amount || 0).toLocaleString()}</span>
              </div>
            )) : <p className="text-gray-400 text-sm">No history yet.</p>}
            <button onClick={() => setHistory(null)} className="btn-secondary w-full mt-4">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
