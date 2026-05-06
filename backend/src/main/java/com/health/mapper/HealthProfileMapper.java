package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.HealthProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 健康档案 Mapper 接口
 */
@Mapper
public interface HealthProfileMapper extends BaseMapper<HealthProfile> {
}
