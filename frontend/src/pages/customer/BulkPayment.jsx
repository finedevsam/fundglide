import { useEffect, useState } from 'react'
import { bulkPayment, getBulkPayments, getBulkPaymentById } from '../../api/account'
import toast from 'react-hot-toast'
import { Plus, Eye } from 'lucide-react'

export default function CustomerBulkPayment() {
  const [batches, setBatches] = useState([])
  const [showModal, setShowModal] = useState(false)
  const [batchDetail, setBatchDetail] = useState(null)
  const [rows, setRows] = useState([{ accountNumber: '', amount: '', narration: '' }])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    getBulkPayments().then(r => setBatches(r.data || [])).catch(() => {})
  }, [])

  const addRow = () => setRows([...rows, { accountNumber: '', amount: '', narration: '' }])
  const updateRow = (i, field, val) => {
    const updated = [...rows]
    updated[i][field] = val
    setRows(updated)
  }
  const removeRow = (i) => setRows(rows.filter((_, idx) => idx !== i))

  const handleSubmit = async () => {
    setLoading(true)
    try {
      await bulkPayment({ payments: rows })
      toast.success('Bulk payment initiated!')
      setShowModal(false)
      setRows([{ accountNumber: '', amount: '', narration: '' }])
      getBulkPayments().then(r => setBatches(r.data || []))
    } catch (err) {
      toast.error(err.response?.data?.message || 'Bulk payment failed')
    } finally { setLoading(false) }
  }

  const viewBatch = async (batchId) => {
    try {
      const res = await getBulkPaymentById(batchId)
      setBatchDetail(res.data)
    } catch { toast.error('Could not load batch details') }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Bulk Payments</h2>
          <p className="text-gray-500 mt-1">Send payments to multiple accounts at once</p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> New Batch
        </button>
      </div>

      <div className="card overflow-x-auto">
        {batches.length === 0 ? (
          <p className="text-center text-gray-400 py-8">No batch payments yet</p>
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left py-3 px-2 font-medium text-gray-500">Batch ID</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Total</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Count</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                <th className="text-left py-3 px-2 font-medium text-gray-500">Date</th>
                <th className="py-3 px-2" />
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {batches.map((b) => (
                <tr key={b.id} className="hover:bg-gray-50">
                  <td className="py-3 px-2 font-mono text-xs">{b.batchId || b.id}</td>
                  <td className="py-3 px-2">${parseFloat(b.totalAmount || 0).toLocaleString()}</td>
                  <td className="py-3 px-2">{b.count || b.totalCount || '-'}</td>
                  <td className="py-3 px-2"><span className="badge-info">{b.status || 'PROCESSED'}</span></td>
                  <td className="py-3 px-2 text-gray-400">{b.date || b.createdAt || '-'}</td>
                  <td className="py-3 px-2">
                    <button onClick={() => viewBatch(b.batchId || b.id)} className="text-blue-600 hover:underline flex items-center gap-1 text-xs">
                      <Eye size={14} /> View
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-2xl max-h-[85vh] flex flex-col">
            <h3 className="text-lg font-semibold mb-4">Create Bulk Payment</h3>
            <div className="overflow-y-auto flex-1 space-y-3 pr-1">
              {rows.map((row, i) => (
                <div key={i} className="grid grid-cols-3 gap-2 items-center">
                  <input type="text" className="input-field text-sm" placeholder="Account No." value={row.accountNumber} onChange={(e) => updateRow(i, 'accountNumber', e.target.value)} />
                  <input type="number" className="input-field text-sm" placeholder="Amount" value={row.amount} onChange={(e) => updateRow(i, 'amount', e.target.value)} />
                  <div className="flex gap-1">
                    <input type="text" className="input-field text-sm flex-1" placeholder="Narration" value={row.narration} onChange={(e) => updateRow(i, 'narration', e.target.value)} />
                    {rows.length > 1 && <button onClick={() => removeRow(i)} className="text-red-500 hover:text-red-700 px-1">✕</button>}
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-4 flex gap-3">
              <button onClick={addRow} className="btn-secondary flex-1">+ Add Row</button>
              <button onClick={() => setShowModal(false)} className="btn-secondary">Cancel</button>
              <button onClick={handleSubmit} className="btn-primary flex-1" disabled={loading}>{loading ? 'Sending...' : 'Submit'}</button>
            </div>
          </div>
        </div>
      )}

      {batchDetail && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-lg max-h-[80vh] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">Batch Details</h3>
            {Array.isArray(batchDetail) ? batchDetail.map((item, i) => (
              <div key={i} className="flex justify-between py-2 border-b border-gray-50 text-sm">
                <span>{item.accountNumber || item.recipientAccount}</span>
                <span className="font-medium">${parseFloat(item.amount || 0).toLocaleString()}</span>
                <span className={item.status === 'SUCCESS' ? 'badge-success' : 'badge-warning'}>{item.status}</span>
              </div>
            )) : <pre className="text-xs">{JSON.stringify(batchDetail, null, 2)}</pre>}
            <button onClick={() => setBatchDetail(null)} className="btn-secondary w-full mt-4">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
