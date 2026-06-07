# MindDream 项目文件索引

本文档提供项目所有重要文件的快速导航。

---

## 📁 项目结构

```
项目根目录/
├── backend/                          # 后端项目
├── frontend/                         # 前端项目
├── data/                             # 数据文件
├── README.md                         # 项目说明
├── CHANGELOG.md                      # 更新日志
├── DEPLOYMENT.md                     # 部署指南
├── FILE_INDEX.md                     # 文件索引（本文件）
├── TROUBLESHOOTING.md                # 故障排查
├── LICENSE                           # MIT 许可证
├── .gitignore                        # Git 忽略规则
└── 启动项目.bat                      # 快速启动脚本
```

---

## 🔧 后端文件 (Spring Boot)

### 配置文件
- [pom.xml](backend/pom.xml) - Maven 依赖配置
- [application.yml](backend/src/main/resources/application.yml) - 应用配置
- [schema.sql](backend/src/main/resources/schema.sql) - 数据库表结构

### 主程序
- [MindDreamApplication.java](backend/src/main/java/com/minddream/MindDreamApplication.java) - 主入口

### 实体类 (Entity)
- [Dream.java](backend/src/main/java/com/minddream/entity/Dream.java) - 梦境实体
- [Statistics.java](backend/src/main/java/com/minddream/entity/Statistics.java) - 统计实体

### 数据访问层 (Repository)
- [DreamRepository.java](backend/src/main/java/com/minddream/repository/DreamRepository.java) - 梦境数据访问
- [StatisticsRepository.java](backend/src/main/java/com/minddream/repository/StatisticsRepository.java) - 统计数据访问

### 业务逻辑层 (Service)
- [DreamService.java](backend/src/main/java/com/minddream/service/DreamService.java) - 梦境业务逻辑
- [StatisticsService.java](backend/src/main/java/com/minddream/service/StatisticsService.java) - 统计业务逻辑

### 控制器 (Controller)
- [DreamController.java](backend/src/main/java/com/minddream/controller/DreamController.java) - 梦境API
- [StatisticsController.java](backend/src/main/java/com/minddream/controller/StatisticsController.java) - 统计API
- [AIAnalysisController.java](backend/src/main/java/com/minddream/controller/AIAnalysisController.java) - AI分析API ⭐

### DTO
- [SearchRequest.java](backend/src/main/java/com/minddream/dto/SearchRequest.java) - 搜索请求
- [StatisticsDTO.java](backend/src/main/java/com/minddream/dto/StatisticsDTO.java) - 统计数据传输

### 配置类 (Config)
- [CorsConfig.java](backend/src/main/java/com/minddream/config/CorsConfig.java) - 跨域配置
- [SwaggerConfig.java](backend/src/main/java/com/minddream/config/SwaggerConfig.java) - API文档配置

### 工具类 (Util)
- [DataImporter.java](backend/src/main/java/com/minddream/util/DataImporter.java) - 数据导入工具

---

## 🎨 前端文件 (Vue 3)

### 配置文件
- [package.json](frontend/package.json) - NPM 依赖
- [vite.config.js](frontend/vite.config.js) - Vite 配置
- [index.html](frontend/index.html) - HTML 入口

### 主程序
- [main.js](frontend/src/main.js) - Vue 入口
- [App.vue](frontend/src/App.vue) - 主组件

### 路由
- [router/index.js](frontend/src/router/index.js) - 路由配置

### API 客户端
- [api/dream.js](frontend/src/api/dream.js) - API 调用封装

### 视图组件 (Views)
- [Dashboard.vue](frontend/src/views/Dashboard.vue) - 数据仪表板 📊
- [DreamList.vue](frontend/src/views/DreamList.vue) - 梦境列表 📋
- [DreamDetail.vue](frontend/src/views/DreamDetail.vue) - 梦境详情 📄
- [Search.vue](frontend/src/views/Search.vue) - 高级搜索 🔍
- [Statistics.vue](frontend/src/views/Statistics.vue) - 统计分析 📈
- [AIAnalysis.vue](frontend/src/views/AIAnalysis.vue) - AI 分析 🤖 ⭐

