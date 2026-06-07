<template>
  <div class="dream-list-unified">
    <!-- Hero区 -->
    <div class="page-hero">
      <div class="hero-badge breathe">
        <el-icon><Collection /></el-icon>
        <span>梦境档案馆</span>
      </div>
      <h1 class="page-title">
        <span class="title-main consciousness-text">探索梦境</span>
        <span class="title-sub">追寻潜意识的足迹</span>
      </h1>
    </div>

    <!-- 筛选工具栏 -->
    <GlassCard variant="elevated" size="md" :interactive="false" class="filter-bar">
      <div class="filter-content">
        <el-select v-model="filters.gender" placeholder="性别" clearable class="filter-item">
          <el-option label="全部性别" value="" />
          <el-option label="♂ 男性" value="M" />
          <el-option label="♀ 女性" value="F" />
        </el-select>
        
        <el-select v-model="filters.ageGroup" placeholder="年龄组" clearable class="filter-item">
          <el-option label="全部年龄" value="" />
          <el-option label="🧑 成人" value="A" />
          <el-option label="👦 儿童" value="C" />
          <el-option label="👴 老年" value="O" />
        </el-select>
        
        <DreamInput
          v-model="filters.keyword"
          placeholder="🔍 搜索梦境内容..."
          clearable
          class="search-input"
          @keyup.enter="searchDreams"
        />
        
        <DreamButton variant="primary" @click="searchDreams">
          <el-icon><Search /></el-icon>
          搜索
        </DreamButton>
      </div>
    </GlassCard>

    <!-- 结果计数 -->
    <div class="result-stats" v-if="!loading && total > 0">
      <span class="stat-text">共找到</span>
      <span class="stat-number consciousness-text">{{ total }}</span>
      <span class="stat-text">个梦境</span>
      <div class="stat-divider"></div>
      <span class="stat-hint">点击卡片查看详情</span>
    </div>

    <!-- 梦境网格 -->
    <div class="dreams-grid" v-if="!loading && dreams.length > 0">
      <div
        v-for="(dream, index) in dreams"
        :key="dream.id"
        class="dream-card-wrapper"
        :style="{ animationDelay: `${index * 0.06}s` }"
        @click="viewDream(dream.id)"
      >
        <!-- 卡片光晕背景 -->
        <div class="card-glow-bg" :class="dream.gender === 'M' ? 'glow-blue' : 'glow-pink'"></div>

        <GlassCard class="dream-card" :interactive="true">

          <div class="dream-header">
            <DreamTag :type="dream.gender === 'M' ? 'primary' : 'danger'" size="sm" effect="dark">
              {{ dream.gender === 'M' ? '♂ 男性' : '♀ 女性' }}
            </DreamTag>
            <DreamTag type="success" size="sm">
              {{ getAgeLabel(dream.ageGroup) }}
            </DreamTag>
            <div class="card-number">#{{ dream.dreamNumber || dream.id }}</div>
          </div>

          <h3 class="dream-title">{{ dream.name }}</h3>

          <div class="dream-excerpt">
            {{ truncate(dream.report, 140) }}
          </div>

          <!-- 底部元信息 -->
          <div class="dream-footer">
            <div class="meta-items">
              <div class="meta-item" v-if="dream.timePeriod">
                <el-icon><Clock /></el-icon>
                <span>{{ dream.timePeriod }}</span>
              </div>
              <div class="meta-item" v-if="dream.birthYear">
                <el-icon><Calendar /></el-icon>
                <span>{{ dream.birthYear }}</span>
              </div>
            </div>

            <div class="read-more">
              <span>阅读</span>
              <el-icon><Right /></el-icon>
            </div>
          </div>
        </GlassCard>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next, jumper"
        @current-change="handlePageChange"
        background
      />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <DreamLoader type="flower" text="寻找梦境中..." />
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && dreams.length === 0" class="empty-state">
      <div class="empty-icon">☁️</div>
      <p class="empty-text">暂无梦境数据</p>
      <p class="empty-hint">尝试调整筛选条件</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Collection, Search, Clock, Document, Right, Calendar } from '@element-plus/icons-vue'
import { dreamAPI } from '@/api/dream'

const router = useRouter()
const loading = ref(true)
const dreams = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const filters = reactive({
  gender: '',
  ageGroup: '',
  keyword: ''
})

onMounted(() => {
  loadDreams()
})

const loadDreams = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      ...filters
    }
    const response = await dreamAPI.searchDreams(params)
    dreams.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载梦境失败:', error)
  } finally {
    loading.value = false
  }
}

const searchDreams = () => {
  currentPage.value = 1
  loadDreams()
}

const handlePageChange = () => {
  loadDreams()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const getAgeLabel = (age) => ({ A: '成人', C: '儿童', O: '老年' }[age] || age)
const truncate = (text, length) => text?.length > length ? text.substring(0, length) + '...' : text
const viewDream = (id) => router.push(`/dreams/${id}`)
</script>

<style scoped>
.dream-list-unified {
  min-height: calc(100vh - 180px);
  padding-bottom: 80px;
}

/* ===== Hero ===== */
.page-hero {
  text-align: center;
  margin-bottom: 48px;
  padding: 20px 0;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 12px 28px;
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.15), rgba(255, 182, 193, 0.15));
  border: 2px solid rgba(255, 170, 128, 0.3);
  border-radius: 100px;
  font-size: 14px;
  font-weight: 600;
  color: #FF6A88;
  margin-bottom: 24px;
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.15);
}

