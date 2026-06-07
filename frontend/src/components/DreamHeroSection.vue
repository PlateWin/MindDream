<template>
  <section class="dream-hero-section">
    <!-- 3个Morphing Blob背景 -->
    <div class="morphing-blobs">
      <div class="mega-blob blob-dream-1"></div>
      <div class="mega-blob blob-dream-2"></div>
      <div class="mega-blob blob-dream-3"></div>
    </div>

    <div class="hero-container">
      <!-- Hero标题区 -->
      <div class="hero-header">
        <div class="hero-badge anti-gravity">
          <span class="badge-pulse"></span>
          <span class="badge-text consciousness-text">✨ Dream Analytics Platform</span>
        </div>
        <h1 class="hero-title">
          <span class="title-word time-slow" :style="{ animationDelay: '0s' }">探索</span>
          <span class="title-word consciousness-text time-normal" :style="{ animationDelay: '0.2s' }">潜意识</span>
          <span class="title-word time-slow" :style="{ animationDelay: '0.4s' }">的</span>
          <span class="title-word consciousness-text time-fast" :style="{ animationDelay: '0.6s' }">奥秘</span>
        </h1>
        <p class="hero-description time-normal">
          在梦境与现实的边界，解读 <span class="highlight-number">{{ totalDreams?.toLocaleString() }}</span> 个灵魂的呓语
        </p>
      </div>

      <!-- 4列Bento Grid -->
      <div class="bento-hero-grid">
        <!-- 大卡片 - 主要统计 -->
        <div 
          class="bento-card bento-large glass-card memory-ripple"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card1"
        >
          <div class="card-glow-layer"></div>
          <div class="card-content">
            <div class="stat-icon-mega time-slow">🌙</div>
            <div class="stat-value-mega consciousness-text">{{ totalDreams?.toLocaleString() }}</div>
            <div class="stat-label-mega">梦境总数</div>
            <div class="dream-wave">
              <div class="wave-bar" v-for="i in 20" :key="i" :style="getWaveStyle(i)"></div>
            </div>
          </div>
          <div class="card-shimmer-dream"></div>
        </div>

        <!-- 中等卡片 - 梦境者 -->
        <div 
          class="bento-card bento-medium glass-card"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card2"
        >
          <div class="card-glow-layer"></div>
          <div class="card-content">
            <div class="stat-icon-medium anti-gravity">👥</div>
            <div class="stat-value-medium">{{ totalDreamers }}</div>
            <div class="stat-label-medium">梦境者</div>
            <div class="mini-dream-chart">
              <div v-for="i in 7" :key="i" class="dream-bar time-fast" :style="getDreamBarStyle(i)"></div>
            </div>
          </div>
        </div>

        <!-- 小卡片组 -->
        <div 
          class="bento-card bento-small glass-card dream-distortion"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card3"
        >
          <div class="card-glow-layer"></div>
          <div class="card-content-compact">
            <span class="icon-compact time-normal">⚡</span>
            <span class="value-compact consciousness-text">{{ emotionCount }}</span>
            <span class="label-compact">情感类型</span>
          </div>
        </div>

        <div 
          class="bento-card bento-small glass-card dream-distortion"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card4"
        >
          <div class="card-glow-layer"></div>
          <div class="card-content-compact">
            <span class="icon-compact time-fast">🎯</span>
            <span class="value-compact consciousness-text">{{ ageGroups }}</span>
            <span class="label-compact">年龄组</span>
          </div>
        </div>

        <!-- 宽卡片 - CTA -->
        <div 
          class="bento-card bento-wide glass-card deep-dream-glow"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card5"
        >
          <div class="card-glow-layer"></div>
          <div class="cta-content">
            <div class="cta-left">
              <h3 class="cta-title consciousness-text">开启梦境之旅</h3>
              <p class="cta-desc">深入探索潜意识的神秘世界</p>
            </div>
            <button class="cta-button memory-ripple">
              <span>探索梦境</span>
              <span class="button-arrow time-fast">→</span>
            </button>
          </div>
        </div>

        <!-- 特色卡片 - 梦境洞察 -->
        <div 
          class="bento-card bento-feature glass-card surreal-perspective"
          @mousemove="handleMagneticTilt"
          @mouseleave="resetTilt"
          ref="card6"
        >
          <div class="card-glow-layer"></div>
          <div class="feature-content">
            <div class="feature-icon time-slow">💭</div>
            <h4 class="feature-title">AI梦境解析</h4>
            <p class="feature-desc">运用人工智能深度解读梦境符号与隐喻</p>
            <div class="feature-tags">
              <span class="feature-tag" v-for="tag in ['情感分析', '符号解读', '心理洞察']" :key="tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  totalDreams: { type: Number, default: 12580 },
  totalDreamers: { type: Number, default: 3420 },
  emotionCount: { type: Number, default: 47 },
  ageGroups: { type: Number, default: 3 }
})

