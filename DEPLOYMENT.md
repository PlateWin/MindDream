# MindDream 部署指南

本文档提供 MindDream 平台的完整部署说明。

---

## ✅ 上线前检查清单

部署到生产前请确认：

| 检查项 | 说明 |
|--------|------|
| 数据库 | 已创建 `minddream` 库并执行 `schema.sql`，生产库账号权限最小化 |
| 配置 | 使用 `application-prod.yml` 或环境变量，**勿将密码提交到仓库** |
| 后端 | `mvn clean package -DskipTests` 通过，JAR 可正常启动 |
| 前端 | `npm run build` 通过，`dist/` 可被 Web 服务器正确托管 |
| API 代理 | Nginx 中 `/api/` 正确代理到后端端口（如 8080） |
| 健康检查 | 可访问 `/api/dreams/count` 与 `/api/swagger-ui.html` |
| 日志 | 生产环境关闭 `show-sql`，配置日志文件与轮转 |
| 版本与文档 | 版本号与 [CHANGELOG.md](CHANGELOG.md) 一致，README 中的快速开始与部署链接有效 |

---

## 📋 部署前准备

### 系统要求

**服务器配置（最低）**
- CPU: 2核
- 内存: 4GB
- 磁盘: 20GB
- 操作系统: Windows Server 2016+ / Ubuntu 18.04+

**软件要求**
- Java JDK 17+
- MySQL 8.0+
- Node.js 16+
- Maven 3.6+

## 🗄️ 数据库部署

### 1. 安装 MySQL 8.0

**Windows:**
```bash
# 下载安装包
https://dev.mysql.com/downloads/mysql/

# 配置 MySQL 服务
# 默认端口: 3306
```

**Linux:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

### 2. 创建数据库

```bash
# 登录 MySQL
mysql -u root -p

# 执行建库脚本
mysql> source d:/project/心理/backend/src/main/resources/schema.sql

# 验证
mysql> USE minddream;
mysql> SHOW TABLES;
```

### 3. 导入数据

**方式一：使用 DataImporter（推荐）**
1. 编辑 `backend/src/main/java/com/minddream/util/DataImporter.java`
2. 设置 `AUTO_IMPORT_ENABLED = true`
3. 启动后端，数据自动导入

**方式二：手动 SQL 导入**
```sql
-- 将 JSONL 转换为 SQL INSERT 语句
-- 或使用数据库导入工具
```

## 🔧 后端部署

### 开发环境部署

```bash
cd d:/project/心理/backend

# 编译项目
mvn clean install

# 运行
mvn spring-boot:run
```

### 生产环境部署

**1. 打包应用**
```bash
cd d:/project/心理/backend
mvn clean package -DskipTests

# 生成的 JAR 文件位于:
# target/minddream-backend-1.0.0.jar
```

**2. 配置生产环境**

在 `backend/src/main/resources/` 下创建 `application-prod.yml`（**勿提交到 Git**，已加入 `.gitignore`）:
```yaml
spring:
  datasource:
    url: jdbc:mysql://your-db-server:3306/minddream
    username: your_username
    password: your_password
  
  jpa:
    hibernate:
      ddl-auto: validate  # 生产环境不自动更新表结构
    show-sql: false

server:
  port: 8080

logging:
  level:
    com.minddream: INFO
    org.springframework: WARN
```

**3. 启动服务**

**Windows:**
```batch
java -jar -Dspring.profiles.active=prod target/minddream-backend-1.0.0.jar
```

**Linux (使用 systemd):**

创建 `/etc/systemd/system/minddream-backend.service`:
```ini
[Unit]
Description=MindDream Backend Service
After=mysql.service

[Service]
Type=simple
User=minddream
WorkingDirectory=/opt/minddream
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod minddream-backend-1.0.0.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

启动服务:
```bash
sudo systemctl daemon-reload
sudo systemctl start minddream-backend
sudo systemctl enable minddream-backend
sudo systemctl status minddream-backend
```

## 🎨 前端部署

### 开发环境部署

```bash
cd d:/project/心理/frontend

# 安装依赖
npm install

# 运行开发服务器
npm run dev
```

### 生产环境部署

**1. 构建生产版本**
```bash
cd d:/project/心理/frontend
npm install
npm run build

# 生成的文件在 dist/ 目录
```

**2. 配置 API 地址**

如果前后端不在同一服务器，编辑 `vite.config.js`:
```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://your-backend-server:8080',
      changeOrigin: true
    }
  }
}
```

**3. 使用 Nginx 部署**

安装 Nginx:
```bash
# Windows
# 下载 nginx-1.24.0.zip
# 解压到 C:\nginx

