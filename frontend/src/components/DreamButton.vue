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
  /* Apple liquid glass处理背景和材质 */
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
