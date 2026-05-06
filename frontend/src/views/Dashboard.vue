<template>
  <div class="dashboard">
    <div class="welcome-section">
      <h1>欢迎回来，{{ userStore.user?.nickname || userStore.user?.username }}！</h1>
      <p>今天是 {{ currentDate }}，祝您身体健康！</p>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6">
        <div class="stat-card weight">
          <div class="stat-icon">
            <el-icon><DataBoard /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">最新体重</p>
            <p class="stat-value">{{ stats.latestWeight || '--' }} <span>kg</span></p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card blood-pressure">
          <div class="stat-icon">
            <el-icon><Stopwatch /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">最新血压</p>
            <p class="stat-value">{{ stats.latestBloodPressure || '--' }}</p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card blood-sugar">
          <div class="stat-icon">
            <el-icon><MagicStick /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">最新血糖</p>
            <p class="stat-value">{{ stats.latestBloodSugar || '--' }} <span>mmol/L</span></p>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="6">
        <div class="stat-card heart-rate">
          <div class="stat-icon">
            <el-icon><Checked /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-label">最新心率</p>
            <p class="stat-value">{{ stats.latestHeartRate || '--' }} <span>bpm</span></p>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>健康趋势</span>
              <el-radio-group v-model="chartType" size="small">
                <el-radio-button label="weight">体重</el-radio-button>
                <el-radio-button label="bloodPressure">血压</el-radio-button>
                <el-radio-button label="heartRate">心率</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="chartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="8">
        <el-card class="quick-actions-card">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <el-button @click="$router.push('/health-records')">
              <el-icon><Plus /></el-icon>
              记录健康数据
            </el-button>
            <el-button @click="$router.push('/appointments')">
              <el-icon><Calendar /></el-icon>
              预约就诊
            </el-button>
            <el-button @click="$router.push('/health-profile')">
              <el-icon><User /></el-icon>
              更新健康档案
            </el-button>
          </div>
        </el-card>

        <el-card class="appointments-card">
          <template #header>
            <div class="card-header">
              <span>近期预约</span>
              <router-link to="/appointments" class="more-link">查看更多</router-link>
            </div>
          </template>
          <div v-if="appointments.length" class="appointments-list">
            <div
              v-for="appointment in appointments"
              :key="appointment.id"
              class="appointment-item"
            >
              <div class="appointment-date">
                <span class="day">{{ formatDay(appointment.appointmentTime) }}</span>
                <span class="time">{{ formatTime(appointment.appointmentTime) }}</span>
              </div>
              <div class="appointment-info">
                <p class="doctor">{{ appointment.doctorName }}</p>
                <p class="department">{{ appointment.department }}</p>
              </div>
              <el-tag :type="getStatusType(appointment.status)" size="small">
                {{ getStatusText(appointment.status) }}
              </el-tag>
            </div>
          </div>
          <el-empty v-else description="暂无预约" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :xs="24">
        <el-card class="articles-card">
          <template #header>
            <div class="card-header">
              <span>推荐文章</span>
              <router-link to="/articles" class="more-link">查看更多</router-link>
            </div>
          </template>
          <div v-if="articles.length" class="articles-list">
            <div
              v-for="article in articles"
              :key="article.id"
              class="article-item"
              @click="$router.push(`/articles/${article.id}`)"
            >
              <div class="article-content">
                <h4>{{ article.title }}</h4>
                <p>{{ article.summary }}</p>
              </div>
              <el-tag size="small" type="info">{{ article.category }}</el-tag>
            </div>
          </div>
          <el-empty v-else description="暂无文章" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { DataBoard, Stopwatch, MagicStick, Checked, Plus, Calendar, User } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import * as echarts from 'echarts'
import api from '@/api'

const userStore = useUserStore()

const currentDate = new Date().toLocaleDateString('zh-CN', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
  weekday: 'long'
})

const stats = ref({
  latestWeight: null,
  latestBloodPressure: null,
  latestBloodSugar: null,
  latestHeartRate: null
})

const appointments = ref([])
const articles = ref([])
const chartType = ref('weight')
const chartRef = ref(null)
let chartInstance = null

