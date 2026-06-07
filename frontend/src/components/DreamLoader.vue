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
