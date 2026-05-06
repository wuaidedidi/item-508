package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 健康档案实体类
 */
@Data
@TableName("health_profile")
public class HealthProfile {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 身高 (cm)
     */
    private BigDecimal height;
    
    /**
     * 体重 (kg)
     */
    private BigDecimal weight;
    
    /**
     * 血型: A, B, AB, O
     */
    private String bloodType;
    
    /**
     * 过敏史
     */
    private String allergies;
    
    /**
     * 病史
     */
    private String medicalHistory;
    
    /**
     * 家族病史
     */
    private String familyHistory;
    
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    
    /**
     * 紧急联系电话
     */
    private String emergencyPhone;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
