package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.BloodPressure;
import org.apache.ibatis.annotations.Mapper;

/**
 * 血压记录 Mapper 接口
 */
@Mapper
public interface BloodPressureMapper extends BaseMapper<BloodPressure> {
}
