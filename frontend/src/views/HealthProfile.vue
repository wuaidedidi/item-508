<template>
  <div class="health-profile">
    <div class="page-header">
      <h1>健康档案</h1>
      <p>管理您的个人健康信息</p>
    </div>

    <el-card v-loading="loading">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="profile-form"
      >
        <el-row :gutter="24">
          <el-col :xs="24" :md="12">
            <el-form-item label="身高" prop="height">
              <el-input-number
                v-model="form.height"
                :min="50"
                :max="250"
                :precision="1"
                controls-position="right"
              />
              <span class="unit">厘米</span>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="体重" prop="weight">
              <el-input-number
                v-model="form.weight"
                :min="20"
                :max="300"
                :precision="1"
                controls-position="right"
              />
              <span class="unit">公斤</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :xs="24" :md="12">
            <el-form-item label="血型" prop="bloodType">
              <el-select v-model="form.bloodType" placeholder="请选择血型" style="width: 100%">
                <el-option label="A型" value="A" />
                <el-option label="B型" value="B" />
                <el-option label="AB型" value="AB" />
                <el-option label="O型" value="O" />
                <el-option label="未知" value="未知" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="BMI指数">
              <div class="bmi-display">
                <span class="bmi-value" :class="bmiClass">{{ bmiValue }}</span>
                <span class="bmi-status">{{ bmiStatus }}</span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="过敏史" prop="allergies">
          <el-input
            v-model="form.allergies"
            type="textarea"
            :rows="3"
          />
        </el-form-item>

        <el-form-item label="病史" prop="medicalHistory">
          <el-input
            v-model="form.medicalHistory"
            type="textarea"
            :rows="3"
          />
        </el-form-item>

        <el-form-item label="家族病史" prop="familyHistory">
          <el-input
            v-model="form.familyHistory"
            type="textarea"
            :rows="3"
          />
        </el-form-item>

        <el-divider content-position="left">紧急联系人</el-divider>

        <el-row :gutter="24">
          <el-col :xs="24" :md="12">
            <el-form-item label="联系人姓名" prop="emergencyContact">
              <el-input v-model="form.emergencyContact" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12">
            <el-form-item label="联系人电话" prop="emergencyPhone">
              <el-input v-model="form.emergencyPhone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">
            保存信息
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'

const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)

const form = reactive({
  height: null,
  weight: null,
  bloodType: '',
  allergies: '',
  medicalHistory: '',
  familyHistory: '',
  emergencyContact: '',
  emergencyPhone: ''
})

const rules = {
  height: [
    { type: 'number', min: 50, max: 250, message: '身高范围应在50-250厘米之间', trigger: 'blur' }
  ],
  weight: [
    { type: 'number', min: 20, max: 300, message: '体重范围应在20-300公斤之间', trigger: 'blur' }
  ]
}

const bmiValue = computed(() => {
  if (!form.height || !form.weight) return '--'
  const heightM = form.height / 100
  const bmi = form.weight / (heightM * heightM)
  return bmi.toFixed(1)
})

const bmiStatus = computed(() => {
  const bmi = parseFloat(bmiValue.value)
  if (isNaN(bmi)) return ''
  if (bmi < 18.5) return '偏瘦'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '偏胖'
  return '肥胖'
})

const bmiClass = computed(() => {
  const bmi = parseFloat(bmiValue.value)
  if (isNaN(bmi)) return ''
  if (bmi < 18.5) return 'underweight'
  if (bmi < 24) return 'normal'
  if (bmi < 28) return 'overweight'
  return 'obese'
})

const fetchProfile = async () => {
  loading.value = true
  try {
    const res = await api.getHealthProfile()
    if (res.data) {
      Object.assign(form, res.data)
    }
  } catch (error) {
    console.error('获取健康档案失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saving.value = true
    try {
      await api.updateHealthProfile(form)
      ElMessage.success('健康档案保存成功')
    } catch (error) {
      ElMessage.error(error.message || '保存失败')
    } finally {
      saving.value = false
    }
  })
}

onMounted(() => {
  fetchProfile()
})
</script>

<style lang="scss" scoped>
.health-profile {
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

  .profile-form {
    max-width: 800px;

    .unit {
      margin-left: 8px;
      color: var(--text-secondary);
    }

    .bmi-display {
      display: flex;
      align-items: center;
      gap: 12px;

      .bmi-value {
        font-size: 24px;
        font-weight: 600;

        &.underweight {
          color: #3b82f6;
        }

        &.normal {
          color: #10b981;
        }

        &.overweight {
          color: #f59e0b;
        }

        &.obese {
          color: #ef4444;
        }
      }

      .bmi-status {
        font-size: 14px;
        color: var(--text-secondary);
      }
    }
  }
}
</style>
