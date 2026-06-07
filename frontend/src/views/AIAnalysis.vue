<template>
  <div class="ai-analysis-unified">
    <!-- 返回按钮 -->
    <DreamButton variant="ghost" size="sm" class="back-button breathe" @click="goBack" icon="←">
      返回
    </DreamButton>

    <!-- Hero 区 -->
    <div class="ai-hero">
      <div class="hero-badge breathe">
        <span class="badge-pulse"></span>
        <span>🤖 AI 梦境顾问</span>
      </div>
      <h1 class="ai-hero-title consciousness-text">智能梦境分析</h1>
      <p class="ai-hero-subtitle">运用人工智能深度解读梦境的象征意义与潜意识信息</p>
    </div>

    <!-- 梦境内容预览卡片 -->
    <GlassCard v-if="dreamReport" variant="flat" :interactive="false" class="dream-preview-card">
      <div class="preview-header">
        <div class="preview-icon">📖</div>
        <h3>梦境内容</h3>
        <DreamButton variant="ghost" size="sm" @click="showFullReport = !showFullReport">
          {{ showFullReport ? '收起' : '展开全文' }}
        </DreamButton>
      </div>
      <div class="dream-excerpt-warm">{{ truncateText(dreamReport, 200) }}</div>
      <transition name="expand">
        <div v-show="showFullReport" class="full-report-warm">{{ dreamReport }}</div>
      </transition>
    </GlassCard>

    <!-- 分析结果 / 开始按钮 -->
    <div v-if="!result" class="analyze-action-card">
      <GlassCard variant="elevated" :interactive="false">
        <div class="action-center">
          <div class="action-icon-big time-slow">✨</div>
          <h2 class="consciousness-text">开启 AI 梦境解析</h2>
          <p class="action-desc">AI 将从情感、符号、潜意识三个维度深度解读你的梦境</p>

          <div class="feature-tags-row">
            <span class="feature-pill" v-for="tag in ['情感分析', '符号解读', '个人洞察', '行动建议']" :key="tag">
              {{ tag }}
            </span>
          </div>

          <DreamButton
            variant="primary"
            @click="analyzeDream"
            :loading="analyzing"
            class="analyze-btn"
          >
            <span v-if="!analyzing">🌙 开始分析</span>
            <span v-else>✨ 分析中...</span>
          </DreamButton>
        </div>
      </GlassCard>
    </div>

    <!-- 分析结果展示 -->
    <div v-if="result" class="results-layout">
      <!-- 置信度头部 -->
      <GlassCard variant="elevated" :interactive="false" class="confidence-card">
        <div class="confidence-content">
          <div class="confidence-left">
            <span class="conf-icon">🎯</span>
            <span class="conf-label">分析置信度</span>
          </div>
          <div class="confidence-bar-wrap">
            <div class="confidence-bar">
              <div class="confidence-fill" :style="{ width: result.confidence + '%' }"></div>
            </div>
            <span class="conf-value consciousness-text">{{ result.confidence }}%</span>
          </div>
        </div>
      </GlassCard>

      <!-- 总体分析 -->
      <GlassCard variant="elevated" :interactive="false" class="result-card">
        <div class="result-header">
          <div class="result-icon">📋</div>
          <h3>总体分析</h3>
        </div>
        <p class="result-text">{{ result.overallSummary }}</p>
      </GlassCard>

      <!-- 情感分析 -->
      <GlassCard v-if="result.emotionalAnalysis" variant="flat" :interactive="false" class="result-card">
        <div class="result-header">
          <div class="result-icon">💭</div>
          <h3>情感分析</h3>
        </div>
        <div class="emotion-primary-row">
          <span class="em-label">主要情感</span>
          <DreamTag :type="getEmotionTagType(result.emotionalAnalysis.primary)" effect="dark" size="lg">
            {{ result.emotionalAnalysis.primary }}
          </DreamTag>
        </div>
        <!-- 自定义强度条 -->
        <div class="intensity-wrap">
          <span class="em-label">情感强度</span>
          <div class="intensity-bar">
            <div
              class="intensity-fill"
              :style="{ width: result.emotionalAnalysis.intensity + '%' }"
            ></div>
          </div>
          <span class="intensity-val">{{ result.emotionalAnalysis.intensity }}%</span>
        </div>
        <!-- 次级情感 -->
        <div class="secondary-emotions" v-if="result.emotionalAnalysis.secondary?.length">
          <span class="em-label">次要情感</span>
          <div class="tag-group">
            <DreamTag
              v-for="(em, i) in result.emotionalAnalysis.secondary"
              :key="i"
              type="warning"
              effect="light"
            >{{ em }}</DreamTag>
          </div>
        </div>
        <p class="result-text small-text">{{ result.emotionalAnalysis.explanation }}</p>
      </GlassCard>

      <!-- 符号解读 -->
      <GlassCard v-if="result.symbolInterpretation?.length" variant="flat" :interactive="false" class="result-card">
        <div class="result-header">
          <div class="result-icon">🔮</div>
          <h3>符号解读</h3>
        </div>
        <div class="symbol-list">
          <div
            v-for="(sym, i) in result.symbolInterpretation"
            :key="i"
            class="symbol-item-warm"
          >
            <div class="sym-title">
              <span class="sym-dot"></span>
              <strong>{{ sym.symbol }}</strong>
            </div>
            <p class="sym-meaning">{{ sym.meaning }}</p>
            <p class="sym-context">情境：{{ sym.context }}</p>
          </div>
        </div>
      </GlassCard>

      <!-- 个人洞察 & 建议 双列 -->
      <div class="result-dual">
        <GlassCard v-if="result.personalInsights?.length" variant="flat" :interactive="false">
          <div class="result-header">
            <div class="result-icon">💡</div>
            <h3>个人洞察</h3>
          </div>
          <ul class="warm-list">
            <li v-for="(ins, i) in result.personalInsights" :key="i">{{ ins }}</li>
          </ul>
        </GlassCard>

        <GlassCard v-if="result.suggestions?.length" variant="flat" :interactive="false">
          <div class="result-header">
            <div class="result-icon">🌿</div>
            <h3>行动建议</h3>
          </div>
          <ul class="warm-list">
            <li v-for="(sug, i) in result.suggestions" :key="i">{{ sug }}</li>
          </ul>
        </GlassCard>
      </div>
    </div>

    <!-- AI 对话区 -->
    <GlassCard variant="elevated" :interactive="false" class="chat-glass-card">
      <div class="chat-card-header">
        <div class="chat-header-icon time-slow">💬</div>
        <div>
          <h3>AI 梦境顾问</h3>
          <p class="chat-subtitle">有关于梦境的疑问？问问 AI 顾问吧！</p>
        </div>
      </div>

      <!-- 对话历史 -->
      <div class="chat-history-warm" ref="chatHistoryRef">
        <div v-if="chatHistory.length === 0" class="chat-empty">
          <div class="chat-empty-icon">🌙</div>
          <p>开始你的梦境探索之旅吧</p>
        </div>
        <div
          v-for="(msg, i) in chatHistory"
          :key="i"
          :class="['chat-bubble-row', msg.type]"
        >
          <div class="bubble-avatar" v-if="msg.type === 'answer'">🤖</div>
          <div class="bubble-content" :class="msg.type">
            <p>{{ msg.content }}</p>
            <span class="bubble-time">{{ formatTime(msg.time) }}</span>
          </div>
          <div class="bubble-avatar user-avatar" v-if="msg.type === 'question'">😊</div>
        </div>
      </div>

      <!-- 快捷问题 -->
      <div class="quick-questions">
        <span class="quick-label">快速提问：</span>
        <button
          v-for="(q, i) in quickQuestions"
          :key="i"
          class="quick-btn"
          @click="chatQuestion = q"
        >{{ q }}</button>
      </div>

      <!-- 输入框 -->
      <div class="chat-input-wrap">
        <input
          v-model="chatQuestion"
          class="chat-input-field"
          placeholder="输入你的问题，例如：为什么会做梦？"
          @keyup.enter="askQuestion"
        />
        <button class="chat-send-btn" @click="askQuestion" :disabled="chatLoading">
          <span v-if="!chatLoading">发送 →</span>
          <span v-else>思考中...</span>
        </button>
      </div>
    </GlassCard>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { dreamAPI } from '@/api/dream'

