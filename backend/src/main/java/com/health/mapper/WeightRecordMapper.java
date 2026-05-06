package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.WeightRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 体重记录 Mapper 接口
 */
@Mapper
public interface WeightRecordMapper extends BaseMapper<WeightRecord> {
}
