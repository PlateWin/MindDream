<template>
  <div class="stats-page">
    <!-- Hero -->
    <div class="stats-hero">
      <h1>📊 数据统计</h1>
      <p>梦境世界的多维可视化分析</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards" v-if="!loading">
      <div class="stat-card" v-for="card in cards" :key="card.label">
        <div class="card-icon">{{ card.icon }}</div>
        <div class="card-value">{{ card.value }}</div>
        <div class="card-label">{{ card.label }}</div>
      </div>
    </div>

    <!-- ===== 情感光谱 ===== -->
    <div class="section" v-if="!loading">
      <h2>🌈 情感光谱分析</h2>
      <div class="chart-row">
        <div class="chart-box">
          <h3>🎯 情感维度雷达</h3>
          <div ref="radarChart" class="chart"></div>
        </div>
        <div class="chart-box">
          <h3>📊 情感频次分布</h3>
          <div ref="emotionBarChart" class="chart"></div>
        </div>
      </div>
    </div>

    <!-- ===== 性别×情感 ===== -->
    <div class="section" v-if="!loading">
      <h2>👫 性别 × 情感交叉分析</h2>
      <div class="chart-row">
        <div class="chart-box chart-box-wide">
          <h3>🔥 性别·情感对比</h3>
          <div ref="genderCrossChart" class="chart"></div>
        </div>
        <div class="chart-box">
          <h3>👤 性别分布</h3>
          <div ref="genderPieChart" class="chart"></div>
        </div>
      </div>
    </div>

    <!-- ===== 年龄分析 ===== -->
    <div class="section" v-if="!loading">
      <h2>📅 年龄分层分析</h2>
      <div class="chart-row">
        <div class="chart-box chart-box-wide">
          <h3>📊 年龄组分布</h3>
          <div ref="ageBarChart" class="chart"></div>
        </div>
        <div class="chart-box">
          <h3>🍩 年龄占比</h3>
          <div ref="agePieChart" class="chart"></div>
        </div>
      </div>
    </div>

    <!-- ===== 角色分布 ===== -->
    <div class="section" v-if="!loading">
      <h2>👥 角色分布 TOP 10</h2>
      <div class="chart-box chart-box-full">
        <div ref="characterChart" class="chart chart-wide"></div>
      </div>
    </div>

    <!-- 加载 -->
    <div v-if="loading" class="loading">加载中...</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { statisticsAPI } from '@/api/dream'

const loading = ref(true)
const cards = ref([])
const chartData = ref({})

// Chart refs
const radarChart = ref(null)
const emotionBarChart = ref(null)
const genderCrossChart = ref(null)
const genderPieChart = ref(null)
const ageBarChart = ref(null)
const agePieChart = ref(null)
const characterChart = ref(null)

const allInstances = []
let resizeHandler = null

onMounted(async () => {
  try {
    const [overview, gender, age, spectrum, genderCross, characters] = await Promise.all([
      statisticsAPI.getOverview(),
      statisticsAPI.getGenderDistribution(),
      statisticsAPI.getAgeDistribution(),
      statisticsAPI.getEmotionSpectrum(),
      statisticsAPI.getGenderEmotionCross(),
      statisticsAPI.getCharacterDistribution()
    ])

    cards.value = [
      { icon: '🌙', value: overview.totalDreams?.toLocaleString(), label: '总梦境数' },
      { icon: '👥', value: overview.totalDreamers, label: '梦境者' },
      { icon: '💭', value: Object.keys(spectrum?.emotions || {}).length, label: '情感类型' }
    ]

    chartData.value = { gender, age, spectrum, genderCross, characters }
  } catch (e) {
    console.error('加载失败:', e)
  }

  loading.value = false

  // Wait for DOM update then init charts
  await nextTick()
  initAllCharts()

  resizeHandler = () => allInstances.forEach(i => i?.resize())
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  allInstances.forEach(i => i?.dispose())
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
})

function nextTick() {
  return new Promise(resolve => setTimeout(resolve, 50))
}

function initAllCharts() {
  if (radarChart.value) initRadar()
  if (emotionBarChart.value) initEmotionBar()
  if (genderCrossChart.value) initGenderCross()
  if (genderPieChart.value) initGenderPie()
  if (ageBarChart.value) initAgeBar()
  if (agePieChart.value) initAgePie()
  if (characterChart.value) initCharacter()
}

