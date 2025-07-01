@echo off
echo 航空机组排班系统启动脚本

echo ================================
echo 启动 MySQL 数据库服务...
net start MySQL
timeout /t 3 /nobreak

echo ================================
echo 启动后端服务...
cd /d %~dp0
start "后端服务" cmd /k "mvn spring-boot:run"
echo 等待后端服务启动...
timeout /t 10 /nobreak

echo ================================
echo 初始化前端服务...
cd frontend
start "前端初始化" cmd /k "npm install && npm run serve"

echo ================================
echo 航空机组排班系统就绪
echo 本地服务地址: http://localhost:8081
echo 网络服务地址: http://192.168.1.102:8081

pause 