@echo off
echo =======================================
echo   MindDream 心理梦境分析平台启动脚本
echo =======================================
echo.

REM 检查 Java
echo [1/4] 检查 Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Java，请安装 JDK 17+
    pause
    exit /b 1
)
echo [✓] Java 已安装

REM 检查 Node.js
echo [2/4] 检查 Node.js...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Node.js，请安装 Node.js 16+
    pause
    exit /b 1
)
echo [✓] Node.js 已安装

REM 启动后端
echo.
echo [3/4] 启动后端服务...
cd backend
start "MindDream Backend" cmd /k "mvn spring-boot:run"
cd ..

REM 等待后端启动
echo 等待后端服务启动 (10秒)...
timeout /t 10 /nobreak >nul

REM 启动前端
echo.
echo [4/4] 启动前端服务...
cd frontend

REM 检查是否已安装依赖
if not exist "node_modules" (
    echo 首次运行，正在安装依赖...
    call npm install
)

start "MindDream Frontend" cmd /k "npm run dev"
cd ..

echo.
echo =======================================
echo   启动完成！
echo =======================================
echo.
echo 后端服务: http://localhost:8080/api
echo API 文档: http://localhost:8080/api/swagger-ui.html
echo 前端应用: http://localhost:5173
echo.
echo 按任意键打开浏览器...
pause >nul

REM 打开浏览器
start http://localhost:5173

echo.
echo 提示: 关闭此窗口不会停止服务
echo 要停止服务，请关闭后端和前端的命令窗口
echo.
pause
