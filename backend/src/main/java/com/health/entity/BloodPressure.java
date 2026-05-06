package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 血压记录实体类
 */
@Data
@TableName("blood_pressure")
public class BloodPressure {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 收缩压 (mmHg)
     */
    private Integer systolic;
    
    /**
     * 舒张压 (mmHg)
     */
    private Integer diastolic;
    
    /**
     * 脉搏 (次/分)
     */
    private Integer pulse;
    
    /**
     * 记录日期
     */
    private LocalDate recordDate;
    
    /**
     * 测量时间: morning-早晨, noon-中午, evening-晚间
     */
    private String measureTime;
    
    /**
     * 备注
     */
    private String note;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
