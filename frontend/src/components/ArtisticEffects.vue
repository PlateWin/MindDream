<template>
  <div>
    <!-- 自定义光标 -->
    <div class="cursor" ref="cursor"></div>
    <div class="cursor-follower" ref="cursorFollower"></div>
    
    <!-- 吉卜力风格环境元素 -->
    <div class="falling-leaves">
      <div v-for="i in 9" :key="`leaf-${i}`" class="leaf"></div>
    </div>
    
    <div class="falling-petals">
      <div v-for="i in 5" :key="`petal-${i}`" class="petal"></div>
    </div>
    
    <div class="floating-clouds">
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
    </div>
    
    <div class="light-particles">
      <div v-for="i in 8" :key="`particle-${i}`" class="light-particle"></div>
    </div>
    
    <!-- 治愈性增强元素（仅抽象元素） -->
    <div class="sunrays"></div>
    
    <div class="ambient-glow"></div>
    
    <div class="twinkling-stars">
      <div v-for="i in 6" :key="`star-${i}`" class="star">✨</div>
    </div>
    
    <div class="dream-sparkles">
      <div v-for="i in 12" :key="`sparkle-${i}`" class="sparkle"></div>
    </div>
    
    <!-- 增强粒子系统 -->
    <div class="floating-orbs">
      <div v-for="i in 8" :key="`orb-${i}`" class="orb"></div>
    </div>
    
    <div class="shimmer-dots">
      <div v-for="i in 15" :key="`dot-${i}`" class="shimmer-dot"></div>
    </div>
    
    <!-- 梦境主题深度元素 -->
    <div class="dream-blobs">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>
    
    <div class="dream-fragments">
      <div v-for="i in 3" :key="`fragment-${i}`" class="fragment"></div>
    </div>
    
    <!-- 粒子背景 -->
    <div class="particles">
      <div v-for="i in 30" :key="i" class="particle" :style="getParticleStyle(i)"></div>
    </div>
    
    <slot />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const cursor = ref(null)
let observer = null
let rafId = null
const cursorFollower = ref(null)
let mouseX = 0
let mouseY = 0

onMounted(() => {
  // 自定义光标追踪
  document.addEventListener('mousemove', handleMouseMove)
  
  // 滚动动画Observer
  observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('revealed')
      }
    })
  }, { threshold: 0.1 })
  
  document.querySelectorAll('.scroll-reveal').forEach(el => {
    observer.observe(el)
  })
})

onBeforeUnmount(() => {
  document.removeEventListener('mousemove', handleMouseMove)
  if (observer) observer.disconnect()
  if (rafId) cancelAnimationFrame(rafId)
})

const handleMouseMove = (e) => {
  mouseX = e.clientX
  mouseY = e.clientY
  
  if (cursor.value) {
    cursor.value.style.left = mouseX + 'px'
    cursor.value.style.top = mouseY + 'px'
  }
  
  if (cursorFollower.value) {
    if (rafId) cancelAnimationFrame(rafId)
    rafId = requestAnimationFrame(() => {
      if (cursorFollower.value) {
        cursorFollower.value.style.left = mouseX + 'px'
        cursorFollower.value.style.top = mouseY + 'px'
      }
    })
  }
}

const getParticleStyle = (i) => {
  const randomX = Math.random() * 100
  const randomY = Math.random() * 150 + 50
  const randomDelay = Math.random() * 8
  const randomSize = 1 + Math.random() * 2
  
  return {
    left: `${randomX}%`,
    bottom: `-${randomY}%`,
    animationDelay: `-${randomDelay}s`,
    width: `${randomSize}px`,
    height: `${randomSize}px`
  }
}
</script>