const route = useRoute()
const router = useRouter()
const analyzing = ref(false)
const result = ref(null)
const dreamReport = ref('')
const dreamData = ref(null)
const showFullReport = ref(false)
const chatQuestion = ref('')
const chatLoading = ref(false)
const chatHistory = ref([])
const chatHistoryRef = ref(null)

const quickQuestions = ['为什么会做梦？', '噩梦意味着什么？', '如何更好地记住梦境？', '梦境能预示未来吗？']

onMounted(async () => {
  try {
    const dream = await dreamAPI.getDreamById(route.params.id)
    dreamData.value = dream
    dreamReport.value = dream.report || ''
  } catch (error) {
    console.error('加载梦境数据失败:', error)
    // Fall back to query if available
    if (route.query.report) {
      dreamReport.value = decodeURIComponent(route.query.report)
    }
  }
})

const scrollChatToBottom = async () => {
  await nextTick()
  if (chatHistoryRef.value) {
    chatHistoryRef.value.scrollTop = chatHistoryRef.value.scrollHeight
  }
}

const analyzeDream = async () => {
  analyzing.value = true
  try {
    const response = await axios.post('/api/ai/analyze', {
      dreamId: route.params.id,
      report: dreamReport.value,
      characters: dreamData.value?.charactersCn || '',
      emotions: dreamData.value?.emotionsCn || ''
    })
    result.value = response.data
    ElMessage.success({ message: '✨ 分析完成！', duration: 2000 })
  } catch (error) {
    console.error('分析失败:', error)
    ElMessage.error('分析失败，请稍后重试')
  } finally {
    analyzing.value = false
  }
}

