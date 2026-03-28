import { useEffect, useState } from 'react'
import { getDepartments, createDepartment, updateDepartment, deleteDepartment } from '../../api/admin'
import toast from 'react-hot-toast'
import { Plus, Edit, Trash2 } from 'lucide-react'

export default function AdminDepartments() {
  const [departments, setDepartments] = useState([])
  const [showModal, setShowModal] = useState(false)
  const [editDept, setEditDept] = useState(null)
  const [form, setForm] = useState({ name: '', description: '' })
  const [loading, setLoading] = useState(false)

  const load = () => getDepartments().then(r => setDepartments(r.data || [])).catch(() => {})
  useEffect(() => { load() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (editDept) {
        await updateDepartment(editDept.id, form)
        toast.success('Department updated!')
      } else {
        await createDepartment(form)
        toast.success('Department created!')
      }
      setShowModal(false)
      setEditDept(null)
      load()
    } catch (err) { toast.error(err.response?.data?.message || 'Failed') }
    finally { setLoading(false) }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this department?')) return
    try {
      await deleteDepartment(id)
      toast.success('Deleted!')
      load()
    } catch { toast.error('Failed to delete') }
  }

  const openEdit = (d) => {
    setForm({ name: d.name || '', description: d.description || '' })
    setEditDept(d)
    setShowModal(true)
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Departments</h2>
          <p className="text-gray-500 mt-1">Manage organizational departments</p>
        </div>
        <button onClick={() => { setForm({ name: '', description: '' }); setEditDept(null); setShowModal(true) }} className="btn-primary flex items-center gap-2">
          <Plus size={16} /> Add Department
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {departments.map((d) => (
          <div key={d.id} className="card">
            <div className="flex items-start justify-between mb-2">
              <h4 className="font-semibold text-gray-900">{d.name}</h4>
              <div className="flex gap-2">
                <button onClick={() => openEdit(d)} className="text-blue-500 hover:text-blue-700"><Edit size={16} /></button>
                <button onClick={() => handleDelete(d.id)} className="text-red-500 hover:text-red-700"><Trash2 size={16} /></button>
              </div>
            </div>
            <p className="text-sm text-gray-500">{d.description || 'No description'}</p>
            <p className="text-xs text-gray-400 mt-2">{d.staffCount || 0} staff members</p>
          </div>
        ))}
        {departments.length === 0 && <p className="text-gray-400 text-sm col-span-3">No departments yet.</p>}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">{editDept ? 'Edit Department' : 'Add Department'}</h3>
            <form onSubmit={handleSubmit} className="space-y-4">
              <input type="text" className="input-field" placeholder="Department Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
              <textarea className="input-field" placeholder="Description (optional)" rows={3} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
              <div className="flex gap-3">
                <button type="button" onClick={() => { setShowModal(false); setEditDept(null) }} className="btn-secondary flex-1">Cancel</button>
                <button type="submit" className="btn-primary flex-1" disabled={loading}>{loading ? '...' : (editDept ? 'Update' : 'Create')}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
