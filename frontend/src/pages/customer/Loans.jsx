import { useEffect, useState } from 'react'
import { getMyLoans, getLoanTypes, applyForLoan, getLoanBreakdown } from '../../api/loan'
import toast from 'react-hot-toast'
import { Plus, Eye } from 'lucide-react'

export default function CustomerLoans() {
  const [loans, setLoans] = useState([])
  const [loanTypes, setLoanTypes] = useState([])
  const [showApply, setShowApply] = useState(false)
  const [breakdown, setBreakdown] = useState(null)
  // ApplyForLoanDto: { loanId, amount, other }
  const [form, setForm] = useState({ loanId: '', amount: '', other: '' })
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getMyLoans().then(r => setLoans(r.data || [])).catch(() => {})
    getLoanTypes().then(r => setLoanTypes(r.data || [])).catch(() => {})
  }, [])

  const handleApply = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await applyForLoan(form)
      toast.success('Loan application submitted!')
      setShowApply(false)
      setForm({ loanId: '', amount: '', other: '' })
      getMyLoans().then(r => setLoans(r.data || []))
    } catch (err) {
      toast.error(err.response?.data?.message || 'Application failed')
    } finally { setLoading(false) }
  }

  const viewBreakdown = async (loanId) => {
    try {
      const res = await getLoanBreakdown(loanId)
      setBreakdown(res.data)
    } catch { toast.error('Could not load breakdown') }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Loans</h2>
          <p className="text-gray-500 mt-1">Manage your loan applications</p>
        </div>
        <button onClick={() => setShowApply(true)} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> Apply for Loan
        </button>
      </div>

      {loans.length === 0 ? (
        <div className="card text-center py-12">
          <p className="text-gray-400">No loans yet. Apply for your first loan!</p>
        </div>
      ) : (
        <div className="card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left py-3 px-2 font-medium text-gray-500">Loan Type</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Amount</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Duration</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {loans.map((loan) => (
                <tr key={loan.id} className="hover:bg-gray-50">
                  <td className="py-3 px-2 font-medium">{loan.loanType?.name || loan.loanTypeName || '-'}</td>
                  <td className="py-3 px-2">${parseFloat(loan.amount || 0).toLocaleString()}</td>
                  <td className="py-3 px-2">{loan.duration || loan.tenor || '-'} months</td>
                  <td className="py-3 px-2">
                    <span className={loan.approved ? 'badge-success' : 'badge-warning'}>
                      {loan.approved ? 'Approved' : 'Pending'}
                    </span>
                  </td>
                  <td className="py-3 px-2">
                    <button onClick={() => viewBreakdown(loan.id)} className="text-blue-600 hover:underline text-sm flex items-center gap-1">
                      <Eye size={14} /> Breakdown
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showApply && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">Apply for Loan</h3>
            <form onSubmit={handleApply} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Loan Type</label>
                <select className="input-field" value={form.loanId} onChange={(e) => setForm({ ...form, loanId: e.target.value })} required>
                  <option value="">Select loan type</option>
                  {loanTypes.map(lt => <option key={lt.id} value={lt.id}>{lt.name} — {lt.rate}% / {lt.tenure} months</option>)}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Amount</label>
                <input type="number" className="input-field" placeholder="Loan amount" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} required />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Purpose / Notes</label>
                <input type="text" className="input-field" placeholder="e.g. Business expansion" value={form.other} onChange={(e) => setForm({ ...form, other: e.target.value })} required />
              </div>
              <div className="flex gap-3">
                <button type="button" onClick={() => setShowApply(false)} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? 'Submitting...' : 'Apply'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {breakdown && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-lg max-h-[80vh] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">Loan Breakdown</h3>
            <div className="space-y-2">
              {Array.isArray(breakdown) ? breakdown.map((item, i) => (
                <div key={i} className="flex justify-between text-sm py-2 border-b border-gray-50">
                  <span className="text-gray-500">Month {i + 1}</span>
                  <span className="font-medium">${parseFloat(item.amount || item.installment || 0).toLocaleString()}</span>
                </div>
              )) : (
                <pre className="text-xs text-gray-600 whitespace-pre-wrap">{JSON.stringify(breakdown, null, 2)}</pre>
              )}
            </div>
            <button onClick={() => setBreakdown(null)} className="btn-secondary w-full mt-4">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
