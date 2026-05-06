package com.health.controller;

import com.health.common.PageResult;
import com.health.common.Result;
import com.health.entity.Appointment;
import com.health.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约控制器
 */
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 获取预约列表
     */
    @GetMapping
    public Result<PageResult<Appointment>> getAppointments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {
        Long userId = getCurrentUserId();
        PageResult<Appointment> result = appointmentService.getAppointments(userId, page, size, status);
        return Result.success(result);
    }

    /**
     * 获取即将到来的预约
     */
    @GetMapping("/upcoming")
    public Result<List<Appointment>> getUpcomingAppointments(
            @RequestParam(defaultValue = "5") Integer limit) {
        Long userId = getCurrentUserId();
        List<Appointment> appointments = appointmentService.getUpcomingAppointments(userId, limit);
        return Result.success(appointments);
    }

    /**
     * 创建预约
     */
    @PostMapping
    public Result<Void> createAppointment(@RequestBody Appointment appointment) {
        Long userId = getCurrentUserId();
        appointmentService.createAppointment(userId, appointment);
        return Result.success("Appointment created successfully", null);
    }

    /**
     * 更新预约
     */
    @PutMapping("/{id}")
    public Result<Void> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        Long userId = getCurrentUserId();
        appointmentService.updateAppointment(userId, id, appointment);
        return Result.success("Appointment updated successfully", null);
    }

    /**
     * 取消预约
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelAppointment(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        appointmentService.cancelAppointment(userId, id);
        return Result.success("Appointment cancelled successfully", null);
    }

    /**
     * 删除预约
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAppointment(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        appointmentService.deleteAppointment(userId, id);
        return Result.success("Appointment deleted successfully", null);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
