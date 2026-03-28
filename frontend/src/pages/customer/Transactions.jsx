import { useEffect, useState } from 'react'
import { getTransactionLogs } from '../../api/account'

export default function CustomerTransactions() {
  const [txns, setTxns] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')

  useEffect(() => {
    getTransactionLogs()
      .then(r => setTxns(r.data || []))
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [])

  const filtered = txns.filter(t =>
    (t.description || t.narration || '').toLowerCase().includes(search.toLowerCase()) ||
    String(t.amount || '').includes(search)
  )

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Transaction History</h2>
        <p className="text-gray-500 mt-1">All your account transactions</p>
      </div>

      <div className="card">
        <input type="text" className="input-field mb-4" placeholder="Search transactions..." value={search} onChange={(e) => setSearch(e.target.value)} />
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600" />
          </div>
        ) : filtered.length === 0 ? (
          <p className="text-center text-gray-400 py-8">No transactions found</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Description</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Amount</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Type</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Date</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map((txn, i) => (
                  <tr key={txn.id || i} className="hover:bg-gray-50">
                    <td className="py-3 px-2 font-medium">{txn.description || txn.narration || 'Transaction'}</td>
                    <td className={`py-3 px-2 font-semibold ${txn.type === 'CREDIT' ? 'text-green-600' : 'text-red-500'}`}>
                      {txn.type === 'CREDIT' ? '+' : '-'}${parseFloat(txn.amount || 0).toLocaleString()}
                    </td>
                    <td className="py-3 px-2">
                      <span className={txn.type === 'CREDIT' ? 'badge-success' : 'badge-danger'}>{txn.type || 'DEBIT'}</span>
                    </td>
                    <td className="py-3 px-2 text-gray-500">{txn.date || txn.createdAt || '-'}</td>
                    <td className="py-3 px-2">
                      <span className={txn.status === 'SUCCESS' || !txn.status ? 'badge-success' : 'badge-warning'}>{txn.status || 'SUCCESS'}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
