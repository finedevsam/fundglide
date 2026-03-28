import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard, CreditCard, ArrowLeftRight, Briefcase,
  PiggyBank, Receipt, Users, X, UserCircle, FileStack
} from 'lucide-react'

const links = [
  { to: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/accounts', icon: CreditCard, label: 'Accounts' },
  { to: '/transfer', icon: ArrowLeftRight, label: 'Transfer' },
  { to: '/loans', icon: Briefcase, label: 'Loans' },
  { to: '/savings', icon: PiggyBank, label: 'Savings' },
  { to: '/transactions', icon: Receipt, label: 'Transactions' },
  { to: '/bulk-payment', icon: FileStack, label: 'Bulk Payment' },
  { to: '/profile', icon: UserCircle, label: 'Profile' },
]

export default function CustomerSidebar({ open, onClose }) {
  return (
    <>
      {open && <div className="fixed inset-0 bg-black/40 z-20 lg:hidden" onClick={onClose} />}
      <aside className={`fixed lg:static inset-y-0 left-0 z-30 w-64 bg-[#1e3a5f] text-white flex flex-col transform transition-transform duration-200 ${open ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}>
        <div className="flex items-center justify-between px-6 py-5 border-b border-white/10">
          <span className="text-xl font-bold tracking-wide">FundGlide</span>
          <button onClick={onClose} className="lg:hidden p-1 rounded hover:bg-white/10">
            <X size={18} />
          </button>
        </div>
        <nav className="flex-1 px-3 py-4 space-y-1">
          {links.map(({ to, icon: Icon, label }) => (
            <NavLink
              key={to}
              to={to}
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                  isActive ? 'bg-white/20 text-white' : 'text-white/70 hover:bg-white/10 hover:text-white'
                }`
              }
            >
              <Icon size={18} />
              {label}
            </NavLink>
          ))}
        </nav>
        <div className="px-6 py-4 border-t border-white/10 text-xs text-white/40">
          © 2024 FundGlide
        </div>
      </aside>
    </>
  )
}
