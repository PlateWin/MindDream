# MindDream 心理梦境分析平台 V1.0 代码文档

---

## 📋 目录

1. [项目概述](#项目概述)
2. [技术栈](#技术栈)
3. [后端代码](#后端代码)
4. [前端代码](#前端代码)
5. [配置文件](#配置文件)
6. [数据库脚本](#数据库脚本)

---

## 项目概述

**MindDream** 是一个专业的心理学梦境数据分析平台，致力于通过 AI 技术帮助用户探索梦境背后的潜意识状态。

- **项目版本**: V1.0.0
- **开发周期**: 2025.12 — 2026.05
- **团队规模**: 17人

---

## 技术栈

### 后端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.1 | 核心框架 |
| Spring Data JPA | - | ORM 持久化 |
| MyBatis Plus | 3.5.5 | 复杂 SQL 查询 |
| MySQL | 8.0 | 关系型数据库 |
| SpringDoc OpenAPI | 2.3.0 | Swagger API 文档 |
| Lombok | - | 代码简化 |
| Gson | - | JSON 处理 |

### 前端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 核心框架 |
| Vite | 5.0 | 构建工具 |
| Element Plus | 2.5 | UI 组件库 |
| ECharts | 5.4 | 数据可视化 |
| Vue Router | 4.2 | 前端路由 |
| Axios | 1.6 | HTTP 客户端 |

---

## 后端代码

### 1. 主应用入口 - MindDreamApplication.java

```java
package com.minddream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MindDream 心理梦境分析平台 - 主应用入口
 */
@SpringBootApplication
public class MindDreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindDreamApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("🌙 MindDream Backend Started!");
        System.out.println("📖 Swagger UI: http://localhost:8080/api/swagger-ui.html");
        System.out.println("📡 API Docs: http://localhost:8080/api/api-docs");
        System.out.println("=================================\n");
    }
}
```

### 2. 梦境实体类 - Dream.java

```java
package com.minddream.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 梦境实体类
 */
@Data
@Entity
@Table(name = "dreams")
public class Dream {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 200)
    private String name;

    @Column(name = "dream_number")
    private Integer dreamNumber;

    @Column(name = "time_period", length = 50)
    private String timePeriod;

    @Column(name = "birth_year", length = 10)
    private String birthYear;

    @Column(length = 1)
    private String gender;

    @Column(name = "age_group", length = 1)
    private String ageGroup;

    @Column(columnDefinition = "TEXT")
    private String report;

    @Column(name = "characters_raw", length = 500)
    private String charactersRaw;

    @Column(name = "characters_cn", columnDefinition = "TEXT")
    private String charactersCn;

    @Column(name = "emotions_raw", length = 500)
    private String emotionsRaw;

    @Column(name = "emotions_cn", columnDefinition = "TEXT")
    private String emotionsCn;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### 3. 梦境控制器 - DreamController.java

```java
package com.minddream.controller;

import com.minddream.entity.Dream;
import com.minddream.dto.SearchRequest;
import com.minddream.service.DreamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 梦境管理控制器
 */
@RestController
@RequestMapping("/dreams")
@RequiredArgsConstructor
@Tag(name = "梦境管理", description = "梦境数据的增删改查接口")
public class DreamController {

    private final DreamService dreamService;

    @GetMapping
    @Operation(summary = "获取梦境列表", description = "支持分页和排序")
    public ResponseEntity<Page<Dream>> getDreams(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortOrder) {

        Page<Dream> dreams = dreamService.getAllDreams(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(dreams);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取梦境详情", description = "根据ID获取单个梦境的完整信息")
    public ResponseEntity<Dream> getDreamById(
            @Parameter(description = "梦境ID") @PathVariable String id) {

        return dreamService.getDreamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    @Operation(summary = "高级搜索", description = "支持多条件组合搜索")
    public ResponseEntity<Page<Dream>> searchDreams(@RequestBody SearchRequest request) {
        Page<Dream> results = dreamService.searchDreams(request);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/random")
    @Operation(summary = "随机获取梦境", description = "随机返回指定数量的梦境")
    public ResponseEntity<List<Dream>> getRandomDreams(
            @Parameter(description = "数量") @RequestParam(defaultValue = "1") int limit) {

        List<Dream> dreams = dreamService.getRandomDreams(limit);
        return ResponseEntity.ok(dreams);
    }

    @GetMapping("/count")
    @Operation(summary = "获取总数", description = "获取梦境总数量")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(dreamService.getTotalCount());
    }

    @PostMapping
    @Operation(summary = "创建梦境", description = "创建新的梦境记录")
    public ResponseEntity<Dream> createDream(@RequestBody Dream dream) {
        Dream savedDream = dreamService.saveDream(dream);
        return ResponseEntity.ok(savedDream);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除梦境", description = "根据ID删除梦境（管理功能）")
    public ResponseEntity<Void> deleteDream(@PathVariable String id) {
        dreamService.deleteDream(id);
        return ResponseEntity.ok().build();
    }
}
```

### 4. AI 分析控制器 - AIAnalysisController.java

```java
package com.minddream.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI 梦境分析控制器
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI 分析", description = "智能梦境分析接口")
public class AIAnalysisController {

    @PostMapping("/analyze")
    @Operation(summary = "分析梦境", description = "使用 AI 分析梦境内容，提供心理学解读")
    public ResponseEntity<AnalysisResult> analyzeDream(@RequestBody AnalysisRequest request) {

        AnalysisResult result = new AnalysisResult();
        result.setDreamId(request.getDreamId());
        result.setReportExcerpt(truncate(request.getReport(), 100));

        // Mock AI 分析结果
        result.setOverallSummary(generateSummary(request.getReport()));
        result.setEmotionalAnalysis(analyzeEmotion(request.getReport()));
        result.setSymbolInterpretation(interpretSymbols(request.getReport()));
        result.setPersonalInsights(generateInsights(request.getCharacters(), request.getEmotions()));
        result.setSuggestions(generateSuggestions());
        result.setConfidence(calculateConfidence(request.getReport()));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/chat")
    @Operation(summary = "梦境对话", description = "与 AI 进行梦境相关的对话")
    public ResponseEntity<ChatResponse> chat(@RequestParam String question) {
        ChatResponse response = new ChatResponse();
        response.setQuestion(question);
        response.setAnswer(generateChatResponse(question));
        response.setTimestamp(new Date());
        return ResponseEntity.ok(response);
    }

    // === Mock AI 分析逻辑 ===

    private String generateSummary(String report) {
        if (report == null || report.isEmpty()) {
            return "梦境内容较少，难以进行深入分析。";
        }

        List<String> summaries = Arrays.asList(
                "这个梦境反映了您潜意识中对于人际关系的思考和情感体验。梦中的场景和人物可能代表着您生活中的某些重要关系或未解决的情绪。",
                "您的梦境展现了丰富的想象力和创造性。梦中出现的场景变化可能暗示着您对生活变化的适应过程。",
                "这个梦境包含了多个层次的象征意义。从心理学角度看，它可能反映了您对安全感、归属感或个人成长的深层需求。",
                "梦境中的叙事结构显示出您的思维正在整合日常经历和情感记忆，这是一个健康的心理过程。");

        return summaries.get(new Random().nextInt(summaries.size()));
    }

    private EmotionalAnalysis analyzeEmotion(String report) {
        EmotionalAnalysis analysis = new EmotionalAnalysis();

        String reportLower = report.toLowerCase();

        // 简单的情感关键词匹配
        if (reportLower.contains("happy") || reportLower.contains("joy")) {
            analysis.setPrimary("积极 - 快乐");
            analysis.setIntensity(75);
        } else if (reportLower.contains("sad") || reportLower.contains("cry")) {
            analysis.setPrimary("消极 - 悲伤");
            analysis.setIntensity(65);
        } else if (reportLower.contains("scary") || reportLower.contains("afraid")) {
            analysis.setPrimary("消极 - 恐惧");
            analysis.setIntensity(70);
        } else {
            analysis.setPrimary("中性 - 平静");
            analysis.setIntensity(50);
        }

        analysis.setSecondary(Arrays.asList("好奇", "期待", "困惑"));
        analysis.setExplanation("梦境中的情感状态反映了您当前的心理状态和潜意识中的情感需求。");

        return analysis;
    }

    private List<SymbolInfo> interpretSymbols(String report) {
        List<SymbolInfo> symbols = new ArrayList<>();

        // 检测常见梦境符号
        Map<String, String> symbolMap = new HashMap<>();
        symbolMap.put("house", "家 - 代表安全感、归属感和自我认同");
        symbolMap.put("water", "水 - 象征情感、潜意识和生命力");
        symbolMap.put("car", "车 - 代表人生方向、控制力和前进动力");
        symbolMap.put("door", "门 - 象征机会、转变和新的可能性");
        symbolMap.put("animal", "动物 - 代表本能、直觉和自然天性");
        symbolMap.put("people", "人物 - 可能代表自我的不同面向或重要关系");

        String reportLower = report.toLowerCase();
        for (Map.Entry<String, String> entry : symbolMap.entrySet()) {
            if (reportLower.contains(entry.getKey())) {
                SymbolInfo symbol = new SymbolInfo();
                symbol.setSymbol(entry.getKey());
                symbol.setMeaning(entry.getValue());
                symbol.setContext("在您的梦境中出现");
                symbols.add(symbol);
            }
        }

        if (symbols.isEmpty()) {
            SymbolInfo defaultSymbol = new SymbolInfo();
            defaultSymbol.setSymbol("梦境场景");
            defaultSymbol.setMeaning("您的梦境场景独特且富有个人意义");
            defaultSymbol.setContext("整体梦境氛围");
            symbols.add(defaultSymbol);
        }

        return symbols;
    }

    private List<String> generateInsights(String characters, String emotions) {
        return Arrays.asList(
                "💡 您的梦境显示出丰富的人际互动，这可能反映您对社交关系的重视",
                "💡 梦中的情感体验可以帮助您了解自己的真实感受",
                "💡 定期记录和分析梦境有助于自我认知和心理健康",
                "💡 梦境是潜意识与意识沟通的桥梁，值得细心倾听");
    }

    private List<String> generateSuggestions() {
        return Arrays.asList(
                "📝 建议保持梦境日记，记录更多细节以便深入分析",
                "🧘 尝试在睡前冥想或放松，提高梦境回忆质量",
                "💭 思考梦境中的元素与现实生活的联系",
                "📚 了解更多心理学知识，加深对自我的理解");
    }

    private int calculateConfidence(String report) {
        // 基于梦境长度计算分析置信度
        int length = report != null ? report.length() : 0;
        if (length < 100)
            return 60;
        if (length < 300)
            return 75;
        if (length < 600)
            return 85;
        return 90;
    }

    private String generateChatResponse(String question) {
        String q = question.toLowerCase();

        if (q.contains("梦") && q.contains("为什么")) {
            return "梦境是大脑在睡眠期间整理信息、处理情绪和巩固记忆的自然过程。每个人每晚都会做梦，只是不一定都能记住。梦境内容往往反映了我们的潜意识思维、日常经历和情感状态。";
        } else if (q.contains("噩梦")) {
            return "噩梦通常是压力、焦虑或未解决情绪的表现。它们实际上是心理的一种自我保护机制，帮助我们在安全的环境中处理负面情绪。如果经常做噩梦，建议关注生活中的压力源，并考虑通过放松技巧改善睡眠质量。";
        } else if (q.contains("预示") || q.contains("预言")) {
            return "从科学角度看，梦境不能预测未来，但它们确实可以反映我们对未来的期望、担忧或潜意识的感知。有时梦境中的'预示'实际上是我们大脑对当前信息的整合和推演。";
        } else if (q.contains("记住")) {
            return "要更好地记住梦境，可以尝试：1) 在床边放置笔记本，醒来后立即记录；2) 保持规律的睡眠时间；3) 睡前提醒自己想要记住梦境；4) 避免闹钟突然惊醒，尝试自然醒来。";
        } else if (q.contains("颜色")) {
            return "研究表明大多数人的梦境是有颜色的，但我们往往不会特别注意到。彩色梦境被认为与更强烈的情感体验和创造性思维相关。如果您能记住梦中的颜色，这往往意味着该梦境对您有特殊意义。";
        } else {
            return "这是一个很有意思的问题！梦境分析是一个复杂而个性化的过程。每个人的梦境都带有独特的个人意义，建议结合自己的生活经历和情感状态来理解梦境。如果您想深入探索，可以咨询专业的心理咨询师。";
        }
    }

    private String truncate(String text, int length) {
        if (text == null || text.length() <= length)
            return text;
        return text.substring(0, length) + "...";
    }

    // === DTO 类 ===

    @Data
    public static class AnalysisRequest {
        private String dreamId;
        private String report;
        private String characters;
        private String emotions;
    }

    @Data
    public static class AnalysisResult {
        private String dreamId;
        private String reportExcerpt;
        private String overallSummary;
        private EmotionalAnalysis emotionalAnalysis;
        private List<SymbolInfo> symbolInterpretation;
        private List<String> personalInsights;
        private List<String> suggestions;
        private int confidence;
    }

    @Data
    public static class EmotionalAnalysis {
        private String primary;
        private int intensity;
        private List<String> secondary;
        private String explanation;
    }

    @Data
    public static class SymbolInfo {
        private String symbol;
        private String meaning;
        private String context;
    }

    @Data
    public static class ChatResponse {
        private String question;
        private String answer;
        private Date timestamp;
    }
}
```

### 5. 梦境服务层 - DreamService.java

```java
package com.minddream.service;

import com.minddream.entity.Dream;
import com.minddream.dto.SearchRequest;
import com.minddream.repository.DreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 梦境业务逻辑层
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DreamService {

    private final DreamRepository dreamRepository;

    /**
     * 分页获取所有梦境
     */
    public Page<Dream> getAllDreams(int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return dreamRepository.findAll(pageable);
    }

    /**
     * 根据ID获取梦境
     */
    public Optional<Dream> getDreamById(String id) {
        return dreamRepository.findById(id);
    }

    /**
     * 高级搜索
     */
    public Page<Dream> searchDreams(SearchRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        return dreamRepository.searchDreams(
                request.getGender(),
                request.getAgeGroup(),
                request.getTimePeriod(),
                request.getKeyword(),
                pageable);
    }

    /**
     * 获取随机梦境
     */
    public List<Dream> getRandomDreams(int limit) {
        return dreamRepository.findRandomDreams(limit);
    }

    /**
     * 删除梦境
     */
    @Transactional
    public void deleteDream(String id) {
        dreamRepository.deleteById(id);
    }

    /**
     * 保存梦境
     */
    @Transactional
    public Dream saveDream(Dream dream) {
        return dreamRepository.save(dream);
    }

    /**
     * 获取总数
     */
    public long getTotalCount() {
        return dreamRepository.count();
    }
}
```

---

## 前端代码

### 1. 主入口文件 - main.js

```javascript
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import './assets/styles/typography.css'
import './assets/styles/static-gradients.css'
import './assets/styles/apple-glass.css'
import './assets/styles/spline-integration.css'
import './assets/styles/main.css'
import './assets/styles/ghibli.css'
import './assets/styles/dream-deep.css'
import './assets/styles/performance.css'
import './assets/styles/apple-unified.css'

// Import Dream Flow Components
import GlassCard from './components/GlassCard.vue'
import DreamButton from './components/DreamButton.vue'
import DreamInput from './components/DreamInput.vue'
import DreamLoader from './components/DreamLoader.vue'
import DreamTag from './components/DreamTag.vue'

const app = createApp(App)

// Register Dream Flow Components Globally
app.component('GlassCard', GlassCard)
app.component('DreamButton', DreamButton)
app.component('DreamInput', DreamInput)
app.component('DreamLoader', DreamLoader)
app.component('DreamTag', DreamTag)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(router)
app.mount('#app')
```

### 2. 路由配置 - router/index.js

```javascript
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
    },
    {
        path: '/dreams',
        name: 'DreamList',
        component: () => import('../views/DreamList.vue')
    },
    {
        path: '/dreams/:id',
        name: 'DreamDetail',
        component: () => import('../views/DreamDetail.vue')
    },
    {
        path: '/dreams/:id/analyze',
        name: 'AIAnalysis',
        component: () => import('../views/AIAnalysis.vue')
    },
    {
        path: '/search',
        name: 'Search',
        component: () => import('../views/Search.vue')
    },
    {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('../views/Statistics.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        }
        return { top: 0, behavior: 'smooth' }
    }
})

export default router
```

### 3. 首页仪表板 - Dashboard.vue（核心代码片段）

```vue
<template>
  <div class="revolutionary-dashboard">
    <!-- Hero区 - 3D视差效果 -->
    <div class="hero-3d" ref="hero">
      <div class="hero-content-3d">
        <div class="hero-badge-3d warm-breathe">
          <div class="badge-glow"></div>
          <span>✨ Dream Analytics Platform</span>
        </div>
        <h1 class="hero-title-3d">
          <span class="title-word" data-word="探索">探索</span>
          <span class="title-word warm-gradient" data-word="潜意识">潜意识</span>
          <span class="title-word" data-word="的">的</span>
          <span class="title-word warm-gradient" data-word="奥秘">奥秘</span>
        </h1>
        <p class="hero-description-3d">
          深入 <span class="highlight-number">{{ stats.totalDreams?.toLocaleString() || 0 }}</span> 个梦境的神秘世界
        </p>
      </div>

      <!-- 3D装饰元素 -->
      <div class="hero-decoration-3d">
        <div class="floating-ring ring-1"></div>
        <div class="floating-ring ring-2"></div>
        <div class="floating-ring ring-3"></div>
      </div>
    </div>

    <!-- Bento Grid布局 -->
    <div class="bento-grid" v-if="!loading">
      <!-- 大卡片 - 主要统计 -->
      <div class="bento-item bento-large card-3d" @mousemove="handleCardMove" @mouseleave="resetCard">
        <div class="bento-content">
          <div class="stat-icon-large">🌙</div>
          <div class="stat-value-large warm-gradient">{{ stats.totalDreams?.toLocaleString() }}</div>
          <div class="stat-label-large">梦境总数</div>
          <div class="stat-progress">
            <div class="progress-bar" :style="{ width: '75%' }"></div>
          </div>
        </div>
        <div class="card-shimmer"></div>
      </div>

      <!-- 更多卡片... -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { statisticsAPI, dreamAPI } from '@/api/dream'

const router = useRouter()
const loading = ref(true)
const stats = ref({})

// 3D卡片效果
const handleCardMove = (e) => {
  const card = e.currentTarget
  const rect = card.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  const centerX = rect.width / 2
  const centerY = rect.height / 2
  const rotateX = (y - centerY) / 20
  const rotateY = (centerX - x) / 20

  card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale3d(1.02, 1.02, 1.02)`
}

const resetCard = (e) => {
  e.currentTarget.style.transform = ''
}
</script>
```

### 4. 梦境列表页 - DreamList.vue

```vue
<template>
  <div class="dream-list-unified">
    <!-- Hero区 -->
    <div class="page-hero">
      <div class="hero-badge breathe">
        <el-icon><Collection /></el-icon>
        <span>梦境档案馆</span>
      </div>
      <h1 class="page-title">
        <span class="title-main consciousness-text">探索梦境</span>
        <span class="title-sub">追寻潜意识的足迹</span>
      </h1>
    </div>

    <!-- 筛选工具栏 -->
    <GlassCard variant="elevated" size="md" :interactive="false" class="filter-bar">
      <div class="filter-content">
        <el-select v-model="filters.gender" placeholder="性别" clearable class="filter-item">
          <el-option label="全部性别" value="" />
          <el-option label="♂ 男性" value="M" />
          <el-option label="♀ 女性" value="F" />
        </el-select>

        <el-select v-model="filters.ageGroup" placeholder="年龄组" clearable class="filter-item">
          <el-option label="全部年龄" value="" />
          <el-option label="🧑 成人" value="A" />
          <el-option label="👦 儿童" value="C" />
          <el-option label="👴 老年" value="O" />
        </el-select>

        <DreamInput
          v-model="filters.keyword"
          placeholder="🔍 搜索梦境内容..."
          clearable
          class="search-input"
          @keyup.enter="searchDreams"
        />

        <DreamButton variant="primary" @click="searchDreams">
          <el-icon><Search /></el-icon>
          搜索
        </DreamButton>
      </div>
    </GlassCard>

    <!-- 梦境网格 -->
    <div class="dreams-grid" v-if="!loading && dreams.length > 0">
      <div
        v-for="(dream, index) in dreams"
        :key="dream.id"
        class="dream-card-wrapper"
        :style="{ animationDelay: `${index * 0.06}s` }"
        @click="viewDream(dream.id)"
      >
        <GlassCard class="dream-card" :interactive="true">
          <div class="dream-header">
            <DreamTag :type="dream.gender === 'M' ? 'primary' : 'danger'" size="sm" effect="dark">
              {{ dream.gender === 'M' ? '♂ 男性' : '♀ 女性' }}
            </DreamTag>
            <DreamTag type="success" size="sm">
              {{ getAgeLabel(dream.ageGroup) }}
            </DreamTag>
          </div>

          <h3 class="dream-title">{{ dream.name }}</h3>
          <div class="dream-excerpt">{{ truncate(dream.report, 140) }}</div>

          <div class="dream-footer">
            <div class="meta-items">
              <div class="meta-item" v-if="dream.timePeriod">
                <el-icon><Clock /></el-icon>
                <span>{{ dream.timePeriod }}</span>
              </div>
            </div>
            <div class="read-more">
              <span>阅读</span>
              <el-icon><Right /></el-icon>
            </div>
          </div>
        </GlassCard>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next, jumper"
        @current-change="handlePageChange"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { dreamAPI } from '@/api/dream'

const router = useRouter()
const loading = ref(true)
const dreams = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const filters = reactive({
  gender: '',
  ageGroup: '',
  keyword: ''
})

const loadDreams = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      ...filters
    }
    const response = await dreamAPI.searchDreams(params)
    dreams.value = response.content || []
    total.value = response.totalElements || 0
  } catch (error) {
    console.error('加载梦境失败:', error)
  } finally {
    loading.value = false
  }
}

const viewDream = (id) => router.push(`/dreams/${id}`)
</script>
```

### 5. AI 分析页 - AIAnalysis.vue

```vue
<template>
  <div class="ai-analysis-unified">
    <!-- Hero 区 -->
    <div class="ai-hero">
      <div class="hero-badge breathe">
        <span class="badge-pulse"></span>
        <span>🤖 AI 梦境顾问</span>
      </div>
      <h1 class="ai-hero-title consciousness-text">智能梦境分析</h1>
      <p class="ai-hero-subtitle">运用人工智能深度解读梦境的象征意义与潜意识信息</p>
    </div>

    <!-- 分析结果 -->
    <div v-if="result" class="results-layout">
      <!-- 置信度 -->
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
        <div class="intensity-wrap">
          <span class="em-label">情感强度</span>
          <div class="intensity-bar">
            <div class="intensity-fill" :style="{ width: result.emotionalAnalysis.intensity + '%' }"></div>
          </div>
          <span class="intensity-val">{{ result.emotionalAnalysis.intensity }}%</span>
        </div>
      </GlassCard>
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
          </div>
          <div class="bubble-avatar user-avatar" v-if="msg.type === 'question'">😊</div>
        </div>
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
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { dreamAPI } from '@/api/dream'

const route = useRoute()
const result = ref(null)
const dreamReport = ref('')
const chatQuestion = ref('')
const chatLoading = ref(false)
const chatHistory = ref([])

const analyzeDream = async () => {
  try {
    const response = await axios.post('/api/ai/analyze', {
      dreamId: route.params.id,
      report: dreamReport.value
    })
    result.value = response.data
  } catch (error) {
    console.error('分析失败:', error)
  }
}

const askQuestion = async () => {
  if (!chatQuestion.value.trim()) return
  chatHistory.value.push({ type: 'question', content: chatQuestion.value })
  const question = chatQuestion.value
  chatQuestion.value = ''
  chatLoading.value = true

  try {
    const response = await axios.get('/api/ai/chat', { params: { question } })
    chatHistory.value.push({ type: 'answer', content: response.data.answer })
  } catch (error) {
    console.error('对话失败:', error)
  } finally {
    chatLoading.value = false
  }
}
</script>
```

---

## 配置文件

### 1. 后端配置 - application.yml

```yaml
spring:
  application:
    name: minddream-backend

  datasource:
    url: jdbc:mysql://localhost:3306/minddream?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: wsxhr666
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

  jackson:
    default-property-inclusion: non_null
    serialization:
      write-dates-as-timestamps: false

server:
  port: 8080
  servlet:
    context-path: /api

# MyBatis Plus 配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto

# Swagger/OpenAPI 配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  api-docs:
    path: /api-docs

# 日志配置
logging:
  level:
    com.minddream: INFO
    org.springframework.web: INFO
    org.hibernate.SQL: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
  charset:
    console: UTF-8
```

### 2. Maven 配置 - pom.xml

```xml
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
```

### 3. 前端配置 - package.json

```json
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
```

---

## 数据库脚本

### MySQL 建表脚本 - schema.sql

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS minddream DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE minddream;

-- 梦境数据表
CREATE TABLE IF NOT EXISTS dreams (
    id VARCHAR(50) PRIMARY KEY COMMENT '梦境唯一标识',
    name VARCHAR(200) COMMENT '梦境者名称',
    dream_number INT COMMENT '梦境编号',
    time_period VARCHAR(100) COMMENT '时间段',
    birth_year VARCHAR(50) COMMENT '出生年份',
    gender CHAR(1) COMMENT '性别: M-男性, F-女性',
    age_group CHAR(1) COMMENT '年龄组: A-成人, C-儿童, O-老年',
    report TEXT COMMENT '梦境报告内容',
    characters_raw VARCHAR(500) COMMENT '角色原始编码',
    characters_cn TEXT COMMENT '角色中文描述',
    emotions_raw VARCHAR(500) COMMENT '情感原始编码',
    emotions_cn TEXT COMMENT '情感中文描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_gender (gender),
    INDEX idx_age (age_group),
    INDEX idx_time (time_period),
    INDEX idx_birth_year (birth_year),
    FULLTEXT idx_report (report)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='梦境数据表';

-- 统计数据表
CREATE TABLE IF NOT EXISTS statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    stat_type VARCHAR(50) NOT NULL COMMENT '统计类型: gender, age, emotion, character, timeline',
    stat_key VARCHAR(100) NOT NULL COMMENT '统计键',
    stat_value VARCHAR(500) COMMENT '统计值',
    count INT DEFAULT 0 COMMENT '计数',
    percentage DECIMAL(5,2) COMMENT '百分比',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (stat_type),
    INDEX idx_key (stat_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据表';
```

---

## API 接口一览

### 梦境接口 `/api/dreams`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dreams` | 分页获取梦境列表 |
| GET | `/api/dreams/{id}` | 获取单条梦境详情 |
| POST | `/api/dreams/search` | 多条件高级搜索 |
| GET | `/api/dreams/random` | 随机获取一条梦境 |
| GET | `/api/dreams/count` | 获取梦境总数 |
| POST | `/api/dreams` | 创建梦境记录 |
| DELETE | `/api/dreams/{id}` | 删除梦境记录 |

### 统计接口 `/api/statistics`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/statistics/overview` | 总体统计概览 |
| GET | `/api/statistics/gender` | 性别分布 |
| GET | `/api/statistics/age` | 年龄组分布 |
| GET | `/api/statistics/emotions` | 情感类型分布 |
| GET | `/api/statistics/characters` | 角色类型分布 |

### AI 接口 `/api/ai`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/ai/analyze` | 提交梦境报告，返回 AI 分析结果 |
| GET | `/api/ai/chat?question=...` | 与 AI 进行梦境主题对话 |

---

## 项目启动说明

### 环境要求

| 工具 | 版本要求 |
|------|----------|
| JDK | 17+ |
| Node.js | 16+ |
| MySQL | 8.0+ |
| Maven | 3.6+ |

### 启动步骤

1. **数据库准备**
   ```sql
   CREATE DATABASE minddream DEFAULT CHARACTER SET utf8mb4;
   source backend/src/main/resources/schema.sql
   ```

2. **启动后端**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **启动前端**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **访问地址**
   - 前端: http://localhost:5173
   - 后端 API: http://localhost:8080/api
   - Swagger 文档: http://localhost:8080/api/swagger-ui.html

---

## 版本信息

- **当前版本**: V1.0.0
- **发布日期**: 2026-02-24
- **开发团队**: CQIEMindDream Team
- **许可证**: MIT License

---

*文档生成时间: 2026年6月2日*
