import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './contexts/AuthContext'
import ProtectedRoute from './components/common/ProtectedRoute'

// Auth pages
import Login from './pages/auth/Login'
import AdminLogin from './pages/auth/AdminLogin'
import Register from './pages/auth/Register'
import ForgotPassword from './pages/auth/ForgotPassword'

// Customer pages
import CustomerLayout from './components/common/CustomerLayout'
import CustomerDashboard from './pages/customer/Dashboard'
import CustomerAccounts from './pages/customer/Accounts'
import CustomerTransfer from './pages/customer/Transfer'
import CustomerLoans from './pages/customer/Loans'
import CustomerSavings from './pages/customer/Savings'
import CustomerTransactions from './pages/customer/Transactions'
import CustomerBulkPayment from './pages/customer/BulkPayment'
import CustomerProfile from './pages/customer/Profile'

// Admin pages
import AdminLayout from './components/common/AdminLayout'
import AdminDashboard from './pages/admin/Dashboard'
import AdminCustomers from './pages/admin/Customers'
import AdminStaff from './pages/admin/Staff'
import AdminLoans from './pages/admin/Loans'
import AdminDepartments from './pages/admin/Departments'
import AdminPermissions from './pages/admin/Permissions'
import AdminConfig from './pages/admin/Config'
import AdminTransactions from './pages/admin/Transactions'

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/admin/login" element={<AdminLogin />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/" element={<Navigate to="/login" replace />} />

          {/* Customer routes */}
          <Route element={<ProtectedRoute role="customer" />}>
            <Route element={<CustomerLayout />}>
              <Route path="/dashboard" element={<CustomerDashboard />} />
              <Route path="/accounts" element={<CustomerAccounts />} />
              <Route path="/transfer" element={<CustomerTransfer />} />
              <Route path="/loans" element={<CustomerLoans />} />
              <Route path="/savings" element={<CustomerSavings />} />
              <Route path="/transactions" element={<CustomerTransactions />} />
              <Route path="/bulk-payment" element={<CustomerBulkPayment />} />
              <Route path="/profile" element={<CustomerProfile />} />
            </Route>
          </Route>

          {/* Admin routes */}
          <Route element={<ProtectedRoute role="admin" />}>
            <Route element={<AdminLayout />}>
              <Route path="/admin/dashboard" element={<AdminDashboard />} />
              <Route path="/admin/customers" element={<AdminCustomers />} />
              <Route path="/admin/staff" element={<AdminStaff />} />
              <Route path="/admin/loans" element={<AdminLoans />} />
              <Route path="/admin/departments" element={<AdminDepartments />} />
              <Route path="/admin/permissions" element={<AdminPermissions />} />
              <Route path="/admin/config" element={<AdminConfig />} />
              <Route path="/admin/transactions" element={<AdminTransactions />} />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
