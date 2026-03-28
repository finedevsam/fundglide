import { useEffect, useState } from 'react'
import { getAllCustomers, getCustomerAccounts } from '../../api/admin'
import { Eye } from 'lucide-react'
import toast from 'react-hot-toast'

export default function AdminCustomers() {
  const [customers, setCustomers] = useState([])
  const [accounts, setAccounts] = useState(null)
  const [loading, setLoading] = useState(true)
  const [search, setSearch] = useState('')

  useEffect(() => {
    getAllCustomers()
      .then(r => setCustomers(r.data || []))
      .catch(() => {})
      .finally(() => setLoading(false))
  }, [])

  const viewAccounts = async (id) => {
    try {
      const res = await getCustomerAccounts(id)
      setAccounts(res.data)
    } catch { toast.error('Could not load accounts') }
  }

  const filtered = customers.filter(c =>
    `${c.firstName} ${c.lastName} ${c.email} ${c.username}`.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Customers</h2>
        <p className="text-gray-500 mt-1">All registered customers</p>
      </div>

      <div className="card">
        <input type="text" className="input-field mb-4" placeholder="Search customers..." value={search} onChange={(e) => setSearch(e.target.value)} />
        {loading ? (
          <div className="flex justify-center py-8"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600" /></div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Name</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Email</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Username</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Phone</th>
                  <th className="text-left py-3 px-2 font-medium text-gray-500">Status</th>
                  <th className="py-3 px-2" />
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map((c) => (
                  <tr key={c.id} className="hover:bg-gray-50">
                    <td className="py-3 px-2 font-medium">{c.firstName} {c.lastName}</td>
                    <td className="py-3 px-2 text-gray-500">{c.email}</td>
                    <td className="py-3 px-2">{c.username}</td>
                    <td className="py-3 px-2 text-gray-500">{c.phone || '-'}</td>
                    <td className="py-3 px-2">
                      <span className={c.active || c.enabled ? 'badge-success' : 'badge-danger'}>
                        {c.active || c.enabled ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td className="py-3 px-2">
                      <button onClick={() => viewAccounts(c.id)} className="text-blue-600 hover:underline flex items-center gap-1 text-xs">
                        <Eye size={14} /> Accounts
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {accounts && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-lg max-h-[80vh] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">Customer Accounts</h3>
            {Array.isArray(accounts) && accounts.length > 0 ? accounts.map((acc) => (
              <div key={acc.id} className="flex justify-between p-3 bg-gray-50 rounded-lg mb-2">
                <div>
                  <p className="font-medium font-mono text-sm">{acc.accountNumber || acc.accountNo}</p>
                  <p className="text-xs text-gray-400 capitalize">{acc.accountType || 'Savings'}</p>
                </div>
                <p className="font-bold">${parseFloat(acc.balance || 0).toLocaleString('en', { minimumFractionDigits: 2 })}</p>
              </div>
            )) : <p className="text-gray-400 text-sm">No accounts found.</p>}
            <button onClick={() => setAccounts(null)} className="btn-secondary w-full mt-4">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
