<template>
  <div class="community-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>宠友社区</h3>
          <el-button type="primary" @click="handleAddPost">发布帖子</el-button>
        </div>
      </template>
      <el-table :data="posts" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题">
          <template #default="scope">
            <a href="javascript:void(0)" @click="handleViewPost(scope.row.id)">{{ scope.row.title }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="likes" label="点赞" width="80"></el-table-column>
        <el-table-column prop="comments" label="评论" width="80"></el-table-column>
        <el-table-column prop="createTime" label="发布时间"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewPost(scope.row.id)">查看</el-button>
            <el-button type="danger" size="small" @click="handleDeletePost(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { postApi } from '../../api/post'

const router = useRouter()

const posts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const handleAddPost = () => {
  router.push('/community/add')
}

const handleViewPost = (id) => {
  router.push(`/community/post/${id}`)
}

const handleDeletePost = (id) => {
  ElMessageBox.confirm('确定要删除这个帖子吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await postApi.deletePost(id)
      ElMessage.success('删除成功')
      await fetchPosts()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  fetchPosts()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  fetchPosts()
}

const fetchPosts = async () => {
  try {
    const response = await postApi.getPosts()
    posts.value = response.data
    total.value = posts.value.length
  } catch (error) {
    console.error('获取帖子失败:', error)
  }
}

onMounted(() => {
  fetchPosts()
})
</script>

<style scoped>
.community-list-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

a {
  color: #409EFF;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>