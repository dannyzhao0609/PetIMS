<template>
  <div class="post-detail-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>{{ post.title }}</h3>
          <div class="post-meta">
            <span>发布时间: {{ post.createTime }}</span>
            <span>点赞: {{ post.likes }}</span>
            <span>评论: {{ post.comments }}</span>
          </div>
        </div>
      </template>
      <div class="post-content">
        {{ post.content }}
      </div>
      <div class="post-attachments" v-if="post.attachments">
        <h4>附件</h4>
        <div class="attachments-list">
          <!-- 附件展示逻辑 -->
        </div>
      </div>
      <div class="post-actions">
        <el-button type="primary" @click="handleLike">
          <el-icon><Star /></el-icon>
          点赞
        </el-button>
        <el-button type="default" @click="showCommentInput = !showCommentInput">
          <el-icon><ChatLineSquare /></el-icon>
          评论
        </el-button>
      </div>
      <div class="comment-section">
        <h4>评论</h4>
        <div v-if="showCommentInput" class="comment-input">
          <el-input v-model="commentContent" type="textarea" placeholder="请输入评论" rows="3"></el-input>
          <el-button type="primary" @click="handleAddComment" style="margin-top: 10px;">提交评论</el-button>
        </div>
        <div class="comments-list">
          <el-card v-for="comment in comments" :key="comment.id" class="comment-card">
            <div class="comment-content">{{ comment.content }}</div>
            <div class="comment-meta">
              <span>{{ comment.userName }}</span>
              <span>{{ comment.createTime }}</span>
            </div>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { postApi } from '../../api/post'
import { Star, ChatLineSquare } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const post = reactive({
  id: '',
  title: '',
  content: '',
  attachments: '',
  likes: 0,
  comments: 0,
  createTime: ''
})

const comments = ref([])
const showCommentInput = ref(false)
const commentContent = ref('')

const handleLike = async () => {
  try {
    const response = await postApi.incrementLikes(post.id)
    if (response.data) {
      post.likes = response.data.likes
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
  }
}

const handleAddComment = async () => {
  if (!commentContent.value) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  // 这里可以调用添加评论的API
  // 暂时模拟添加评论
  try {
    await postApi.incrementComments(post.id)
    post.comments++
    comments.value.push({
      id: Date.now(),
      content: commentContent.value,
      userName: '当前用户',
      createTime: new Date().toLocaleString()
    })
    commentContent.value = ''
    showCommentInput.value = false
    ElMessage.success('评论成功')
  } catch (error) {
    console.error('评论失败:', error)
    ElMessage.error('评论失败')
  }
}

const fetchPostDetail = async () => {
  try {
    const response = await postApi.getPostById(route.params.id)
    if (response.data) {
      Object.assign(post, response.data)
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
  }
}

const fetchComments = async () => {
  // 这里可以调用获取评论的API
  // 暂时使用模拟数据
  comments.value = [
    {
      id: 1,
      content: '这是一条评论',
      userName: '用户1',
      createTime: '2024-01-01 12:00:00'
    },
    {
      id: 2,
      content: '这是另一条评论',
      userName: '用户2',
      createTime: '2024-01-01 13:00:00'
    }
  ]
}

onMounted(() => {
  fetchPostDetail()
  fetchComments()
})
</script>

<style scoped>
.post-detail-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}

.post-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.post-content {
  margin: 20px 0;
  line-height: 1.6;
}

.post-attachments {
  margin: 20px 0;
}

.attachments-list {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.post-actions {
  margin: 20px 0;
  display: flex;
  gap: 10px;
}

.comment-section {
  margin-top: 30px;
}

.comment-input {
  margin-bottom: 20px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.comment-card {
  margin-bottom: 0;
}

.comment-content {
  margin-bottom: 10px;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}
</style>