function initRadar() {
  const inst = echarts.init(radarChart.value)
  allInstances.push(inst)
  const dims = chartData.value.spectrum?.dimensions || {}
  const total = chartData.value.spectrum?.totalEmotions || 1
  const keys = Object.keys(dims)
  if (!keys.length) return

  inst.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0, textStyle: { color: '#64748B' } },
    radar: {
      radius: '65%',
      indicator: keys.map(k => ({ name: k, max: 100 })),
      axisName: { color: '#475569', fontSize: 13 }
    },
    series: [{
      type: 'radar',
      data: [{
        value: Object.values(dims).map(v => +((v / total) * 100).toFixed(1)),
        name: '情感维度',
        areaStyle: { color: 'rgba(255,154,139,0.3)' },
        lineStyle: { color: '#FF9A8B', width: 2 },
        itemStyle: { color: '#FF9A8B' }
      }]
    }]
  })
}

function initEmotionBar() {
  const inst = echarts.init(emotionBarChart.value)
  allInstances.push(inst)
  const emotions = chartData.value.spectrum?.emotions || {}
  const data = Object.entries(emotions).sort((a, b) => b[1] - a[1])
  if (!data.length) return

  inst.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '8%', bottom: '3%', top: '5%', containLabel: true },
    xAxis: { type: 'value', show: false },
    yAxis: {
      type: 'category',
      data: data.map(d => d[0]).reverse(),
      axisLabel: { color: '#64748B', fontSize: 13 },
      axisTick: { show: false },
      axisLine: { show: false }
    },
    series: [{
      type: 'bar',
      data: data.map(d => d[1]).reverse(),
      itemStyle: { borderRadius: [0, 12, 12, 0], color: '#FF9A8B' },
      barWidth: '55%',
      label: { show: true, position: 'right', color: '#64748B', formatter: '{c}次' }
    }]
  })
}

function initGenderCross() {
  const inst = echarts.init(genderCrossChart.value)
  allInstances.push(inst)
  const cross = chartData.value.genderCross || {}
  const maleEmo = cross['男性'] || {}
  const femaleEmo = cross['女性'] || {}
  const all = {}
  Object.entries(maleEmo).forEach(([k, v]) => all[k] = (all[k] || 0) + v)
  Object.entries(femaleEmo).forEach(([k, v]) => all[k] = (all[k] || 0) + v)
  const topEmotions = Object.entries(all).sort((a, b) => b[1] - a[1]).slice(0, 8).map(e => e[0])

  inst.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    legend: { data: ['男性', '女性'], textStyle: { color: '#64748B' }, top: 0 },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      data: topEmotions,
      axisLabel: { color: '#64748B' },
      axisTick: { show: false }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#F1F5F9' } } },
    series: [
      {
        name: '男性', type: 'bar',
        data: topEmotions.map(e => maleEmo[e] || 0),
        itemStyle: { borderRadius: [8, 8, 0, 0], color: '#60A5FA' },
        barWidth: '35%', barGap: '20%'
      },
      {
        name: '女性', type: 'bar',
        data: topEmotions.map(e => femaleEmo[e] || 0),
        itemStyle: { borderRadius: [8, 8, 0, 0], color: '#F472B6' },
        barWidth: '35%'
      }
    ]
  })
}

function initGenderPie() {
  const inst = echarts.init(genderPieChart.value)
  allInstances.push(inst)
  const data = Object.entries(chartData.value.gender || {}).map(([k, v]) => ({ name: k, value: v }))

  inst.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
    series: [{
      type: 'pie', radius: ['55%', '82%'],
      itemStyle: { borderRadius: 12, borderColor: '#fff', borderWidth: 3 },
      label: { show: true, position: 'outside', formatter: '{b}\n{d}%', color: '#475569' },
      emphasis: { label: { fontSize: 18, fontWeight: 'bold' } },
      data,
      color: ['#60A5FA', '#F472B6']
    }]
  })
}

