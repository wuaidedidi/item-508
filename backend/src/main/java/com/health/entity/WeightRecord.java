package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 体重记录实体类
 */
@Data
@TableName("weight_record")
public class WeightRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 体重 (kg)
     */
    private BigDecimal weight;
    
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
