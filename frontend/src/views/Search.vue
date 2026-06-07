<template>
  <div class="search-unified">
    <!-- Hero区 -->
    <div class="search-hero">
      <div class="search-icon-float">
        <span class="icon-emoji">🔍</span>
        <div class="icon-glow"></div>
      </div>
      <h1 class="consciousness-text">高级搜索</h1>
      <p class="search-subtitle">通过多维度条件精确查找梦境</p>
    </div>

    <!-- 搜索表单 -->
    <GlassCard variant="elevated" size="lg" :interactive="false" class="search-form">
      <div class="form-section">
        <div class="section-label">
          <div class="label-icon">👤</div>
          <span>基础信息</span>
        </div>
        <div class="form-grid">
          <el-select v-model="searchForm.gender" placeholder="性别" clearable>
            <el-option label="男性" value="M" />
            <el-option label="女性" value="F" />
          </el-select>
          <el-select v-model="searchForm.ageGroup" placeholder="年龄组" clearable>
            <el-option label="成人" value="A" />
            <el-option label="儿童" value="C" />
            <el-option label="老年" value="O" />
          </el-select>
        </div>
      </div>

      <div class="form-section">
        <div class="section-label">
          <div class="label-icon">📝</div>
          <span>内容搜索</span>
        </div>
        <DreamInput
          v-model="searchForm.keyword"
          type="textarea"
          placeholder="输入关键词搜索梦境内容..."
          :rows="3"
        />
      </div>

      <div class="form-section">
        <div class="section-label">
          <div class="label-icon">⏰</div>
          <span>时间筛选</span>
        </div>
        <DreamInput 
          v-model="searchForm.timePeriod" 
          placeholder="时间段（如：夜晚、清晨）" 
          clearable
        />
      </div>

      <div class="form-actions">
        <DreamButton variant="ghost" @click="resetForm" icon="🔄">
          重置
        </DreamButton>
        <DreamButton variant="primary" @click="handleSearch" :loading="loading">
          搜索梦境
        </DreamButton>
      </div>
    </GlassCard>

    <!-- 搜索结果 -->
    <div class="search-results" v-if="hasSearched">
      <!-- 搜索结果统计 -->
      <div class="results-meta">
        <h2>搜索结果</h2>
        <div class="results-count">
          找到 <span class="consciousness-text">{{ total }}</span> 个梦境
        </div>
      </div>

      <div class="results-grid" v-if="results.length > 0">
        <div
          v-for="dream in results"
          :key="dream.id"
          class="result-card-wrapper"
          @click="viewDream(dream.id)"
        >
          <GlassCard class="result-card">
            <div class="result-accent-bar" :class="dream.gender === 'M' ? 'accent-blue' : 'accent-pink'"></div>
            <div class="result-badges">
              <DreamTag size="sm" :type="dream.gender === 'M' ? 'primary' : 'danger'" effect="dark">
                {{ dream.gender === 'M' ? '♂ 男' : '♀ 女' }}
              </DreamTag>
              <DreamTag size="sm" type="success">
                {{ getAgeLabel(dream.ageGroup) }}
              </DreamTag>
            </div>
            <h3 class="result-title">{{ dream.name }}</h3>
            <p class="result-excerpt">{{ truncate(dream.report, 100) }}</p>
            <div class="result-footer">
              <span class="result-time" v-if="dream.timePeriod">
                <el-icon><Clock /></el-icon> {{ dream.timePeriod }}
              </span>
              <span class="read-pill">
                查看
                <el-icon><ArrowRight /></el-icon>
              </span>
            </div>
          </GlassCard>
        </div>
      </div>

      <div v-else class="empty-results">
        <div class="empty-icon">🌙</div>
        <p>未找到匹配的梦境</p>
        <p class="empty-hint">尝试调整搜索条件</p>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next"
          @current-change="handlePageChange"
          background
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowRight, Clock } from '@element-plus/icons-vue'
import { dreamAPI } from '@/api/dream'

const router = useRouter()
const loading = ref(false)
const hasSearched = ref(false)
const results = ref([])
const currentPage = ref(1)
const pageSize = ref(9)
const total = ref(0)

const searchForm = reactive({
  gender: '',
  ageGroup: '',
  keyword: '',
  timePeriod: ''
})

const handleSearch = async () => {
  loading.value = true
  hasSearched.value = true
  currentPage.value = 1
  await loadResults()
  loading.value = false
}

