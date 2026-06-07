<template>
  <div 
    :class="['glass-card', 'apple-liquid-glass', variantClass, sizeClass, { 'glass-card-interactive': interactive }]"
    @mousemove="interactive ? handleMagneticTilt : null"
    @mouseleave="interactive ? resetTilt : null"
    ref="cardRef"
  >
    <div v-if="interactive" class="card-glow-layer"></div>
    <div class="card-content">
      <slot></slot>
    </div>
    <div v-if="shimmer" class="card-shimmer"></div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  variant: {
    type: String,
    default: 'default',
    validator: (v) => ['default', 'elevated', 'flat'].includes(v)
  },
  size: {
    type: String,
    default: 'md',
    validator: (v) => ['sm', 'md', 'lg'].includes(v)
  },
  interactive: {
    type: Boolean,
    default: true
  },
  shimmer: {
    type: Boolean,
    default: true
  }
})

const cardRef = ref(null)

const variantClass = computed(() => `glass-card-${props.variant}`)
const sizeClass = computed(() => `glass-card-${props.size}`)

const handleMagneticTilt = (e) => {
  if (!cardRef.value) return
  
  const card = cardRef.value
  const rect = card.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  const centerX = rect.width / 2
  const centerY = rect.height / 2
  
  const rotateX = ((y - centerY) / centerY) * 8
  const rotateY = ((centerX - x) / centerX) * 8
  
  card.style.transform = `
    perspective(1500px) 
    rotateX(${rotateX}deg) 
    rotateY(${rotateY}deg) 
    scale3d(1.02, 1.02, 1.02)
    translateZ(15px)
  `
}

const resetTilt = () => {
  if (!cardRef.value) return
  cardRef.value.style.transform = 'perspective(1500px) rotateX(0) rotateY(0) scale3d(1, 1, 1) translateZ(0)'
}
</script>

<style scoped>
.glass-card {
  /* Apple liquid glass已经包含所有样式 */
  /* 移除overflow以允许外发光 */
  overflow: visible !important;
}

/* 移除旧的伪元素 - 由apple-liquid-glass处理 */
.glass-card::before,
.glass-card::after {
  display: none;
}

/* Variants - 增强玻璃效果 */
.glass-card-default {
  background: rgba(255, 255, 255, 0.68);
  backdrop-filter: blur(60px) saturate(180%) brightness(105%);
}

.glass-card-elevated {
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(70px) saturate(200%) brightness(108%);
  box-shadow: 
    0 16px 56px rgba(255, 154, 139, 0.14),
    0 4px 16px rgba(255, 255, 255, 0.5) inset,
    0 -4px 12px rgba(255, 217, 131, 0.08) inset,
    0 0 0 1px rgba(255, 255, 255, 0.98) inset;
}

.glass-card-flat {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(50px) saturate(160%) brightness(103%);
  box-shadow: 
    0 4px 20px rgba(255, 154, 139, 0.06),
    0 2px 8px rgba(255, 255, 255, 0.3) inset,
    0 0 0 1px rgba(255, 255, 255, 0.85) inset;
}

/* Sizes */
.glass-card-sm {
  padding: 20px;
  border-radius: 24px;
}

.glass-card-md {
  padding: 32px;
  border-radius: 32px;
}

.glass-card-lg {
  padding: 48px;
  border-radius: 36px;
}

/* Interactive */
.glass-card-interactive {
  cursor: pointer;
}

.glass-card-interactive:hover {
  background: rgba(255, 255, 255, 0.82);
  backdrop-filter: blur(80px) saturate(200%) brightness(108%);
  box-shadow: 
    0 20px 60px rgba(255, 154, 139, 0.16),
    0 4px 16px rgba(255, 255, 255, 0.6) inset,
    0 -4px 16px rgba(255, 217, 131, 0.1) inset,
    0 0 40px rgba(255, 217, 131, 0.12),
    0 0 0 2px rgba(255, 217, 131, 0.3) inset;
  transform: translateY(-2px);
}

.glass-card-interactive:hover::before {
  opacity: 0.9;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.8) 0%,
    rgba(255, 255, 255, 0) 100%
  );
}

/* Glow Layer */
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

.glass-card-interactive:hover .card-glow-layer {
  opacity: 0.6;
}

@keyframes glowRotate {
  0% { background-position: 0% 50%; }
  100% { background-position: 400% 50%; }
}

/* Shimmer */
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
  pointer-events: none;
}

@keyframes shimmer {
  0% { transform: translateX(-100%) translateY(-100%) rotate(45deg); }
  100% { transform: translateX(100%) translateY(100%) rotate(45deg); }
}

.card-content {
  position: relative;
  z-index: 1;
}
</style>
