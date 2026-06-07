@echo off
chcp 65001 > nul
cls
echo ========================================
echo   MindDream 后端启动 (UTF-8)
echo ========================================
echo.

cd /d D:\project\心理\backend

echo [启动中] 正在启动后端服务...
echo.
echo 提示: 
echo - 首次启动会下载依赖，需要等待
echo - 日志现在应该能正确显示中文
echo - 等待看到 "MindDream Backend Started!" 即启动成功
echo.

set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
set MAVEN_OPTS=-Dfile.encoding=UTF-8

mvn spring-boot:run

pause