const card1 = ref(null)
const card2 = ref(null)
const card3 = ref(null)
const card4 = ref(null)
const card5 = ref(null)
const card6 = ref(null)

// 3D磁性倾斜效果
const handleMagneticTilt = (e) => {
  const card = e.currentTarget
  const rect = card.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  const centerX = rect.width / 2
  const centerY = rect.height / 2
  
  // 磁性计算
  const rotateX = ((y - centerY) / centerY) * 8
  const rotateY = ((centerX - x) / centerX) * 8
  
  card.style.transform = `
    perspective(1500px) 
    rotateX(${rotateX}deg) 
    rotateY(${rotateY}deg) 
    scale3d(1.03, 1.03, 1.03)
    translateZ(20px)
  `
}

const resetTilt = (e) => {
  e.currentTarget.style.transform = 'perspective(1500px) rotateX(0) rotateY(0) scale3d(1, 1, 1) translateZ(0)'
}

const getWaveStyle = (i) => ({
  height: `${Math.sin(i * 0.5) * 30 + 50}%`,
  animationDelay: `${i * 0.1}s`
})

const getDreamBarStyle = (i) => ({
  height: `${Math.random() * 60 + 40}%`,
  animationDelay: `${i * 0.15}s`
})
</script>

<style scoped>
.dream-hero-section {
  position: relative;
  min-height: 100vh;
  padding: 80px 20px 120px;
  background: linear-gradient(135deg, #FFF9F5 0%, #FFEFDF 50%, #FFF5ED 100%);
  overflow: hidden;
}

/* 3个Morphing Blob - 40秒慢速循环 */
.morphing-blobs {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.mega-blob {
  position: absolute;
  filter: blur(100px);
  opacity: 0.25;
  animation: megaBlobMorph 40s ease-in-out infinite;
  will-change: transform, border-radius;
}

.blob-dream-1 {
  width: 600px;
  height: 600px;
  top: -10%;
  left: 10%;
  background: radial-gradient(circle, #FFD983, #FF9A8B);
  border-radius: 43% 57% 68% 32% / 45% 52% 48% 55%;
  animation-delay: 0s;
}

.blob-dream-2 {
  width: 700px;
  height: 700px;
  top: 40%;
  right: 5%;
  background: radial-gradient(circle, #FFB6C1, #A5B4FC);
  border-radius: 38% 62% 55% 45% / 52% 38% 62% 48%;
  animation-delay: 13.3s;
}

.blob-dream-3 {
  width: 550px;
  height: 550px;
  bottom: 10%;
  left: 25%;
  background: radial-gradient(circle, #A8D5BA, #E0F2FE);
  border-radius: 52% 48% 42% 58% / 48% 53% 47% 52%;
  animation-delay: 26.6s;
}

@keyframes megaBlobMorph {
  0%, 100% {
    border-radius: 43% 57% 68% 32% / 45% 52% 48% 55%;
    transform: translate(0, 0) rotate(0deg) scale(1);
  }
  25% {
    border-radius: 58% 42% 35% 65% / 52% 48% 52% 48%;
    transform: translate(40px, -50px) rotate(90deg) scale(1.15);
  }
  50% {
    border-radius: 32% 68% 55% 45% / 62% 43% 57% 38%;
    transform: translate(-50px, 30px) rotate(180deg) scale(0.92);
  }
  75% {
    border-radius: 65% 35% 48% 52% / 45% 58% 42% 55%;
    transform: translate(25px, 40px) rotate(270deg) scale(1.08);
  }
}

.hero-container {
  position: relative;
  z-index: 1;
  max-width: 1400px;
  margin: 0 auto;
}

/* Hero Header */
.hero-header {
  text-align: center;
  margin-bottom: 80px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 14px 32px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px) saturate(180%);
  border: 2px solid rgba(255, 217, 131, 0.3);
  border-radius: 100px;
  margin-bottom: 40px;
  box-shadow: 
    0 4px 24px rgba(255, 154, 139, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.badge-pulse {
  width: 10px;
  height: 10px;
  background: #FF9A8B;
  border-radius: 50%;
  animation: badgePulse 2s ease-in-out infinite;
}

@keyframes badgePulse {
  0%, 100% { opacity: 1; transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 154, 139, 0.7); }
  50% { opacity: 0.7; transform: scale(1.2); box-shadow: 0 0 0 10px rgba(255, 154, 139, 0); }
}

.badge-text {
  font-weight: 600;
  font-size: 15px;
}

.hero-title {
  font-size: 72px;
  font-weight: 900;
  line-height: 1.1;
  margin: 0 0 32px 0;
  letter-spacing: -0.03em;
}

.title-word {
  display: inline-block;
  margin: 0 12px;
}

.hero-description {
  font-size: 22px;
  color: #64748B;
  line-height: 1.6;
  max-width: 700px;
  margin: 0 auto;
}

.highlight-number {
  font-size: 32px;
  font-weight: 800;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Bento Grid - 4列 */
.bento-hero-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 28px;
}

.bento-large {
  grid-column: span 2;
  grid-row: span 2;
}

.bento-medium {
  grid-column: span 2;
}

.bento-small {
  grid-column: span 1;
}

.bento-wide {
  grid-column: span 4;
}

.bento-feature {
  grid-column: span 2;
}

/* Glassmorphism卡片 */
.glass-card {
  position: relative;
  padding: 40px;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(40px) saturate(200%);
  border-radius: 36px;
  border: 2px solid rgba(255, 255, 255, 0.6);
  box-shadow: 
    0 12px 48px rgba(255, 154, 139, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.9) inset;
  transition: all 0.8s cubic-bezier(0.23, 1, 0.32, 1);
  transform-style: preserve-3d;
  cursor: pointer;
}

.glass-card:hover {
  background: rgba(255, 255, 255, 0.85);
  box-shadow: 
    0 24px 72px rgba(255, 154, 139, 0.2),
    0 0 60px rgba(255, 217, 131, 0.15),
    0 0 0 2px rgba(255, 217, 131, 0.4) inset;
}

.card-glow-layer {
  position: absolute;
  inset: -2px;
  background: linear-gradient(135deg, #FF9A8B, #FFD983, #FFB6C1, #FF9A8B);
  background-size: 400% 400%;
  border-radius: inherit;
  z-index: -1;
  opacity: 0;
  animation: glowRotate 8s linear infinite;
  transition: opacity 0.6s ease;
}

.glass-card:hover .card-glow-layer {
  opacity: 0.6;
}

@keyframes glowRotate {
  0% { background-position: 0% 50%; }
  100% { background-position: 400% 50%; }
}

/* 大卡片内容 */
.stat-icon-mega {
  font-size: 80px;
  margin-bottom: 24px;
  display: inline-block;
  filter: drop-shadow(0 8px 16px rgba(255, 217, 131, 0.3));
}

.stat-value-mega {
  font-size: 88px;
  font-weight: 900;
  line-height: 1;
  margin-bottom: 16px;
}

.stat-label-mega {
  font-size: 20px;
  color: #64748B;
  font-weight: 600;
  margin-bottom: 32px;
}

.dream-wave {
  display: flex;
  gap: 6px;
  align-items: flex-end;
  height: 80px;
}

.wave-bar {
  flex: 1;
  background: linear-gradient(180deg, rgba(255, 154, 139, 0.6), rgba(255, 217, 131, 0.4));
  border-radius: 8px 8px 0 0;
  animation: waveFlow 3s ease-in-out infinite;
}

@keyframes waveFlow {
  0%, 100% { transform: scaleY(1); opacity: 0.8; }
  50% { transform: scaleY(1.3); opacity: 1; }
}

/* 中卡片 */
.stat-icon-medium {
  font-size: 56px;
  margin-bottom: 20px;
}

.stat-value-medium {
  font-size: 56px;
  font-weight: 800;
  color: #0F172A;
  margin-bottom: 12px;
}

.stat-label-medium {
  font-size: 17px;
  color: #64748B;
  margin-bottom: 24px;
}

.mini-dream-chart {
  display: flex;
  gap: 6px;
  align-items: flex-end;
  height: 70px;
}

.dream-bar {
  flex: 1;
  background: linear-gradient(180deg, #FF9A8B, #FFD983);
  border-radius: 6px 6px 0 0;
  animation: barPulse 2s ease-in-out infinite;
}

@keyframes barPulse {
  0%, 100% { transform: scaleY(1); }
  50% { transform: scaleY(1.2); }
}

/* 小卡片 */
.card-content-compact {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  text-align: center;
}

.icon-compact {
  font-size: 48px;
}

.value-compact {
  font-size: 42px;
  font-weight: 800;
}

.label-compact {
  font-size: 14px;
  color: #64748B;
  font-weight: 600;
}

/* CTA卡片 */
.cta-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 32px;
}

.cta-title {
  font-size: 32px;
  margin: 0 0 12px 0;
  font-weight: 800;
}

.cta-desc {
  font-size: 16px;
  color: #64748B;
  margin: 0;
}

.cta-button {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px 40px;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  border: none;
  border-radius: 24px;
  font-size: 18px;
  font-weight: 700;
  color: white;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(255, 154, 139, 0.4);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.cta-button:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 16px 40px rgba(255, 154, 139, 0.5);
}

.button-arrow {
  font-size: 24px;
  transition: transform 0.3s ease;
}

.cta-button:hover .button-arrow {
  transform: translateX(6px);
}

/* 特色卡片 */
.feature-content {
  text-align: center;
}

.feature-icon {
  font-size: 64px;
  margin-bottom: 24px;
}

.feature-title {
  font-size: 24px;
  margin: 0 0 16px 0;
  font-weight: 700;
}

.feature-desc {
  font-size: 15px;
  color: #64748B;
  line-height: 1.6;
  margin-bottom: 24px;
}

.feature-tags {
  display: flex;
  gap: 10px;
  justify-content: center;
  flex-wrap: wrap;
}

.feature-tag {
  padding: 8px 16px;
  background: rgba(255, 217, 131, 0.2);
  border-radius: 16px;
  font-size: 13px;
  font-weight: 600;
  color: #FF6A88;
}

/* 响应式 */
@media (max-width: 1200px) {
  .bento-hero-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .bento-large, .bento-medium, .bento-wide, .bento-feature {
    grid-column: span 2;
  }
  .hero-title {
    font-size: 56px;
  }
}

@media (max-width: 768px) {
  .bento-hero-grid {
    grid-template-columns: 1fr;
  }
  .bento-large, .bento-small, .bento-medium, .bento-wide, .bento-feature {
    grid-column: span 1;
  }
  .hero-title {
    font-size: 40px;
  }
  .cta-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>
