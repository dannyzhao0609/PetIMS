-- 宠物管理系统数据库初始化脚本

CREATE DATABASE IF NOT EXISTS petims DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE petims;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    UNIQUE KEY uk_username_tenant (username, tenant_id),
    UNIQUE KEY uk_email_tenant (email, tenant_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 宠物表
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '宠物ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    species VARCHAR(50) COMMENT '品种',
    breed VARCHAR(50) COMMENT '亚种',
    gender VARCHAR(10) COMMENT '性别',
    birthday DATE COMMENT '出生日期',
    weight DECIMAL(5,2) COMMENT '体重',
    height DECIMAL(5,2) COMMENT '身高',
    avatar VARCHAR(255) COMMENT '头像',
    remarks TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物表';

-- 健康记录表
CREATE TABLE IF NOT EXISTS health_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    record_type VARCHAR(50) NOT NULL COMMENT '记录类型(疫苗/疾病/体检/用药)',
    title VARCHAR(100) COMMENT '标题',
    description TEXT COMMENT '描述',
    date DATE COMMENT '日期',
    attachments VARCHAR(255) COMMENT '附件路径',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_pet_id (pet_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_record_type (record_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康记录表';

-- 喂食记录表
CREATE TABLE IF NOT EXISTS feeding_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    food_name VARCHAR(100) COMMENT '食物名称',
    amount DECIMAL(5,2) COMMENT '数量',
    unit VARCHAR(20) COMMENT '单位',
    feeding_time DATETIME COMMENT '喂食时间',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_pet_id (pet_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_feeding_time (feeding_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='喂食记录表';

-- 卫生记录表
CREATE TABLE IF NOT EXISTS hygiene_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    record_type VARCHAR(50) NOT NULL COMMENT '记录类型(洗澡/美容/驱虫)',
    date DATE COMMENT '日期',
    description TEXT COMMENT '描述',
    attachments VARCHAR(255) COMMENT '附件路径',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_pet_id (pet_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_record_type (record_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卫生记录表';

-- 日常监控表
CREATE TABLE IF NOT EXISTS monitoring_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    record_type VARCHAR(50) NOT NULL COMMENT '记录类型(活动量/睡眠/位置)',
    value VARCHAR(100) COMMENT '数值',
    unit VARCHAR(20) COMMENT '单位',
    record_time DATETIME COMMENT '记录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_pet_id (pet_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_record_type (record_type),
    INDEX idx_record_time (record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日常监控表';

-- 帖子表
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    pet_id BIGINT COMMENT '宠物ID',
    title VARCHAR(100) COMMENT '标题',
    content TEXT COMMENT '内容',
    attachments VARCHAR(255) COMMENT '附件路径',
    likes INT DEFAULT 0 COMMENT '点赞数',
    comments INT DEFAULT 0 COMMENT '评论数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    INDEX idx_user_id (user_id),
    INDEX idx_pet_id (pet_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- 插入测试数据
INSERT INTO users (tenant_id, username, password, email, phone) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6z8i', 'admin@petims.com', '13800138000'),
(1, 'testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6z8i', 'test@petims.com', '13900139000');

-- 插入宠物数据
INSERT INTO pets (tenant_id, user_id, name, species, breed, gender, birthday, weight, height, remarks) VALUES
(1, 1, '小白', '狗', '金毛', '公', '2022-01-15', 25.5, 60.0, '活泼可爱'),
(1, 1, '喵喵', '猫', '英短', '母', '2023-03-20', 4.2, 30.0, '温顺乖巧'),
(1, 2, '旺财', '狗', '柴犬', '公', '2022-06-10', 10.5, 40.0, '忠诚护主');

-- 插入健康记录
INSERT INTO health_records (tenant_id, pet_id, record_type, title, description, date) VALUES
(1, 1, '疫苗', '狂犬疫苗', '接种狂犬疫苗', '2024-01-10'),
(1, 1, '体检', '年度体检', '健康状况良好', '2024-02-15'),
(1, 2, '疫苗', '猫三联', '接种猫三联疫苗', '2024-03-01');

-- 插入喂食记录
INSERT INTO feeding_records (tenant_id, pet_id, food_name, amount, unit, feeding_time, notes) VALUES
(1, 1, '狗粮', 200.0, 'g', '2024-12-01 08:00:00', '早餐'),
(1, 1, '狗粮', 250.0, 'g', '2024-12-01 18:00:00', '晚餐'),
(1, 2, '猫粮', 80.0, 'g', '2024-12-01 09:00:00', '早餐'),
(1, 2, '猫粮', 90.0, 'g', '2024-12-01 19:00:00', '晚餐');

-- 插入卫生记录
INSERT INTO hygiene_records (tenant_id, pet_id, record_type, date, description) VALUES
(1, 1, '洗澡', '2024-11-25', '全身清洗'),
(1, 1, '驱虫', '2024-11-20', '体外驱虫'),
(1, 2, '洗澡', '2024-11-28', '全身清洗');

-- 插入监控记录
INSERT INTO monitoring_records (tenant_id, pet_id, record_type, value, unit, record_time) VALUES
(1, 1, '活动量', '5200', '步', '2024-12-01 23:59:59'),
(1, 1, '睡眠', '12', '小时', '2024-12-01 23:59:59'),
(1, 2, '活动量', '1800', '步', '2024-12-01 23:59:59'),
(1, 2, '睡眠', '16', '小时', '2024-12-01 23:59:59');

-- 传感器表
CREATE TABLE IF NOT EXISTS sensors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '传感器ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    sensor_code VARCHAR(50) NOT NULL COMMENT '传感器编号',
    sensor_name VARCHAR(100) COMMENT '传感器名称',
    sensor_type VARCHAR(50) NOT NULL COMMENT '传感器类型(GPS/蓝牙)',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态(active/inactive)',
    battery_level INT COMMENT '电量百分比',
    last_active_time DATETIME COMMENT '最后活跃时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    UNIQUE KEY uk_sensor_code_tenant (sensor_code, tenant_id),
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='传感器表';

-- 宠物-传感器绑定表
CREATE TABLE IF NOT EXISTS pet_sensor_bindings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '绑定ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    sensor_id BIGINT NOT NULL COMMENT '传感器ID',
    bind_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    unbind_time DATETIME COMMENT '解绑时间',
    status VARCHAR(20) DEFAULT 'bound' COMMENT '状态(bound/unbound)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
    UNIQUE KEY uk_pet_sensor_tenant (pet_id, sensor_id, tenant_id),
    INDEX idx_pet_id (pet_id),
    INDEX idx_sensor_id (sensor_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物-传感器绑定表';

-- 位置跟踪表
CREATE TABLE IF NOT EXISTS location_tracks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '位置ID',
    tenant_id BIGINT NOT NULL DEFAULT 1 COMMENT '租户ID',
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    sensor_id BIGINT COMMENT '传感器ID',
    latitude DECIMAL(10, 7) NOT NULL COMMENT '纬度',
    longitude DECIMAL(10, 7) NOT NULL COMMENT '经度',
    address VARCHAR(255) COMMENT '地址描述',
    track_time DATETIME NOT NULL COMMENT '跟踪时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_pet_id (pet_id),
    INDEX idx_sensor_id (sensor_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_track_time (track_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='位置跟踪表';

-- 插入帖子数据
INSERT INTO posts (tenant_id, user_id, pet_id, title, content, likes, comments) VALUES
(1, 1, 1, '小白的幸福生活', '今天带小白去公园玩耍，开心极了！', 15, 3),
(1, 1, 2, '喵喵的慵懒日常', '喵喵今天又在阳光下睡了一下午~', 28, 8),
(1, 2, 3, '旺财的第一次旅行', '带旺财去郊外露营，他玩得超开心！', 42, 12);

-- 插入传感器数据
INSERT INTO sensors (tenant_id, user_id, sensor_code, sensor_name, sensor_type, status, battery_level, last_active_time) VALUES
(1, 1, 'GPS001', '小白的定位器', 'GPS', 'active', 85, NOW()),
(1, 1, 'GPS002', '喵喵的定位器', 'GPS', 'active', 92, NOW()),
(1, 2, 'GPS003', '旺财的定位器', 'GPS', 'inactive', 45, '2024-11-15 10:30:00');

-- 插入传感器绑定数据
INSERT INTO pet_sensor_bindings (tenant_id, pet_id, sensor_id, status) VALUES
(1, 1, 1, 'bound'),
(1, 2, 2, 'bound');

-- 插入位置跟踪数据
INSERT INTO location_tracks (tenant_id, pet_id, sensor_id, latitude, longitude, address, track_time) VALUES
(1, 1, 1, 39.9042000, 116.4074000, '北京市朝阳区天安门附近', DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
(1, 1, 1, 39.9045000, 116.4078000, '北京市朝阳区王府井', DATE_SUB(NOW(), INTERVAL 5 MINUTE)),
(1, 1, 1, 39.9048000, 116.4082000, '北京市朝阳区东方广场', NOW()),
(1, 2, 2, 31.2304000, 121.4737000, '上海市黄浦区外滩', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(1, 2, 2, 31.2307000, 121.4740000, '上海市黄浦区南京路', NOW());
