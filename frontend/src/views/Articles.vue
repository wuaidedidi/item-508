<template>
  <div class="articles">
    <div class="page-header">
      <h1>健康资讯</h1>
      <p>阅读健康知识，守护您的健康</p>
    </div>

    <div class="filter-bar">
      <el-radio-group v-model="category" @change="handleCategoryChange">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="健康饮食">健康饮食</el-radio-button>
        <el-radio-button label="运动健身">运动健身</el-radio-button>
        <el-radio-button label="心理健康">心理健康</el-radio-button>
        <el-radio-button label="疾病预防">疾病预防</el-radio-button>
        <el-radio-button label="养生保健">养生保健</el-radio-button>
      </el-radio-group>
    </div>

    <div v-loading="loading" class="articles-grid">
      <div
        v-for="article in articles"
        :key="article.id"
        class="article-card"
        @click="$router.push(`/articles/${article.id}`)"
      >
        <div class="article-cover">
          <img v-if="article.coverImage" :src="article.coverImage" :alt="article.title" />
          <div v-else class="placeholder-cover">
            <el-icon :size="48"><Document /></el-icon>
          </div>
        </div>
        <div class="article-content">
          <el-tag size="small" type="info">{{ article.category }}</el-tag>
          <h3>{{ article.title }}</h3>
          <p>{{ article.summary }}</p>
          <div class="article-meta">
            <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }} 次浏览</span>
            <span>{{ formatDate(article.createdAt) }}</span>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && !articles.length" description="暂无文章" />

    <el-pagination
      v-if="total > 12"
      v-model:current-page="page"
      :total="total"
      :page-size="12"
      layout="prev, pager, next"
      class="pagination"
      @current-change="fetchArticles"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Document, View } from '@element-plus/icons-vue'
import api from '@/api'

const loading = ref(false)
const articles = ref([])
const category = ref('')
const page = ref(1)
const total = ref(0)

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

const fetchArticles = async () => {
  loading.value = true
  try {
    const params = { page: page.value, size: 12 }
    if (category.value) params.category = category.value
    const res = await api.getArticles(params)
    articles.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleCategoryChange = () => {
  page.value = 1
  fetchArticles()
}

onMounted(() => {
  fetchArticles()
})
</script>

<style lang="scss" scoped>
.articles {
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

  .filter-bar {
    margin-bottom: 24px;
  }

  .articles-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 24px;
    min-height: 200px;
  }

  .article-card {
    background: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    }

    .article-cover {
      height: 160px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .placeholder-cover {
        width: 100%;
        height: 100%;
        background: linear-gradient(135deg, #e0e7ff, #c7d2fe);
        display: flex;
        align-items: center;
        justify-content: center;
        color: #6366f1;
      }
    }

    .article-content {
      padding: 16px;

      .el-tag {
        margin-bottom: 8px;
      }

      h3 {
        font-size: 16px;
        font-weight: 600;
        margin-bottom: 8px;
        color: var(--text-primary);
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      p {
        font-size: 14px;
        color: var(--text-secondary);
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        margin-bottom: 12px;
      }

      .article-meta {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        color: var(--text-muted);

        span {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }

  .pagination {
    margin-top: 24px;
    justify-content: center;
  }
}
</style>
