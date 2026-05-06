<template>
  <div class="admin-dashboard">
    <div class="page-header">
      <h1>控制台</h1>
      <p>系统概览和快捷操作</p>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6">
        <div class="stat-card users">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">用户总数</p>
            <p class="stat-value">{{ stats.totalUsers || 0 }}</p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card articles">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">文章总数</p>
            <p class="stat-value">{{ stats.totalArticles || 0 }}</p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card appointments">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">预约总数</p>
            <p class="stat-value">{{ stats.totalAppointments || 0 }}</p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card today">
          <div class="stat-icon">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">今日预约</p>
            <p class="stat-value">{{ stats.todayAppointments || 0 }}</p>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/admin/users')">
              <el-icon><User /></el-icon>
              用户管理
            </el-button>
            <el-button @click="$router.push('/admin/articles')">
              <el-icon><Document /></el-icon>
              文章管理
            </el-button>
            <el-button @click="$router.push('/dashboard')">
              <el-icon><Monitor /></el-icon>
              前台首页
            </el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :lg="12">
        <el-card>
          <template #header>
            <span>系统信息</span>
          </template>
          <div class="system-info">
            <div class="info-item">
              <span class="label">系统名称</span>
              <span class="value">个人健康管理系统</span>
            </div>
            <div class="info-item">
              <span class="label">系统版本</span>
              <span class="value">v1.0.0</span>
            </div>
            <div class="info-item">
              <span class="label">技术栈</span>
              <span class="value">Spring Boot + Vue 3 + MySQL</span>
            </div>
            <div class="info-item">
              <span class="label">当前时间</span>
              <span class="value">{{ currentTime }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { User, Document, Calendar, Clock, Monitor } from '@element-plus/icons-vue'
import api from '@/api'

const stats = ref({
  totalUsers: 0,
  totalArticles: 0,
  totalAppointments: 0,
  todayAppointments: 0
})

const currentTime = ref('')
let timer = null

const updateTime = () => {
  currentTime.value = new Date().toLocaleString('zh-CN')
}

const fetchStats = async () => {
  try {
    const res = await api.getStatistics()
    if (res.data) {
      stats.value = res.data
    }
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  fetchStats()
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style lang="scss" scoped>
.admin-dashboard {
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

  .stats-row {
    margin-bottom: 24px;
  }

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
    }

    &.users .stat-icon {
      background: rgba(99, 102, 241, 0.1);
      color: #6366f1;
    }

    &.articles .stat-icon {
      background: rgba(16, 185, 129, 0.1);
      color: #10b981;
    }

    &.appointments .stat-icon {
      background: rgba(245, 158, 11, 0.1);
      color: #f59e0b;
    }

    &.today .stat-icon {
      background: rgba(239, 68, 68, 0.1);
      color: #ef4444;
    }

    .stat-info {
      .stat-label {
        font-size: 14px;
        color: var(--text-secondary);
        margin-bottom: 4px;
      }

      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: var(--text-primary);
      }
    }
  }

  .quick-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }

  .system-info {
    .info-item {
      display: flex;
      justify-content: space-between;
      padding: 12px 0;
      border-bottom: 1px solid var(--border-color);

      &:last-child {
        border-bottom: none;
      }

      .label {
        color: var(--text-secondary);
      }

      .value {
        font-weight: 500;
        color: var(--text-primary);
      }
    }
  }
}
</style>
