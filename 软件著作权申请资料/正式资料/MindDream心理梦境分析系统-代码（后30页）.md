MindDream心理梦境分析系统 V1.0 源代码文档（后30页）

================================================================================
软件名称：MindDream心理梦境分析系统
版本号：V1.0
代码行数：后30页（共1500行）
================================================================================

文件1：App.vue（前端根组件）
--------------------------------------------------------------------------------
<template>
  <div id="app-root">
    <!-- Spline Background Layer (最底层) -->
    <SplineBackground />

    <!-- Spline Texture Provider + Content Container -->
    <SplineTexture>
      <ArtisticEffects>
        <div id="app">
          <el-container class="app-container">
            <!-- 艺术化顶部导航 -->
            <el-header class="app-header">
              <div class="header-content">
                <div class="logo glow float">
                  <div class="logo-icon">
                    <el-icon :size="36" style="background: linear-gradient(135deg, #FF9A8B, #FFD983); -webkit-background-clip: text; -webkit-text-fill-color: transparent;"><Moon /></el-icon>
                  </div>
                  <h1 class="logo-text warm-gradient">MindDream</h1>
                  <span class="logo-subtitle">梦境分析平台</span>
                </div>

                <el-menu
                  :default-active="activeIndex"
                  mode="horizontal"
                  :ellipsis="false"
                  @select="handleSelect"
                  class="header-menu"
                >
                  <el-menu-item index="/" class="menu-item">
                    <el-icon><DataBoard /></el-icon>
                    <span>仪表板</span>
                  </el-menu-item>
                  <el-menu-item index="/dreams" class="menu-item">
                    <el-icon><Document /></el-icon>
                    <span>梦境库</span>
                  </el-menu-item>
                  <el-menu-item index="/search" class="menu-item">
                    <el-icon><Search /></el-icon>
                    <span>高级搜索</span>
                  </el-menu-item>
                  <el-menu-item index="/statistics" class="menu-item">
                    <el-icon><DataAnalysis /></el-icon>
                    <span>数据分析</span>
                  </el-menu-item>
                </el-menu>
              </div>
            </el-header>

            <!-- 主内容区 -->
            <el-main class="app-main">
              <router-view v-slot="{ Component }">
                <transition name="page" mode="out-in">
                  <component :is="Component" />
                </transition>
              </router-view>
            </el-main>
          </el-container>
        </div>
      </ArtisticEffects>
    </SplineTexture>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Moon, DataBoard, Document, Search, DataAnalysis } from '@element-plus/icons-vue'
import ArtisticEffects from './components/ArtisticEffects.vue'
import SplineBackground from './components/SplineBackground.vue'
import SplineTexture from './components/SplineTexture.vue'

const router = useRouter()
const route = useRoute()
const activeIndex = ref('/')

// Map nested routes to their parent menu item
const getMenuIndex = (path) => {
  const menuPaths = ['/', '/dreams', '/search', '/statistics']
  const match = menuPaths.find(p => p !== '/' && path.startsWith(p))
  return match || (path === '/' ? '/' : '')
}

watch(() => route.path, (newPath) => {
  activeIndex.value = getMenuIndex(newPath)
}, { immediate: true })

const handleSelect = (key) => {
  if (key !== route.path) {
    router.push(key)
  }
}
</script>

<style scoped>
#app-root {
  position: relative;
  min-height: 100vh;
  overflow-x: hidden;
}

.app-container {
  min-height: 100vh;
  position: relative;
  z-index: 1;
}

.app-header {
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(80px) saturate(200%);
  border-bottom: none;
  box-shadow:
    0 4px 32px rgba(100, 116, 139, 0.06),
    0 1px 0 rgba(255, 255, 255, 0.8) inset;
  padding: 0;
  height: 88px;
  position: sticky;
  top: 0;
  z-index: 1000;
  transition: all 0.4s ease;
}

/* 流光彩虹底部边框 */
.app-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg,
    #FF9A8B 0%,
    #FFD983 25%,
    #A8D5BA 50%,
    #A5B4FC 75%,
    #FF9A8B 100%
  );
  background-size: 300% 100%;
  animation: rainbowBorder 6s linear infinite;
  opacity: 0.7;
}

@keyframes rainbowBorder {
  0% { background-position: 0% 50%; }
  100% { background-position: 300% 50%; }
}

