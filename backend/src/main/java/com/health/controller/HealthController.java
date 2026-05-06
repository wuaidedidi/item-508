package com.health.controller;

import com.health.common.PageResult;
import com.health.common.Result;
import com.health.entity.*;
import com.health.service.HealthDataService;
import com.health.service.HealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康数据控制器
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthProfileService healthProfileService;

    @Autowired
    private HealthDataService healthDataService;

    // ==================== 健康档案 ====================

    @GetMapping("/profile")
    public Result<HealthProfile> getProfile() {
        Long userId = getCurrentUserId();
        HealthProfile profile = healthProfileService.getProfile(userId);
        return Result.success(profile);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody HealthProfile profile) {
        Long userId = getCurrentUserId();
        healthProfileService.updateProfile(userId, profile);
        return Result.success("Profile updated successfully", null);
    }

    // ==================== 体重记录 ====================

    @GetMapping("/weight")
    public Result<PageResult<WeightRecord>> getWeightRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        PageResult<WeightRecord> result = healthDataService.getWeightRecords(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/weight/recent")
    public Result<List<WeightRecord>> getRecentWeightRecords(
            @RequestParam(defaultValue = "7") Integer limit) {
        Long userId = getCurrentUserId();
        List<WeightRecord> records = healthDataService.getRecentWeightRecords(userId, limit);
        return Result.success(records);
    }

    @PostMapping("/weight")
    public Result<Void> addWeightRecord(@RequestBody WeightRecord record) {
        Long userId = getCurrentUserId();
        healthDataService.addWeightRecord(userId, record);
        return Result.success("Weight record added successfully", null);
    }

    @DeleteMapping("/weight/{id}")
    public Result<Void> deleteWeightRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        healthDataService.deleteWeightRecord(userId, id);
        return Result.success("体重记录删除成功", null);
    }

    @PutMapping("/weight/{id}")
    public Result<Void> updateWeightRecord(@PathVariable Long id, @RequestBody WeightRecord record) {
        Long userId = getCurrentUserId();
        record.setId(id);
        record.setUserId(userId);
        healthDataService.updateWeightRecord(record);
        return Result.success("体重记录更新成功", null);
    }

    // ==================== 血压记录 ====================

    @GetMapping("/blood-pressure")
    public Result<PageResult<BloodPressure>> getBloodPressureRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        PageResult<BloodPressure> result = healthDataService.getBloodPressureRecords(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/blood-pressure/recent")
    public Result<List<BloodPressure>> getRecentBloodPressureRecords(
            @RequestParam(defaultValue = "7") Integer limit) {
        Long userId = getCurrentUserId();
        List<BloodPressure> records = healthDataService.getRecentBloodPressureRecords(userId, limit);
        return Result.success(records);
    }

    @PostMapping("/blood-pressure")
    public Result<Void> addBloodPressureRecord(@RequestBody BloodPressure record) {
        Long userId = getCurrentUserId();
        healthDataService.addBloodPressureRecord(userId, record);
        return Result.success("Blood pressure record added successfully", null);
    }

    @DeleteMapping("/blood-pressure/{id}")
    public Result<Void> deleteBloodPressureRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        healthDataService.deleteBloodPressureRecord(userId, id);
        return Result.success("血压记录删除成功", null);
    }

    @PutMapping("/blood-pressure/{id}")
    public Result<Void> updateBloodPressureRecord(@PathVariable Long id, @RequestBody BloodPressure record) {
        Long userId = getCurrentUserId();
        record.setId(id);
        record.setUserId(userId);
        healthDataService.updateBloodPressureRecord(record);
        return Result.success("血压记录更新成功", null);
    }

    // ==================== 血糖记录 ====================

    @GetMapping("/blood-sugar")
    public Result<PageResult<BloodSugar>> getBloodSugarRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        PageResult<BloodSugar> result = healthDataService.getBloodSugarRecords(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/blood-sugar/recent")
    public Result<List<BloodSugar>> getRecentBloodSugarRecords(
            @RequestParam(defaultValue = "7") Integer limit) {
        Long userId = getCurrentUserId();
        List<BloodSugar> records = healthDataService.getRecentBloodSugarRecords(userId, limit);
        return Result.success(records);
    }

    @PostMapping("/blood-sugar")
    public Result<Void> addBloodSugarRecord(@RequestBody BloodSugar record) {
        Long userId = getCurrentUserId();
        healthDataService.addBloodSugarRecord(userId, record);
        return Result.success("Blood sugar record added successfully", null);
    }

    @DeleteMapping("/blood-sugar/{id}")
    public Result<Void> deleteBloodSugarRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        healthDataService.deleteBloodSugarRecord(userId, id);
        return Result.success("血糖记录删除成功", null);
    }

    @PutMapping("/blood-sugar/{id}")
    public Result<Void> updateBloodSugarRecord(@PathVariable Long id, @RequestBody BloodSugar record) {
        Long userId = getCurrentUserId();
        record.setId(id);
        record.setUserId(userId);
        healthDataService.updateBloodSugarRecord(record);
        return Result.success("血糖记录更新成功", null);
    }

    // ==================== 心率记录 ====================

    @GetMapping("/heart-rate")
    public Result<PageResult<HeartRate>> getHeartRateRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = getCurrentUserId();
        PageResult<HeartRate> result = healthDataService.getHeartRateRecords(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/heart-rate/recent")
    public Result<List<HeartRate>> getRecentHeartRateRecords(
            @RequestParam(defaultValue = "7") Integer limit) {
        Long userId = getCurrentUserId();
        List<HeartRate> records = healthDataService.getRecentHeartRateRecords(userId, limit);
        return Result.success(records);
    }

    @PostMapping("/heart-rate")
    public Result<Void> addHeartRateRecord(@RequestBody HeartRate record) {
        Long userId = getCurrentUserId();
        healthDataService.addHeartRateRecord(userId, record);
        return Result.success("Heart rate record added successfully", null);
    }

    @DeleteMapping("/heart-rate/{id}")
    public Result<Void> deleteHeartRateRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        healthDataService.deleteHeartRateRecord(userId, id);
        return Result.success("心率记录删除成功", null);
    }

    @PutMapping("/heart-rate/{id}")
    public Result<Void> updateHeartRateRecord(@PathVariable Long id, @RequestBody HeartRate record) {
        Long userId = getCurrentUserId();
        record.setId(id);
        record.setUserId(userId);
        healthDataService.updateHeartRateRecord(record);
        return Result.success("心率记录更新成功", null);
    }

    // ==================== 健康概览 ====================

    @GetMapping("/overview")
    public Result<Map<String, Object>> getHealthOverview() {
        Long userId = getCurrentUserId();
        
        Map<String, Object> overview = new HashMap<>();
        overview.put("profile", healthProfileService.getProfile(userId));
        overview.put("recentWeight", healthDataService.getRecentWeightRecords(userId, 7));
        overview.put("recentBloodPressure", healthDataService.getRecentBloodPressureRecords(userId, 7));
        overview.put("recentBloodSugar", healthDataService.getRecentBloodSugarRecords(userId, 7));
        overview.put("recentHeartRate", healthDataService.getRecentHeartRateRecords(userId, 7));
        
        return Result.success(overview);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
