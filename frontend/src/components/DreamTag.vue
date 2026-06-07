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

/* Types - Plain Effect */
.dream-tag-default.dream-tag-plain {
  background: transparent;
  border: 1px solid rgba(148, 163, 184, 0.3);
  color: #64748B;
}

.dream-tag-primary.dream-tag-plain {
  background: transparent;
  border: 1px solid rgba(255, 154, 139, 0.4);
  color: #FF6A88;
}

.dream-tag-success.dream-tag-plain {
  background: transparent;
  border: 1px solid rgba(16, 185, 129, 0.3);
  color: #059669;
}

.dream-tag-warning.dream-tag-plain {
  background: transparent;
  border: 1px solid rgba(255, 217, 131, 0.4);
  color: #D97706;
}

.dream-tag-danger.dream-tag-plain {
  background: transparent;
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #DC2626;
}

/* Types - Dark Effect */
.dream-tag-default.dream-tag-dark {
  background: #64748B;
  color: white;
}

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
