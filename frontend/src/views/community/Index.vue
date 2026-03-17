<template>
  <div class="page-container">
    <div class="page-header glass-card">
      <h2>宠友互动</h2>
    </div>
    <div class="page-content">
      <el-empty description="功能开发中..." />
    </div>
  </div>
</template>

<script setup>
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

  .page-content {
    .glass-card {
      padding: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>
