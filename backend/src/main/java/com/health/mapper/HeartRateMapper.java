package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.HeartRate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 心率记录 Mapper 接口
 */
@Mapper
public interface HeartRateMapper extends BaseMapper<HeartRate> {
}
