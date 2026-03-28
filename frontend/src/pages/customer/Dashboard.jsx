import { useEffect, useState } from 'react'
import { getAccounts } from '../../api/account'
import { getTransactionLogs } from '../../api/account'
import { getMyLoans } from '../../api/loan'
import { getTargetSavings } from '../../api/savings'
import { useAuth } from '../../contexts/AuthContext'
import { CreditCard, ArrowLeftRight, Briefcase, PiggyBank } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function CustomerDashboard() {
  const { user } = useAuth()
  const [accounts, setAccounts] = useState([])
  const [loans, setLoans] = useState([])
  const [savings, setSavings] = useState([])
  const [txns, setTxns] = useState([])

  useEffect(() => {
    getAccounts().then(r => setAccounts(r.data || [])).catch(() => {})
    getMyLoans().then(r => setLoans(r.data || [])).catch(() => {})
    getTargetSavings().then(r => setSavings(r.data || [])).catch(() => {})
    getTransactionLogs().then(r => setTxns(r.data || [])).catch(() => {})
  }, [])

  const totalBalance = accounts.reduce((sum, a) => sum + (parseFloat(a.balance) || 0), 0)

  const stats = [
    { label: 'Total Balance', value: `$${totalBalance.toLocaleString('en', { minimumFractionDigits: 2 })}`, icon: CreditCard, color: 'bg-blue-500', link: '/accounts' },
    { label: 'Transactions', value: txns.length, icon: ArrowLeftRight, color: 'bg-purple-500', link: '/transactions' },
    { label: 'Active Loans', value: loans.length, icon: Briefcase, color: 'bg-orange-500', link: '/loans' },
    { label: 'Savings Goals', value: savings.length, icon: PiggyBank, color: 'bg-green-500', link: '/savings' },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Welcome back, {user?.firstName || 'there'} 👋</h2>
        <p className="text-gray-500 mt-1">Here's your financial overview</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map(({ label, value, icon: Icon, color, link }) => (
          <Link to={link} key={label} className="stat-card flex items-center gap-4 hover:shadow-md transition-shadow">
            <div className={`${color} p-3 rounded-xl`}>
              <Icon size={22} className="text-white" />
            </div>
            <div>
              <p className="text-sm text-gray-500">{label}</p>
              <p className="text-xl font-bold text-gray-900">{value}</p>
            </div>
          </Link>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">My Accounts</h3>
          {accounts.length === 0 ? (
            <p className="text-gray-400 text-sm">No accounts found.</p>
          ) : (
            <div className="space-y-3">
              {accounts.slice(0, 4).map((acc) => (
                <div key={acc.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div>
                    <p className="font-medium text-sm">{acc.accountNumber || acc.accountNo}</p>
                    <p className="text-xs text-gray-400 capitalize">{acc.accountType || 'Savings'}</p>
                  </div>
                  <p className="font-bold text-blue-700">${parseFloat(acc.balance || 0).toLocaleString('en', { minimumFractionDigits: 2 })}</p>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Recent Transactions</h3>
          {txns.length === 0 ? (
            <p className="text-gray-400 text-sm">No transactions yet.</p>
          ) : (
            <div className="space-y-3">
              {txns.slice(0, 5).map((txn, i) => (
                <div key={txn.id || i} className="flex items-center justify-between py-2 border-b border-gray-50 last:border-0">
                  <div>
                    <p className="text-sm font-medium">{txn.description || txn.narration || 'Transaction'}</p>
                    <p className="text-xs text-gray-400">{txn.date || txn.createdAt || ''}</p>
                  </div>
                  <p className={`font-semibold text-sm ${txn.type === 'CREDIT' ? 'text-green-600' : 'text-red-500'}`}>
                    {txn.type === 'CREDIT' ? '+' : '-'}${parseFloat(txn.amount || 0).toLocaleString()}
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Quick Actions</h3>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          {[
            { to: '/transfer', label: 'Transfer', icon: ArrowLeftRight },
            { to: '/loans', label: 'Apply Loan', icon: Briefcase },
            { to: '/savings', label: 'Save', icon: PiggyBank },
            { to: '/bulk-payment', label: 'Bulk Pay', icon: CreditCard },
          ].map(({ to, label, icon: Icon }) => (
            <Link key={to} to={to} className="flex flex-col items-center gap-2 p-4 border-2 border-dashed border-gray-200 rounded-xl hover:border-blue-400 hover:bg-blue-50 transition-colors">
              <Icon size={22} className="text-blue-600" />
              <span className="text-sm font-medium text-gray-700">{label}</span>
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}
