<template>
  <div class="revolutionary-dashboard">
    <!-- Hero区 - 3D视差效果 -->
    <div class="hero-3d" ref="hero">
      <div class="hero-content-3d">
        <div class="hero-badge-3d warm-breathe">
          <div class="badge-glow"></div>
          <span>✨ Dream Analytics Platform</span>
        </div>
        <h1 class="hero-title-3d">
          <span class="title-word" data-word="探索">探索</span>
          <span class="title-word warm-gradient" data-word="潜意识">潜意识</span>
          <span class="title-word" data-word="的">的</span>
          <span class="title-word warm-gradient" data-word="奥秘">奥秘</span>
        </h1>
        <p class="hero-description-3d">
          深入 <span class="highlight-number">{{ stats.totalDreams?.toLocaleString() || 0 }}</span> 个梦境的神秘世界
        </p>
      </div>
      
      <!-- 3D装饰元素 -->
      <div class="hero-decoration-3d">
        <div class="floating-ring ring-1"></div>
        <div class="floating-ring ring-2"></div>
        <div class="floating-ring ring-3"></div>
      </div>
    </div>

    <!-- 梦境输入区 -->
    <div class="dream-input-section" v-if="!loading">
      <div class="dream-input-card card-3d">
        <div class="input-header">
          <h2>✨ 记录你的梦境</h2>
          <p class="input-description">分享你的梦境,让AI帮你探索潜意识的奥秘</p>
        </div>
        
        <div class="input-form">
          <div class="form-row">
            <div class="form-group">
              <label>姓名</label>
              <input 
                v-model="newDream.name" 
                type="text" 
                placeholder="请输入你的名字或昵称"
                class="dream-form-input"
              />
            </div>
            
            <div class="form-group">
              <label>性别</label>
              <el-select v-model="newDream.gender" placeholder="请选择" class="dream-form-select" clearable>
                <el-option label="♂ 男" value="M" />
                <el-option label="♀ 女" value="F" />
              </el-select>
            </div>
            
            <div class="form-group">
              <label>年龄段</label>
              <el-select v-model="newDream.ageGroup" placeholder="请选择" class="dream-form-select" clearable>
                <el-option label="🧒 儿童 (0-12岁)" value="C" />
                <el-option label="🧑 成人 (13-60岁)" value="A" />
                <el-option label="👴 老年 (60岁以上)" value="O" />
              </el-select>
            </div>
          </div>
          
          <div class="form-group">
            <label>梦境内容</label>
            <textarea 
              v-model="newDream.report" 
              rows="6" 
              placeholder="请详细描述你的梦境...在梦中你看到了什么?感受到了什么?"
              class="dream-form-input dream-textarea"
            ></textarea>
          </div>
          
          <div class="form-actions">
            <button 
              @click="submitDream" 
              :disabled="!canSubmit || submitting"
              class="submit-dream-btn"
            >
              <span v-if="!submitting">🌙 提交梦境</span>
              <span v-else>✨ 提交中...</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Bento Grid布局 -->
    <div class="bento-grid" v-if="!loading">
      <!-- 大卡片 - 主要统计 -->
      <div class="bento-item bento-large card-3d" @mousemove="handleCardMove" @mouseleave="resetCard">
        <div class="bento-content">
          <div class="stat-icon-large">🌙</div>
          <div class="stat-value-large warm-gradient">{{ stats.totalDreams?.toLocaleString() }}</div>
          <div class="stat-label-large">梦境总数</div>
          <div class="stat-progress">
            <div class="progress-bar" :style="{ width: '75%' }"></div>
          </div>
        </div>
        <div class="card-shimmer"></div>
      </div>

      <!-- 中卡片 - 二级统计 -->
      <div class="bento-item bento-medium card-3d" @mousemove="handleCardMove" @mouseleave="resetCard">
        <div class="bento-content">
          <div class="stat-icon-medium">👥</div>
          <div class="stat-value-medium">{{ stats.totalDreamers }}</div>
          <div class="stat-label-medium">梦境者</div>
          <div class="mini-chart">
            <div v-for="(h, i) in miniChartHeights" :key="i" class="chart-bar" :style="{ height: h + '%' }"></div>
          </div>
        </div>
      </div>

      <!-- 小卡片组 -->
      <div class="bento-item bento-small card-3d" @mousemove="handleCardMove" @mouseleave="resetCard">
        <div class="bento-content-compact">
          <span class="small-icon">⚡</span>
          <span class="small-value">{{ Object.keys(stats.emotionDistribution || {}).length }}</span>
          <span class="small-label">情感类型</span>
        </div>
      </div>

      <div class="bento-item bento-small card-3d" @mousemove="handleCardMove" @mouseleave="resetCard">
        <div class="bento-content-compact">
          <span class="small-icon">🎯</span>
          <span class="small-value">{{ Object.keys(stats.ageDistribution || {}).length }}</span>
          <span class="small-label">年龄组</span>
        </div>
      </div>

      <!-- 图表卡片 - 横向 -->
      <div class="bento-item bento-wide card-3d">
        <div class="chart-header-modern">
          <h3>情感光谱分析</h3>
          <div class="chart-legend">
            <span class="legend-dot"></span>
            <span>Top 10 Emotions</span>
          </div>
        </div>
        <div ref="emotionChart" class="modern-chart"></div>
      </div>

      <!-- 圆形图表组 -->
      <div class="bento-item bento-circle card-3d">
        <h4>Gender</h4>
        <div ref="genderChart" class="circle-chart"></div>
      </div>

      <div class="bento-item bento-circle card-3d">
        <h4>Age</h4>
        <div ref="ageChart" class="circle-chart"></div>
      </div>

      <!-- 梦境卡片 - 全宽 -->
      <div class="bento-item bento-full card-3d" v-if="randomDreams.length > 0">
        <div class="dreams-header">
          <h2>✨ 近期梦境</h2>
          <el-button text @click="refreshDreams">
            <el-icon><Refresh /></el-icon> 刷新
          </el-button>
        </div>
        <div class="dreams-carousel">
          <div 
            v-for="(dream, index) in randomDreams" 
            :key="dream.id"
            class="dream-card-modern"
            :style="{ animationDelay: `${index * 0.15}s` }"
            @click="viewDream(dream.id)"
          >
            <div class="dream-number-badge">{{ String(index + 1).padStart(2, '0') }}</div>
            <h3>{{ dream.name }}</h3>
            <p>{{ truncate(dream.report, 100) }}</p>
            <div class="dream-tags">
              <span class="tag-modern">{{ getGenderLabel(dream.gender) }}</span>
              <span class="tag-modern">{{ getAgeLabel(dream.ageGroup) }}</span>
            </div>
            <div class="dream-arrow">→</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-modern">
      <div class="loader-rings">
        <div class="ring"></div>
        <div class="ring"></div>
        <div class="ring"></div>
      </div>
      <p>加载梦境中...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { statisticsAPI, dreamAPI } from '@/api/dream'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(true)
