<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-left">
        <div class="brand">
          <div class="logo">
            <el-icon :size="40"><Checked /></el-icon>
          </div>
          <h1>个人健康管理</h1>
          <p>您的健康数据管家</p>
        </div>
        <div class="features">
          <div class="feature-item">
            <el-icon><TrendCharts /></el-icon>
            <span>追踪健康数据</span>
          </div>
          <div class="feature-item">
            <el-icon><Calendar /></el-icon>
            <span>管理就诊预约</span>
          </div>
          <div class="feature-item">
            <el-icon><Document /></el-icon>
            <span>健康资讯阅读</span>
          </div>
        </div>
      </div>
      
      <div class="login-right">
        <div class="login-form-container">
          <h2>欢迎回来</h2>
          <p class="subtitle">登录您的账号以继续</p>
          
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            class="login-form"
            @submit.prevent="handleLogin"
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
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                class="login-btn"
                @click="handleLogin"
              >
                登 录
              </el-button>
            </el-form-item>
          </el-form>
          
          <div class="login-footer">
            <span>还没有账号？</span>
            <router-link to="/register">立即注册</router-link>
          </div>
          
          <div class="demo-account">
            <el-divider>演示账号</el-divider>
            <div class="account-list">
              <el-tag @click="fillAccount('admin', '123456')" class="account-tag">
                管理员: admin / 123456
              </el-tag>
              <el-tag @click="fillAccount('user', '123456')" class="account-tag" type="success">
                用户: user / 123456
              </el-tag>
            </div>
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
import { User, Lock, Checked, TrendCharts, Calendar, Document } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const fillAccount = (username, password) => {
  form.username = username
  form.password = password
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await userStore.login(form)
      ElMessage.success('登录成功！')
      
      if (userStore.user?.role === 'ADMIN') {
        router.push('/admin/dashboard')
      } else {
        router.push('/dashboard')
      }
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f766e 0%, #059669 50%, #10b981 100%);
  padding: 20px;
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
}

.login-left {
  flex: 1;
  padding: 60px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
  backdrop-filter: blur(10px);
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, #059669 0%, #047857 100%);
    z-index: -1;
  }

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
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
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

  .features {
    .feature-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 0;
      font-size: 16px;

      .el-icon {
        font-size: 20px;
      }
    }
  }
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
}

.login-form-container {
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

.login-form {
  .el-input {
    --el-input-height: 48px;
  }

  .el-form-item {
    margin-bottom: 24px;
  }
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: var(--text-secondary);

  a {
    color: var(--primary-color);
    font-weight: 500;
    margin-left: 4px;
  }
}

.demo-account {
  margin-top: 32px;

  .el-divider {
    --el-text-color-regular: var(--text-muted);
    font-size: 12px;
  }

  .account-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .account-tag {
    cursor: pointer;
    padding: 8px 16px;
    font-size: 13px;
    justify-content: center;

    &:hover {
      opacity: 0.8;
    }
  }
}

@media (max-width: 768px) {
  .login-left {
    display: none;
  }

  .login-right {
    padding: 40px 24px;
  }
}
</style>