### 样式
- [assets/styles/main.css](frontend/src/assets/styles/main.css) - 全局样式

---

## 📚 文档文件

### 项目文档
- [README.md](README.md) - 项目说明和快速开始
- [DEVELOPMENT.md](DEVELOPMENT.md) - 开发文档（现阶段总结 + 开发日志，每次变更需在此记录）
- [DEPLOYMENT.md](DEPLOYMENT.md) - 完整部署指南
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - 故障排查
- [FILE_INDEX.md](FILE_INDEX.md) - 本文件索引
- [CHANGELOG.md](CHANGELOG.md) - 更新日志
- [LICENSE](LICENSE) - MIT 许可证

---

## 🚀 启动脚本

- [启动项目.bat](启动项目.bat) - Windows 一键启动脚本

**使用方法**:
```bash
# Windows
双击运行 启动项目.bat

# 或手动启动
# 后端
cd backend
mvn spring-boot:run

# 前端 (新终端)
cd frontend
npm run dev
```

---

## 📊 数据文件

### 原始数据
- [data/train_parsed_part1.jsonl](data/train_parsed_part1.jsonl)
- [data/train_parsed_part2.jsonl](data/train_parsed_part2.jsonl)
- [data/train_parsed_part3.jsonl](data/train_parsed_part3.jsonl)

### 数据处理
- [codes/data.py](codes/data.py) - Python 数据解析脚本

---

## 🔗 快速链接

### 本地开发
- **前端应用**: http://localhost:5173
- **后端 API**: http://localhost:8080/api
- **API 文档**: http://localhost:8080/api/swagger-ui.html

### 主要功能页面
- 仪表板: http://localhost:5173/
- 梦境列表: http://localhost:5173/dreams
- 高级搜索: http://localhost:5173/search
- 数据统计: http://localhost:5173/statistics

---

## ⭐ 核心功能文件

### 最重要的文件 (必看)

**后端核心**:
1. [DreamController.java](backend/src/main/java/com/minddream/controller/DreamController.java) - 主要 API 端点
2. [AIAnalysisController.java](backend/src/main/java/com/minddream/controller/AIAnalysisController.java) - AI 功能
3. [DreamService.java](backend/src/main/java/com/minddream/service/DreamService.java) - 业务逻辑
4. [schema.sql](backend/src/main/resources/schema.sql) - 数据库结构

**前端核心**:
1. [Dashboard.vue](frontend/src/views/Dashboard.vue) - 首页仪表板
2. [AIAnalysis.vue](frontend/src/views/AIAnalysis.vue) - AI 分析页面
3. [api/dream.js](frontend/src/api/dream.js) - API 客户端
4. [main.css](frontend/src/assets/styles/main.css) - 样式系统

---

## 📝 文件说明

### 文件命名规范
- **Java**: 大驼峰 (PascalCase) - `DreamController.java`
- **Vue**: 大驼峰 (PascalCase) - `Dashboard.vue`
- **JavaScript**: 小驼峰 (camelCase) - `dream.js`
- **配置**: 小写+点 - `application.yml`

### 代码组织
- **后端**: 按层分包 (controller, service, repository, entity...)
- **前端**: 按功能分目录 (views, components, api...)

---

## 🎯 快速定位指南

**想要修改...**
- ✅ API 端点 → 查看 `backend/.../controller/`
- ✅ 数据库查询 → 查看 `backend/.../repository/`
- ✅ 业务逻辑 → 查看 `backend/.../service/`
- ✅ 页面UI → 查看 `frontend/src/views/`
- ✅ 全局样式 → 查看 `frontend/src/assets/styles/main.css`
- ✅ API调用 → 查看 `frontend/src/api/dream.js`
- ✅ 路由配置 → 查看 `frontend/src/router/index.js`

---

## 📈 项目统计

- **后端文件**: 15+ Java 类
- **前端文件**: 6 个 Vue 组件 + 配置
- **总代码行数**: ~5500 行
- **API 端点**: 15+
- **数据库表**: 2 个主表 + 多个索引

---

这个索引文件帮助您快速找到项目中的任何文件！🎯
