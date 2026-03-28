import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard, Users, UserCog, Briefcase,
  Building2, Shield, Settings, Receipt, X
} from 'lucide-react'

const links = [
  { to: '/admin/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/admin/customers', icon: Users, label: 'Customers' },
  { to: '/admin/staff', icon: UserCog, label: 'Staff' },
  { to: '/admin/loans', icon: Briefcase, label: 'Loans' },
  { to: '/admin/departments', icon: Building2, label: 'Departments' },
  { to: '/admin/permissions', icon: Shield, label: 'Permissions' },
  { to: '/admin/config', icon: Settings, label: 'Configuration' },
  { to: '/admin/transactions', icon: Receipt, label: 'Transactions' },
]

export default function AdminSidebar({ open, onClose }) {
  return (
    <>
      {open && <div className="fixed inset-0 bg-black/40 z-20 lg:hidden" onClick={onClose} />}
      <aside className={`fixed lg:static inset-y-0 left-0 z-30 w-64 bg-gray-900 text-white flex flex-col transform transition-transform duration-200 ${open ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}>
        <div className="flex items-center justify-between px-6 py-5 border-b border-white/10">
          <div>
            <span className="text-xl font-bold tracking-wide">FundGlide</span>
            <p className="text-xs text-white/50 mt-0.5">Admin Portal</p>
          </div>
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
          © 2024 FundGlide Admin
        </div>
      </aside>
    </>
  )
}
