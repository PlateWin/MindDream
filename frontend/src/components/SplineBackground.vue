<template>
  <div class="spline-background-layer">
    <!-- CSS背景，无需canvas -->
    <div class="blob-container">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>
  </div>
</template>

<script setup>
// 纯CSS实现，无JavaScript阻塞
</script>

<style scoped>
.spline-background-layer {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -2;
  pointer-events: none;
  overflow: hidden;
  opacity: 0.4;
}

.blob-container {
  width: 100%;
  height: 100%;
  position: relative;
}

.blob {
  position: absolute;
  border-radius: 40% 60% 70% 30% / 40% 50% 60% 50%;
  filter: blur(80px);
  opacity: 0.6;
  animation: blobFloat 20s ease-in-out infinite;
}

.blob-1 {
  width: 500px;
  height: 500px;
  top: 20%;
  left: 10%;
  background: radial-gradient(circle, rgba(255, 217, 131, 0.3), transparent);
  animation-delay: 0s;
}

.blob-2 {
  width: 600px;
  height: 600px;
  top: 50%;
  right: 10%;
  background: radial-gradient(circle, rgba(255, 183, 147, 0.3), transparent);
  animation-delay: 7s;
}

.blob-3 {
  width: 450px;
  height: 450px;
  bottom: 20%;
  left: 40%;
  background: radial-gradient(circle, rgba(168, 213, 186, 0.3), transparent);
  animation-delay: 14s;
}

@keyframes blobFloat {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  33% {
    transform: translate(30px, -30px) rotate(120deg);
  }
  66% {
    transform: translate(-30px, 30px) rotate(240deg);
  }
}
</style>
