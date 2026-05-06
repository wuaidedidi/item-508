<template>
  <div class="article-detail" v-loading="loading">
    <div v-if="article" class="article-container">
      <div class="article-header">
        <el-button text @click="$router.back()">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <el-tag size="small" type="info">{{ article.category }}</el-tag>
      </div>

      <h1 class="article-title">{{ article.title }}</h1>

      <div class="article-meta">
        <span><el-icon><User /></el-icon> {{ article.author || '健康小编' }}</span>
        <span><el-icon><Calendar /></el-icon> {{ formatDate(article.createdAt) }}</span>
        <span><el-icon><View /></el-icon> {{ article.viewCount || 0 }} 次浏览</span>
      </div>

      <div v-if="article.coverImage" class="article-cover">
        <img :src="article.coverImage" :alt="article.title" />
      </div>

      <div class="article-content" v-html="article.content"></div>

      <el-divider />

      <div class="article-footer">
        <p>感谢阅读！希望这篇文章对您有所帮助。</p>
        <el-button type="primary" @click="$router.push('/articles')">
          浏览更多文章
        </el-button>
      </div>
    </div>

    <el-empty v-else-if="!loading" description="文章不存在" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowLeft, User, Calendar, View } from '@element-plus/icons-vue'
import api from '@/api'

const route = useRoute()
const loading = ref(false)
const article = ref(null)

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const fetchArticle = async () => {
  loading.value = true
  try {
    const res = await api.getArticle(route.params.id)
    article.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchArticle()
})
</script>

<style lang="scss" scoped>
.article-detail {
  max-width: 800px;
  margin: 0 auto;
  min-height: 400px;

  .article-container {
    background: white;
    border-radius: 12px;
    padding: 32px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }

  .article-header {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 24px;
  }

  .article-title {
    font-size: 28px;
    font-weight: 700;
    color: var(--text-primary);
    margin-bottom: 16px;
    line-height: 1.4;
  }

  .article-meta {
    display: flex;
    gap: 24px;
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 24px;

    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .article-cover {
    margin-bottom: 24px;
    border-radius: 8px;
    overflow: hidden;

    img {
      width: 100%;
      max-height: 400px;
      object-fit: cover;
    }
  }

  .article-content {
    font-size: 16px;
    line-height: 1.8;
    color: var(--text-primary);

    :deep(p) {
      margin-bottom: 16px;
    }

    :deep(h2) {
      font-size: 22px;
      font-weight: 600;
      margin: 32px 0 16px;
    }

    :deep(h3) {
      font-size: 18px;
      font-weight: 600;
      margin: 24px 0 12px;
    }

    :deep(ul), :deep(ol) {
      padding-left: 24px;
      margin-bottom: 16px;
    }

    :deep(li) {
      margin-bottom: 8px;
    }

    :deep(blockquote) {
      border-left: 4px solid var(--primary-color);
      padding-left: 16px;
      margin: 16px 0;
      color: var(--text-secondary);
      font-style: italic;
    }
  }

  .article-footer {
    text-align: center;
    padding-top: 16px;

    p {
      color: var(--text-secondary);
      margin-bottom: 16px;
    }
  }
}
</style>