const fetchDashboardData = async () => {
  try {
    // Fetch health stats from recent records
    const [weightRes, bpRes, bsRes, hrRes] = await Promise.all([
      api.getWeightRecords({ page: 1, size: 1 }),
      api.getBloodPressureRecords({ page: 1, size: 1 }),
      api.getBloodSugarRecords({ page: 1, size: 1 }),
      api.getHeartRateRecords({ page: 1, size: 1 })
    ])

    if (weightRes.data?.records?.length) {
      stats.value.latestWeight = weightRes.data.records[0].weight
    }
    if (bpRes.data?.records?.length) {
      const bp = bpRes.data.records[0]
      stats.value.latestBloodPressure = `${bp.systolic}/${bp.diastolic}`
    }
    if (bsRes.data?.records?.length) {
      stats.value.latestBloodSugar = bsRes.data.records[0].value
    }
    if (hrRes.data?.records?.length) {
      stats.value.latestHeartRate = hrRes.data.records[0].rate
    }

    // Fetch appointments
    const appointmentsRes = await api.getUpcomingAppointments()
    if (appointmentsRes.data) {
      appointments.value = appointmentsRes.data.slice(0, 3)
    }

    // Fetch recommended articles
    const articlesRes = await api.getRecommendedArticles()
    if (articlesRes.data) {
      articles.value = articlesRes.data.slice(0, 4)
    }
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

const initChart = async () => {
  await nextTick()
  // Give the container time to render properly
  setTimeout(async () => {
    if (!chartRef.value) return

    // Dispose existing instance if any
    if (chartInstance) {
      chartInstance.dispose()
    }

    chartInstance = echarts.init(chartRef.value)
    await updateChart()
    // Force resize after init to ensure proper dimensions
    chartInstance.resize()
  }, 100)
}

const updateChart = async () => {
  if (!chartInstance) return

  let data = []
  let yAxisName = ''
  let seriesName = ''

  try {
    if (chartType.value === 'weight') {
      const res = await api.getWeightRecords({ page: 1, size: 30 })
      if (res.data?.records) {
        data = res.data.records.reverse().map(item => ({
          date: item.recordDate,
          value: item.weight
        }))
      }
      yAxisName = '体重 (kg)'
      seriesName = '体重'
    } else if (chartType.value === 'bloodPressure') {
      const res = await api.getBloodPressureRecords({ page: 1, size: 30 })
      if (res.data?.records) {
        data = res.data.records.reverse()
      }
      yAxisName = '血压 (mmHg)'
      seriesName = '血压'
    } else {
      const res = await api.getHeartRateRecords({ page: 1, size: 30 })
      if (res.data?.records) {
        data = res.data.records.reverse().map(item => ({
          date: item.recordDate,
          value: item.rate
        }))
      }
      yAxisName = '心率 (bpm)'
      seriesName = '心率'
    }
  } catch (error) {
    console.error('获取图表数据失败:', error)
  }

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '12%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.date || item.recordDate),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: yAxisName
    },
    series: chartType.value === 'bloodPressure' ? [
      {
        name: '收缩压',
        type: 'line',
        smooth: true,
        data: data.map(item => item.systolic),
        itemStyle: { color: '#ef4444' }
      },
      {
        name: '舒张压',
        type: 'line',
        smooth: true,
        data: data.map(item => item.diastolic),
        itemStyle: { color: '#3b82f6' }
      }
    ] : [
      {
        name: seriesName,
        type: 'line',
        smooth: true,
        data: data.map(item => item.value),
        areaStyle: {
          opacity: 0.3
        },
        itemStyle: { color: '#10b981' }
      }
    ]
  }

  chartInstance.setOption(option)
}

watch(chartType, () => {
  updateChart()
})

const formatDay = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const getStatusType = (status) => {
  const types = {
    'PENDING': 'warning',
    'CONFIRMED': 'success',
    'CANCELLED': 'danger',
    'COMPLETED': 'info',
    'pending': 'warning',
    'confirmed': 'success',
    'cancelled': 'danger',
    'completed': 'info'
  }
  return types[status] || ''
}

const getStatusText = (status) => {
  const texts = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'CANCELLED': '已取消',
    'COMPLETED': '已完成',
    'pending': '待确认',
    'confirmed': '已确认',
    'cancelled': '已取消',
    'completed': '已完成'
  }
  return texts[status] || status
}

const handleResize = () => {
  chartInstance?.resize()
}

onMounted(() => {
  fetchDashboardData()
  initChart()

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style lang="scss" scoped>
.dashboard {
  .welcome-section {
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
    margin-bottom: 20px;
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

    &.weight .stat-icon {
      background: rgba(16, 185, 129, 0.1);
      color: #10b981;
    }

    &.blood-pressure .stat-icon {
      background: rgba(239, 68, 68, 0.1);
      color: #ef4444;
    }

    &.blood-sugar .stat-icon {
      background: rgba(245, 158, 11, 0.1);
      color: #f59e0b;
    }

    &.heart-rate .stat-icon {
      background: rgba(99, 102, 241, 0.1);
      color: #6366f1;
    }

    .stat-info {
      min-width: 0;
      flex: 1;

      .stat-label {
        font-size: 14px;
        color: var(--text-secondary);
        margin-bottom: 4px;
        white-space: nowrap;
      }

      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: var(--text-primary);
        white-space: nowrap;

        span {
          font-size: 14px;
          font-weight: 400;
          color: var(--text-secondary);
        }
      }
    }
  }

  .content-row {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    span {
      font-weight: 600;
    }

    .more-link {
      font-size: 14px;
      color: var(--primary-color);
    }
  }

  .chart-card {
    .chart-container {
      height: 300px;
    }
  }

  .quick-actions-card {
    margin-bottom: 20px;

    .quick-actions {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .el-button {
        width: 100%;
        justify-content: flex-start;
        text-align: left;
        margin-left: 0;
      }
    }
  }

  .appointments-card {
    .appointments-list {
      .appointment-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 0;
        border-bottom: 1px solid var(--border-color);

        &:last-child {
          border-bottom: none;
        }

        .appointment-date {
          text-align: center;
          min-width: 50px;

          .day {
            display: block;
            font-size: 16px;
            font-weight: 600;
            color: var(--primary-color);
          }

          .time {
            font-size: 12px;
            color: var(--text-secondary);
          }
        }

        .appointment-info {
          flex: 1;

          .doctor {
            font-weight: 500;
            margin-bottom: 2px;
          }

          .department {
            font-size: 13px;
            color: var(--text-secondary);
          }
        }
      }
    }
  }

  .articles-card {
    .articles-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 16px;

      .article-item {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        gap: 12px;
        padding: 16px;
        background: #f9fafb;
        border-radius: 8px;
        cursor: pointer;
        transition: background 0.2s;

        &:hover {
          background: #f3f4f6;
        }

        .article-content {
          flex: 1;

          h4 {
            font-size: 15px;
            font-weight: 500;
            margin-bottom: 4px;
            color: var(--text-primary);
          }

          p {
            font-size: 13px;
            color: var(--text-secondary);
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }
        }
      }
    }
  }
}
</style>
