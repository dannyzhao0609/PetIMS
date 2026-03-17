<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>宠友互动</h2>
      <el-button type="primary" class="glow-button" @click="handleAdd">
        <el-icon><Plus /></el-icon>发布帖子
      </el-button>
    </div>
    <div class="page-content">
      <div class="post-list" v-if="postList.length > 0">
        <div class="post-item glass-card" v-for="post in postList" :key="post.id">
          <div class="post-header">
            <el-avatar :size="48" style="background: linear-gradient(135deg, #3b82f6, #8b5cf6)">
              {{ post.userName?.charAt(0)?.toUpperCase() || 'U' }}
            </el-avatar>
            <div class="post-user">
              <div class="user-name">{{ post.userName || '用户' }}</div>
              <div class="post-time">{{ formatTime(post.createTime) }}</div>
            </div>
            <div class="post-actions">
              <el-button link type="primary" size="small" @click.stop="handleEdit(post)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button link type="danger" size="small" @click.stop="handleDelete(post)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          <div class="post-content">
            <h3 class="post-title">{{ post.title }}</h3>
            <p class="post-desc">{{ post.content }}</p>
          </div>
          <div class="post-footer">
            <div class="post-stats">
              <span class="stat-item">
                <el-icon><ChatDotRound /></el-icon>
                {{ post.comments || 0 }} 评论
              </span>
              <span class="stat-item">
                <el-icon><Star /></el-icon>
                {{ post.likes || 0 }} 点赞
              </span>
            </div>
          </div>
        </div>
      </div>
      <el-empty v-else description="暂无帖子，快来发布第一条吧~" />
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑帖子' : '发布帖子'"
      width="600px"
      class="post-dialog"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, ChatDotRound, Star } from '@element-plus/icons-vue'
import { getPosts, createPost, updatePost, deletePost } from '@/api/post'

const postList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  title: '',
  content: '',
  attachments: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

onMounted(() => {
  fetchPostList()
})

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  return date.toLocaleDateString('zh-CN')
}

const fetchPostList = async () => {
  try {
    const res = await getPosts()
    postList.value = res.data || []
  } catch (error) {
    console.error('获取帖子列表失败:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    title: '',
    content: '',
    attachments: ''
  })
  dialogVisible.value = true
}

const handleEdit = (post) => {
  isEdit.value = true
  Object.assign(form, post)
  dialogVisible.value = true
}

const handleDelete = async (post) => {
  try {
    await ElMessageBox.confirm('确定要删除这条帖子吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePost(post.id)
    postList.value = postList.value.filter(p => p.id !== post.id)
    ElMessage.success('删除成功')
  } catch {
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    form.userId = userInfo.id || 1
    
    if (isEdit.value) {
      await updatePost(form)
      ElMessage.success('更新成功')
    } else {
      await createPost(form)
      ElMessage.success('发布成功')
    }
    fetchPostList()
    dialogVisible.value = false
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped lang="scss">
.page-container {
  .page-header {
    padding: 20px 24px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      color: #f1f5f9;
    }
  }

  .post-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .post-item {
    padding: 24px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 30px rgba(0, 212, 255, 0.15);
    }

    .post-header {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 16px;

      .post-user {
        flex: 1;

        .user-name {
          font-size: 16px;
          font-weight: 600;
          color: #f1f5f9;
          margin-bottom: 4px;
        }

        .post-time {
          font-size: 13px;
          color: #64748b;
        }
      }

      .post-actions {
        display: flex;
        gap: 4px;
      }
    }

    .post-content {
      margin-bottom: 16px;

      .post-title {
        font-size: 18px;
        font-weight: 600;
        color: #f1f5f9;
        margin: 0 0 12px 0;
      }

      .post-desc {
        font-size: 14px;
        color: #94a3b8;
        margin: 0;
        line-height: 1.6;
      }
    }

    .post-footer {
      .post-stats {
        display: flex;
        gap: 24px;

        .stat-item {
          display: flex;
          align-items: center;
          gap: 6px;
          color: #64748b;
          font-size: 14px;
          cursor: pointer;
          transition: color 0.2s;

          &:hover {
            color: #00d4ff;
          }
        }
      }
    }
  }
}

:deep(.post-dialog) {
  .el-dialog {
    background: rgba(30, 41, 59, 0.95);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.1);

    .el-dialog__header {
      .el-dialog__title {
        color: #f1f5f9;
      }
    }

    .el-form-item__label {
      color: #94a3b8;
    }

    .el-input__wrapper,
    .el-textarea__inner {
      background: rgba(15, 23, 42, 0.8);
      border-color: rgba(255, 255, 255, 0.1);

      &:hover {
        border-color: rgba(0, 212, 255, 0.5);
      }

      .el-input__inner {
        color: #f1f5f9;
      }
    }
  }
}
</style>