const miniChartHeights = [45, 72, 58, 88, 63, 76, 52]
const stats = ref({})
const randomDreams = ref([])
const hero = ref(null)

const genderChart = ref(null)
const ageChart = ref(null)
const emotionChart = ref(null)

// ECharts instances for cleanup
let genderChartInstance = null
let ageChartInstance = null
let emotionChartInstance = null
let scrollHandler = null
let resizeHandler = null

// 新梦境表单
const newDream = ref({
  name: '',
  gender: '',
  ageGroup: '',
  report: '',
  id: '',
  dreamNumber: null,
  timePeriod: new Date().getFullYear().toString(),
  birthYear: ''
})

const submitting = ref(false)

// 表单验证
const canSubmit = computed(() => {
  return newDream.value.name.trim() !== '' &&
         newDream.value.gender !== '' &&
         newDream.value.ageGroup !== '' &&
         newDream.value.report.trim().length >= 10
})

onMounted(async () => {
  await loadData()
  await loadRandomDreams()
  initCharts()
  loading.value = false
  setupParallax()
  
  // Add resize handler for charts
  resizeHandler = () => {
    genderChartInstance?.resize()
    ageChartInstance?.resize()
    emotionChartInstance?.resize()
  }
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  // Dispose ECharts instances
  genderChartInstance?.dispose()
  ageChartInstance?.dispose()
  emotionChartInstance?.dispose()
  
  // Remove event listeners
  if (scrollHandler) window.removeEventListener('scroll', scrollHandler)
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
})

