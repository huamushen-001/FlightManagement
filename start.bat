@echo off
echo ���ջ����Ű�ϵͳ�����ű�

echo ================================
echo ���� MySQL ���ݿ����...
net start MySQL
timeout /t 3 /nobreak

echo ================================
echo ������˷���...
cd /d %~dp0
start "��˷���" cmd /k "mvn spring-boot:run"
echo �ȴ���˷�������...
timeout /t 10 /nobreak

echo ================================
echo ��ʼ��ǰ�˷���...
cd frontend
start "ǰ�˳�ʼ��" cmd /k "npm install && npm run serve"

echo ================================
echo ���ջ����Ű�ϵͳ����
echo ���ط����ַ: http://localhost:8081
echo ��������ַ: http://192.168.1.102:8081

pause 