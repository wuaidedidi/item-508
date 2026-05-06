-- 设置字符集确保中文正确存储
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_client=utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS health_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE health_db;

-- 设置数据库字符集
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100),
    `phone` VARCHAR(20),
    `avatar` VARCHAR(255),
    `nickname` VARCHAR(50),
    `gender` TINYINT DEFAULT 0 COMMENT '0-未知 1-男 2-女',
    `birthday` VARCHAR(20),
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT 'ADMIN/USER/DOCTOR',
    `status` TINYINT DEFAULT 1 COMMENT '0-禁用 1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 健康档案表
CREATE TABLE IF NOT EXISTS `health_profile` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `height` DECIMAL(5,2) COMMENT '身高(cm)',
    `weight` DECIMAL(5,2) COMMENT '体重(kg)',
    `blood_type` VARCHAR(10) COMMENT '血型',
    `allergies` TEXT COMMENT '过敏史',
    `medical_history` TEXT COMMENT '病史',
    `family_history` TEXT COMMENT '家族病史',
    `emergency_contact` VARCHAR(50) COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) COMMENT '紧急联系电话',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 体重记录表
CREATE TABLE IF NOT EXISTS `weight_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `weight` DECIMAL(5,2) NOT NULL COMMENT '体重(kg)',
    `record_date` DATE NOT NULL,
    `note` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 血压记录表
CREATE TABLE IF NOT EXISTS `blood_pressure` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `systolic` INT NOT NULL COMMENT '收缩压(mmHg)',
    `diastolic` INT NOT NULL COMMENT '舒张压(mmHg)',
    `pulse` INT COMMENT '脉搏(次/分)',
    `record_date` DATE NOT NULL,
    `measure_time` VARCHAR(20) COMMENT 'morning/noon/evening',
    `note` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 血糖记录表
CREATE TABLE IF NOT EXISTS `blood_sugar` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `value` DECIMAL(4,2) NOT NULL COMMENT '血糖值(mmol/L)',
    `measure_time` VARCHAR(20) COMMENT 'fasting/before_meal/after_meal/bedtime',
    `record_date` DATE NOT NULL,
    `note` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 心率记录表
CREATE TABLE IF NOT EXISTS `heart_rate` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `rate` INT NOT NULL COMMENT '心率(次/分)',
    `status` VARCHAR(20) COMMENT 'rest/exercise/sleep',
    `record_date` DATE NOT NULL,
    `note` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 预约表
CREATE TABLE IF NOT EXISTS `appointment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `doctor_name` VARCHAR(50),
    `department` VARCHAR(50),
    `hospital` VARCHAR(100),
    `appointment_time` DATETIME NOT NULL,
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/completed/cancelled',
    `type` VARCHAR(20) COMMENT 'checkup/consultation/followup',
    `note` TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `summary` VARCHAR(500),
    `content` TEXT NOT NULL,
    `cover_image` VARCHAR(255),
    `category` VARCHAR(50) COMMENT 'nutrition/exercise/mental/disease/lifestyle',
    `author` VARCHAR(50),
    `view_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1 COMMENT '0-草稿 1-已发布',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===================== 初始化数据 =====================

-- 管理员账号 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`, `role`, `status`, `deleted`) VALUES
('admin', '$2a$10$EqKcp1WFKVQISheBxkVJdeL.jRFVKPHpw36DqNc5q2LJpIqLnEUlG', 'admin@health.com', '13800000001', 'Administrator', 'ADMIN', 1, 0);