const loadData = async () => {
  try {
    const [overview, gender, age, emotions] = await Promise.all([
      statisticsAPI.getOverview(),
      statisticsAPI.getGenderDistribution(),
      statisticsAPI.getAgeDistribution(),
      statisticsAPI.getEmotionDistribution()
    ])
    
    stats.value = {
      totalDreams: overview.totalDreams,
      totalDreamers: overview.totalDreamers,
      genderDistribution: gender,
      ageDistribution: age,
      emotionDistribution: emotions
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const loadRandomDreams = async () => {
  try {
    randomDreams.value = await dreamAPI.getRandomDreams(3)
  } catch (error) {
    console.error('加载随机梦境失败:', error)
  }
}

// 提交梦境
const submitDream = async () => {
  if (!canSubmit.value || submitting.value) return
  
  try {
    submitting.value = true
    
    // 生成唯一ID
    newDream.value.id = `dream_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
    
    await dreamAPI.createDream(newDream.value)
    
    ElMessage.success({
      message: '✨ 梦境记录成功!感谢分享你的梦境',
      duration: 3000,
      showClose: true
    })
    
    // 重置表单
    newDream.value = {
      name: '',
      gender: '',
      ageGroup: '',
      report: '',
      id: '',
      dreamNumber: null,
      timePeriod: new Date().getFullYear().toString(),
      birthYear: ''
    }
    
    // 刷新统计数据
    await loadData()
    await loadRandomDreams()
  } catch (error) {
    console.error('提交梦境失败:', error)
    ElMessage.error({
      message: '提交失败,请稍后重试',
      duration: 3000,
      showClose: true
    })
  } finally {
    submitting.value = false
  }
}

const setupParallax = () => {
  scrollHandler = () => {
    if (hero.value) {
      const scrolled = window.pageYOffset
      hero.value.style.transform = `translateY(${scrolled * 0.3}px)`
    }
  }
  window.addEventListener('scroll', scrollHandler, { passive: true })
}

const handleCardMove = (e) => {
  const card = e.currentTarget
  const rect = card.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  const centerX = rect.width / 2
  const centerY = rect.height / 2
  const rotateX = (y - centerY) / 20
  const rotateY = (centerX - x) / 20
  
  card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale3d(1.02, 1.02, 1.02)`
}

const resetCard = (e) => {
  e.currentTarget.style.transform = ''
}

const initCharts = () => {
  if (genderChart.value) initGenderChart()
  if (ageChart.value) initAgeChart()
  if (emotionChart.value) initEmotionChart()
}

const initGenderChart = () => {
  genderChartInstance = echarts.init(genderChart.value)
  const chart = genderChartInstance
  const data = Object.entries(stats.value.genderDistribution || {}).map(([key, value]) => ({
    name: key === 'M' ? '男性' : '女性',
    value
  }))
  
  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['50%', '80%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: { show: true, fontSize: 16, fontWeight: 'bold' }
      },
      data,
      color: ['#FF9A8B', '#FFB6C1']
    }]
  })
}

const initAgeChart = () => {
  ageChartInstance = echarts.init(ageChart.value)
  const chart = ageChartInstance
  const ageMap = { A: '成人', C: '儿童', O: '老年' }
  const data = Object.entries(stats.value.ageDistribution || {}).map(([key, value]) => ({
    name: ageMap[key] || key,
    value
  }))
  
  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['50%', '80%'],
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: { show: true, fontSize: 16, fontWeight: 'bold' }
      },
      data,
      color: ['#FFD983', '#A8D5BA', '#E0F2FE']
    }]
  })
}