.header-content {
  max-width: 1600px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 48px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
  cursor: pointer;
  transition: all 0.4s ease;
}

.logo:hover {
  transform: translateY(-3px);
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow:
    0 4px 16px rgba(255, 154, 139, 0.2),
    0 0 0 1px rgba(255, 217, 131, 0.2) inset;
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  transform-style: preserve-3d;
}

.logo:hover .logo-icon {
  box-shadow:
    0 8px 32px rgba(255, 154, 139, 0.35),
    0 0 0 1px rgba(255, 217, 131, 0.5) inset;
  transform: rotate(-15deg) scale(1.1);
}

.logo-text {
  margin: 0;
  font-size: 32px;
  font-weight: 900;
  letter-spacing: -0.02em;
  background: linear-gradient(135deg, #FF9A8B, #FF6A88, #FFD983);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: warmGradientFlow 6s ease-in-out infinite;
}

@keyframes warmGradientFlow {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.logo-subtitle {
  font-size: 13px;
  color: #64748B;
  font-weight: 500;
  margin-left: -8px;
  opacity: 0.7;
}

.header-menu {
  background: transparent;
  border: none;
  display: flex;
  gap: 8px;
}

:deep(.el-menu-item) {
  color: #475569;
  font-weight: 600;
  font-size: 15px;
  border: none !important;
  border-radius: 16px;
  padding: 12px 24px;
  transition: all 0.5s cubic-bezier(0.165, 0.84, 0.44, 1);
  position: relative;
  overflow: hidden;
  margin: 0 4px;
}

:deep(.el-menu-item)::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(165, 180, 252, 0.08), transparent);
  transition: left 0.6s ease;
}

:deep(.el-menu-item:hover)::before {
  left: 100%;
}

:deep(.el-menu-item:hover) {
  background: rgba(255, 154, 139, 0.1) !important;
  color: #FF6A88 !important;
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(255, 106, 136, 0.12);
}

:deep(.el-menu-item.is-active) {
  color: #FF6A88 !important;
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.18) 0%, rgba(255, 217, 131, 0.12) 100%) !important;
  box-shadow:
    0 4px 20px rgba(255, 106, 136, 0.15),
    0 0 0 1px rgba(255, 154, 139, 0.25) inset;
  font-weight: 700;
}

.app-main {
  padding: 48px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
  position: relative;
  z-index: 1;
}
</style>

文件2：GlassCard.vue（液态玻璃卡片组件）
--------------------------------------------------------------------------------
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
  overflow: visible !important;
}

.glass-card::before,
.glass-card::after {
  display: none;
}

/* Variants */
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

文件3：DreamButton.vue（按钮组件）
--------------------------------------------------------------------------------
<template>
  <button
    :class="['dream-button', 'apple-liquid-glass', variantClass, sizeClass, { 'dream-button-loading': loading, 'apple-glass-button': variant === 'primary' }]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <span v-if="loading" class="button-spinner"></span>
    <span v-if="icon && !loading" class="button-icon">{{ icon }}</span>
    <span class="button-text"><slot></slot></span>
    <span v-if="arrow" class="button-arrow">→</span>
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  variant: {
    type: String,
    default: 'primary',
    validator: (v) => ['primary', 'secondary', 'ghost'].includes(v)
  },
  size: {
    type: String,
    default: 'md',
    validator: (v) => ['sm', 'md', 'lg'].includes(v)
  },
  icon: String,
  arrow: Boolean,
  loading: Boolean,
  disabled: Boolean
})

const emit = defineEmits(['click'])

const variantClass = computed(() => `dream-button-${props.variant}`)
const sizeClass = computed(() => `dream-button-${props.size}`)

const handleClick = (e) => {
  if (!props.loading && !props.disabled) {
    emit('click', e)
  }
}
</script>

<style scoped>
.dream-button {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 32px;
  border: none;
  border-radius: 20px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: visible;
}

/* Primary */
.dream-button-primary {
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  color: white;
  box-shadow: 0 8px 24px rgba(255, 154, 139, 0.4);
}

.dream-button-primary:hover:not(:disabled) {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 16px 40px rgba(255, 154, 139, 0.5);
}

/* Secondary */
.dream-button-secondary {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  color: #FF6A88;
  border: 2px solid rgba(255, 154, 139, 0.3);
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.15);
}