-- 测试用户账号 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`, `gender`, `birthday`, `role`, `status`, `deleted`) VALUES
('user', '$2a$10$EqKcp1WFKVQISheBxkVJdeL.jRFVKPHpw36DqNc5q2LJpIqLnEUlG', 'user@test.com', '13800000002', 'Test User', 1, '1990-01-15', 'USER', 1, 0),
('zhangsan', '$2a$10$EqKcp1WFKVQISheBxkVJdeL.jRFVKPHpw36DqNc5q2LJpIqLnEUlG', 'zhangsan@test.com', '13800000003', 'Zhang San', 1, '1985-06-20', 'USER', 1, 0),
('lisi', '$2a$10$EqKcp1WFKVQISheBxkVJdeL.jRFVKPHpw36DqNc5q2LJpIqLnEUlG', 'lisi@test.com', '13800000004', 'Li Si', 2, '1992-03-10', 'USER', 1, 0);

-- 健康档案
INSERT INTO `health_profile` (`user_id`, `height`, `weight`, `blood_type`, `allergies`, `medical_history`, `emergency_contact`, `emergency_phone`) VALUES
(2, 175.0, 70.0, 'A', 'None', 'None', 'Family Contact', '13900000001'),
(3, 178.0, 75.0, 'B', 'Penicillin Allergy', 'Hypertension History', 'Wife', '13900000002'),
(4, 165.0, 55.0, 'O', 'Pollen Allergy', 'None', 'Husband', '13900000003');

-- 体重记录
INSERT INTO `weight_record` (`user_id`, `weight`, `record_date`, `note`) VALUES
(2, 70.5, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'Morning measurement'),
(2, 70.3, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Morning measurement'),
(2, 70.0, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Morning measurement'),
(2, 69.8, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'After exercise'),
(2, 69.5, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Morning measurement'),
(2, 69.3, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Morning measurement'),
(2, 69.0, CURDATE(), 'Morning measurement');

-- 血压记录
INSERT INTO `blood_pressure` (`user_id`, `systolic`, `diastolic`, `pulse`, `record_date`, `measure_time`, `note`) VALUES
(2, 120, 80, 72, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'morning', 'Normal'),
(2, 118, 78, 70, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'morning', 'Normal'),
(2, 122, 82, 75, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'evening', 'After dinner'),
(2, 119, 79, 71, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'morning', 'Normal'),
(2, 121, 81, 73, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'morning', 'Normal'),
(2, 117, 77, 69, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'morning', 'Good'),
(2, 120, 80, 72, CURDATE(), 'morning', 'Normal');

-- 血糖记录
INSERT INTO `blood_sugar` (`user_id`, `value`, `measure_time`, `record_date`, `note`) VALUES
(2, 5.2, 'fasting', DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'Normal'),
(2, 7.8, 'after_meal', DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'After lunch'),
(2, 5.0, 'fasting', DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Normal'),
(2, 8.1, 'after_meal', DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'After dinner'),
(2, 5.3, 'fasting', DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Normal'),
(2, 5.1, 'fasting', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Good'),
(2, 5.0, 'fasting', CURDATE(), 'Excellent');

-- 心率记录
INSERT INTO `heart_rate` (`user_id`, `rate`, `status`, `record_date`, `note`) VALUES
(2, 68, 'rest', DATE_SUB(CURDATE(), INTERVAL 6 DAY), 'Morning rest'),
(2, 72, 'rest', DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Morning rest'),
(2, 110, 'exercise', DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'After running'),
(2, 70, 'rest', DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Morning rest'),
(2, 65, 'sleep', DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'During sleep'),
(2, 71, 'rest', DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Morning rest'),
(2, 69, 'rest', CURDATE(), 'Morning rest');

-- 预约记录
INSERT INTO `appointment` (`user_id`, `doctor_name`, `department`, `hospital`, `appointment_time`, `status`, `type`, `note`) VALUES
(2, 'Dr. Wang', 'Internal Medicine', 'City Hospital', DATE_ADD(NOW(), INTERVAL 2 DAY), 'pending', 'checkup', 'Annual checkup'),
(2, 'Dr. Li', 'Cardiology', 'Central Hospital', DATE_ADD(NOW(), INTERVAL 5 DAY), 'pending', 'consultation', 'Heart examination'),
(2, 'Dr. Zhang', 'General Practice', 'Community Clinic', DATE_SUB(NOW(), INTERVAL 7 DAY), 'completed', 'followup', 'Follow-up visit'),
(3, 'Dr. Chen', 'Cardiology', 'City Hospital', DATE_ADD(NOW(), INTERVAL 3 DAY), 'pending', 'consultation', 'Blood pressure check');

-- 健康文章
INSERT INTO `article` (`title`, `summary`, `content`, `cover_image`, `category`, `author`, `view_count`, `status`) VALUES
('健康饮食的10个秘诀', '保持身体健康和预防疾病的必备营养建议。', 
'<h2>引言</h2><p>健康的饮食是良好健康的基础。这里有10个基本建议，帮助您保持均衡和营养的饮食。</p>
<h3>1. 食物多样化</h3><p>每天摄入不同种类的食物，确保获得所有必需的营养素。</p>
<h3>2. 控制份量</h3><p>注意进食量。使用较小的盘子可以帮助控制份量。</p>
<h3>3. 增加蔬菜摄入</h3><p>每餐争取让蔬菜占据盘子的一半。</p>
<h3>4. 选择全谷物</h3><p>用全谷物代替精制谷物，以获取更多纤维和营养。</p>
<h3>5. 限制糖分摄入</h3><p>减少含糖饮料和零食的摄入。</p>
<h3>6. 保持充足水分</h3><p>每天至少喝8杯水。</p>
<h3>7. 摄入优质蛋白</h3><p>选择鱼、家禽、豆类和坚果作为蛋白质来源。</p>
<h3>8. 减少盐分摄入</h3><p>每日钠摄入量限制在2300毫克以下。</p>
<h3>9. 规划饮食</h3><p>饮食计划有助于避免不健康的食物选择。</p>
<h3>10. 享受美食</h3><p>细嚼慢咽，专注进食，有助于消化。</p>', 
'https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=400', '健康饮食', '陈医生', 256, 1),

('规律运动的益处', '探索规律的体育活动如何改变您的健康和福祉。',
'<h2>为什么要运动？</h2><p>规律的体育活动是您为健康所能做的最重要的事情之一。</p>
<h3>身体益处</h3>
<ul>
<li>增强肌肉和骨骼</li>
<li>改善心血管健康</li>
<li>帮助维持健康体重</li>
<li>降低患慢性病的风险</li>
</ul>
<h3>心理益处</h3>
<ul>
<li>减轻压力和焦虑</li>
<li>改善情绪和睡眠</li>
<li>提高精力水平</li>
<li>增强认知功能</li>
</ul>
<h3>如何开始</h3>
<p>每周5天，每天进行30分钟的中等强度活动。这可以包括散步、游泳、骑自行车或任何您喜欢的活动。</p>',
'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400', '运动健身', '李教练', 189, 1),

('日常压力管理', '减轻压力和改善心理健康的实用策略。',
'<h2>了解压力</h2><p>压力是面对挑战时的自然反应，但长期压力会损害您的健康。</p>
<h3>压力的迹象</h3>
<ul>
<li>睡眠困难</li>
<li>情绪变化</li>
<li>身体紧张</li>
<li>难以集中注意力</li>
</ul>
<h3>压力管理技巧</h3>
<h4>1. 深呼吸</h4>
<p>每天练习5-10分钟的深呼吸练习。</p>
<h4>2. 冥想</h4>
<p>从5分钟冥想开始，逐渐增加时间。</p>
<h4>3. 体育活动</h4>
<p>规律是有效的减压方式。</p>
<h4>4. 社交联系</h4>
<p>与朋友和家人共度时光，寻求情感支持。</p>
<h4>5. 时间管理</h4>
<p>确定任务优先级，避免过度承诺。</p>',
'https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=400', '心理健康', '王心理师', 145, 1),

('了解血压', '监测和维持健康血压水平的综合指南。',
'<h2>什么是血压？</h2><p>血压是血液泵送至心脏时对动脉壁产生的压力。</p>
<h3>血压数值</h3>
<ul>
<li><strong>正常：</strong> 低于 120/80 mmHg</li>
<li><strong>升高：</strong> 120-129/低于 80 mmHg</li>
<li><strong>高血压（1期）：</strong> 130-139/80-89 mmHg</li>
<li><strong>高血压（2期）：</strong> 140 或更高/90 或更高 mmHg</li>
</ul>
<h3>维持健康血压的建议</h3>
<ul>
<li>保持健康体重</li>
<li>规律运动</li>
<li>饮食均衡，低钠</li>
<li>限制酒精摄入</li>
<li>管理压力</li>
<li>按医嘱服药</li>
</ul>',
'https://images.unsplash.com/photo-1559757175-5700dde675bc?w=400', '疾病预防', '张医生', 178, 1),

('睡眠与健康的关系', '优质睡眠如何影响您的整体健康以及改善睡眠的建议。',
'<h2>睡眠的重要性</h2><p>优质睡眠对身心健康至关重要。</p>
<h3>良好睡眠的益处</h3>
<ul>
<li>改善记忆力和注意力</li>
<li>增强免疫功能</li>
<li>降低压力水平</li>
<li>维持健康体重</li>
<li>降低患慢性病的风险</li>
</ul>
<h3>改善睡眠的建议</h3>
<h4>1. 保持规律作息</h4>
<p>每天同一时间上床睡觉和起床。</p>
<h4>2. 营造舒适的睡眠环境</h4>
<p>保持卧室黑暗、安静和凉爽。</p>
<h4>3. 限制屏幕时间</h4>
<p>睡前至少1小时避免使用电子设备。</p>
<h4>4. 避免咖啡因</h4>
<p>限制咖啡因摄入，尤其是下午。</p>
<h4>5. 规律运动</h4>
<p>体育活动有助于改善睡眠质量。</p>',
'https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=400', '养生保健', '刘专家', 134, 1),

('糖尿病的预防与管理', '关于有效预防和控制糖尿病的基本信息。',
'<h2>了解糖尿病</h2><p>糖尿病是一种影响身体处理血糖能力的慢性疾病。</p>
<h3>糖尿病类型</h3>
<ul>
<li><strong>1型：</strong> 身体不产生胰岛素</li>
<li><strong>2型：</strong> 身体无法有效使用胰岛素</li>
<li><strong>妊娠期糖尿病：</strong> 怀孕期间发生</li>
</ul>
<h3>预防策略</h3>
<ul>
<li>保持健康体重</li>
<li>规律运动</li>
<li>饮食均衡</li>
<li>监测血糖水平</li>
<li>定期体检</li>
</ul>
<h3>糖尿病管理</h3>
<ul>
<li>按医嘱服药</li>
<li>定期监测血糖</li>
<li>遵循糖尿病饮食</li>
<li>保持体育活动</li>
<li>管理压力</li>
</ul>',
'https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=400', '疾病预防', '赵医生', 167, 1);
