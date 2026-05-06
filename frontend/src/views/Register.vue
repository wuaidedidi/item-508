<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-left">
        <div class="brand">
          <div class="logo">
            <el-icon :size="40"><Checked /></el-icon>
          </div>
          <h1>个人健康管理</h1>
          <p>开启您的健康之旅</p>
        </div>
        <div class="benefits">
          <div class="benefit-item">
            <el-icon><Check /></el-icon>
            <span>免费使用</span>
          </div>
          <div class="benefit-item">
            <el-icon><Check /></el-icon>
            <span>多维度健康追踪</span>
          </div>
          <div class="benefit-item">
            <el-icon><Check /></el-icon>
            <span>安全可靠</span>
          </div>
        </div>
      </div>
      
      <div class="register-right">
        <div class="register-form-container">
          <h2>创建账号</h2>
          <p class="subtitle">填写以下信息开始使用</p>
          
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            class="register-form"
            @submit.prevent="handleRegister"
          >
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                placeholder="用户名"
                size="large"
                :prefix-icon="User"
              />
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="密码"
                size="large"
                :prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="form.confirmPassword"
                type="password"
                placeholder="确认密码"
                size="large"
                :prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            
            <el-form-item prop="email">
              <el-input
                v-model="form.email"
                placeholder="邮箱（选填）"
                size="large"
                :prefix-icon="Message"
              />
            </el-form-item>
            
            <el-form-item prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="手机号（选填）"
                size="large"
                :prefix-icon="Phone"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="register-btn"
                @click="handleRegister"
              >
                注 册
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="register-footer">
            <span>已有账号？</span>
            <router-link to="/login">立即登录</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, Phone, Checked, Check } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validatePhone = (rule, value, callback) => {
  if (value && !/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('手机号格式不正确'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { validator: validatePhone, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await userStore.register({
        username: form.username,
        password: form.password,
        email: form.email,
        phone: form.phone
      })
      ElMessage.success('注册成功！请登录')
      router.push('/login')
    } catch (error) {
      ElMessage.error(error.message || '注册失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 650px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.register-left {
  flex: 1;
  padding: 60px;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;

  .brand {
    margin-bottom: 60px;

    .logo {
      width: 70px;
      height: 70px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 20px;
    }

    h1 {
      font-size: 32px;
      font-weight: 700;
      margin-bottom: 10px;
    }

    p {
      font-size: 16px;
      opacity: 0.9;
    }
  }

  .benefits {
    .benefit-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 0;
      font-size: 16px;

      .el-icon {
        font-size: 20px;
        background: rgba(255, 255, 255, 0.2);
        padding: 4px;
        border-radius: 50%;
      }
    }
  }
}

.register-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
}

.register-form-container {
  width: 100%;
  max-width: 360px;

  h2 {
    font-size: 28px;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 8px;
  }

  .subtitle {
    color: var(--text-secondary);
    margin-bottom: 32px;
  }
}

.register-form {
  .el-input {
    --el-input-height: 48px;
  }

  .el-form-item {
    margin-bottom: 20px;
  }
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: var(--text-secondary);

  a {
    color: var(--primary-color);
    font-weight: 500;
    margin-left: 4px;
  }
}

@media (max-width: 768px) {
  .register-left {
    display: none;
  }

  .register-right {
    padding: 40px 24px;
  }
}
</style>
