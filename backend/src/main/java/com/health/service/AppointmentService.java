package com.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.common.PageResult;
import com.health.entity.Appointment;
import com.health.exception.BusinessException;
import com.health.mapper.AppointmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 预约服务类
 */
@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * 获取预约列表
     */
    public PageResult<Appointment> getAppointments(Long userId, Integer page, Integer size, String status) {
        Page<Appointment> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getUserId, userId);
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Appointment::getStatus, status);
        }
        wrapper.orderByDesc(Appointment::getAppointmentTime);
        
        Page<Appointment> result = appointmentMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取即将到来的预约
     */
    public List<Appointment> getUpcomingAppointments(Long userId, int limit) {
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getUserId, userId)
               .eq(Appointment::getStatus, "pending")
               .ge(Appointment::getAppointmentTime, LocalDateTime.now())
               .orderByAsc(Appointment::getAppointmentTime)
               .last("LIMIT " + limit);
        return appointmentMapper.selectList(wrapper);
    }

    /**
     * 创建预约
     */
    @Transactional
    public void createAppointment(Long userId, Appointment appointment) {
        logger.info("Creating appointment for user: {}", userId);
        
        appointment.setUserId(userId);
        appointment.setId(null);
        appointment.setStatus("pending");
        
        appointmentMapper.insert(appointment);
        logger.info("Appointment created successfully");
    }

    /**
     * 更新预约
     */
    @Transactional
    public void updateAppointment(Long userId, Long appointmentId, Appointment updateData) {
        logger.info("Updating appointment: {}", appointmentId);
        
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null || !appointment.getUserId().equals(userId)) {
            throw BusinessException.notFound("Appointment not found");
        }
        
        if (updateData.getDoctorName() != null) {
            appointment.setDoctorName(updateData.getDoctorName());
        }
        if (updateData.getDepartment() != null) {
            appointment.setDepartment(updateData.getDepartment());
        }
        if (updateData.getHospital() != null) {
            appointment.setHospital(updateData.getHospital());
        }
        if (updateData.getAppointmentTime() != null) {
            appointment.setAppointmentTime(updateData.getAppointmentTime());
        }
        if (updateData.getType() != null) {
            appointment.setType(updateData.getType());
        }
        if (updateData.getNote() != null) {
            appointment.setNote(updateData.getNote());
        }
        if (updateData.getStatus() != null) {
            appointment.setStatus(updateData.getStatus());
        }
        
        appointmentMapper.updateById(appointment);
        logger.info("Appointment updated successfully");
    }

    /**
     * 取消预约
     */
    @Transactional
    public void cancelAppointment(Long userId, Long appointmentId) {
        logger.info("Cancelling appointment: {}", appointmentId);
        
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null || !appointment.getUserId().equals(userId)) {
            throw BusinessException.notFound("Appointment not found");
        }
        
        appointment.setStatus("cancelled");
        appointmentMapper.updateById(appointment);
        logger.info("Appointment cancelled successfully");
    }

    /**
     * 删除预约
     */
    @Transactional
    public void deleteAppointment(Long userId, Long appointmentId) {
        Appointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null || !appointment.getUserId().equals(userId)) {
            throw BusinessException.notFound("Appointment not found");
        }
        
        appointmentMapper.deleteById(appointmentId);
        logger.info("Deleted appointment: {}", appointmentId);
    }

    /**
     * 获取预约总数
     */
    public Long getAppointmentCount() {
        return appointmentMapper.selectCount(null);
    }

    /**
     * 获取今日预约数
     */
    public Long getTodayAppointmentCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(Appointment::getAppointmentTime, startOfDay, endOfDay);
        return appointmentMapper.selectCount(wrapper);
    }
}
