import { useEffect, useState } from 'react'
import { getAllCustomers, getAllTransactions } from '../../api/admin'
import { adminGetAllLoans } from '../../api/loan'
import { getStaff } from '../../api/admin'
import { Users, Receipt, Briefcase, UserCog } from 'lucide-react'

export default function AdminDashboard() {
  const [customers, setCustomers] = useState([])
  const [transactions, setTransactions] = useState([])
  const [loans, setLoans] = useState([])
  const [staff, setStaff] = useState([])

  useEffect(() => {
    getAllCustomers().then(r => setCustomers(r.data || [])).catch(() => {})
    getAllTransactions().then(r => setTransactions(r.data || [])).catch(() => {})
    adminGetAllLoans().then(r => setLoans(r.data || [])).catch(() => {})
    getStaff().then(r => setStaff(r.data || [])).catch(() => {})
  }, [])

  const pendingLoans = loans.filter(l => !l.approved).length

  const stats = [
    { label: 'Total Customers', value: customers.length, icon: Users, color: 'bg-blue-500' },
    { label: 'Transactions', value: transactions.length, icon: Receipt, color: 'bg-purple-500' },
    { label: 'Pending Loans', value: pendingLoans, icon: Briefcase, color: 'bg-orange-500' },
    { label: 'Staff Members', value: staff.length, icon: UserCog, color: 'bg-green-500' },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Admin Dashboard</h2>
        <p className="text-gray-500 mt-1">Platform overview and key metrics</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map(({ label, value, icon: Icon, color }) => (
          <div key={label} className="stat-card flex items-center gap-4">
            <div className={`${color} p-3 rounded-xl`}>
              <Icon size={22} className="text-white" />
            </div>
            <div>
              <p className="text-sm text-gray-500">{label}</p>
              <p className="text-2xl font-bold text-gray-900">{value}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Recent Customers</h3>
          {customers.length === 0 ? <p className="text-gray-400 text-sm">No customers yet.</p> : (
            <div className="space-y-3">
              {customers.slice(0, 6).map((c) => (
                <div key={c.id} className="flex items-center gap-3 py-2 border-b border-gray-50 last:border-0">
                  <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 text-sm font-bold">
                    {(c.firstName || c.username || 'U')[0].toUpperCase()}
                  </div>
                  <div>
                    <p className="text-sm font-medium">{c.firstName} {c.lastName}</p>
                    <p className="text-xs text-gray-400">{c.email || c.username}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Recent Transactions</h3>
          {transactions.length === 0 ? <p className="text-gray-400 text-sm">No transactions yet.</p> : (
            <div className="space-y-3">
              {transactions.slice(0, 6).map((txn, i) => (
                <div key={txn.id || i} className="flex justify-between py-2 border-b border-gray-50 last:border-0">
                  <div>
                    <p className="text-sm font-medium">{txn.description || txn.narration || 'Transaction'}</p>
                    <p className="text-xs text-gray-400">{txn.date || txn.createdAt}</p>
                  </div>
                  <p className={`text-sm font-semibold ${txn.type === 'CREDIT' ? 'text-green-600' : 'text-red-500'}`}>
                    ${parseFloat(txn.amount || 0).toLocaleString()}
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Pending Loan Applications</h3>
        {pendingLoans === 0 ? <p className="text-gray-400 text-sm">No pending loans.</p> : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-2 px-2 font-medium text-gray-500">Customer</th>
                  <th className="text-left py-2 px-2 font-medium text-gray-500">Amount</th>
                  <th className="text-left py-2 px-2 font-medium text-gray-500">Type</th>
                  <th className="text-left py-2 px-2 font-medium text-gray-500">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {loans.filter(l => !l.approved).slice(0, 5).map((loan) => (
                  <tr key={loan.id}>
                    <td className="py-2 px-2">{loan.customerName || loan.customer?.firstName || '-'}</td>
                    <td className="py-2 px-2">${parseFloat(loan.amount || 0).toLocaleString()}</td>
                    <td className="py-2 px-2">{loan.loanType?.name || '-'}</td>
                    <td className="py-2 px-2"><span className="badge-warning">Pending</span></td>
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