const initEmotionChart = () => {
  emotionChartInstance = echarts.init(emotionChart.value)
  const chart = emotionChartInstance
  const data = Object.entries(stats.value.emotionDistribution || {})
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10)
  
  chart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'none' } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '5%', containLabel: true },
    xAxis: {
      type: 'category',
      data: data.map(d => d[0]),
      axisLabel: { color: '#64748B', fontSize: 12, rotate: 0 },
      axisTick: { show: false },
      axisLine: { show: false }
    },
    yAxis: {
      type: 'value',
      show: false
    },
    series: [{
      type: 'bar',
      data: data.map(d => d[1]),
      itemStyle: {
        borderRadius: [20, 20, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#FF9A8B' },
          { offset: 1, color: '#FFD983' }
        ])
      },
      barWidth: '60%',
      emphasis: {
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#FF6A88' },
            { offset: 1, color: '#FFC870' }
          ])
        }
      }
    }]
  })
}

const refreshDreams = async () => {
  await loadRandomDreams()
}

const getGenderLabel = (gender) => gender === 'M' ? '男' : '女'
const getAgeLabel = (age) => ({ A: '成人', C: '儿童', O: '老年' }[age] || age)
const truncate = (text, length) => text?.length > length ? text.substring(0, length) + '...' : text
const viewDream = (id) => router.push(`/dreams/${id}`)
</script>

<style scoped>
.revolutionary-dashboard {
  min-height: 100vh;
  padding-bottom: 80px;
}

/* 3D Hero区 */
.hero-3d {
  position: relative;
  text-align: center;
  padding: 80px 20px;
  margin-bottom: 60px;
  transform-style: preserve-3d;
}

.hero-content-3d {
  position: relative;
  z-index: 2;
}

.hero-badge-3d {
  display: inline-block;
  padding: 12px 32px;
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.15), rgba(255, 217, 131, 0.15));
  border: 2px solid rgba(255, 170, 128, 0.3);
  border-radius: 100px;
  font-size: 14px;
  font-weight: 600;
  color: #FF6A88;
  margin-bottom: 32px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(255, 154, 139, 0.2);
}

.badge-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 217, 131, 0.3), transparent 70%);
  animation: badgeGlow 4s ease-in-out infinite;
}

@keyframes badgeGlow {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.5); opacity: 1; }
}

.hero-title-3d {
  font-size: 64px;
  font-weight: 900;
  margin: 0 0 24px 0;
  line-height: 1.2;
}

.title-word {
  display: inline-block;
  margin: 0 8px;
  animation: wordFloat 3s ease-in-out infinite;
}

.title-word:nth-child(1) { animation-delay: 0s; }
.title-word:nth-child(2) { animation-delay: 0.2s; }
.title-word:nth-child(3) { animation-delay: 0.4s; }
.title-word:nth-child(4) { animation-delay: 0.6s; }

@keyframes wordFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-description-3d {
  font-size: 20px;
  color: #64748B;
}

