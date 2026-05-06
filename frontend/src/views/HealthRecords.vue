<template>
  <div class="health-records">
    <div class="page-header">
      <h1>健康数据</h1>
      <p>记录和追踪您的健康指标</p>
    </div>

    <el-tabs v-model="activeTab" type="border-card">
      <!-- Weight Tab -->
      <el-tab-pane label="体重记录" name="weight">
        <div class="tab-header">
          <el-button type="primary" @click="showWeightDialog = true">
            <el-icon><Plus /></el-icon>
            添加记录
          </el-button>
        </div>
        <el-table :data="weightRecords" v-loading="loading.weight" stripe>
          <el-table-column prop="recordDate" label="日期" width="120" />
          <el-table-column prop="weight" label="体重 (kg)" width="120" />
          <el-table-column prop="note" label="备注" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link @click="editWeight(row)">编辑</el-button>
              <el-button type="danger" link @click="deleteWeight(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="weightTotal > 10"
          v-model:current-page="weightPage"
          :total="weightTotal"
          layout="prev, pager, next"
          @current-change="fetchWeightRecords"
        />
      </el-tab-pane>

      <!-- Blood Pressure Tab -->
      <el-tab-pane label="血压记录" name="bloodPressure">
        <div class="tab-header">
          <el-button type="primary" @click="showBpDialog = true">
            <el-icon><Plus /></el-icon>
            添加记录
          </el-button>
        </div>
        <el-table :data="bpRecords" v-loading="loading.bp" stripe>
          <el-table-column prop="recordDate" label="日期" width="120" />
          <el-table-column label="血压 (mmHg)" width="150">
            <template #default="{ row }">
              {{ row.systolic }}/{{ row.diastolic }}
            </template>
          </el-table-column>
          <el-table-column prop="pulse" label="脉搏" width="100" />
          <el-table-column prop="note" label="备注" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link @click="editBp(row)">编辑</el-button>
              <el-button type="danger" link @click="deleteBp(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="bpTotal > 10"
          v-model:current-page="bpPage"
          :total="bpTotal"
          layout="prev, pager, next"
          @current-change="fetchBpRecords"
        />
      </el-tab-pane>

      <!-- Blood Sugar Tab -->
      <el-tab-pane label="血糖记录" name="bloodSugar">
        <div class="tab-header">
          <el-button type="primary" @click="showBsDialog = true">
            <el-icon><Plus /></el-icon>
            添加记录
          </el-button>
        </div>
        <el-table :data="bsRecords" v-loading="loading.bs" stripe>
          <el-table-column prop="recordDate" label="日期" width="120" />
          <el-table-column prop="value" label="血糖 (mmol/L)" width="150" />
          <el-table-column prop="measureTime" label="测量时段" width="120">
            <template #default="{ row }">
              {{ getMeasureTimeText(row.measureTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="note" label="备注" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link @click="editBs(row)">编辑</el-button>
              <el-button type="danger" link @click="deleteBs(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="bsTotal > 10"
          v-model:current-page="bsPage"
          :total="bsTotal"
          layout="prev, pager, next"
          @current-change="fetchBsRecords"
        />
      </el-tab-pane>

      <!-- Heart Rate Tab -->
      <el-tab-pane label="心率记录" name="heartRate">
        <div class="tab-header">
          <el-button type="primary" @click="showHrDialog = true">
            <el-icon><Plus /></el-icon>
            添加记录
          </el-button>
        </div>
        <el-table :data="hrRecords" v-loading="loading.hr" stripe>
          <el-table-column prop="recordDate" label="日期" width="120" />
          <el-table-column prop="rate" label="心率 (bpm)" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              {{ getStatusText(row.status) }}
            </template>
          </el-table-column>
          <el-table-column prop="note" label="备注" />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link @click="editHr(row)">编辑</el-button>
              <el-button type="danger" link @click="deleteHr(row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="hrTotal > 10"
          v-model:current-page="hrPage"
          :total="hrTotal"
          layout="prev, pager, next"
          @current-change="fetchHrRecords"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- Weight Dialog -->
    <el-dialog v-model="showWeightDialog" :title="weightForm.id ? '编辑体重记录' : '添加体重记录'" width="500px" @close="resetWeightForm">
      <el-form ref="weightFormRef" :model="weightForm" :rules="weightRules" label-width="80px">
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker v-model="weightForm.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="体重" prop="weight">
          <el-input-number v-model="weightForm.weight" :min="20" :max="300" :precision="1" style="width: 150px" />
          <span class="unit">kg</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="weightForm.note" type="textarea" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showWeightDialog = false">取消</el-button>
        <el-button type="primary" @click="saveWeight">保存</el-button>
      </template>
    </el-dialog>

    <!-- Blood Pressure Dialog -->
    <el-dialog v-model="showBpDialog" :title="bpForm.id ? '编辑血压记录' : '添加血压记录'" width="500px" @close="resetBpForm">
      <el-form ref="bpFormRef" :model="bpForm" :rules="bpRules" label-width="80px">
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker v-model="bpForm.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="收缩压" prop="systolic">
          <el-input-number v-model="bpForm.systolic" :min="60" :max="250" style="width: 150px" />
          <span class="unit">mmHg</span>
        </el-form-item>
        <el-form-item label="舒张压" prop="diastolic">
          <el-input-number v-model="bpForm.diastolic" :min="40" :max="150" style="width: 150px" />
          <span class="unit">mmHg</span>
        </el-form-item>
        <el-form-item label="脉搏">
          <el-input-number v-model="bpForm.pulse" :min="40" :max="200" style="width: 150px" />
          <span class="unit">次/分钟</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="bpForm.note" type="textarea" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBpDialog = false">取消</el-button>
        <el-button type="primary" @click="saveBp">保存</el-button>
      </template>
    </el-dialog>

    <!-- Blood Sugar Dialog -->
    <el-dialog v-model="showBsDialog" :title="bsForm.id ? '编辑血糖记录' : '添加血糖记录'" width="500px" @close="resetBsForm">
      <el-form ref="bsFormRef" :model="bsForm" :rules="bsRules" label-width="80px">
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker v-model="bsForm.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="血糖值" prop="value">
          <el-input-number v-model="bsForm.value" :min="1" :max="30" :precision="1" style="width: 150px" />
          <span class="unit">mmol/L</span>
        </el-form-item>
        <el-form-item label="测量时段" prop="measureTime">
          <el-select v-model="bsForm.measureTime" placeholder="选择时段" style="width: 100%">
            <el-option label="空腹" value="fasting" />
            <el-option label="餐前" value="before_meal" />
            <el-option label="餐后" value="after_meal" />
            <el-option label="睡前" value="bedtime" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="bsForm.note" type="textarea" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBsDialog = false">取消</el-button>
        <el-button type="primary" @click="saveBs">保存</el-button>
      </template>
    </el-dialog>

    <!-- Heart Rate Dialog -->
    <el-dialog v-model="showHrDialog" :title="hrForm.id ? '编辑心率记录' : '添加心率记录'" width="500px" @close="resetHrForm">
      <el-form ref="hrFormRef" :model="hrForm" :rules="hrRules" label-width="80px">
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker v-model="hrForm.recordDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="心率" prop="rate">
          <el-input-number v-model="hrForm.rate" :min="40" :max="200" style="width: 150px" />
          <span class="unit">bpm</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="hrForm.status" placeholder="选择状态" style="width: 100%">
            <el-option label="静息" value="rest" />
            <el-option label="运动后" value="exercise" />
            <el-option label="睡眠" value="sleep" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="hrForm.note" type="textarea" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showHrDialog = false">取消</el-button>
        <el-button type="primary" @click="saveHr">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import api from '@/api'

const activeTab = ref('weight')
const loading = reactive({ weight: false, bp: false, bs: false, hr: false })

// Weight
const weightRecords = ref([])
const weightPage = ref(1)
const weightTotal = ref(0)
const showWeightDialog = ref(false)
const weightFormRef = ref(null)
const weightForm = reactive({ id: null, recordDate: '', weight: null, note: '' })
const weightRules = {
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  weight: [{ required: true, message: '请输入体重', trigger: 'change' }]
}

// Blood Pressure
const bpRecords = ref([])
const bpPage = ref(1)
const bpTotal = ref(0)
const showBpDialog = ref(false)
const bpFormRef = ref(null)
const bpForm = reactive({ id: null, recordDate: '', systolic: null, diastolic: null, pulse: null, note: '' })
const bpRules = {
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  systolic: [{ required: true, message: '请输入收缩压', trigger: 'change' }],
  diastolic: [{ required: true, message: '请输入舒张压', trigger: 'change' }]
}

// Blood Sugar
const bsRecords = ref([])
const bsPage = ref(1)
const bsTotal = ref(0)
const showBsDialog = ref(false)
const bsFormRef = ref(null)
const bsForm = reactive({ id: null, recordDate: '', value: null, measureTime: '', note: '' })
const bsRules = {
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  value: [{ required: true, message: '请输入血糖值', trigger: 'change' }],
  measureTime: [{ required: true, message: '请选择测量时段', trigger: 'change' }]
}

// Heart Rate
const hrRecords = ref([])
const hrPage = ref(1)
const hrTotal = ref(0)
const showHrDialog = ref(false)
const hrFormRef = ref(null)
const hrForm = reactive({ id: null, recordDate: '', rate: null, status: '', note: '' })
const hrRules = {
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  rate: [{ required: true, message: '请输入心率', trigger: 'change' }]
}

const getMeasureTimeText = (time) => {
  const map = { fasting: '空腹', before_meal: '餐前', after_meal: '餐后', bedtime: '睡前' }
  return map[time] || time
}

const getStatusText = (status) => {
  const map = { rest: '静息', exercise: '运动后', sleep: '睡眠' }
  return map[status] || status
}

// Reset forms
const resetWeightForm = () => {
  Object.assign(weightForm, { id: null, recordDate: '', weight: null, note: '' })
}
const resetBpForm = () => {
  Object.assign(bpForm, { id: null, recordDate: '', systolic: null, diastolic: null, pulse: null, note: '' })
}
const resetBsForm = () => {
  Object.assign(bsForm, { id: null, recordDate: '', value: null, measureTime: '', note: '' })
}
const resetHrForm = () => {
  Object.assign(hrForm, { id: null, recordDate: '', rate: null, status: '', note: '' })
}

// Fetch functions
const fetchWeightRecords = async () => {
  loading.weight = true
  try {
    const res = await api.getWeightRecords({ page: weightPage.value, size: 10 })
    weightRecords.value = res.data?.records || []
    weightTotal.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.weight = false
  }
}

const fetchBpRecords = async () => {
  loading.bp = true
  try {
    const res = await api.getBloodPressureRecords({ page: bpPage.value, size: 10 })
    bpRecords.value = res.data?.records || []
    bpTotal.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.bp = false
  }
}

const fetchBsRecords = async () => {
  loading.bs = true
  try {
    const res = await api.getBloodSugarRecords({ page: bsPage.value, size: 10 })
    bsRecords.value = res.data?.records || []
    bsTotal.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.bs = false
  }
}

const fetchHrRecords = async () => {
  loading.hr = true
  try {
    const res = await api.getHeartRateRecords({ page: hrPage.value, size: 10 })
    hrRecords.value = res.data?.records || []
    hrTotal.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.hr = false
  }
}

// Edit functions
const editWeight = (row) => {
  Object.assign(weightForm, row)
  showWeightDialog.value = true
}

const editBp = (row) => {
  Object.assign(bpForm, row)
  showBpDialog.value = true
}

const editBs = (row) => {
  Object.assign(bsForm, row)
  showBsDialog.value = true
}

const editHr = (row) => {
  Object.assign(hrForm, row)
  showHrDialog.value = true
}

// Save functions
const saveWeight = async () => {
  if (!weightFormRef.value) return
  await weightFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (weightForm.id) {
        await api.updateWeightRecord(weightForm.id, weightForm)
      } else {
        await api.addWeightRecord(weightForm)
      }
      ElMessage.success('保存成功')
      showWeightDialog.value = false
      resetWeightForm()
      fetchWeightRecords()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    }
  })
}