const askQuestion = async () => {
  if (!chatQuestion.value.trim()) return
  chatHistory.value.push({ type: 'question', content: chatQuestion.value, time: new Date() })
  const question = chatQuestion.value
  chatQuestion.value = ''
  chatLoading.value = true
  await scrollChatToBottom()
  try {
    const response = await axios.get('/api/ai/chat', { params: { question } })
    chatHistory.value.push({ type: 'answer', content: response.data.answer, time: new Date() })
    await scrollChatToBottom()
  } catch (error) {
    console.error('对话失败:', error)
    ElMessage.error('对话失败，请稍后重试')
  } finally {
    chatLoading.value = false
  }
}

const goBack = () => router.back()

const getEmotionTagType = (emotion) => {
  if (emotion?.includes('积极')) return 'success'
  if (emotion?.includes('消极')) return 'danger'
  return 'warning'
}

const truncateText = (text, length) => {
  if (!text || text.length <= length) return text
  return text.substring(0, length) + '...'
}

const formatTime = (time) => {
  return new Date(time).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}
</script>

<style scoped>
.ai-analysis-unified {
  max-width: 1000px;
  margin: 0 auto;
  padding-bottom: 80px;
  position: relative;
}

/* 返回按钮 */
.back-button {
  margin-bottom: 32px;
}

/* Hero 区 */
.ai-hero {
  text-align: center;
  margin-bottom: 48px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 12px 28px;
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.15), rgba(255, 182, 193, 0.15));
  border: 2px solid rgba(255, 170, 128, 0.3);
  border-radius: 100px;
  font-size: 14px;
  font-weight: 600;
  color: #FF6A88;
  margin-bottom: 24px;
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.15);
}

.badge-pulse {
  width: 8px;
  height: 8px;
  background: #FF9A8B;
  border-radius: 50%;
  animation: badgePulse 2s ease-in-out infinite;
}

@keyframes badgePulse {
  0%, 100% { opacity: 1; transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 154, 139, 0.7); }
  50% { opacity: 0.7; transform: scale(1.2); box-shadow: 0 0 0 8px rgba(255, 154, 139, 0); }
}

.ai-hero-title {
  font-size: 52px;
  font-weight: 900;
  margin: 0 0 16px 0;
}

.ai-hero-subtitle {
  font-size: 16px;
  color: #64748B;
  margin: 0;
}

