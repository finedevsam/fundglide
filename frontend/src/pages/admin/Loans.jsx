import { useEffect, useState } from 'react'
import { adminGetAllLoans, adminApproveLoan, adminGetLoanBreakdown, createLoanType, getLoanTypes, updateLoanType } from '../../api/loan'
import toast from 'react-hot-toast'
import { CheckCircle, Eye, Plus, Edit } from 'lucide-react'

export default function AdminLoans() {
  const [loans, setLoans] = useState([])
  const [loanTypes, setLoanTypes] = useState([])
  const [breakdown, setBreakdown] = useState(null)
  const [showTypeModal, setShowTypeModal] = useState(false)
  const [editType, setEditType] = useState(null)
  const [typeForm, setTypeForm] = useState({ name: '', tenure: '', rate: '', code: '' })
  const [loading, setLoading] = useState(false)
  const [activeTab, setActiveTab] = useState('loans')

  const load = () => {
    adminGetAllLoans().then(r => setLoans(r.data || [])).catch(() => {})
    getLoanTypes().then(r => setLoanTypes(r.data || [])).catch(() => {})
  }
  useEffect(() => { load() }, [])

  const approveLoan = async (id) => {
    try {
      await adminApproveLoan(id)
      toast.success('Loan approved!')
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed to approve') }
  }

  const viewBreakdown = async (id) => {
    try {
      const res = await adminGetLoanBreakdown(id)
      setBreakdown(res.data)
    } catch { toast.error('Could not load breakdown') }
  }

  const handleTypeSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (editType) {
        await updateLoanType(editType.id, typeForm)
        toast.success('Loan type updated!')
      } else {
        await createLoanType(typeForm)
        toast.success('Loan type created!')
      }
      setShowTypeModal(false)
      setEditType(null)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const openEditType = (lt) => {
    setTypeForm({ name: lt.name || '', tenure: lt.tenure || '', rate: lt.rate || '', code: lt.code || '' })
    setEditType(lt)
    setShowTypeModal(true)
  }

  const set = (f) => (e) => setTypeForm({ ...typeForm, [f]: e.target.value })

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Loan Management</h2>
          <p className="text-gray-500 mt-1">Review and approve loan applications</p>
        </div>
        <button onClick={() => { setTypeForm({ name: '', tenure: '', rate: '', code: '' }); setEditType(null); setShowTypeModal(true) }} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> Loan Type
        </button>
      </div>

      <div className="flex gap-2">
        <button onClick={() => setActiveTab('loans')} className={`px-4 py-2 rounded-lg text-sm font-medium ${activeTab === 'loans' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>Applications</button>
        <button onClick={() => setActiveTab('types')} className={`px-4 py-2 rounded-lg text-sm font-medium ${activeTab === 'types' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>Loan Types</button>
      </div>

      {activeTab === 'loans' && (
        <div className="card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left py-3 px-2 font-medium text-gray-500">Customer</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Amount</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Type</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Duration</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                <th className="py-3 px-2" />
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {loans.map((loan) => (
                <tr key={loan.id} className="hover:bg-gray-50">
                  <td className="py-3 px-2">{loan.customer?.firstName || loan.customerName || '-'} {loan.customer?.lastName || ''}</td>
                  <td className="py-3 px-2 font-medium">${parseFloat(loan.amount || 0).toLocaleString()}</td>
                  <td className="py-3 px-2 text-gray-500">{loan.loanType?.name || '-'}</td>
                  <td className="py-3 px-2">{loan.duration || '-'} mo.</td>
                  <td className="py-3 px-2">
                    <span className={loan.approved ? 'badge-success' : 'badge-warning'}>{loan.approved ? 'Approved' : 'Pending'}</span>
                  </td>
                  <td className="py-3 px-2">
                    <div className="flex gap-2">
                      {!loan.approved && (
                        <button onClick={() => approveLoan(loan.id)} className="text-green-600 hover:underline flex items-center gap-1 text-xs">
                          <CheckCircle size={14} /> Approve
                        </button>
                      )}
                      <button onClick={() => viewBreakdown(loan.id)} className="text-blue-600 hover:underline flex items-center gap-1 text-xs">
                        <Eye size={14} /> Breakdown
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {activeTab === 'types' && (
        <div className="card overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left py-3 px-2 font-medium text-gray-500">Name</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Code</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Rate (%)</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Tenure</th>
                <th className="py-3 px-2" />
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {loanTypes.map((lt) => (
                <tr key={lt.id} className="hover:bg-gray-50">
                  <td className="py-3 px-2 font-medium">{lt.name}</td>
                  <td className="py-3 px-2 font-mono text-xs">{lt.code}</td>
                  <td className="py-3 px-2">{lt.rate}%</td>
                  <td className="py-3 px-2">{lt.tenure} months</td>
                  <td className="py-3 px-2">
                    <button onClick={() => openEditType(lt)} className="text-blue-600 hover:underline flex items-center gap-1 text-xs">
                      <Edit size={14} /> Edit
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showTypeModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">{editType ? 'Edit Loan Type' : 'Create Loan Type'}</h3>
            <form onSubmit={handleTypeSubmit} className="space-y-4">
              <input type="text" className="input-field" placeholder="Loan Type Name (e.g. Personal Loan)" value={typeForm.name} onChange={set('name')} required />
              <input type="text" className="input-field" placeholder="Code (e.g. PL001)" value={typeForm.code} onChange={set('code')} required />
              <input type="number" className="input-field" placeholder="Rate (% e.g. 5)" value={typeForm.rate} onChange={set('rate')} required step="0.01" />
              <input type="number" className="input-field" placeholder="Tenure (months e.g. 12)" value={typeForm.tenure} onChange={set('tenure')} required />
              <div className="flex gap-3">
                <button type="button" onClick={() => { setShowTypeModal(false); setEditType(null) }} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? '...' : (editType ? 'Update' : 'Create')}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {breakdown && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-lg max-h-[80vh] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">Loan Breakdown</h3>
            {Array.isArray(breakdown) ? breakdown.map((item, i) => (
              <div key={i} className="flex justify-between py-2 border-b border-gray-50 text-sm">
                <span className="text-gray-500">Month {i + 1}</span>
                <span className="font-medium">${parseFloat(item.amount || item.installment || 0).toLocaleString()}</span>
              </div>
            )) : <pre className="text-xs">{JSON.stringify(breakdown, null, 2)}</pre>}
            <button onClick={() => setBreakdown(null)} className="btn-secondary w-full mt-4">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
