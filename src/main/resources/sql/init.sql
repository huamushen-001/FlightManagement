-- 创建数据库
CREATE DATABASE IF NOT EXISTS flight_crew_scheduling DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE flight_crew_scheduling;

-- 机组成员表
CREATE TABLE IF NOT EXISTS crew_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    employee_id VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
    position VARCHAR(20) NOT NULL COMMENT '岗位',
    qualification_level VARCHAR(20) NOT NULL COMMENT '资质等级',
    contact VARCHAR(20) NOT NULL COMMENT '联系方式',
    email VARCHAR(100) COMMENT '邮箱',
    status VARCHAR(20) DEFAULT '在职' COMMENT '状态',
    hire_date DATETIME COMMENT '入职日期',
    max_flight_hours INT DEFAULT 0 COMMENT '最大飞行小时数',
    current_flight_hours INT DEFAULT 0 COMMENT '当前飞行小时数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志'
) COMMENT '机组成员表';

-- 航班表
CREATE TABLE IF NOT EXISTS flight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    flight_number VARCHAR(20) NOT NULL UNIQUE COMMENT '航班号',
    departure_airport VARCHAR(50) NOT NULL COMMENT '出发机场',
    arrival_airport VARCHAR(50) NOT NULL COMMENT '到达机场',
    departure_time DATETIME NOT NULL COMMENT '出发时间',
    arrival_time DATETIME NOT NULL COMMENT '到达时间',
    flight_duration INT COMMENT '飞行时长（分钟）',
    aircraft_type VARCHAR(20) COMMENT '机型',
    status VARCHAR(20) DEFAULT '计划' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志'
) COMMENT '航班表';

-- 排班表
CREATE TABLE IF NOT EXISTS schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    flight_id BIGINT NOT NULL COMMENT '航班ID',
    crew_member_id BIGINT NOT NULL COMMENT '机组成员ID',
    role VARCHAR(20) COMMENT '角色',
    status VARCHAR(20) DEFAULT '已分配' COMMENT '状态',
    work_hours INT DEFAULT 0 COMMENT '工作时长（小时）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志',
    FOREIGN KEY (flight_id) REFERENCES flight(id),
    FOREIGN KEY (crew_member_id) REFERENCES crew_member(id)
) COMMENT '排班表';

-- 创建索引
CREATE INDEX idx_crew_member_name ON crew_member(name);
CREATE INDEX idx_crew_member_position ON crew_member(position);
CREATE INDEX idx_crew_member_qualification ON crew_member(qualification_level);
CREATE INDEX idx_flight_number ON flight(flight_number);
CREATE INDEX idx_flight_departure_time ON flight(departure_time);
CREATE INDEX idx_flight_route ON flight(departure_airport, arrival_airport);
CREATE INDEX idx_schedule_flight_id ON schedule(flight_id);
CREATE INDEX idx_schedule_crew_member_id ON schedule(crew_member_id);
CREATE INDEX idx_schedule_time ON schedule(create_time);

-- 插入测试数据
INSERT INTO crew_member (name, employee_id, position, qualification_level, contact, email, hire_date, max_flight_hours) VALUES
('张三', 'CM001', '飞行员', '高级', '13800138001', 'zhangsan@airline.com', '2020-01-01 00:00:00', 2000),
('李四', 'CM002', '飞行员', '中级', '13800138002', 'lisi@airline.com', '2021-03-15 00:00:00', 1500),
('王五', 'CM003', '空乘', '高级', '13800138003', 'wangwu@airline.com', '2019-06-20 00:00:00', 1000),
('赵六', 'CM004', '空乘', '中级', '13800138004', 'zhaoliu@airline.com', '2022-01-10 00:00:00', 800),
('孙七', 'CM005', '维修工程师', '高级', '13800138005', 'sunqi@airline.com', '2018-09-01 00:00:00', 1200);

INSERT INTO flight (flight_number, departure_airport, arrival_airport, departure_time, arrival_time, aircraft_type) VALUES
('CA1234', '北京首都国际机场', '上海浦东国际机场', '2024-01-15 08:00:00', '2024-01-15 10:30:00', 'B737-800'),
('CA5678', '上海浦东国际机场', '广州白云国际机场', '2024-01-15 14:00:00', '2024-01-15 16:30:00', 'A320-200'),
('MU9012', '广州白云国际机场', '深圳宝安国际机场', '2024-01-15 18:00:00', '2024-01-15 19:00:00', 'B737-700'),
('MU3456', '深圳宝安国际机场', '北京首都国际机场', '2024-01-16 07:00:00', '2024-01-16 09:30:00', 'A330-300');

INSERT INTO schedule (flight_id, crew_member_id, role, work_hours) VALUES
(1, 1, '机长', 2),
(1, 2, '副驾驶', 2),
(1, 3, '乘务长', 2),
(1, 4, '乘务员', 2),
(2, 1, '机长', 2),
(2, 2, '副驾驶', 2),
(2, 3, '乘务长', 2),
(3, 2, '机长', 1),
(3, 4, '乘务员', 1),
(4, 1, '机长', 2),
(4, 3, '乘务长', 2); 