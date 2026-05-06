package com.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.health.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约 Mapper 接口
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