.highlight-number {
  font-size: 28px;
  font-weight: 800;
  background: linear-gradient(135deg, #FF9A8B, #FF6A88);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* 3D装饰 */
.hero-decoration-3d {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  height: 400px;
  pointer-events: none;
}

.floating-ring {
  position: absolute;
  border: 2px solid;
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0.3;
}

.ring-1 {
  width: 300px;
  height: 300px;
  border-color: #FF9A8B;
  animation: ringFloat1 8s ease-in-out infinite;
}

.ring-2 {
  width: 400px;
  height: 400px;
  border-color: #FFD983;
  animation: ringFloat2 10s ease-in-out infinite;
}

.ring-3 {
  width: 500px;
  height: 500px;
  border-color: #FFB6C1;
  animation: ringFloat3 12s ease-in-out infinite;
}

@keyframes ringFloat1 {
  0%, 100% { transform: translate(-50%, -50%) scale(1) rotate(0deg); }
  50% { transform: translate(-50%, -50%) scale(1.1) rotate(180deg); }
}

@keyframes ringFloat2 {
  0%, 100% { transform: translate(-50%, -50%) scale(1) rotate(0deg); }
  50% { transform: translate(-50%, -50%) scale(0.9) rotate(-180deg); }
}

@keyframes ringFloat3 {
  0%, 100% { transform: translate(-50%, -50%) scale(1) rotate(0deg); }
  50% { transform: translate(-50%, -50%) scale(1.05) rotate(360deg); }
}

/* Bento Grid - 最新布局 */
.bento-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.bento-large {
  grid-column: span 2;
  grid-row: span 2;
}

.bento-medium {
  grid-column: span 2;
  grid-row: span 1;
}

.bento-small {
  grid-column: span 1;
  grid-row: span 1;
}

.bento-wide {
  grid-column: span 4;
  grid-row: span 1;
}

.bento-circle {
  grid-column: span 1;
  grid-row: span 1;
}

.bento-full {
  grid-column: span 4;
}

/* 3D卡片效果 - 增强版 */
.card-3d {
  position: relative;
  padding: 32px;
  border-radius: 32px !important;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(40px) saturate(180%);
  border: 2px solid transparent;
  background-clip: padding-box;
  box-shadow: 
    0 10px 40px rgba(255, 154, 139, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  transition: all 0.6s cubic-bezier(0.23, 1, 0.32, 1);
  overflow: hidden;
  cursor: pointer;
}

/* 渐变边框动画 */
.card-3d::before {
  content: '';
  position: absolute;
  inset: -2px;
  background: linear-gradient(
    45deg,
    #FF9A8B,
    #FFD983,
    #FFB6C1,
    #FF9A8B
  );
  background-size: 300% 300%;
  border-radius: inherit;
  z-index: -1;
  opacity: 0;
  animation: borderGradientMove 6s linear infinite;
  transition: opacity 0.6s ease;
}

.card-3d:hover::before {
  opacity: 1;
}

@keyframes borderGradientMove {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.card-3d:hover {
  box-shadow: 
    0 20px 60px rgba(255, 154, 139, 0.2),
    0 0 0 1px rgba(255, 217, 131, 0.4) inset,
    0 0 40px rgba(255, 170, 128, 0.15);
  background: rgba(255, 255, 255, 0.9);
}

.card-shimmer {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.3) 50%,
    transparent 70%
  );
  transform: rotate(45deg);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%) translateY(-100%) rotate(45deg); }
  100% { transform: translateX(100%) translateY(100%) rotate(45deg); }
}

/* 大卡片内容 - 增强动画 */
.stat-icon-large {
  font-size: 64px;
  margin-bottom: 20px;
  display: inline-block;
  animation: iconBounce 3s ease-in-out infinite;
  filter: drop-shadow(0 4px 12px rgba(255, 217, 131, 0.3));
}

@keyframes iconBounce {
  0%, 100% { transform: translateY(0) rotate(0deg) scale(1); }
  25% { transform: translateY(-10px) rotate(-5deg) scale(1.05); }
  75% { transform: translateY(-5px) rotate(5deg) scale(1.02); }
}

.stat-value-large {
  font-size: 72px;
  font-weight: 900;
  line-height: 1;
  margin-bottom: 12px;
  animation: numberPulse 2s ease-in-out infinite;
}

@keyframes numberPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.stat-label-large {
  font-size: 18px;
  color: #64748B;
  font-weight: 600;
  margin-bottom: 24px;
  animation: labelSlide 1s cubic-bezier(0.23, 1, 0.32, 1);
}

@keyframes labelSlide {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.stat-progress {
  height: 8px;
  background: rgba(241, 245, 249, 0.8);
  border-radius: 100px;
  overflow: hidden;
  position: relative;
}

.stat-progress::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.5),
    transparent
  );
  animation: progressShine 2s infinite;
}

@keyframes progressShine {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #FF9A8B, #FFD983);
  border-radius: 100px;
  animation: progressFill 2s ease-out;
  position: relative;
  overflow: hidden;
}

.progress-bar::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  width: 50%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.4),
    transparent
  );
  animation: progressGlow 2s ease-in-out infinite;
}

@keyframes progressGlow {
  0% { left: -50%; }
  100% { left: 150%; }
}

@keyframes progressFill {
  from { width: 0; }
}

/* 中卡片 - 增强 */
.stat-icon-medium {
  font-size: 48px;
  margin-bottom: 16px;
  display: inline-block;
  animation: iconFloat 4s ease-in-out infinite;
}

@keyframes iconFloat {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-8px) rotate(10deg); }
}

.stat-value-medium {
  font-size: 48px;
  font-weight: 800;
  color: #0F172A;
  margin-bottom: 8px;
  animation: valueGlow 3s ease-in-out infinite;
}

