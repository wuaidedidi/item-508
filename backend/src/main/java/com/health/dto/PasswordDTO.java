package com.health.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改密码请求 DTO
 */
@Data
public class PasswordDTO {
    
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;
    
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String newPassword;
}