const saveBp = async () => {
  if (!bpFormRef.value) return
  await bpFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (bpForm.id) {
        await api.updateBloodPressureRecord(bpForm.id, bpForm)
      } else {
        await api.addBloodPressureRecord(bpForm)
      }
      ElMessage.success('保存成功')
      showBpDialog.value = false
      resetBpForm()
      fetchBpRecords()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    }
  })
}

const saveBs = async () => {
  if (!bsFormRef.value) return
  await bsFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (bsForm.id) {
        await api.updateBloodSugarRecord(bsForm.id, bsForm)
      } else {
        await api.addBloodSugarRecord(bsForm)
      }
      ElMessage.success('保存成功')
      showBsDialog.value = false
      resetBsForm()
      fetchBsRecords()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    }
  })
}

const saveHr = async () => {
  if (!hrFormRef.value) return
  await hrFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      if (hrForm.id) {
        await api.updateHeartRateRecord(hrForm.id, hrForm)
      } else {
        await api.addHeartRateRecord(hrForm)
      }
      ElMessage.success('保存成功')
      showHrDialog.value = false
      resetHrForm()
      fetchHrRecords()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    }
  })
}

// Delete functions
const deleteWeight = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此记录吗？', '确认删除', { type: 'warning' })
    await api.deleteWeightRecord(id)
    ElMessage.success('删除成功')
    fetchWeightRecords()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

const deleteBp = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此记录吗？', '确认删除', { type: 'warning' })
    await api.deleteBloodPressureRecord(id)
    ElMessage.success('删除成功')
    fetchBpRecords()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

const deleteBs = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此记录吗？', '确认删除', { type: 'warning' })
    await api.deleteBloodSugarRecord(id)
    ElMessage.success('删除成功')
    fetchBsRecords()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

const deleteHr = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此记录吗？', '确认删除', { type: 'warning' })
    await api.deleteHeartRateRecord(id)
    ElMessage.success('删除成功')
    fetchHrRecords()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

onMounted(() => {
  fetchWeightRecords()
  fetchBpRecords()
  fetchBsRecords()
  fetchHrRecords()
})
</script>

<style lang="scss" scoped>
.health-records {
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

  .tab-header {
    margin-bottom: 16px;
    display: flex;
    justify-content: flex-end;
  }

  .unit {
    margin-left: 8px;
    color: var(--text-secondary);
  }

  .el-pagination {
    margin-top: 16px;
    justify-content: center;
  }
}
</style>