@keyframes valueGlow {
  0%, 100% { text-shadow: 0 0 0 rgba(255, 154, 139, 0); }
  50% { text-shadow: 0 0 20px rgba(255, 154, 139, 0.3); }
}

.stat-label-medium {
  font-size: 15px;
  color: #64748B;
  margin-bottom: 20px;
}

.mini-chart {
  display: flex;
  gap: 4px;
  align-items: flex-end;
  height: 60px;
}

.chart-bar {
  flex: 1;
  background: linear-gradient(180deg, #FF9A8B, #FFD983);
  border-radius: 4px 4px 0 0;
  animation: barGrow 1s ease-out backwards;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.chart-bar::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  animation: barShine 3s infinite;
}

@keyframes barShine {
  0% { transform: translateX(-100%) translateY(-100%) rotate(45deg); }
  100% { transform: translateX(100%) translateY(100%) rotate(45deg); }
}

.card-3d:hover .chart-bar {
  transform: scaleY(1.1);
}

.chart-bar:nth-child(1) { animation-delay: 0.1s; }
.chart-bar:nth-child(2) { animation-delay: 0.2s; }
.chart-bar:nth-child(3) { animation-delay: 0.3s; }
.chart-bar:nth-child(4) { animation-delay: 0.4s; }
.chart-bar:nth-child(5) { animation-delay: 0.5s; }
.chart-bar:nth-child(6) { animation-delay: 0.6s; }
.chart-bar:nth-child(7) { animation-delay: 0.7s; }

@keyframes barGrow {
  from { transform: scaleY(0); }
  to { transform: scaleY(1); }
}

/* 小卡片 - 增强 */
.bento-content-compact {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 12px;
}

.small-icon {
  font-size: 40px;
  animation: iconSpin 6s ease-in-out infinite;
}

@keyframes iconSpin {
  0%, 100% { transform: rotate(0deg) scale(1); }
  50% { transform: rotate(20deg) scale(1.1); }
}

.small-value {
  font-size: 36px;
  font-weight: 800;
  color: #0F172A;
  animation: numberFlicker 2s ease-in-out infinite;
}

@keyframes numberFlicker {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.8; }
}

.small-label {
  font-size: 13px;
  color: #64748B;
  font-weight: 600;
  animation: labelFade 1.5s ease-in-out;
}

@keyframes labelFade {
  from { opacity: 0; }
  to { opacity: 1; }
}

/* 现代图表 */
.chart-header-modern {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header-modern h3 {
  margin: 0;
  font-size: 20px;
}

.chart-legend {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #64748B;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
}

.modern-chart {
  height: 280px;
}

.circle-chart {
  height: 200px;
}

.bento-circle h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  text-align: center;
}

/* 梦境轮播 */
.dreams-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}

.dreams-header h2 {
  margin: 0;
  font-size: 28px;
}

.dreams-carousel {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.dream-card-modern {
  position: relative;
  padding: 24px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 24px;
  border: 1px solid rgba(255, 217, 131, 0.2);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.23, 1, 0.32, 1);
  animation: dreamSlideIn 0.6s cubic-bezier(0.23, 1, 0.32, 1) backwards;
}

@keyframes dreamSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dream-card-modern:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(255, 154, 139, 0.15);
  border-color: rgba(255, 170, 128, 0.4);
}

.dream-number-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  font-size: 32px;
  font-weight: 900;
  color: rgba(255, 154, 139, 0.15);
}

.dream-card-modern h3 {
  font-size: 18px;
  margin: 0 0 12px 0;
}

.dream-card-modern p {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
  margin-bottom: 16px;
}

.dream-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.tag-modern {
  padding: 4px 12px;
  background: rgba(255, 217, 131, 0.2);
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  color: #FF6A88;
}

.dream-arrow {
  font-size: 24px;
  color: #FF9A8B;
  opacity: 0;
  transition: all 0.3s ease;
}

.dream-card-modern:hover .dream-arrow {
  opacity: 1;
  transform: translateX(4px);
}

/* 加载状态 */
.loading-modern {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  gap: 28px;
}

.loader-rings {
  position: relative;
  width: 80px;
  height: 80px;
}