.dream-button-secondary:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.95);
  border-color: rgba(255, 154, 139, 0.5);
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(255, 154, 139, 0.25);
}

/* Ghost */
.dream-button-ghost {
  background: transparent;
  color: #64748B;
  border: 2px solid rgba(100, 116, 139, 0.2);
}

.dream-button-ghost:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.4);
  border-color: rgba(255, 154, 139, 0.3);
  color: #FF6A88;
}

/* Sizes */
.dream-button-sm {
  padding: 10px 24px;
  font-size: 14px;
  border-radius: 20px;
  gap: 8px;
}

.dream-button-md {
  padding: 14px 32px;
  font-size: 16px;
  border-radius: 24px;
  gap: 10px;
}

.dream-button-lg {
  padding: 18px 40px;
  font-size: 18px;
  border-radius: 28px;
  gap: 12px;
}

/* States */
.dream-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none !important;
}

.dream-button-loading {
  pointer-events: none;
}

/* Elements */
.button-text {
  position: relative;
  z-index: 1;
}

.button-icon {
  font-size: 1.2em;
  position: relative;
  z-index: 1;
}

.button-arrow {
  font-size: 1.3em;
  transition: transform 0.3s ease;
  position: relative;
  z-index: 1;
}

.dream-button:hover .button-arrow {
  transform: translateX(4px);
}

.button-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  position: relative;
  z-index: 1;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>

文件4：DreamInput.vue（输入框组件）
--------------------------------------------------------------------------------
<template>
  <div :class="['dream-input-wrapper', 'apple-glass-input', { 'dream-input-focused': isFocused, 'dream-input-error': error, 'dream-input-success': success }]">
    <div v-if="label" class="dream-input-label">{{ label }}</div>
    <div class="dream-input-container">
      <span v-if="prefix" class="dream-input-prefix">{{ prefix }}</span>
      <input
        v-if="type !== 'textarea'"
        :type="type"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :class="['dream-input', { 'has-prefix': prefix, 'has-suffix': suffix }]"
        @input="handleInput"
        @focus="isFocused = true"
        @blur="isFocused = false"
      />
      <textarea
        v-else
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :rows="rows"
        :class="['dream-textarea', { 'has-prefix': prefix, 'has-suffix': suffix }]"
        @input="handleInput"
        @focus="isFocused = true"
        @blur="isFocused = false"
      ></textarea>
      <span v-if="suffix" class="dream-input-suffix">{{ suffix }}</span>
      <span v-if="clearable && modelValue" class="dream-input-clear" @click="handleClear">✕</span>
    </div>
    <div v-if="error || success || hint" class="dream-input-message">
      <span v-if="error" class="message-error">{{ error }}</span>
      <span v-else-if="success" class="message-success">{{ success }}</span>
      <span v-else class="message-hint">{{ hint }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  modelValue: [String, Number],
  type: {
    type: String,
    default: 'text'
  },
  label: String,
  placeholder: String,
  prefix: String,
  suffix: String,
  clearable: Boolean,
  disabled: Boolean,
  error: String,
  success: String,
  hint: String,
  rows: {
    type: Number,
    default: 3
  }
})

const emit = defineEmits(['update:modelValue'])

const isFocused = ref(false)

const handleInput = (e) => {
  emit('update:modelValue', e.target.value)
}