.page-title {
  margin: 0;
}

.title-main {
  display: block;
  font-size: 56px;
  line-height: 1.2;
  margin-bottom: 12px;
}

.title-sub {
  display: block;
  font-size: 18px;
  color: #64748B;
  font-weight: 500;
}

/* ===== 筛选栏 ===== */
.filter-bar {
  margin-bottom: 32px;
}

.filter-content {
  display: flex;
  gap: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-item {
  min-width: 150px;
}

.search-input {
  flex: 1;
  min-width: 250px;
}

/* ===== 结果计数 ===== */
.result-stats {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 28px;
  font-size: 14px;
  color: #64748B;
}

.stat-number {
  font-size: 28px;
  font-weight: 900;
  line-height: 1;
}

.stat-divider {
  width: 1px;
  height: 16px;
  background: rgba(100, 116, 139, 0.2);
  margin: 0 4px;
}

.stat-hint {
  color: #94A3B8;
}

/* ===== 梦境网格 ===== */
.dreams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 28px;
  margin-bottom: 48px;
}

/* 卡片包装 */
.dream-card-wrapper {
  position: relative;
  animation: cardSlideUp 0.6s cubic-bezier(0.23, 1, 0.32, 1) backwards;
  cursor: pointer;
}

@keyframes cardSlideUp {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.96);
    filter: blur(4px);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
    filter: blur(0);
  }
}

/* 卡片背景光晕 */
.card-glow-bg {
  position: absolute;
  inset: -10px;
  border-radius: 32px;
  opacity: 0;
  transition: opacity 0.5s ease;
  filter: blur(20px);
  z-index: -1;
}

.glow-blue {
  background: radial-gradient(ellipse at 50% 50%, rgba(165, 180, 252, 0.3), transparent 70%);
}

.glow-pink {
  background: radial-gradient(ellipse at 50% 50%, rgba(255, 154, 139, 0.3), transparent 70%);
}

.dream-card-wrapper:hover .card-glow-bg {
  opacity: 1;
}

/* 卡片主体 */
.dream-card {
  position: relative;
  overflow: hidden;
  transition: transform 0.4s cubic-bezier(0.23, 1, 0.32, 1);
}

.dream-card-wrapper:hover .dream-card {
  transform: translateY(-6px);
}

/* 顶部装饰条 */
.card-accent-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  border-radius: 20px 20px 0 0;
}

.accent-pink {
  background: linear-gradient(90deg, #FF9A8B, #FFD983);
}

.accent-blue {
  background: linear-gradient(90deg, #A5B4FC, #67E8F9);
}

/* 卡片头部 */
.dream-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  margin-top: 8px;
}

.card-number {
  margin-left: auto;
  font-size: 12px;
  color: #94A3B8;
  font-weight: 600;
  letter-spacing: 0.05em;
}

/* 标题 */
.dream-title {
  font-size: 20px;
  margin: 0 0 14px 0;
  color: #0F172A;
  font-weight: 800;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.3s ease;
}

.dream-card-wrapper:hover .dream-title {
  color: #FF6A88;
}

/* 摘要 */
.dream-excerpt {
  color: #64748B;
  font-size: 14px;
  line-height: 1.75;
  margin-bottom: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 底部 */
.dream-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 170, 128, 0.12);
}

.meta-items {
  display: flex;
  gap: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: #94A3B8;
}

/* 阅读更多按钮 */
.read-more {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 700;
  color: #FF9A8B;
  background: rgba(255, 154, 139, 0.1);
  padding: 6px 14px;
  border-radius: 100px;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.dream-card-wrapper:hover .read-more {
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  color: white;
  transform: translateX(4px);
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.35);
}

/* ===== 分页 ===== */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 48px;
}

:deep(.el-pagination.is-background .el-pager li.is-active) {
  background: linear-gradient(135deg, #FF9A8B, #FFD983) !important;
  border-color: transparent;
}

/* ===== 加载/空状态 ===== */
.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 24px;
}

.empty-icon {
  font-size: 88px;
  animation: floatEmpty 4s ease-in-out infinite;
  filter: drop-shadow(0 8px 16px rgba(255, 154, 139, 0.2));
}

@keyframes floatEmpty {
  0%, 100% { transform: translateY(0) rotate(-5deg); }
  50% { transform: translateY(-18px) rotate(5deg); }
}

.empty-text {
  font-size: 22px;
  color: #475569;
  margin: 0;
  font-weight: 700;
}

.empty-hint {
  font-size: 14px;
  color: #94A3B8;
  margin: 0;
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .dreams-grid {
    grid-template-columns: 1fr;
  }
  .title-main {
    font-size: 40px;
  }
  .filter-content {
    flex-direction: column;
    align-items: stretch;
  }
  .filter-item, .search-input {
    width: 100%;
  }
}
</style>