.ring {
  position: absolute;
  border: 3px solid transparent;
  border-top-color: #FF9A8B;
  border-radius: 50%;
  animation: ringRotate 1.5s cubic-bezier(0.5, 0, 0.5, 1) infinite;
}

.ring:nth-child(1) {
  width: 80px;
  height: 80px;
  animation-delay: -0.45s;
}

.ring:nth-child(2) {
  width: 60px;
  height: 60px;
  top: 10px;
  left: 10px;
  border-top-color: #FFD983;
  animation-delay: -0.3s;
}

.ring:nth-child(3) {
  width: 40px;
  height: 40px;
  top: 20px;
  left: 20px;
  border-top-color: #FFB6C1;
  animation-delay: -0.15s;
}

@keyframes ringRotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.warm-gradient {
  background: linear-gradient(135deg, #FF9A8B 0%, #FF6A88 40%, #FF99AC 100%);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: warmGradient 5s ease-in-out infinite;
}

@keyframes warmGradient {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

/* 梦境输入区域 */
.dream-input-section {
  max-width: 1400px;
  margin: 0 auto 60px auto;
  padding: 0 20px;
}

.dream-input-card {
  padding: 48px !important;
  animation: slideInUp 0.8s cubic-bezier(0.23, 1, 0.32, 1);
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.input-header {
  text-align: center;
  margin-bottom: 40px;
}

.input-header h2 {
  font-size: 32px;
  font-weight: 800;
  margin: 0 0 12px 0;
  background: linear-gradient(135deg, #FF9A8B 0%, #FF6A88 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.input-description {
  font-size: 16px;
  color: #64748B;
  margin: 0;
}

.input-form {
  max-width: 900px;
  margin: 0 auto;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.dream-form-input {
  width: 100%;
  height: 48px;
  box-sizing: border-box;
  padding: 0 20px;
  background: #FFFFFF;
  border: none;
  border-radius: 16px;
  font-size: 15px;
  color: #0F172A;
  transition: all 0.4s ease;
  outline: none;
  box-shadow: 
    0 0 0 1.5px rgba(255, 200, 170, 0.25) inset,
    0 2px 8px rgba(255, 154, 139, 0.04);
  font-family: inherit;
}

.dream-form-input:focus {
  box-shadow: 
    0 0 0 2px rgba(255, 154, 139, 0.4) inset,
    0 0 0 5px rgba(255, 154, 139, 0.08),
    0 4px 20px rgba(255, 154, 139, 0.12);
}

.dream-form-input:hover {
  box-shadow: 
    0 0 0 1.5px rgba(255, 170, 128, 0.35) inset,
    0 4px 16px rgba(255, 154, 139, 0.08);
}

.dream-form-input::placeholder {
  color: #94A3B8;
}

.dream-textarea {
  height: auto;
  padding: 14px 20px;
  resize: vertical;
  line-height: 1.6;
  min-height: 150px;
}

/* El-Select sizing handled by global apple-unified.css */
.dream-form-select {
  width: 100%;
}

.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.submit-dream-btn {
  padding: 16px 48px;
  font-size: 16px;
  font-weight: 700;
  color: white;
  background: linear-gradient(135deg, #FF9A8B 0%, #FF6A88 100%);
  border: none;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.23, 1, 0.32, 1);
  box-shadow: 
    0 8px 24px rgba(255, 106, 136, 0.3),
    0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  position: relative;
  overflow: hidden;
}

.submit-dream-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.submit-dream-btn:hover:not(:disabled)::before {
  left: 100%;
}

.submit-dream-btn:hover:not(:disabled) {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 
    0 12px 32px rgba(255, 106, 136, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.8) inset,
    0 0 40px rgba(255, 170, 128, 0.2);
}

.submit-dream-btn:active:not(:disabled) {
  transform: translateY(-1px) scale(0.98);
}

.submit-dream-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* 响应式 */
@media (max-width: 1200px) {
  .bento-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .bento-large, .bento-medium, .bento-wide, .bento-full {
    grid-column: span 2;
  }
  .dreams-carousel {
    grid-template-columns: 1fr;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .dream-input-card {
    padding: 32px !important;
  }
}
</style>