function initAgeBar() {
  const inst = echarts.init(ageBarChart.value)
  allInstances.push(inst)
  const order = ['儿童', '青少年', '青年', '成人', '老年']
  const dist = chartData.value.age || {}
  const data = order.filter(a => dist[a] != null).map(a => ({ name: a, value: dist[a] }))

  inst.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '8%', containLabel: true },
    xAxis: {
      type: 'category', data: data.map(d => d.name),
      axisLabel: { color: '#64748B', fontSize: 13 },
      axisTick: { show: false }
    },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#F1F5F9' } } },
    series: [{
      type: 'bar', data: data.map(d => d.value),
      itemStyle: { borderRadius: [12, 12, 0, 0], color: '#FF9A8B' },
      barWidth: '50%',
      label: { show: true, position: 'top', color: '#475569', fontSize: 14, fontWeight: 700, formatter: '{c}人' }
    }]
  })
}

function initAgePie() {
  const inst = echarts.init(agePieChart.value)
  allInstances.push(inst)
  const data = Object.entries(chartData.value.age || {}).map(([k, v]) => ({ name: k, value: v }))

  inst.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
    series: [{
      type: 'pie', radius: ['55%', '82%'],
      itemStyle: { borderRadius: 12, borderColor: '#fff', borderWidth: 3 },
      label: { show: true, position: 'outside', formatter: '{b}\n{d}%', color: '#475569' },
      emphasis: { label: { fontSize: 18, fontWeight: 'bold' } },
      data,
      color: ['#A8D5BA', '#FFD983', '#FFB6C1', '#FF9A8B', '#E0F2FE']
    }]
  })
}

function initCharacter() {
  const inst = echarts.init(characterChart.value)
  allInstances.push(inst)
  const data = Object.entries(chartData.value.characters || {}).sort((a, b) => b[1] - a[1]).slice(0, 10)

  inst.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'none' } },
    grid: { left: '2%', right: '10%', bottom: '3%', top: '5%', containLabel: true },
    xAxis: { type: 'value', show: false },
    yAxis: {
      type: 'category', data: data.map(d => d[0]).reverse(),
      axisLabel: { color: '#64748B', fontSize: 14 },
      axisTick: { show: false }, axisLine: { show: false }
    },
    series: [{
      type: 'bar', data: data.map(d => d[1]).reverse(),
      itemStyle: { borderRadius: [0, 20, 20, 0], color: '#FF9A8B' },
      barWidth: '55%',
      label: { show: true, position: 'right', color: '#64748B', fontSize: 13, formatter: '{c}次' }
    }]
  })
}
</script>

<style scoped>
.stats-page {
  padding: 40px 24px 80px;
  max-width: 1200px;
  margin: 0 auto;
}

.stats-hero {
  text-align: center;
  margin-bottom: 48px;
}

.stats-hero h1 {
  font-size: 48px;
  margin: 0 0 12px;
  color: #0F172A;
}

.stats-hero p {
  font-size: 16px;
  color: #64748B;
  margin: 0;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 56px;
}

.stat-card {
  background: rgba(255,255,255,0.7);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 32px;
  text-align: center;
  border: 1px solid rgba(255,255,255,0.8);
  box-shadow: 0 8px 32px rgba(0,0,0,0.06);
}

.card-icon { font-size: 40px; margin-bottom: 12px; }
.card-value { font-size: 40px; font-weight: 900; color: #0F172A; }
.card-label { font-size: 14px; color: #64748B; margin-top: 4px; }

.section {
  margin-bottom: 56px;
}

.section h2 {
  text-align: center;
  font-size: 28px;
  color: #0F172A;
  margin: 0 0 32px;
}

.chart-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.chart-box {
  background: rgba(255,255,255,0.7);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 28px;
  border: 1px solid rgba(255,255,255,0.8);
  box-shadow: 0 8px 32px rgba(0,0,0,0.06);
}

.chart-box-wide { grid-column: span 1; }
.chart-box-full { grid-column: span 2; }

.chart-box h3 {
  margin: 0 0 16px;
  font-size: 18px;
  color: #334155;
}

.chart { height: 380px; }
.chart-wide { height: 400px; }

.loading {
  text-align: center;
  padding: 100px;
  font-size: 18px;
  color: #94A3B8;
}

@media (max-width: 768px) {
  .stats-cards { grid-template-columns: 1fr; }
  .chart-row { grid-template-columns: 1fr; }
}
</style>
