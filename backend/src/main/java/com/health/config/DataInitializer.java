package com.health.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.health.entity.User;
import com.health.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器 - 确保管理员密码正确加密
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 检查并修复管理员密码
        fixAdminPassword();
    }

    private void fixAdminPassword() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, "admin");
        User admin = userMapper.selectOne(wrapper);

        if (admin != null) {
            // 检查密码是否能匹配 "123456"
            if (!passwordEncoder.matches("123456", admin.getPassword())) {
                logger.info("修复管理员密码...");
                String encodedPassword = passwordEncoder.encode("123456");
                admin.setPassword(encodedPassword);
                userMapper.updateById(admin);
                logger.info("管理员密码已修复");
            } else {
                logger.info("管理员密码验证正常");
            }
        }

        // 同样修复其他测试用户
        String[] testUsers = {"user", "zhangsan", "lisi"};
        for (String username : testUsers) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, username);
            User user = userMapper.selectOne(wrapper);
            if (user != null && !passwordEncoder.matches("123456", user.getPassword())) {
                logger.info("修复用户 {} 的密码...", username);
                user.setPassword(passwordEncoder.encode("123456"));
                userMapper.updateById(user);
            }
        }
    }
}