/* 梦境预览 */
.dream-preview-card {
  margin-bottom: 32px;
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.preview-icon {
  font-size: 28px;
}

.preview-header h3 {
  margin: 0;
  flex: 1;
  font-size: 20px;
  color: #0F172A;
}

.dream-excerpt-warm {
  color: #475569;
  font-size: 15px;
  line-height: 1.8;
  background: rgba(255, 248, 240, 0.6);
  padding: 16px 20px;
  border-radius: 16px;
  border-left: 4px solid rgba(255, 154, 139, 0.5);
}

.full-report-warm {
  margin-top: 16px;
  color: #475569;
  font-size: 14px;
  line-height: 1.8;
  white-space: pre-wrap;
  background: rgba(255, 248, 240, 0.4);
  padding: 16px 20px;
  border-radius: 16px;
}

.expand-enter-active,
.expand-leave-active {
  transition: all 0.4s ease;
  max-height: 1000px;
  overflow: hidden;
}
.expand-enter-from,
.expand-leave-to {
  max-height: 0;
  opacity: 0;
}

/* 开始分析 */
.analyze-action-card {
  margin-bottom: 32px;
}

.action-center {
  text-align: center;
  padding: 20px;
}

.action-icon-big {
  font-size: 80px;
  margin-bottom: 24px;
  display: inline-block;
  filter: drop-shadow(0 8px 16px rgba(255, 217, 131, 0.4));
}

.action-center h2 {
  font-size: 36px;
  margin: 0 0 16px 0;
}

.action-desc {
  color: #64748B;
  font-size: 16px;
  margin-bottom: 32px;
}

.feature-tags-row {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 40px;
}

.feature-pill {
  padding: 8px 20px;
  background: rgba(255, 217, 131, 0.2);
  border: 1px solid rgba(255, 217, 131, 0.4);
  border-radius: 100px;
  font-size: 14px;
  font-weight: 600;
  color: #FF6A88;
}

.analyze-btn {
  font-size: 18px;
  padding: 16px 48px;
}

/* 结果区 */
.results-layout {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-bottom: 32px;
}

/* 置信度卡片 */
.confidence-card {
  background: linear-gradient(135deg, rgba(255, 217, 131, 0.1), rgba(255, 154, 139, 0.1)) !important;
}

.confidence-content {
  display: flex;
  align-items: center;
  gap: 24px;
}

.confidence-left {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 700;
  color: #0F172A;
  white-space: nowrap;
}

.conf-icon {
  font-size: 28px;
}

.conf-label {
  font-size: 16px;
}

.confidence-bar-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 16px;
}

.confidence-bar {
  flex: 1;
  height: 12px;
  background: rgba(255, 154, 139, 0.15);
  border-radius: 100px;
  overflow: hidden;
}

