<template>
  <div class="dream-detail-unified" v-if="dream">
    <!-- 返回按钮 -->
    <DreamButton variant="ghost" size="sm" class="back-button breathe" @click="goBack" icon="←">
      返回
    </DreamButton>

    <!-- Hero区 -->
    <div class="detail-hero">
      <!-- 装饰光斑 -->
      <div class="hero-orbs">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
      </div>
      <div class="hero-badges">
        <DreamTag :type="dream.gender === 'M' ? 'primary' : 'danger'" effect="dark" size="lg">
          {{ dream.gender === 'M' ? '男性' : '女性' }}
        </DreamTag>
        <DreamTag type="success" effect="dark" size="lg">
          {{ getAgeLabel(dream.ageGroup) }}
        </DreamTag>
        <DreamTag v-if="dream.timePeriod" effect="plain" size="lg" icon="🕐">
          {{ dream.timePeriod }}
        </DreamTag>
      </div>
      
      <h1 class="detail-title">
        <span class="title-number">#{{ dream.dreamNumber || dream.id }}</span>
        <span class="consciousness-text">{{ dream.name }}</span>
      </h1>
      
      <div class="detail-meta-bar">
        <div class="meta-item" v-if="dream.birthYear">
          <el-icon><Calendar /></el-icon>
          <span>{{ dream.birthYear }}</span>
        </div>
        <div class="divider">•</div>
        <div class="meta-item">
          <el-icon><Document /></el-icon>
          <span>梦境报告</span>
        </div>
      </div>
    </div>

    <!-- 主要内容区 - 双列布局 -->
    <div class="content-grid">
      <!-- 左侧主内容 -->
      <div class="main-content">
        <!-- 梦境报告 -->
        <GlassCard variant="elevated" size="lg" :interactive="false">
          <div class="card-header">
            <div class="header-icon">📖</div>
            <h2>梦境叙述</h2>
          </div>
          <div class="report-text">{{ dream.report }}</div>
        </GlassCard>

        <!-- AI分析入口 -->
        <div class="ai-card-container">
          <GlassCard class="ai-card deep-dream-glow" @click="goToAnalysis">
            <div class="ai-card-icon time-slow">✨</div>
            <div class="ai-card-content">
              <h3 class="consciousness-text">AI智能分析</h3>
              <p>深入解析这个梦境的象征意义</p>
            </div>
            <el-icon class="ai-card-arrow"><ArrowRight /></el-icon>
          </GlassCard>
          <div class="ai-card-shadow"></div>
        </div>
      </div>

      <!-- 右侧信息栏 -->
      <div class="sidebar">
        <!-- 人物分析 -->
        <GlassCard v-if="dream.charactersCn" variant="flat" :interactive="false">
          <div class="info-header">
            <div class="info-icon">👥</div>
            <h3>出现人物</h3>
          </div>
          <div class="character-tags">
            <DreamTag 
              v-for="(char, index) in parseJsonArray(dream.charactersCn)" 
              :key="index"
              type="primary"
              effect="light"
            >
              {{ char }}
            </DreamTag>
          </div>
        </GlassCard>

        <!-- 情感分析 -->
        <GlassCard v-if="dream.emotionsCn" variant="flat" :interactive="false">
          <div class="info-header">
            <div class="info-icon">💭</div>
            <h3>情感色彩</h3>
          </div>
          <div class="emotion-tags">
            <DreamTag 
              v-for="(emotion, index) in parseJsonArray(dream.emotionsCn)" 
              :key="index"
              type="warning"
              effect="light"
            >
              {{ emotion }}
            </DreamTag>
          </div>
        </GlassCard>

        <!-- 数据原始信息 -->
        <GlassCard variant="flat" :interactive="false">
          <div class="info-header">
            <div class="info-icon">🔍</div>
            <h3>原始编码</h3>
          </div>
          <div class="raw-data">
            <div class="raw-item" v-if="dream.charactersRaw">
              <span class="raw-label">人物编码:</span>
              <code>{{ dream.charactersRaw }}</code>
            </div>
            <div class="raw-item" v-if="dream.emotionsRaw">
              <span class="raw-label">情感编码:</span>
              <code>{{ dream.emotionsRaw }}</code>
            </div>
          </div>
        </GlassCard>
      </div>
    </div>
  </div>

  <!-- 加载状态 -->
  <div v-else-if="loading" class="loading-state">
    <DreamLoader type="rings" text="加载梦境详情..." />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowRight, Calendar, Document } from '@element-plus/icons-vue'