# Linux
sudo apt install nginx
```

配置 Nginx (`nginx.conf` 或 `/etc/nginx/sites-available/minddream`):
```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    root /var/www/minddream/frontend/dist;
    index index.html;

    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 代理后端 API
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

启动 Nginx:
```bash
# Windows
cd C:\nginx
start nginx

# Linux
sudo systemctl start nginx
sudo systemctl enable nginx
```

## 🔒 安全配置

### 1. HTTPS 配置

使用 Let's Encrypt 免费证书 (Linux):
```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d your-domain.com
```

### 2. 数据库安全

```sql
-- 创建专用数据库用户
CREATE USER 'minddream'@'localhost' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON minddream.* TO 'minddream'@'localhost';
FLUSH PRIVILEGES;

-- 限制远程访问
-- 编辑 /etc/mysql/mysql.conf.d/mysqld.cnf
bind-address = 127.0.0.1
```

### 3. 防火墙配置

```bash
# Linux (ufw)
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable

# Windows Firewall
# 打开 Windows Defender 防火墙
# 添加入站规则: 端口 80, 443
```

## 📊 性能优化

### 数据库优化

**1. 添加索引**
```sql
-- 已在 schema.sql 中包含
CREATE INDEX idx_gender ON dreams(gender);
CREATE INDEX idx_age ON dreams(age_group);
CREATE FULLTEXT INDEX idx_report ON dreams(report);
```

**2. 优化查询**
```sql
-- MySQL 配置优化 (my.cnf)
[mysqld]
innodb_buffer_pool_size = 1G
max_connections = 200
query_cache_size = 32M
```

### 应用优化

**后端 JVM 参数**
```bash
java -jar \
  -Xms512m \
  -Xmx2g \
  -XX:+UseG1GC \
  -Dspring.profiles.active=prod \
  minddream-backend-1.0.0.jar
```

**前端缓存**
- 使用 Nginx gzip 压缩
- 静态资源 CDN
- 启用浏览器缓存

## 📝 监控与维护

### 日志管理

**后端日志**
```bash
# 配置日志文件 (application-prod.yml)
logging:
  file:
    name: /var/log/minddream/backend.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

**访问日志**
```nginx
# Nginx 访问日志
access_log /var/log/nginx/minddream-access.log;
error_log /var/log/nginx/minddream-error.log;
```

### 数据备份

```bash
# 每日自动备份脚本
#!/bin/bash
BACKUP_DIR=/backup/minddream
DATE=$(date +%Y%m%d)

mysqldump -u root -p minddream > $BACKUP_DIR/minddream_$DATE.sql
gzip $BACKUP_DIR/minddream_$DATE.sql

# 保留最近 30 天备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete
```

设置 cron 任务:
```bash
crontab -e
# 添加: 每天凌晨 2 点备份
0 2 * * * /path/to/backup.sh
```

## 🆘 故障排除

### 后端无法启动

**检查日志:**
```bash
# 查看启动日志
tail -f /var/log/minddream/backend.log

# 常见问题:
# 1. 端口被占用: 修改 application.yml 中的端口
# 2. 数据库连接失败: 检查数据库配置和网络
# 3. 内存不足: 增加 JVM 堆内存
```

### 前端无法访问

**检查 Nginx:**
```bash
# 测试配置
sudo nginx -t

# 查看错误日志
sudo tail -f /var/log/nginx/error.log

# 重启 Nginx
sudo systemctl restart nginx
```

### 数据库连接超时

```sql
-- 检查连接数
SHOW VARIABLES LIKE 'max_connections';
SHOW STATUS LIKE 'Threads_connected';

-- 增加最大连接数
SET GLOBAL max_connections = 500;
```

## ✅ 部署验证

部署完成后，执行以下检查:

1. **后端健康检查**
   - 访问: http://your-server:8080/api/dreams/count
   - 应返回梦境总数

2. **API 文档访问**
   - 访问: http://your-server:8080/api/swagger-ui.html
   - 应显示完整 API 文档

3. **前端功能测试**
   - 访问: http://your-domain.com
   - 测试仪表板、搜索、统计等功能

4. **性能测试**
   ```bash
   # 使用 Apache Bench
   ab -n 1000 -c 10 http://your-server:8080/api/dreams?page=0&size=10
   ```

## 📞 技术支持

如有部署问题，请联系技术团队:
- Email: support@minddream.com
- 文档: https://minddream.com/docs