.confidence-fill {
  height: 100%;
  background: linear-gradient(90deg, #FF9A8B, #FFD983);
  border-radius: 100px;
  transition: width 1s cubic-bezier(0.23, 1, 0.32, 1);
}

.conf-value {
  font-size: 24px;
  font-weight: 900;
  white-space: nowrap;
}

/* 结果卡片通用 */
.result-card {
  width: 100%;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 2px solid rgba(255, 170, 128, 0.15);
}

.result-icon {
  font-size: 28px;
}

.result-header h3 {
  margin: 0;
  font-size: 20px;
  color: #0F172A;
}

.result-text {
  color: #475569;
  font-size: 15px;
  line-height: 1.8;
  margin: 0;
}

.small-text {
  font-size: 13px;
  color: #64748B;
  margin-top: 16px;
}

/* 情感分析 */
.emotion-primary-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.em-label {
  font-size: 14px;
  color: #64748B;
  font-weight: 600;
  white-space: nowrap;
  min-width: 64px;
}

.intensity-wrap {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.intensity-bar {
  flex: 1;
  height: 10px;
  background: rgba(255, 154, 139, 0.15);
  border-radius: 100px;
  overflow: hidden;
}

.intensity-fill {
  height: 100%;
  background: linear-gradient(90deg, #FF9A8B, #FFD983);
  border-radius: 100px;
  transition: width 0.8s cubic-bezier(0.23, 1, 0.32, 1);
}

.intensity-val {
  font-size: 18px;
  font-weight: 700;
  color: #FF6A88;
  min-width: 48px;
}

.secondary-emotions {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.tag-group {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* 符号解读 */
.symbol-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.symbol-item-warm {
  padding: 16px 20px;
  background: rgba(255, 248, 240, 0.6);
  border-left: 4px solid #FF9A8B;
  border-radius: 12px;
}

.sym-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
  font-size: 16px;
  color: #0F172A;
}

.sym-dot {
  width: 10px;
  height: 10px;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  border-radius: 50%;
  flex-shrink: 0;
}

.sym-meaning {
  color: #475569;
  font-size: 14px;
  margin: 0 0 6px 20px;
  line-height: 1.6;
}

.sym-context {
  color: #94A3B8;
  font-size: 13px;
  margin: 0 0 0 20px;
}

/* 洞察建议双列 */
.result-dual {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.warm-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.warm-list li {
  padding: 12px 16px;
  background: rgba(255, 248, 240, 0.6);
  border-radius: 12px;
  color: #475569;
  font-size: 14px;
  line-height: 1.6;
  border-left: 3px solid rgba(255, 154, 139, 0.4);
}

/* 聊天区 */
.chat-glass-card {
  margin-top: 8px;
}

.chat-card-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 2px solid rgba(255, 170, 128, 0.15);
}

.chat-header-icon {
  font-size: 48px;
}

.chat-card-header h3 {
  margin: 0 0 6px 0;
  font-size: 22px;
  color: #0F172A;
}

.chat-subtitle {
  color: #64748B;
  font-size: 14px;
  margin: 0;
}

/* 对话历史 */
.chat-history-warm {
  max-height: 420px;
  overflow-y: auto;
  margin-bottom: 20px;
  padding: 4px;
  scroll-behavior: smooth;
}

.chat-empty {
  text-align: center;
  padding: 48px 20px;
  color: #94A3B8;
}

.chat-empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.chat-empty p {
  font-size: 15px;
  margin: 0;
}

.chat-bubble-row {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  margin-bottom: 20px;
}

.chat-bubble-row.question {
  flex-direction: row-reverse;
}

.bubble-avatar {
  font-size: 28px;
  flex-shrink: 0;
}

.user-avatar {
  align-self: flex-end;
}

.bubble-content {
  max-width: 72%;
  padding: 14px 18px;
  border-radius: 20px;
}

.bubble-content.question {
  background: linear-gradient(135deg, rgba(255, 154, 139, 0.2), rgba(255, 217, 131, 0.15));
  border-bottom-right-radius: 4px;
  border: 1px solid rgba(255, 154, 139, 0.2);
  text-align: right;
}

.bubble-content.answer {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-bottom-left-radius: 4px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.08);
}

.bubble-content p {
  color: #0F172A;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 6px 0;
}

.bubble-time {
  font-size: 11px;
  color: #94A3B8;
}

/* 快捷问题 */
.quick-questions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.quick-label {
  font-size: 13px;
  color: #94A3B8;
  white-space: nowrap;
}

.quick-btn {
  padding: 6px 16px;
  background: rgba(255, 248, 240, 0.8);
  border: 1px solid rgba(255, 154, 139, 0.25);
  border-radius: 100px;
  font-size: 13px;
  color: #FF6A88;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.quick-btn:hover {
  background: rgba(255, 154, 139, 0.15);
  border-color: rgba(255, 154, 139, 0.5);
  transform: translateY(-2px);
}

/* 输入框 */
.chat-input-wrap {
  display: flex;
  gap: 12px;
  align-items: stretch;
}

.chat-input-field {
  flex: 1;
  padding: 14px 20px;
  background: rgba(255, 255, 255, 0.8);
  border: 2px solid rgba(255, 154, 139, 0.2);
  border-radius: 20px;
  font-size: 14px;
  color: #0F172A;
  outline: none;
  transition: all 0.3s ease;
  font-family: inherit;
}

.chat-input-field::placeholder {
  color: #94A3B8;
}

.chat-input-field:focus {
  border-color: rgba(255, 154, 139, 0.5);
  box-shadow: 0 0 0 4px rgba(255, 154, 139, 0.1);
  background: rgba(255, 255, 255, 0.95);
}

.chat-send-btn {
  padding: 14px 28px;
  background: linear-gradient(135deg, #FF9A8B, #FFD983);
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 700;
  color: white;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  white-space: nowrap;
  box-shadow: 0 4px 16px rgba(255, 154, 139, 0.3);
}

.chat-send-btn:hover:not(:disabled) {
  transform: translateY(-3px) scale(1.03);
  box-shadow: 0 8px 24px rgba(255, 154, 139, 0.4);
}

.chat-send-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* 响应式 */
@media (max-width: 768px) {
  .ai-hero-title {
    font-size: 36px;
  }
  .result-dual {
    grid-template-columns: 1fr;
  }
  .chat-input-wrap {
    flex-direction: column;
  }
  .confidence-content {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
