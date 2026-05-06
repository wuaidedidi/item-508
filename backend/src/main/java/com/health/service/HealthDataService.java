package com.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.common.PageResult;
import com.health.entity.WeightRecord;
import com.health.entity.BloodPressure;
import com.health.entity.BloodSugar;
import com.health.entity.HeartRate;
import com.health.exception.BusinessException;
import com.health.mapper.WeightRecordMapper;
import com.health.mapper.BloodPressureMapper;
import com.health.mapper.BloodSugarMapper;
import com.health.mapper.HeartRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 健康数据服务类
 */
@Service
public class HealthDataService {

    private static final Logger logger = LoggerFactory.getLogger(HealthDataService.class);

    @Autowired
    private WeightRecordMapper weightRecordMapper;

    @Autowired
    private BloodPressureMapper bloodPressureMapper;

    @Autowired
    private BloodSugarMapper bloodSugarMapper;

    @Autowired
    private HeartRateMapper heartRateMapper;

    // ==================== 体重记录 ====================

    public PageResult<WeightRecord> getWeightRecords(Long userId, Integer page, Integer size) {
        Page<WeightRecord> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<WeightRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeightRecord::getUserId, userId)
               .orderByDesc(WeightRecord::getRecordDate);
        
        Page<WeightRecord> result = weightRecordMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<WeightRecord> getRecentWeightRecords(Long userId, int limit) {
        LambdaQueryWrapper<WeightRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeightRecord::getUserId, userId)
               .orderByDesc(WeightRecord::getRecordDate)
               .last("LIMIT " + limit);
        return weightRecordMapper.selectList(wrapper);
    }

    @Transactional
    public void addWeightRecord(Long userId, WeightRecord record) {
        logger.info("Adding weight record for user: {}", userId);
        record.setUserId(userId);
        record.setId(null);
        weightRecordMapper.insert(record);
    }

    @Transactional
    public void deleteWeightRecord(Long userId, Long recordId) {
        WeightRecord record = weightRecordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw BusinessException.notFound("记录不存在");
        }
        weightRecordMapper.deleteById(recordId);
        logger.info("Deleted weight record: {}", recordId);
    }

    @Transactional
    public void updateWeightRecord(WeightRecord record) {
        logger.info("Updating weight record: {}", record.getId());
        weightRecordMapper.updateById(record);
    }

    // ==================== 血压记录 ====================

    public PageResult<BloodPressure> getBloodPressureRecords(Long userId, Integer page, Integer size) {
        Page<BloodPressure> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BloodPressure> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BloodPressure::getUserId, userId)
               .orderByDesc(BloodPressure::getRecordDate);
        
        Page<BloodPressure> result = bloodPressureMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<BloodPressure> getRecentBloodPressureRecords(Long userId, int limit) {
        LambdaQueryWrapper<BloodPressure> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BloodPressure::getUserId, userId)
               .orderByDesc(BloodPressure::getRecordDate)
               .last("LIMIT " + limit);
        return bloodPressureMapper.selectList(wrapper);
    }

    @Transactional
    public void addBloodPressureRecord(Long userId, BloodPressure record) {
        logger.info("Adding blood pressure record for user: {}", userId);
        record.setUserId(userId);
        record.setId(null);
        bloodPressureMapper.insert(record);
    }

    @Transactional
    public void deleteBloodPressureRecord(Long userId, Long recordId) {
        BloodPressure record = bloodPressureMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw BusinessException.notFound("记录不存在");
        }
        bloodPressureMapper.deleteById(recordId);
        logger.info("Deleted blood pressure record: {}", recordId);
    }

    @Transactional
    public void updateBloodPressureRecord(BloodPressure record) {
        logger.info("Updating blood pressure record: {}", record.getId());
        bloodPressureMapper.updateById(record);
    }

    // ==================== 血糖记录 ====================

    public PageResult<BloodSugar> getBloodSugarRecords(Long userId, Integer page, Integer size) {
        Page<BloodSugar> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BloodSugar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BloodSugar::getUserId, userId)
               .orderByDesc(BloodSugar::getRecordDate);
        
        Page<BloodSugar> result = bloodSugarMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<BloodSugar> getRecentBloodSugarRecords(Long userId, int limit) {
        LambdaQueryWrapper<BloodSugar> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BloodSugar::getUserId, userId)
               .orderByDesc(BloodSugar::getRecordDate)
               .last("LIMIT " + limit);
        return bloodSugarMapper.selectList(wrapper);
    }

    @Transactional
    public void addBloodSugarRecord(Long userId, BloodSugar record) {
        logger.info("Adding blood sugar record for user: {}", userId);
        record.setUserId(userId);
        record.setId(null);
        bloodSugarMapper.insert(record);
    }

    @Transactional
    public void deleteBloodSugarRecord(Long userId, Long recordId) {
        BloodSugar record = bloodSugarMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw BusinessException.notFound("记录不存在");
        }
        bloodSugarMapper.deleteById(recordId);
        logger.info("Deleted blood sugar record: {}", recordId);
    }

    @Transactional
    public void updateBloodSugarRecord(BloodSugar record) {
        logger.info("Updating blood sugar record: {}", record.getId());
        bloodSugarMapper.updateById(record);
    }

    // ==================== 心率记录 ====================

    public PageResult<HeartRate> getHeartRateRecords(Long userId, Integer page, Integer size) {
        Page<HeartRate> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<HeartRate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeartRate::getUserId, userId)
               .orderByDesc(HeartRate::getRecordDate);
        
        Page<HeartRate> result = heartRateMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    public List<HeartRate> getRecentHeartRateRecords(Long userId, int limit) {
        LambdaQueryWrapper<HeartRate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HeartRate::getUserId, userId)
               .orderByDesc(HeartRate::getRecordDate)
               .last("LIMIT " + limit);
        return heartRateMapper.selectList(wrapper);
    }

    @Transactional
    public void addHeartRateRecord(Long userId, HeartRate record) {
        logger.info("Adding heart rate record for user: {}", userId);
        record.setUserId(userId);
        record.setId(null);
        heartRateMapper.insert(record);
    }

    @Transactional
    public void deleteHeartRateRecord(Long userId, Long recordId) {
        HeartRate record = heartRateMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId)) {
            throw BusinessException.notFound("记录不存在");
        }
        heartRateMapper.deleteById(recordId);
        logger.info("Deleted heart rate record: {}", recordId);
    }

    @Transactional
    public void updateHeartRateRecord(HeartRate record) {
        logger.info("Updating heart rate record: {}", record.getId());
        heartRateMapper.updateById(record);
    }
}
