<template>
  <div class="not-found-page">
    <div class="not-found-content">
      <div class="not-found-icon">🌙</div>
      <h1 class="not-found-code">404</h1>
      <h2 class="not-found-title consciousness-text">迷失在梦境中</h2>
      <p class="not-found-desc">你访问的页面似乎不存在，也许它只存在于梦境之中...</p>
      <DreamButton variant="primary" @click="goHome" icon="🏠" arrow>
        返回首页
      </DreamButton>
    </div>

    <!-- 装饰元素 -->
    <div class="floating-elements">
      <div class="float-item" v-for="i in 5" :key="i" :style="getFloatStyle(i)">✨</div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goHome = () => router.push('/')

const getFloatStyle = (i) => ({
  left: `${15 + i * 16}%`,
  animationDelay: `${i * 0.8}s`,
  fontSize: `${20 + Math.random() * 20}px`,
  opacity: 0.3 + Math.random() * 0.4
})
</script>

<style scoped>
.not-found-page {
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.not-found-content {
  text-align: center;
  position: relative;
  z-index: 2;
}

.not-found-icon {
  font-size: 96px;
  margin-bottom: 24px;
  animation: iconFloat 4s ease-in-out infinite;
  filter: drop-shadow(0 12px 24px rgba(255, 154, 139, 0.3));
}

@keyframes iconFloat {
  0%, 100% { transform: translateY(0) rotate(-5deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

.not-found-code {
  font-size: 120px;
  font-weight: 900;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 12px 0;
  line-height: 1;
  letter-spacing: -0.04em;
}

.not-found-title {
  font-size: 36px;
  margin: 0 0 16px 0;
}

.not-found-desc {
  font-size: 16px;
  color: #64748B;
  margin: 0 0 40px 0;
  max-width: 400px;
}

.floating-elements {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.float-item {
  position: absolute;
  top: 20%;
  animation: floatAround 6s ease-in-out infinite;
}

@keyframes floatAround {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  25% { transform: translateY(-30px) rotate(10deg); }
  75% { transform: translateY(10px) rotate(-5deg); }
}
</style>
