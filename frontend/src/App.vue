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

:deep(.el-menu-item .el-icon) {
  font-size: 18px;
  margin-right: 8px;
  transition: transform 0.4s ease;
}

:deep(.el-menu-item:hover .el-icon) {
  transform: scale(1.1) rotate(5deg);
  color: #FF6A88;
}

.app-main {
  padding: 48px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
  position: relative;
  z-index: 1;
}

@media (max-width: 1024px) {
  .header-content {
    padding: 0 32px;
  }
  .logo-subtitle {
    display: none;
  }
  .app-main {
    padding: 32px;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 20px;
    flex-direction: column;
    gap: 16px;
    justify-content: center;
  }
  .app-header {
    height: auto;
    padding: 16px 0;
  }
  .logo-text {
    font-size: 24px;
  }
  .logo-icon {
    width: 42px;
    height: 42px;
  }
  .header-menu {
    width: 100%;
    justify-content: center;
  }
  :deep(.el-menu-item) {
    padding: 10px 16px;
    font-size: 14px;
  }
  :deep(.el-menu-item span) {
    display: none;
  }
  .app-main {
    padding: 20px;
  }
}
</style>
