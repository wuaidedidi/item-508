<template>
  <div class="article-management">
    <div class="page-header">
      <h1>文章管理</h1>
      <p>管理健康资讯文章</p>
    </div>

    <el-card>
      <div class="toolbar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索文章标题"
          style="width: 240px"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="showAddDialog">
          <el-icon><Plus /></el-icon>
          添加文章
        </el-button>
      </div>

      <el-table :data="articles" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="100" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="editArticle(row)">编辑</el-button>
            <el-button type="danger" link @click="deleteArticle(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 10"
        v-model:current-page="page"
        :total="total"
        layout="prev, pager, next"
        class="pagination"
        @current-change="fetchArticles"
      />
    </el-card>

    <!-- Article Dialog -->
    <el-dialog
      v-model="showDialog"
      :title="form.id ? '编辑文章' : '添加文章'"
      width="700px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类">
            <el-option label="健康饮食" value="健康饮食" />
            <el-option label="运动健身" value="运动健身" />
            <el-option label="心理健康" value="心理健康" />
            <el-option label="疾病预防" value="疾病预防" />
            <el-option label="养生保健" value="养生保健" />
          </el-select>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者名称" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="2"
            placeholder="请输入文章摘要"
          />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="8"
            placeholder="请输入文章内容（支持HTML）"
          />
        </el-form-item>
        <el-form-item label="封面图" prop="coverImage">
          <el-input v-model="form.coverImage" placeholder="请输入封面图URL（可选）" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveArticle">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import api from '@/api'

const loading = ref(false)
const saving = ref(false)
const articles = ref([])
const page = ref(1)
const total = ref(0)
const searchKeyword = ref('')
const showDialog = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  category: '',
  author: '',
  summary: '',
  content: '',
  coverImage: '',
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入文章标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入文章内容', trigger: 'blur' }]
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

let searchTimer = null
const handleSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    page.value = 1
    fetchArticles()
  }, 300)
}

const fetchArticles = async () => {
  loading.value = true
  try {
    const params = { page: page.value, size: 10 }
    if (searchKeyword.value) params.keyword = searchKeyword.value
    const res = await api.getAdminArticles(params)
    articles.value = res.data?.records || []
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
    title: '',
    category: '',
    author: '',
    summary: '',
    content: '',
    coverImage: '',
    status: 1
  })
}

const showAddDialog = () => {
  resetForm()
  showDialog.value = true
}

const editArticle = (row) => {
  Object.assign(form, row)
  showDialog.value = true
}

const saveArticle = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    saving.value = true
    try {
      if (form.id) {
        await api.updateArticle(form.id, form)
      } else {
        await api.createArticle(form)
      }
      ElMessage.success('保存成功')
      showDialog.value = false
      fetchArticles()
    } catch (e) {
      ElMessage.error(e.message || '保存失败')
    } finally {
      saving.value = false
    }
  })
}

const deleteArticle = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除此文章吗？', '确认删除', { type: 'warning' })
    await api.deleteArticle(id)
    ElMessage.success('删除成功')
    fetchArticles()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e.message || '删除失败')
  }
}

onMounted(() => {
  fetchArticles()
})
</script>

<style lang="scss" scoped>
.article-management {
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
    justify-content: space-between;
    align-items: center;
  }

  .pagination {
    margin-top: 16px;
    justify-content: center;
  }
}
</style>
