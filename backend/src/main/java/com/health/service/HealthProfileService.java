package com.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.health.entity.HealthProfile;
import com.health.exception.BusinessException;
import com.health.mapper.HealthProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 健康档案服务类
 */
@Service
public class HealthProfileService {

    private static final Logger logger = LoggerFactory.getLogger(HealthProfileService.class);

    @Autowired
    private HealthProfileMapper healthProfileMapper;

    /**
     * 获取健康档案
     */
    public HealthProfile getProfile(Long userId) {
        LambdaQueryWrapper<HealthProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthProfile::getUserId, userId);
        HealthProfile profile = healthProfileMapper.selectOne(wrapper);
        
        if (profile == null) {
            // 如果不存在，创建一个空档案
            profile = new HealthProfile();
            profile.setUserId(userId);
            healthProfileMapper.insert(profile);
            logger.info("Created empty health profile for user: {}", userId);
        }
        
        return profile;
    }

    /**
     * 更新健康档案
     */
    @Transactional
    public void updateProfile(Long userId, HealthProfile updateData) {
        logger.info("Updating health profile for user: {}", userId);
        
        LambdaQueryWrapper<HealthProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HealthProfile::getUserId, userId);
        HealthProfile profile = healthProfileMapper.selectOne(wrapper);
        
        if (profile == null) {
            // 创建新档案
            updateData.setUserId(userId);
            updateData.setId(null);
            healthProfileMapper.insert(updateData);
            logger.info("Created health profile for user: {}", userId);
        } else {
            // 更新现有档案
            updateData.setId(profile.getId());
            updateData.setUserId(userId);
            healthProfileMapper.updateById(updateData);
            logger.info("Updated health profile for user: {}", userId);
        }
    }
}
