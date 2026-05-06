package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约实体类
 */
@Data
@TableName("appointment")
public class Appointment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
     * 科室
     */
    private String department;
    
    /**
     * 医院名称
     */
    private String hospital;
    
    /**
     * 预约时间
     */
    private LocalDateTime appointmentTime;
    
    /**
     * 状态: pending-待就诊, completed-已完成, cancelled-已取消
     */
    private String status;
    
    /**
     * 预约类型: checkup-体检, consultation-问诊, followup-复诊
     */
    private String type;
    
    /**
     * 备注
     */
    private String note;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
