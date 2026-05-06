package com.health.vo;

import lombok.Data;

/**
 * 登录响应 VO
 */
@Data
public class LoginVO {
    
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private String token;
    
    public LoginVO() {}
    
    public LoginVO(Long id, String username, String nickname, String email, 
                   String phone, String avatar, String role, String token) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.role = role;
        this.token = token;
    }
}
