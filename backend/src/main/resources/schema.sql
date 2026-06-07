-- 创建数据库
CREATE DATABASE IF NOT EXISTS minddream DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE minddream;

-- 梦境数据表
CREATE TABLE IF NOT EXISTS dreams (
    id VARCHAR(50) PRIMARY KEY COMMENT '梦境唯一标识',
    name VARCHAR(200) COMMENT '梦境者名称',
    dream_number INT COMMENT '梦境编号',
    time_period VARCHAR(100) COMMENT '时间段',
    birth_year VARCHAR(50) COMMENT '出生年份',
    gender CHAR(1) COMMENT '性别: M-男性, F-女性',
    age_group CHAR(1) COMMENT '年龄组: A-成人, C-儿童, O-老年',
    report TEXT COMMENT '梦境报告内容',
    characters_raw VARCHAR(500) COMMENT '角色原始编码',
    characters_cn TEXT COMMENT '角色中文描述',
    emotions_raw VARCHAR(500) COMMENT '情感原始编码',
    emotions_cn TEXT COMMENT '情感中文描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_gender (gender),
    INDEX idx_age (age_group),
    INDEX idx_time (time_period),
    INDEX idx_birth_year (birth_year),
    FULLTEXT idx_report (report)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='梦境数据表';

-- 统计数据表
CREATE TABLE IF NOT EXISTS statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    stat_type VARCHAR(50) NOT NULL COMMENT '统计类型: gender, age, emotion, character, timeline',
    stat_key VARCHAR(100) NOT NULL COMMENT '统计键',
    stat_value VARCHAR(500) COMMENT '统计值',
    count INT DEFAULT 0 COMMENT '计数',
    percentage DECIMAL(5,2) COMMENT '百分比',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (stat_type),
    INDEX idx_key (stat_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据表';
