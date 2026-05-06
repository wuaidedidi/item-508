package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 心率记录实体类
 */
@Data
@TableName("heart_rate")
public class HeartRate {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 心率 (次/分)
     */
    private Integer rate;
    
    /**
     * 状态: rest-静息, exercise-运动后, sleep-睡眠
     */
    private String status;
    
    /**
     * 记录日期
     */
    private LocalDate recordDate;
    
    /**
     * 备注
     */
    private String note;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
