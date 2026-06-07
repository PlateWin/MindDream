# 快速故障排除指南

## ❌ 错误：ECONNREFUSED

**症状**：前端页面空白，控制台显示 `http proxy error` 和 `ECONNREFUSED`

**原因**：后端服务未运行

---

## ✅ 解决步骤

### 第一步：确认 MySQL 正在运行

```bash
# Windows: 检查服务
services.msc
# 找到 MySQL80，确保"正在运行"

# 或命令启动
net start MySQL80
```

### 第二步：配置数据库连接

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/minddream?...
    username: root
    password: 你的密码  # ← 改成你的 MySQL 密码
```

### 第三步：创建数据库

```sql
mysql -u root -p

CREATE DATABASE minddream;
USE minddream;

-- 然后执行 backend/src/main/resources/schema.sql 中的内容
```

### 第四步：启动后端

**方式1**：双击 `启动后端.bat`

**方式2**：命令行
```bash
cd d:/project/心理/backend
mvn clean install
mvn spring-boot:run
```

**成功标志**：看到
```
🌙 MindDream Backend Started!
```

### 第五步：验证

浏览器访问：http://localhost:8080/api/swagger-ui.html

应该能看到 API 文档页面。

---

## 🔍 常见问题

### Q1: Maven 下载依赖很慢
**A**: 配置国内镜像（阿里云）

在 `~/.m2/settings.xml` 添加：
```xml
<mirror>
  <id>aliyun</id>
  <mirrorOf>central</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

### Q2: 数据库连接失败
**A**: 检查
- MySQL 是否运行？
- 用户名密码是否正确？
- 数据库 `minddream` 是否已创建？

### Q3: 端口 8080 被占用
**A**: 
```bash
# 查找占用 8080 的进程
netstat -ano | findstr :8080

# 结束进程
taskkill /F /PID <进程ID>
```

### Q4: 没有数据显示
**A**: 需要导入数据

编辑 `backend/src/main/java/com/minddream/util/DataImporter.java`:
```java
private static final boolean AUTO_IMPORT_ENABLED = true;  // 改为 true
```

重启后端，数据会自动导入。

---

## 📝 检查清单

启动前确认：
- [ ] MySQL 服务正在运行
- [ ] 数据库 `minddream` 已创建
- [ ] `application.yml` 中的密码正确
- [ ] 后端成功启动（看到启动成功信息）
- [ ] 可以访问 http://localhost:8080/api/swagger-ui.html
- [ ] 前端可以显示数据

---

## 🆘 仍然有问题？

查看后端控制台的**完整错误信息**，可能是：
- 数据库连接错误
- 端口冲突
- 依赖下载失败

将错误信息提供给我，我可以进一步帮助您！
