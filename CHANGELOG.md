# 更新日志 (Changelog)

本文档记录 MindDream 心理梦境分析平台的所有重要变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [1.0.0] - 2026-02-24

### 新增 (Added)

#### 后端
- 梦境 CRUD 与分页列表接口 `GET /api/dreams`
- 梦境详情接口 `GET /api/dreams/{id}`
- 高级搜索接口 `POST /api/dreams/search`（关键词 / 性别 / 年龄组 / 情感 / 时间段）
- 随机梦境接口 `GET /api/dreams/random`
- 梦境总数接口 `GET /api/dreams/count`
- 统计概览接口 `GET /api/statistics/overview`
- 性别 / 年龄 / 情感 / 角色分布统计接口
- AI 梦境分析接口 `POST /api/ai/analyze`（综合解读、情感分析、符号解读、个人洞察、行动建议）
- AI 梦境对话接口 `GET /api/ai/chat`
- SpringDoc OpenAPI (Swagger) API 文档
- JSONL 数据导入工具 `DataImporter`
- CORS 跨域配置

#### 前端
- 首页仪表板（Morphing Blob 背景、Bento Grid 统计卡片、3D 磁力倾斜）
- 梦境列表页（分页浏览）
- 梦境详情页（报告、角色、情感展示）
- 高级搜索页（多条件过滤）
- 数据统计页（ECharts 性别/年龄/情感/角色图表）
- AI 分析页（梦境解读、符号解读、AI 对话）
- Apple Liquid Glass 风格 UI（液态玻璃、多主题）
- Vue 3 + Vite + Element Plus + ECharts 技术栈
- 统一 API 封装 `dream.js`

#### 文档与工程
- 项目说明 README.md
- 部署指南 DEPLOYMENT.md
- 故障排查 TROUBLESHOOTING.md
- 文件索引 FILE_INDEX.md
- 更新日志 CHANGELOG.md
- MIT 许可证 LICENSE
- Windows 一键启动脚本 `启动项目.bat` / `启动后端.bat`

### 技术栈

- **后端**: Spring Boot 3.2.1, JPA, MyBatis Plus, MySQL 8.0
- **前端**: Vue 3.4, Vite 5, Element Plus 2.5, ECharts 5.4

---

## [Unreleased]

### 计划中 (Planned)

- 用户系统与登录
- 梦境录入与个人历史
- NLP 情绪分类模型（目标准确率 ≥ 70%）
- 关键词提取（TF-IDF / TextRank）
- BERT/RoBERTa 微调
- 安全加固（SQL 注入 / XSS）
- CI/CD 流水线
- 正式上线与 GitHub 开源（目标 ≥ 50 Stars）

---

[1.0.0]: https://github.com/PlateWin/MindDream/releases/tag/v1.0.0
