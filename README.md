# 🌙 MindDream · 心理梦境分析平台

<div align="center">

**探索潜意识的奥秘 · Dream Analytics Platform**

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.4-42b883?style=flat-square&logo=vuedotjs)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

*项目周期：2025.12 — 2026.05（共 6 个月）*

</div>

---

## 📑 目录

| 章节 | 说明 |
|------|------|
| [项目简介](#-项目简介) | 核心目标与定位 |
| [系统架构](#-系统架构) | 前后端与数据库架构 |
| [技术栈](#-技术栈) | 后端 / 前端技术选型 |
| [项目结构](#-项目结构) | 目录与文件说明 |
| [功能特性](#-功能特性) | 各页面与模块说明 |
| [API 接口](#-api-接口一览) | REST 接口一览 |
| [快速开始](#-快速开始) | 环境、数据库、启动步骤 |
| [项目路线图](#-项目路线图v20-计划) | 阶段规划与当前阶段 |
| [项目管理](#-项目管理) | 文档导航、开发流程、版本与入口速查 |
| [生产部署](#-生产部署) | 打包与 Nginx 配置 |

---

## 📖 项目简介

**MindDream** 是一个专业的心理学梦境数据分析平台，致力于通过 AI 技术帮助用户探索梦境背后的潜意识状态。平台集成了梦境数据管理、多维度可视化统计、智能 AI 分析和对话功能，旨在为每一个灵魂架起通往潜意识世界的桥梁。

### 🎯 核心目标

| 目标 | 说明 |
|------|------|
| 🧠 情绪分类 | 对梦境情绪做 6 分类，准确率目标 ≥ 70% |
| 🔒 隐私保护 | 确保用户数据 100% 合规存储 |
| 🚀 开源社区 | 项目发布至 GitHub，目标 ≥ 50 Stars |
| 📊 可视化洞察 | 生成梦境中的潜意识关键词云、潜在示例 |

---

## 🏗️ 系统架构

```
┌────────────────────────────────────────────────────────────┐
│                        用户浏览器                           │
│              Vue 3 + Vite + Element Plus + ECharts          │
│                  Apple Liquid Glass UI 风格                  │
└────────────────────┬───────────────────────────────────────┘
                     │ HTTP / REST API
                     ▼
┌────────────────────────────────────────────────────────────┐
│                      后端服务 :8080                          │
│          Spring Boot 3.2.1 + JPA + MyBatis Plus             │
│    ┌──────────────┬───────────────┬───────────────┐        │
│    │  Dream API   │  Statistics   │  AI Analysis  │        │
│    │  梦境管理    │  数据统计     │  AI 分析对话  │        │
│    └──────────────┴───────────────┴───────────────┘        │
└────────────────────┬───────────────────────────────────────┘
                     │ JDBC / JPA
                     ▼
┌────────────────────────────────────────────────────────────┐
│                    MySQL 8.0 数据库                          │
│              dreams 表 · statistics 表                       │
└────────────────────────────────────────────────────────────┘
```

### 未来规划（v2.0 目标）

```
前端 React + Tailwind CSS + ECharts/D3.js
算法层 PyTorch + BERT/RoBERTa 微调（6分类）
AI平台 Dify（工作流编排、知识库、AI对话）
可视化 Stable Diffusion / ControlNet（梦境图片生成，可选）
```

---

## 🛠️ 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.1 | 核心框架 |
| Spring Data JPA | — | ORM 持久化 |
| MyBatis Plus | 3.5.5 | 复杂 SQL 查询 |
| MySQL | 8.0 | 关系型数据库 |
| SpringDoc OpenAPI | 2.3.0 | Swagger API 文档 |
| Lombok | — | 代码简化 |
| Gson | — | JSON 处理 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 核心框架 |
| Vite | 5.0 | 构建工具 |
| Element Plus | 2.5 | UI 组件库 |
| ECharts | 5.4 | 数据可视化 |
| Vue Router | 4.2 | 前端路由 |
| Axios | 1.6 | HTTP 客户端 |
| @splinetool/runtime | 1.12+ | Spline 3D 场景 |

---

## 📁 项目结构

```
心理/
├── backend/                            # Spring Boot 后端
│   ├── src/main/java/com/minddream/
│   │   ├── MindDreamApplication.java   # 应用入口
│   │   ├── controller/                 # 控制器层（REST API）
│   │   │   ├── DreamController.java    # 梦境 CRUD & 搜索接口
│   │   │   ├── StatisticsController.java # 统计分析接口
│   │   │   └── AIAnalysisController.java # AI 分析 & 对话接口
│   │   ├── service/                    # 业务逻辑层
│   │   │   ├── DreamService.java
│   │   │   └── StatisticsService.java
│   │   ├── repository/                 # 数据访问层（JPA + MyBatis Plus）
│   │   │   ├── DreamRepository.java
│   │   │   └── StatisticsRepository.java
│   │   ├── entity/                     # JPA 实体类
│   │   │   ├── Dream.java              # 梦境（id/name/report/emotions/characters...）
│   │   │   └── Statistics.java
│   │   ├── dto/                        # 数据传输对象
│   │   │   ├── SearchRequest.java
│   │   │   └── StatisticsDTO.java
│   │   ├── config/                     # 配置类
│   │   │   ├── CorsConfig.java         # 跨域配置
│   │   │   └── SwaggerConfig.java
│   │   └── util/
│   │       └── DataImporter.java       # JSONL 数据导入工具
│   └── src/main/resources/
│       ├── application.yml             # 应用配置（端口/DB/日志）
│       └── schema.sql                  # 数据库建表脚本
│
├── frontend/                           # Vue 3 前端
│   ├── src/
│   │   ├── views/                      # 页面级组件
│   │   │   ├── Dashboard.vue           # 🏠 首页仪表板（Bento Grid 统计）
│   │   │   ├── DreamList.vue           # 📋 梦境列表（分页浏览）
│   │   │   ├── DreamDetail.vue         # 📄 梦境详情（角色/情感展示）
│   │   │   ├── Search.vue              # 🔍 高级搜索（多条件过滤）
│   │   │   ├── Statistics.vue          # 📈 数据统计（ECharts 图表）
│   │   │   ├── AIAnalysis.vue          # 🤖 AI 分析（梦境解读 & 对话）
│   │   │   └── NotFound.vue            # 404 页面
│   │   ├── components/                 # 可复用组件
│   │   │   ├── GlassCard.vue           # 液态玻璃卡片（3D 磁力倾斜）
│   │   │   ├── DreamHeroSection.vue    # 首页 Hero（Morphing Blob + Bento Grid）
│   │   │   ├── DreamInput.vue          # 梦境输入组件
│   │   │   ├── DreamButton.vue         # 按钮组件
│   │   │   ├── DreamLoader.vue         # 加载动画
│   │   │   ├── DreamTag.vue            # 标签组件
│   │   │   ├── ArtisticEffects.vue     # 艺术效果
│   │   │   ├── SplineBackground.vue    # Spline 3D 背景
│   │   │   └── SplineTexture.vue       # Spline 纹理
│   │   ├── assets/styles/              # 样式系统（多主题 CSS）
│   │   │   ├── main.css                # 全局基础样式
│   │   │   ├── apple-glass.css         # Apple 液态玻璃主题
│   │   │   ├── apple-unified.css       # Apple 统一组件样式
│   │   │   ├── dream-deep.css          # 深梦主题
│   │   │   ├── ghibli.css              # 吉卜力主题
│   │   │   ├── typography.css          # 排版系统
│   │   │   ├── performance.css         # 性能优化
│   │   │   ├── spline-integration.css  # Spline 集成样式
│   │   │   └── static-gradients.css    # 静态渐变
│   │   ├── api/
│   │   │   └── dream.js                # 统一 API 调用封装（Axios）
│   │   ├── router/
│   │   │   └── index.js                # Vue Router 路由配置
│   │   ├── App.vue                     # 根组件
│   │   └── main.js                     # Vue 应用入口
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── data/                               # 原始梦境数据
│   ├── train_parsed_part1.jsonl        # 训练集（JSONL 格式）
│   ├── train_parsed_part2.jsonl
│   └── train_parsed_part3.jsonl
│
├── codes/
│   └── data.py                         # Python 数据解析脚本
│
├── 启动项目.bat                         # Windows 一键启动
├── 启动后端.bat                         # 仅启动后端
├── README.md                           # 项目说明（本文件）
├── CHANGELOG.md                        # 更新日志
├── DEPLOYMENT.md                       # 部署指南
├── FILE_INDEX.md                       # 文件快速索引
├── TROUBLESHOOTING.md                  # 故障排查
├── LICENSE                             # MIT 许可证
└── .gitignore                          # Git 忽略规则
```

---

## ✨ 功能特性

### 🏠 首页仪表板（Dashboard）
- **Morphing Blob 背景** — 3 个慢速变形有机色块，40 秒循环
- **Bento Grid 统计卡片** — 展示梦境总数、梦境者数、情感类型数、年龄组数
- **3D 磁力倾斜效果** — 鼠标悬停时卡片实时跟随光标进行 3D 透视旋转
- **AI 梦境解析入口** — 直接引导用户进入情感分析、符号解读、心理洞察

### 📋 梦境列表（DreamList）
- 分页浏览所有梦境记录
- 展示梦境者信息（姓名、性别、年龄、时间段）
- 情感标签快速预览

### 📄 梦境详情（DreamDetail）
- 完整梦境报告文本展示
- **角色分析** — 中英文角色类型对照
- **情感分析** — 情绪标签与强度展示

### 🔍 高级搜索（Search）
- 多条件组合过滤（关键词 / 性别 / 年龄组 / 情感 / 时间段）
- 实时搜索结果分页展示

### 📈 数据统计（Statistics）
- **性别分布** — 环形图
- **年龄分布** — 柱状图
- **情感分布** — 多维图表
- **角色类型分析** — 词频/分布图
- 全部使用 ECharts 5 交互式图表

### 🤖 AI 分析（AIAnalysis）
- **梦境综合解读** — 整体心理学摘要
- **情感分析** — 主情绪 + 次级情绪 + 强度百分比
- **符号解读** — 自动识别梦境中的常见心理学符号（家、水、车、门…）
- **个人洞察** — 基于角色与情感生成的个性化见解
- **行动建议** — 改善睡眠与自我认知的实用建议
- **AI 梦境对话** — 输入问题，获取心理学知识解答

---

## 🔌 API 接口一览

### 梦境接口 `/api/dreams`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dreams` | 分页获取梦境列表 |
| GET | `/api/dreams/{id}` | 获取单条梦境详情 |
| POST | `/api/dreams/search` | 多条件高级搜索 |
| GET | `/api/dreams/random` | 随机获取一条梦境 |
| GET | `/api/dreams/count` | 获取梦境总数 |

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

> 📚 完整接口文档：启动后端后访问 [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html)

---

## 🚀 快速开始

### 环境要求

| 工具 | 版本要求 |
|------|----------|
| JDK | 17+ |
| Node.js | 16+ |
| MySQL | 8.0+ |
| Maven | 3.6+ |

### 1. 数据库准备

```sql
-- 创建数据库
CREATE DATABASE minddream DEFAULT CHARACTER SET utf8mb4;

-- 导入表结构
source backend/src/main/resources/schema.sql
```

### 2. 配置后端

编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/minddream?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password   # ← 改为你的密码
```

### 3. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端运行在：`http://localhost:8080`

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在：`http://localhost:5173`

### 5. 导入数据

将 `data/` 目录下的 `.jsonl` 文件导入数据库：

```java
// 在 DataImporter.java 中设置
private static final boolean AUTO_IMPORT_ENABLED = true;
// 重启后端即可自动导入
```

### 一键启动（Windows）

```bash
双击运行 启动项目.bat
```

---

## 🗓️ 项目路线图（v2.0 计划）

本项目按 **6 个月、5 个阶段**推进，以下为阶段规划（来自项目计划书 v2.0）：

```
Phase 1 · 2025.12  需求分析与技术选型
         PRD文档 / 高保真原型 / 系统架构图 / Dify初始化

Phase 2 · 2026.01  模块开发与准备
         用户系统 / 梦境录入存储接口 / NLP v1模型（目标60%准确率）

Phase 3 · 2026.02  算法集成与原型验证  ← **当前阶段**
         MVP 已上线 / 关键词提取（TF-IDF/TextRank）/ 前端与真实 API 联调

Phase 4 · 2026.03  模型优化迭代
         BERT/RoBERTa微调（目标70%准确率）/ 历史记录功能 / 性能调优

Phase 5 · 2026.04  系统集成与灰测
         全链路回归测试 / 安全加固（SQL注入/XSS）/ CI/CD流水线

Phase 6 · 2026.05  正式发布与开源
         平台v1.0上线 / GitHub开源 / 文档Wiki / 目标50 Stars
```

---

## 👥 团队构成（17 人）

| 角色 | 人数 | 职责 |
|------|------|------|
| 项目经理（PM） | 1 | 进度计划、Dify工作流、需求协调 |
| 算法工程师 | 3 | NLP模型选型/训练/微调、关键词提取、AIGC可视化 |
| 前端工程师 | 6 | UI/UX实现、ECharts/D3可视化、前端架构 |
| 后端工程师 | 6 | REST API、数据库设计、安全、CI/CD |
| QA 工程师 | 1 | 自动化测试（Cypress/Playwright）、E2E测试 |

---

## 🗃️ 数据库表结构

### `dreams` 表（核心）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | VARCHAR(50) | 梦境唯一标识 |
| `name` | VARCHAR(200) | 梦境者姓名 |
| `dream_number` | INT | 梦境编号 |
| `time_period` | VARCHAR(50) | 时间段（童年/青年/成年…）|
| `birth_year` | VARCHAR(10) | 出生年份 |
| `gender` | CHAR(1) | 性别（M/F）|
| `age_group` | CHAR(1) | 年龄组 |
| `report` | TEXT | 梦境报告正文 |
| `characters_raw` | VARCHAR(500) | 原始角色列表（英文）|
| `characters_cn` | TEXT | 角色列表（中文）|
| `emotions_raw` | VARCHAR(500) | 原始情感列表（英文）|
| `emotions_cn` | TEXT | 情感列表（中文）|
| `created_at` | DATETIME | 创建时间 |
| `updated_at` | DATETIME | 更新时间 |

---

## 🎨 UI 设计系统

MindDream 采用 **Apple Liquid Glass** 风格设计语言，特点包括：

- **液态玻璃卡片** — `backdrop-filter: blur(60px) saturate(180%)`，多层内阴影
- **3D 磁力倾斜** — 鼠标悬停时 `perspective(1500px) rotateX/Y` 实时跟随
- **Morphing Blob** — 有机变形色块背景，40秒慢速循环动画
- **Bento Grid** — 4列不等宽网格布局，大/中/小/宽卡片自由组合
- **多主题系统** — Apple Glass / Deep Dream / Ghibli 三套主题随时切换
- **Shimmer 光效** — 卡片表面流动的光泽扫光动画
- **渐变色彩** — 主色调：珊瑚粉 `#FF9A8B` × 金黄 `#FFD983` 渐变

---

## 🔧 开发指南

### 添加新统计维度

1. `StatisticsService.java` — 添加业务方法
2. `StatisticsController.java` — 添加 REST 端点
3. `frontend/src/api/dream.js` — 添加 API 调用
4. `Statistics.vue` — 添加 ECharts 图表渲染

### 添加新搜索条件

1. `SearchRequest.java` — 添加字段
2. `DreamRepository.java` — 修改 SQL 查询
3. `Search.vue` — 添加表单字段

### 添加新页面

1. `frontend/src/views/` — 创建 `.vue` 文件
2. `frontend/src/router/index.js` — 注册路由
3. `frontend/src/api/dream.js` — 添加 API 调用（如需）

---

## 📋 项目管理

### 文档导航

| 文档 | 说明 |
|------|------|
| [README.md](README.md) | 项目说明、快速开始、架构与功能（本文件） |
| [DEVELOPMENT.md](DEVELOPMENT.md) | **开发文档**：现阶段情况总结 + 每次变更在此写开发日志 |
| [FILE_INDEX.md](FILE_INDEX.md) | 文件快速索引与核心文件导航 |
| [DEPLOYMENT.md](DEPLOYMENT.md) | 部署指南、上线检查清单、Nginx/环境配置 |
| [CHANGELOG.md](CHANGELOG.md) | 版本更新日志与计划功能 |
| [TROUBLESHOOTING.md](TROUBLESHOOTING.md) | 常见问题与故障排查 |
| [LICENSE](LICENSE) | MIT 许可证 |

### 日常开发流程

1. **拉取与分支** — 从主分支拉取最新代码，功能开发使用独立分支。
2. **本地联调** — 先启动 MySQL，再启动后端（`启动后端.bat` 或 `mvn spring-boot:run`），最后启动前端（`npm run dev`）。
3. **接口调试** — 使用 [Swagger UI](http://localhost:8080/api/swagger-ui.html) 或前端页面验证 API。
4. **提交规范** — 提交信息建议包含类型与简述（如 `feat: 梦境列表分页` / `fix: 搜索条件空值处理`）。

### 版本与发布

- **当前版本**：v1.0.0（见 [CHANGELOG.md](CHANGELOG.md)）。
- **发布前**：完成 [DEPLOYMENT.md](DEPLOYMENT.md) 中的「上线前检查清单」，后端打包 `mvn clean package -DskipTests`，前端打包 `npm run build`。
- **路线图**：见下方「项目路线图」章节，按阶段推进功能与迭代。

### 关键入口速查

| 需求 | 位置 |
|------|------|
| 改 API / 加接口 | `backend/.../controller/`、`service/`、`repository/` |
| 改页面 / 加路由 | `frontend/src/views/`、`router/index.js` |
| 改样式 / 主题 | `frontend/src/assets/styles/` |
| 改数据库表 | `backend/src/main/resources/schema.sql`、`entity/` |
| 数据导入 | `backend/.../util/DataImporter.java`、`data/*.jsonl` |

---

## 📦 生产部署

### 后端打包

```bash
cd backend
mvn clean package -DskipTests
java -jar target/minddream-backend-1.0.0.jar
```

### 前端打包

```bash
cd frontend
npm run build
# 将 dist/ 目录部署到 Nginx
```

### Nginx 配置参考

```nginx
server {
    listen 80;
    root /var/www/minddream/dist;
    index index.html;

    # 前端 SPA 路由
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 代理后端 API
    location /api/ {
        proxy_pass http://localhost:8080/api/;
    }
}
```

> 详细部署见 [DEPLOYMENT.md](DEPLOYMENT.md)，上线前请查看其中的 **上线前检查清单**。

---

## 📜 更新日志

版本与变更记录见 [CHANGELOG.md](CHANGELOG.md)。

---

## 🔗 快速访问

| 服务 | 地址 |
|------|------|
| 前端应用 | http://localhost:5173 |
| 后端 API | http://localhost:8080/api |
| Swagger 文档 | http://localhost:8080/api/swagger-ui.html |
| 首页仪表板 | http://localhost:5173/ |
| 梦境列表 | http://localhost:5173/dreams |
| 高级搜索 | http://localhost:5173/search |
| 数据统计 | http://localhost:5173/statistics |
| AI 分析 | 从梦境详情页进入 `/dreams/:id/analyze` |

---

## 📄 许可证

[MIT License](LICENSE) © 2025 CQIEMindDream Team

---

## 📧 联系方式

- **团队**: CQIEMindDream Team
- **邮箱**: 2913626680@qq.com

---

<div align="center">

*「在梦境与现实的边界，解读每一个灵魂的呓语」*

🌙 **MindDream** · 让潜意识说话

</div>
