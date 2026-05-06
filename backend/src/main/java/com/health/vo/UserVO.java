package com.health.vo;

import lombok.Data;

/**
 * 用户信息 VO
 */
@Data
public class UserVO {
    
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private String birthday;
    private String role;
    private Integer status;
    private String createdAt;
}
