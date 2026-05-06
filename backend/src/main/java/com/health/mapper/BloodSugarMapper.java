package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.BloodSugar;
import org.apache.ibatis.annotations.Mapper;

/**
 * 血糖记录 Mapper 接口
 */
@Mapper
public interface BloodSugarMapper extends BaseMapper<BloodSugar> {
}
