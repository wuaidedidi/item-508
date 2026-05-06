package com.health.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 健康文章实体类
 */
@Data
@TableName("article")
public class Article {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 文章标题
     */
    private String title;
    
    /**
     * 文章摘要
     */
    private String summary;
    
    /**
     * 文章内容
     */
    private String content;
    
    /**
     * 封面图片
     */
    private String coverImage;
    
    /**
     * 分类: nutrition-营养健康, exercise-运动健身, mental-心理健康, disease-疾病预防, lifestyle-生活方式
     */
    private String category;
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 浏览量
     */
    private Integer viewCount;
    
    /**
     * 状态: 0-草稿, 1-已发布
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}
