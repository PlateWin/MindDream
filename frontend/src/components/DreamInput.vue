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
  /* No background/border — the wrapper .apple-glass-input handles it */
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
  /* Focus handled by wrapper .dream-input-focused state */
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

/* Error State */
.dream-input-error .dream-input,
.dream-input-error .dream-textarea {
  border-color: rgba(239, 68, 68, 0.5);
}

.dream-input-error .dream-input:focus,
.dream-input-error .dream-textarea:focus {
  border-color: rgba(239, 68, 68, 0.7);
  box-shadow: 
    0 4px 16px rgba(239, 68, 68, 0.15),
    0 0 20px rgba(239, 68, 68, 0.1);
}

/* Success State */
.dream-input-success .dream-input,
.dream-input-success .dream-textarea {
  border-color: rgba(16, 185, 129, 0.5);
}

.dream-input-success .dream-input:focus,
.dream-input-success .dream-textarea:focus {
  border-color: rgba(16, 185, 129, 0.7);
  box-shadow: 
    0 4px 16px rgba(16, 185, 129, 0.15),
    0 0 20px rgba(16, 185, 129, 0.1);
}
</style>