const loadResults = async () => {
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      ...searchForm
    }
    const response = await dreamAPI.searchDreams(params)
    results.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('搜索失败:', error)
  }
}

const resetForm = () => {
  Object.assign(searchForm, {
    gender: '',
    ageGroup: '',
    keyword: '',
    timePeriod: ''
  })
  hasSearched.value = false
  results.value = []
}

const handlePageChange = () => {
  loadResults()
  window.scrollTo({ top: 400, behavior: 'smooth' })
}

const getAgeLabel = (age) => ({ A: '成人', C: '儿童', O: '老年' }[age] || age)
const truncate = (text, length) => text?.length > length ? text.substring(0, length) + '...' : text
const viewDream = (id) => router.push(`/dreams/${id}`)
</script>

<style scoped>
.search-unified {
  max-width: 900px;
  margin: 0 auto;
  padding-bottom: 60px;
}

/* Hero区 */
.search-hero {
  text-align: center;
  margin-bottom: 48px;
  position: relative;
}

.search-icon-float {
  position: relative;
  font-size: 64px;
  margin-bottom: 24px;
  display: inline-block;
  animation: floatIcon 4s ease-in-out infinite;
}

.icon-emoji {
  position: relative;
  z-index: 1;
  display: block;
  filter: drop-shadow(0 8px 24px rgba(255, 154, 139, 0.5));
}

.icon-glow {
  position: absolute;
  inset: -20px;
  background: radial-gradient(circle, rgba(255, 217, 131, 0.4) 0%, transparent 70%);
  filter: blur(16px);
  animation: iconGlowPulse 3s ease-in-out infinite;
  z-index: 0;
}

@keyframes iconGlowPulse {
  0%, 100% { opacity: 0.6; transform: scale(0.9); }
  50% { opacity: 1; transform: scale(1.1); }
}

@keyframes floatIcon {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-15px) rotate(10deg); }
}

.search-hero h1 {
  font-size: 48px;
  margin: 0 0 16px 0;
}

.search-subtitle {
  font-size: 16px;
  color: #64748B;
}

/* 搜索表单 */
.search-form {
  margin-bottom: 56px;
}

.form-section {
  margin-bottom: 32px;
}

.form-section:last-child {
  margin-bottom: 0;
}

.section-label {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: 600;
  color: #0F172A;
}

.label-icon {
  font-size: 24px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 40px;
  padding-top: 32px;
  border-top: 2px solid rgba(255, 170, 128, 0.15);
}

/* 搜索结果 */
/* 结果统计头部 */
.results-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 2px solid rgba(255, 170, 128, 0.15);
}

.results-meta h2 {
  margin: 0;
  font-size: 26px;
  color: #0F172A;
}

.results-count {
  font-size: 16px;
  color: #64748B;
}

.results-count span {
  font-size: 24px;
  font-weight: 800;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 40px;
}

/* 结果卡片包装器 */
.result-card-wrapper {
  cursor: pointer;
  transition: transform 0.4s cubic-bezier(0.23, 1, 0.32, 1);
}

.result-card-wrapper:hover {
  transform: translateY(-6px);
}

.result-card {
  position: relative;
  overflow: hidden;
  height: 100%;
}

/* 顶部装饰条 */
.result-accent-bar {
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

.result-badges {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.result-title {
  font-size: 18px;
  margin: 0 0 12px 0;
}

.result-excerpt {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
  margin: 0 0 16px 0;
}

.result-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  padding-top: 12px;
  border-top: 1px solid rgba(255, 170, 128, 0.1);
}

.result-time {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #94A3B8;
  font-size: 12px;
}

.read-pill {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 5px 12px;
  background: rgba(255, 154, 139, 0.1);
  border-radius: 100px;
  color: #FF6A88;
  font-weight: 600;
  font-size: 12px;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.result-card-wrapper:hover .read-pill {
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 154, 139, 0.35);
}

/* 空结果 */
.empty-results {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 24px;
}

.empty-results p {
  font-size: 18px;
  color: #64748B;
  margin: 0 0 8px 0;
}

.empty-hint {
  font-size: 14px !important;
  color: #94A3B8 !important;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
  .results-grid {
    grid-template-columns: 1fr;
  }
  .search-hero h1 {
    font-size: 36px;
  }
}
</style>
