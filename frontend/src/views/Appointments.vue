<template>
  <div class="appointments">
    <div class="page-header">
      <h1>就诊预约</h1>
      <p>管理您的医疗预约</p>
    </div>

    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="showDialog = true">
          <el-icon><Plus /></el-icon>
          新建预约
        </el-button>
      </div>

      <el-table :data="appointments" v-loading="loading" stripe>
        <el-table-column prop="appointmentTime" label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.appointmentTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="医生" width="120" />
        <el-table-column prop="department" label="科室" width="120" />
        <el-table-column prop="hospital" label="医院" width="150" />
        <el-table-column prop="reason" label="就诊原因" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="editAppointment(row)">编辑</el-button>
            <el-button
              v-if="row.status === 'PENDING' || row.status === 'CONFIRMED'"
              type="warning"
              link
              @click="cancelAppointment(row.id)"
            >
              取消
            </el-button>
            <el-button type="danger" link @click="deleteAppointment(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 10"
        v-model:current-page="page"
        :total="total"
        layout="prev, pager, next"
        class="pagination"
        @current-change="fetchAppointments"
      />
    </el-card>

    <!-- Appointment Dialog -->
    <el-dialog
      v-model="showDialog"
      :title="form.id ? '编辑预约' : '新建预约'"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            v-model="form.appointmentTime"
            type="datetime"
            placeholder="选择预约时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="医生姓名" prop="doctorName">
          <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
        </el-form-item>
        <el-form-item label="科室" prop="department">
          <el-select v-model="form.department" placeholder="请选择科室">
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="骨科" value="骨科" />
            <el-option label="眼科" value="眼科" />
            <el-option label="皮肤科" value="皮肤科" />
            <el-option label="心内科" value="心内科" />
            <el-option label="神经内科" value="神经内科" />
            <el-option label="妇产科" value="妇产科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="口腔科" value="口腔科" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="医院" prop="hospital">
          <el-input v-model="form.hospital" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="就诊原因" prop="reason">
          <el-input
            v-model="form.reason"
            type="textarea"
            :rows="3"
            placeholder="请描述您的就诊原因或症状"
          />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input
            v-model="form.note"
            type="textarea"
            :rows="2"
            placeholder="可选备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveAppointment">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

const loading = ref(false)
const saving = ref(false)
const appointments = ref([])
const page = ref(1)
const total = ref(0)
const showDialog = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  appointmentTime: '',
  doctorName: '',
  department: '',
  hospital: '',
  reason: '',
  note: ''
})

const rules = {
  appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
  doctorName: [{ required: true, message: '请输入医生姓名', trigger: 'blur' }],
  department: [{ required: true, message: '请选择科室', trigger: 'change' }],
  hospital: [{ required: true, message: '请输入医院名称', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const s = status?.toUpperCase?.() || status
  const types = {
    'PENDING': 'warning',
    'CONFIRMED': 'success',
    'CANCELLED': 'danger',
    'COMPLETED': 'info'
  }
  return types[s] || ''
}

const getStatusText = (status) => {
  const s = status?.toUpperCase?.() || status
  const texts = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'CANCELLED': '已取消',
    'COMPLETED': '已完成'
  }
  return texts[s] || status
}

const formatDateTime = (dt) => {
  if (!dt) return ''
  return new Date(dt).toLocaleString('zh-CN')
}

const fetchAppointments = async () => {
  loading.value = true
  try {
    const res = await api.getAppointments({ page: page.value, size: 10 })
    appointments.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    appointmentTime: '',
    doctorName: '',
    department: '',
    hospital: '',
    reason: '',
    note: ''
  })
}

const editAppointment = (row) => {
  Object.assign(form, row)
  showDialog.value = true
}

const saveAppointment = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saving.value = true
    try {
      if (form.id) {
        await api.updateAppointment(form.id, form)
      } else {
        await api.createAppointment(form)
      }
      ElMessage.success('保存成功')
      showDialog.value = false
      resetForm()
      fetchAppointments()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    } finally {
      saving.value = false
    }
  })
}

const cancelAppointment = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消此预约吗？', '确认取消', { type: 'warning' })
    await api.cancelAppointment(id)
    ElMessage.success('预约已取消')
    fetchAppointments()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '操作失败')
  }
}

const deleteAppointment = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此预约吗？', '确认删除', { type: 'warning' })
    await api.deleteAppointment(id)
    ElMessage.success('删除成功')
    fetchAppointments()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

onMounted(() => {
  fetchAppointments()
})
</script>

<style lang="scss" scoped>
.appointments {
  .page-header {
    margin-bottom: 24px;

    h1 {
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 8px;
    }

    p {
      color: var(--text-secondary);
    }
  }

  .toolbar {
    margin-bottom: 16px;
    display: flex;
    justify-content: flex-end;
  }

  .pagination {
    margin-top: 16px;
    justify-content: center;
  }
}
</style>
