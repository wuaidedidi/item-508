package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 血糖记录实体类
 */
@Data
@TableName("blood_sugar")
public class BloodSugar {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 血糖值 (mmol/L)
     */
    private BigDecimal value;
    
    /**
     * 测量时间: fasting-空腹, before_meal-餐前, after_meal-餐后, bedtime-睡前
     */
    private String measureTime;
    
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