const handleClear = () => {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.dream-input-wrapper {
  width: 100%;
}

.dream-input-label {
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
}

.dream-input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.dream-input,
.dream-textarea {
  width: 100%;
  padding: 14px 20px;
  background: transparent;
  border: none;
  border-radius: 20px;
  font-size: 15px;
  color: #0F172A;
  transition: all 0.4s ease;
  outline: none;
  box-shadow: none;
}

.dream-input.has-prefix {
  padding-left: 48px;
}

.dream-input.has-suffix {
  padding-right: 48px;
}

.dream-textarea {
  resize: vertical;
  font-family: inherit;
  line-height: 1.6;
}

.dream-input:focus,
.dream-textarea:focus {
  outline: none;
}

.dream-input:disabled,
.dream-textarea:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.dream-input-prefix,
.dream-input-suffix {
  position: absolute;
  font-size: 18px;
  color: #94A3B8;
  pointer-events: none;
}

.dream-input-prefix {
  left: 18px;
}

.dream-input-suffix {
  right: 18px;
}

.dream-input-clear {
  position: absolute;
  right: 18px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(100, 116, 139, 0.1);
  border-radius: 50%;
  font-size: 12px;
  color: #64748B;
  cursor: pointer;
  transition: all 0.3s ease;
}

.dream-input-clear:hover {
  background: rgba(255, 106, 136, 0.2);
  color: #FF6A88;
}

.dream-input-message {
  margin-top: 8px;
  font-size: 13px;
}

.message-error {
  color: #EF4444;
}

.message-success {
  color: #10B981;
}

.message-hint {
  color: #94A3B8;
}
</style>

文件5：DreamTag.vue（标签组件）
--------------------------------------------------------------------------------
<template>
  <span :class="['dream-tag', typeClass, sizeClass, effectClass]">
    <span v-if="icon" class="tag-icon">{{ icon }}</span>
    <span class="tag-text"><slot></slot></span>
    <span v-if="closable" class="tag-close" @click.stop="handleClose">✕</span>
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    default: 'default',
    validator: (v) => ['default', 'primary', 'success', 'warning', 'danger'].includes(v)
  },
  size: {
    type: String,
    default: 'md',
    validator: (v) => ['sm', 'md', 'lg'].includes(v)
  },
  effect: {
    type: String,
    default: 'light',
    validator: (v) => ['light', 'plain', 'dark'].includes(v)
  },
  icon: String,
  closable: Boolean
})

const emit = defineEmits(['close'])

const typeClass = computed(() => `dream-tag-${props.type}`)
const sizeClass = computed(() => `dream-tag-${props.size}`)
const effectClass = computed(() => `dream-tag-${props.effect}`)

const handleClose = () => {
  emit('close')
}
</script>

<style scoped>
.dream-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border-radius: 14px;
  font-size: 13px;
  font-weight: 600;
  border: none;
  transition: all 0.3s cubic-bezier(0.4, 0.0, 0.2, 1);
  cursor: default;
}

.dream-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 154, 139, 0.2);
}

/* Types - Light Effect */
.dream-tag-default.dream-tag-light {
  background: rgba(148, 163, 184, 0.15);
  color: #64748B;
}

.dream-tag-primary.dream-tag-light {
  background: rgba(255, 154, 139, 0.2);
  color: #FF6A88;
}

.dream-tag-success.dream-tag-light {
  background: rgba(16, 185, 129, 0.15);
  color: #059669;
}

.dream-tag-warning.dream-tag-light {
  background: rgba(255, 217, 131, 0.25);
  color: #D97706;
}

.dream-tag-danger.dream-tag-light {
  background: rgba(239, 68, 68, 0.15);
  color: #DC2626;
}

