import { useEffect, useState } from 'react'
import { getStaff, createStaff, updateStaff, assignPermission, revokePermission } from '../../api/admin'
import { getPermissions } from '../../api/config'
import { getDepartments } from '../../api/admin'
import toast from 'react-hot-toast'
import { Plus, Edit, Shield } from 'lucide-react'

export default function AdminStaff() {
  const [staff, setStaff] = useState([])
  const [permissions, setPermissions] = useState([])
  const [departments, setDepartments] = useState([])
  const [showCreate, setShowCreate] = useState(false)
  const [editStaff, setEditStaff] = useState(null)
  const [permModal, setPermModal] = useState(null)
  const [selectedPerm, setSelectedPerm] = useState('')
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '', username: '', password: '', departmentId: '' })
  const [loading, setLoading] = useState(false)

  const load = () => getStaff().then(r => setStaff(r.data || [])).catch(() => {})

  useEffect(() => {
    load()
    getPermissions().then(r => setPermissions(r.data || [])).catch(() => {})
    getDepartments().then(r => setDepartments(r.data || [])).catch(() => {})
  }, [])

  const handleCreate = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await createStaff(form)
      toast.success('Staff created!')
      setShowCreate(false)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleUpdate = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      await updateStaff(editStaff.id, form)
      toast.success('Staff updated!')
      setEditStaff(null)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleAssign = async () => {
    if (!selectedPerm) return
    setLoading(true)
    try {
      await assignPermission(permModal.id, { permissionId: selectedPerm })
      toast.success('Permission assigned!')
      setPermModal(null)
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const set = (f) => (e) => setForm({ ...form, [f]: e.target.value })

  const openEdit = (s) => {
    setForm({ firstName: s.firstName || '', lastName: s.lastName || '', email: s.email || '', username: s.username || '', password: '', departmentId: s.departmentId || '' })
    setEditStaff(s)
  }

  const StaffForm = ({ onSubmit }) => (
    <form onSubmit={onSubmit} className="space-y-4">
      <div className="grid grid-cols-2 gap-3">
        <input type="text" className="input-field" placeholder="First Name" value={form.firstName} onChange={set('firstName')} required />
        <input type="text" className="input-field" placeholder="Last Name" value={form.lastName} onChange={set('lastName')} required />
      </div>
      <input type="email" className="input-field" placeholder="Email" value={form.email} onChange={set('email')} required />
      <input type="text" className="input-field" placeholder="Username" value={form.username} onChange={set('username')} required />
      <input type="password" className="input-field" placeholder="Password" value={form.password} onChange={set('password')} required={!editStaff} />
      <select className="input-field" value={form.departmentId} onChange={set('departmentId')}>
        <option value="">Select Department</option>
        {departments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
      </select>
      <div className="flex gap-3">
        <button type="button" onClick={() => { setShowCreate(false); setEditStaff(null) }} className="btn-secondary flex-1">Cancel</button>
        <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? '...' : (editStaff ? 'Update' : 'Create')}</button>
      </div>
    </form>
  )

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Staff Management</h2>
          <p className="text-gray-500 mt-1">Manage admin staff and permissions</p>
        </div>
        <button onClick={() => { setForm({ firstName: '', lastName: '', email: '', username: '', password: '', departmentId: '' }); setShowCreate(true) }} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> Add Staff
        </button>
      </div>

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-gray-100">
              <th className="text-left py-3 px-2 font-medium text-gray-500">Name</th>
              <th className="text-left py-3 px-2 font-medium text-gray-500">Email</th>
              <th className="text-left py-3 px-2 font-medium text-gray-500">Username</th>
              <th className="text-left py-3 px-2 font-medium text-gray-500">Department</th>
              <th className="py-3 px-2" />
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-50">
            {staff.map((s) => (
              <tr key={s.id} className="hover:bg-gray-50">
                <td className="py-3 px-2 font-medium">{s.firstName} {s.lastName}</td>
                <td className="py-3 px-2 text-gray-500">{s.email}</td>
                <td className="py-3 px-2">{s.username}</td>
                <td className="py-3 px-2 text-gray-500">{s.department?.name || '-'}</td>
                <td className="py-3 px-2">
                  <div className="flex gap-2">
                    <button onClick={() => openEdit(s)} className="text-blue-600 hover:underline flex items-center gap-1 text-xs"><Edit size={14} /> Edit</button>
                    <button onClick={() => setPermModal(s)} className="text-purple-600 hover:underline flex items-center gap-1 text-xs"><Shield size={14} /> Permissions</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {(showCreate || editStaff) && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">{editStaff ? 'Edit Staff' : 'Add Staff'}</h3>
            <StaffForm onSubmit={editStaff ? handleUpdate : handleCreate} />
          </div>
        </div>
      )}

      {permModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-1">Permissions</h3>
            <p className="text-sm text-gray-500 mb-4">{permModal.firstName} {permModal.lastName}</p>
            <div className="mb-4">
              <h4 className="text-sm font-medium text-gray-700 mb-2">Assigned Permissions</h4>
              {(permModal.permissions || []).length === 0 ? <p className="text-gray-400 text-sm">None assigned</p> : (
                <div className="flex flex-wrap gap-2">
                  {(permModal.permissions || []).map(p => (
                    <span key={p.id} className="badge-info flex items-center gap-1">
                      {p.name}
                      <button onClick={() => revokePermission(permModal.id, p.id).then(() => { toast.success('Revoked'); setPermModal(null) }).catch(() => toast.error('Failed'))} className="ml-1 text-red-500">✕</button>
                    </span>
                  ))}
                </div>
              )}
            </div>
            <div className="flex gap-2 mb-4">
              <select className="input-field" value={selectedPerm} onChange={(e) => setSelectedPerm(e.target.value)}>
                <option value="">Select permission to assign</option>
                {permissions.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
              </select>
              <button onClick={handleAssign} className="btn-primary whitespace-nowrap" disabled={loading}>Assign</button>
            </div>
            <button onClick={() => setPermModal(null)} className="btn-secondary w-full">Close</button>
          </div>
        </div>
      )}
    </div>
  )
}