import { dreamAPI } from '@/api/dream'

const route = useRoute()
const router = useRouter()
const dream = ref(null)
const loading = ref(true)

onMounted(async () => {
  await loadDream()
})

const loadDream = async () => {
  try {
    dream.value = await dreamAPI.getDreamById(route.params.id)
  } catch (error) {
    console.error('加载梦境失败:', error)
  } finally {
    loading.value = false
  }
}

const parseJsonArray = (str) => {
  try {
    return JSON.parse(str)
  } catch {
    return []
  }
}

const getAgeLabel = (age) => ({ A: '成人', C: '儿童', O: '老年' }[age] || age)

const goBack = () => router.back()

const goToAnalysis = () => {
  router.push(`/dreams/${dream.value.id}/analyze`)
}
</script>

<style scoped>
.dream-detail-unified {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  padding-bottom: 60px;
}

/* 返回按钮 */
.back-button {
  position: fixed;
  top: 120px;
  left: 40px;
  z-index: 100;
}

/* Hero 装饰光斑 */
.hero-orbs {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
  border-radius: 32px;
  z-index: 0;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.2;
  animation: orbFloat 8s ease-in-out infinite;
}

.orb-1 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #FF9A8B, #FFD983);
  top: -50px;
  right: -80px;
}

.orb-2 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, #A5B4FC, #67E8F9);
  bottom: -30px;
  left: -60px;
  animation-delay: 3s;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(15px, -15px) scale(1.1); }
}
.detail-hero {
  text-align: center;
  margin-bottom: 56px;
  padding: 40px 0;
}

.hero-badges {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-bottom: 28px;
  flex-wrap: wrap;
}

.detail-title {
  font-size: 52px;
  line-height: 1.2;
  margin: 0 0 24px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.title-number {
  font-size: 18px;
  color: #94A3B8;
  font-weight: 600;
  letter-spacing: 0.1em;
}

.detail-meta-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  color: #64748B;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.divider {
  color: #CBD5E1;
}

/* 内容网格 */
.content-grid {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 32px;
  align-items: start;
}

/* 主内容区 */
.main-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 2px solid rgba(255, 170, 128, 0.15);
}

.header-icon {
  font-size: 32px;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  color: #0F172A;
}

.report-text {
  font-size: 16px;
  line-height: 2;
  color: #475569;
  text-align: justify;
  white-space: pre-wrap;
}

/* AI卡片 */
.ai-card-container {
  position: relative;
}

.ai-card-shadow {
  position: absolute;
  inset: 10px;
  bottom: -15px;
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.3), rgba(255, 217, 131, 0.2));
  border-radius: 20px;
  filter: blur(16px);
  z-index: -1;
  transition: all 0.4s ease;
}

.ai-card-container:hover .ai-card-shadow {
  inset: 6px;
  bottom: -20px;
  opacity: 1.5;
}

.ai-card {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 20px;
  background: linear-gradient(135deg, rgba(255, 217, 131, 0.15), rgba(255, 183, 147, 0.15));
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.ai-card:hover {
  transform: scale(1.02) translateX(4px);
}

.ai-card-icon {
  font-size: 48px;
}

.ai-card-content {
  flex: 1;
}

.ai-card-content h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 800;
}

.ai-card-content p {
  margin: 0;
  color: #64748B;
  font-size: 14px;
}

.ai-card-arrow {
  font-size: 24px;
  color: #FF6A88;
  transition: transform 0.3s ease;
}

.ai-card:hover .ai-card-arrow {
  transform: translateX(8px);
}

/* 侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: sticky;
  top: 120px;
}

.info-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.info-icon {
  font-size: 24px;
}

.info-header h3 {
  margin: 0;
  font-size: 18px;
  color: #0F172A;
}

/* 标签组 */
.character-tags,
.emotion-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* 原始数据 */
.raw-data {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.raw-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.raw-label {
  font-size: 12px;
  color: #94A3B8;
  font-weight: 600;
}

code {
  background: rgba(241, 245, 249, 0.8);
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 12px;
  color: #64748B;
  font-family: 'Courier New', monospace;
  display: block;
  word-break: break-all;
}

/* 加载状态 */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
}

/* 响应式 */
@media (max-width: 1024px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
  .sidebar {
    position: relative;
    top: 0;
  }
  .back-button {
    position: absolute;
    top: -60px;
    left: 0;
  }
}

@media (max-width: 768px) {
  .detail-title {
    font-size: 36px;
  }
}
</style>