/* Types - Dark Effect */
.dream-tag-primary.dream-tag-dark {
  background: linear-gradient(135deg, #FF9A8B, #FF6A88);
  color: white;
}

.dream-tag-success.dream-tag-dark {
  background: #10B981;
  color: white;
}

.dream-tag-warning.dream-tag-dark {
  background: #FFD983;
  color: #78350F;
}

.dream-tag-danger.dream-tag-dark {
  background: #EF4444;
  color: white;
}

/* Sizes */
.dream-tag-sm {
  padding: 4px 10px;
  font-size: 12px;
  border-radius: 12px;
  gap: 4px;
}

.dream-tag-md {
  padding: 6px 14px;
  font-size: 13px;
  border-radius: 14px;
  gap: 6px;
}

.dream-tag-lg {
  padding: 8px 18px;
  font-size: 14px;
  border-radius: 16px;
  gap: 8px;
}

/* Elements */
.tag-icon {
  font-size: 1.1em;
}

.tag-text {
  line-height: 1;
}

.tag-close {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  font-size: 10px;
  opacity: 0.6;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-close:hover {
  opacity: 1;
  background: rgba(0, 0, 0, 0.1);
}
</style>

文件6：DreamLoader.vue（加载动画组件）
--------------------------------------------------------------------------------
<template>
  <div :class="['dream-loader', `dream-loader-${type}`]">
    <!-- Flower Spinner -->
    <div v-if="type === 'spinner'" class="loader-flower">
      <div class="petal" v-for="i in 6" :key="i"></div>
    </div>

    <!-- Rings -->
    <div v-else-if="type === 'rings'" class="loader-rings">
      <div class="ring"></div>
      <div class="ring"></div>
      <div class="ring"></div>
    </div>

    <!-- Wave Progress -->
    <div v-else-if="type === 'wave'" class="loader-wave">
      <div class="wave-bar" v-for="i in 5" :key="i"></div>
    </div>

    <!-- Pulse -->
    <div v-else-if="type === 'pulse'" class="loader-pulse">
      <div class="pulse-dot"></div>
      <div class="pulse-ring"></div>
    </div>

    <div v-if="text" class="loader-text">{{ text }}</div>
  </div>
</template>

<script setup>
defineProps({
  type: {
    type: String,
    default: 'spinner',
    validator: (v) => ['spinner', 'rings', 'wave', 'pulse'].includes(v)
  },
  text: String
})
</script>

<style scoped>
.dream-loader {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

/* Flower Spinner */
.loader-flower {
  position: relative;
  width: 60px;
  height: 60px;
  animation: rotate 3s linear infinite;
}

.petal {
  position: absolute;
  width: 20px;
  height: 20px;
  background: linear-gradient(135deg, #FFB6C1, #FF9A8B);
  border-radius: 50% 0;
  top: 50%;
  left: 50%;
  transform-origin: 0 0;
}

.petal:nth-child(1) { transform: rotate(0deg) translateX(20px); }
.petal:nth-child(2) { transform: rotate(60deg) translateX(20px); }
.petal:nth-child(3) { transform: rotate(120deg) translateX(20px); }
.petal:nth-child(4) { transform: rotate(180deg) translateX(20px); }
.petal:nth-child(5) { transform: rotate(240deg) translateX(20px); }
.petal:nth-child(6) { transform: rotate(300deg) translateX(20px); }

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Rings */
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

/* Wave */
.loader-wave {
  display: flex;
  gap: 8px;
  align-items: flex-end;
  height: 50px;
}

.wave-bar {
  width: 8px;
  height: 100%;
  background: linear-gradient(180deg, #FF9A8B, #FFD983);
  border-radius: 4px;
  animation: waveMotion 1s ease-in-out infinite;
}

.wave-bar:nth-child(1) { animation-delay: 0s; }
.wave-bar:nth-child(2) { animation-delay: 0.1s; }
.wave-bar:nth-child(3) { animation-delay: 0.2s; }
.wave-bar:nth-child(4) { animation-delay: 0.3s; }
.wave-bar:nth-child(5) { animation-delay: 0.4s; }

@keyframes waveMotion {
  0%, 100% { transform: scaleY(0.3); }
  50% { transform: scaleY(1); }
}

/* Pulse */
.loader-pulse {
  position: relative;
  width: 60px;
  height: 60px;
}

.pulse-dot {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 20px;
  height: 20px;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  border-radius: 50%;
}

.pulse-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 20px;
  height: 20px;
  border: 3px solid #FF9A8B;
  border-radius: 50%;
  animation: pulsate 2s ease-out infinite;
}

@keyframes pulsate {
  0% {
    width: 20px;
    height: 20px;
    opacity: 1;
  }
  100% {
    width: 60px;
    height: 60px;
    opacity: 0;
  }
}

/* Text */
.loader-text {
  font-size: 15px;
  color: #64748B;
  font-weight: 500;
}
</style>

文件7：package.json（前端依赖配置）
--------------------------------------------------------------------------------
{
  "name": "minddream-frontend",
  "version": "1.0.0",
  "type": "module",
  "description": "MindDream 心理梦境分析平台前端",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "@element-plus/icons-vue": "^2.3.1",
    "@splinetool/runtime": "^1.12.33",
    "axios": "^1.6.5",
    "echarts": "^5.4.3",
    "element-plus": "^2.5.4",
    "vue": "^3.4.15",
    "vue-router": "^4.2.5"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.3",
    "vite": "^5.0.11"
  }
}

文件8：pom.xml（后端Maven配置）
--------------------------------------------------------------------------------
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
        <relativePath/>
    </parent>

    <groupId>com.minddream</groupId>
    <artifactId>minddream-backend</artifactId>
    <version>1.0.0</version>
    <name>MindDream Backend</name>
    <description>心理梦境分析平台后端服务</description>

    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <springdoc.version>2.3.0</springdoc.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- MySQL Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- MyBatis Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Gson -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>

        <!-- SpringDoc OpenAPI (Swagger) -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Spring Boot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                    <jvmArguments>-Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8</jvmArguments>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

================================================================================
后30页代码结束
================================================================================
