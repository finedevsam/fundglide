import { useEffect, useState } from 'react'
import { getPermissions, createPermission, updatePermission, deletePermission } from '../../api/config'
import toast from 'react-hot-toast'
import { Plus, Edit, Trash2 } from 'lucide-react'

export default function AdminPermissions() {
  const [permissions, setPermissions] = useState([])
  const [showModal, setShowModal] = useState(false)
  const [editPerm, setEditPerm] = useState(null)
  const [form, setForm] = useState({ name: '', description: '' })
  const [loading, setLoading] = useState(false)

  const load = () => getPermissions().then(r => setPermissions(r.data || [])).catch(() => {})
  useEffect(() => { load() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (editPerm) {
        await updatePermission(editPerm.id, form)
        toast.success('Permission updated!')
      } else {
        await createPermission(form)
        toast.success('Permission created!')
      }
      setShowModal(false)
      setEditPerm(null)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this permission?')) return
    try {
      await deletePermission(id)
      toast.success('Deleted!')
      load()
    } catch { toast.error('Failed to delete') }
  }

  const openEdit = (p) => {
    setForm({ name: p.name || '', description: p.description || '' })
    setEditPerm(p)
    setShowModal(true)
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Permissions</h2>
          <p className="text-gray-500 mt-1">Manage roles and access permissions</p>
        </div>
        <button onClick={() => { setForm({ name: '', description: '' }); setEditPerm(null); setShowModal(true) }} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> Add Permission
        </button>
      </div>

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b border-gray-100">
              <th className="text-left py-3 px-2 font-medium text-gray-500">Name</th>
              <th className="text-left py-3 px-2 font-medium text-gray-500">Description</th>
              <th className="py-3 px-2" />
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-50">
            {permissions.map((p) => (
              <tr key={p.id} className="hover:bg-gray-50">
                <td className="py-3 px-2 font-medium"><span className="badge-info">{p.name}</span></td>
                <td className="py-3 px-2 text-gray-500">{p.description || '-'}</td>
                <td className="py-3 px-2">
                  <div className="flex gap-3">
                    <button onClick={() => openEdit(p)} className="text-blue-600 hover:text-blue-800"><Edit size={15} /></button>
                    <button onClick={() => handleDelete(p.id)} className="text-red-500 hover:text-red-700"><Trash2 size={15} /></button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {permissions.length === 0 && <p className="text-center text-gray-400 py-8">No permissions defined yet.</p>}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">{editPerm ? 'Edit Permission' : 'Add Permission'}</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <input type="text" className="input-field" placeholder="Permission Name (e.g. MANAGE_USERS)" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
              <textarea className="input-field" placeholder="Description" rows={3} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
              <div className="flex gap-3">
                <button type="button" onClick={() => { setShowModal(false); setEditPerm(null) }} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? '...' : (editPerm ? 'Update' : 'Create')}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
