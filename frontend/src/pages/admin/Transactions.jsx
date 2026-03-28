import { useEffect, useState } from 'react'
import { getAllTransactions, getAccountLogs } from '../../api/admin'

export default function AdminTransactions() {
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')
  const [tab, setTab] = useState('all')

  useEffect(() => {
    const fetch = tab === 'all' ? getAllTransactions : getAccountLogs
    setLoading(true)
    fetch()
      .then(r => setTransactions(r.data || []))
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [tab])

  const filtered = transactions.filter(t =>
    `${t.description || ''} ${t.narration || ''} ${t.amount || ''} ${t.reference || ''}`.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Transactions</h2>
        <p className="text-gray-500 mt-1">All platform transaction records</p>
      </div>

      <div className="flex gap-2">
        <button onClick={() => setTab('all')} className={`px-4 py-2 rounded-lg text-sm font-medium ${tab === 'all' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>All Transactions</button>
        <button onClick={() => setTab('account')} className={`px-4 py-2 rounded-lg text-sm font-medium ${tab === 'account' ? 'bg-blue-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}>Account Logs</button>
      </div>

      <div className="card">
        <input type="text" className="input-field mb-4" placeholder="Search by description, amount, or reference..." value={search} onChange={(e) => setSearch(e.target.value)} />
        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600" /></div>
        ) : filtered.length === 0 ? (
          <p className="text-center text-gray-400 py-8">No transactions found</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Reference</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Description</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Amount</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Type</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Customer</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Date</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map((txn, i) => (
                  <tr key={txn.id || i} className="hover:bg-gray-50">
                    <td className="py-3 px-2 font-mono text-xs text-gray-400">{txn.reference || txn.id || '-'}</td>
                    <td className="py-3 px-2">{txn.description || txn.narration || 'Transaction'}</td>
                    <td className={`py-3 px-2 font-semibold ${txn.type === 'CREDIT' ? 'text-green-600' : 'text-red-500'}`}>
                      {txn.type === 'CREDIT' ? '+' : '-'}${parseFloat(txn.amount || 0).toLocaleString()}
                    </td>
                    <td className="py-3 px-2"><span className={txn.type === 'CREDIT' ? 'badge-success' : 'badge-danger'}>{txn.type || '-'}</span></td>
                    <td className="py-3 px-2 text-gray-500">{txn.customer?.firstName || txn.customerName || '-'}</td>
                    <td className="py-3 px-2 text-gray-400">{txn.date || txn.createdAt || '-'}</td>
                    <td className="py-3 px-2"><span className="badge-success">{txn.status || 'SUCCESS'}</span></td>
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
