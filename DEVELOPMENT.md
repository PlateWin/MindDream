# MindDream 开发文档

本文档用于记录 **每次开发变更的日志**，便于团队追溯改动、同步进度。**请在有代码或配置变更时，在本文件底部追加一条开发日志。**

---

## 📌 文档说明

| 文档 | 用途 |
|------|------|
| **DEVELOPMENT.md**（本文件） | 开发过程日志：每次改动在此按时间记录，含日期、类型、简述、涉及文件 |
| [CHANGELOG.md](CHANGELOG.md) | 版本发布日志：仅记录正式版本的「新增/修复/变更」汇总 |
| [README.md](README.md) | 项目说明、架构、快速开始、项目管理入口 |

**约定**：完成一次功能/修复/重构或配置变更后，在下方「开发日志」中追加一条记录。

---

## 📊 现阶段情况总结

> 更新日期：**2026-03-04**

### 当前阶段与版本

| 项目 | 说明 |
|------|------|
| **项目阶段** | Phase 3 · 算法集成与原型验证（当前阶段） |
| **当前版本** | v1.0.0 |
| **计划周期** | 2025.12 — 2026.05（共 6 个月） |

### 已完成功能

**后端**

- 梦境 CRUD、分页列表、详情、高级搜索、随机一条、总数接口
- 统计概览及性别/年龄/情感/角色分布接口
- AI 梦境分析（`/api/ai/analyze`）、AI 对话（`/api/ai/chat`）
- SpringDoc Swagger API 文档、CORS、JSONL 数据导入（DataImporter）

**前端**

- 首页仪表板（Morphing Blob、Bento Grid、3D 磁力倾斜）
- 梦境列表、梦境详情、高级搜索、数据统计（ECharts）、AI 分析页
- Apple Liquid Glass 风格、多主题（Apple / Deep Dream / Ghibli）
- Vue 3 + Vite + Element Plus + ECharts + Spline 3D，统一 API 封装 `dream.js`

**工程与文档**

- README、FILE_INDEX、DEPLOYMENT、CHANGELOG、TROUBLESHOOTING、项目管理章节
- Windows 一键启动脚本：`启动项目.bat`、`启动后端.bat`

### 进行中 / 待办（Phase 3 及后续）

- 关键词提取（TF-IDF / TextRank）集成
- 前端与真实 API 全链路联调与验证
- Phase 4：NLP 模型优化（BERT/RoBERTa）、历史记录、性能调优
- Phase 5：安全加固、CI/CD、全链路测试
- Phase 6：正式发布、GitHub 开源（目标 ≥ 50 Stars）

### 技术栈快览

- **后端**：Spring Boot 3.2.1、JPA、MyBatis Plus、MySQL 8.0、SpringDoc 2.3.0
- **前端**：Vue 3.4、Vite 5、Element Plus 2.5、ECharts 5.4、@splinetool/runtime
- **本地**：JDK 17、Node 16+、Maven 3.6+

---

## 📝 开发日志书写规范

每条日志建议包含：

- **日期**：YYYY-MM-DD
- **类型**：`feat`（新功能）/ `fix`（修复）/ `refactor`（重构）/ `docs`（文档）/ `config`（配置）/ `chore`（杂项）
- **简述**：一句话说明做了什么
- **涉及文件/模块**（可选）：便于回溯

示例：

```markdown
### 2026-03-04
- **docs** 新增开发文档 DEVELOPMENT.md，总结现阶段情况，约定后续变更在此写日志。涉及：README 文档导航、DEVELOPMENT.md。
```

---

## 📅 开发日志

（以下按时间**倒序**，最新在最上）

---

### 2026-03-04

- **docs** 新增开发文档 `DEVELOPMENT.md`：约定每次变更在此记录日志；编写现阶段情况总结（阶段 Phase 3、版本 v1.0.0、已完成功能、待办、技术栈）；补充开发日志书写规范与示例。在 README 的「文档导航」中增加本文件链接。
- **涉及**：新建 `DEVELOPMENT.md`，修改 `README.md`（项目管理 · 文档导航表）。
