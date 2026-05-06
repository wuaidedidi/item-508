package com.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.common.PageResult;
import com.health.dto.LoginDTO;
import com.health.dto.PasswordDTO;
import com.health.dto.RegisterDTO;
import com.health.entity.User;
import com.health.exception.BusinessException;
import com.health.mapper.UserMapper;
import com.health.security.JwtUtils;
import com.health.vo.LoginVO;
import com.health.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户注册
     */
    @Transactional
    public void register(RegisterDTO dto) {
        logger.info("Registering user: {}", dto.getUsername());
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw BusinessException.of(400, "用户名已存在");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setRole("USER");
        user.setStatus(1);
        user.setDeleted(0);
        
        userMapper.insert(user);
        logger.info("User registered successfully: {}", dto.getUsername());
    }

    /**
     * 用户登录
     */
    public LoginVO login(LoginDTO dto) {
        logger.info("User login attempt: {}", dto.getUsername());
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        wrapper.eq(User::getDeleted, 0);
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            logger.warn("User not found: {}", dto.getUsername());
            throw BusinessException.of(401, "用户名或密码错误");
        }
        
        logger.info("Found user: {}, checking password...", user.getUsername());
        
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            logger.warn("Invalid password for user: {}", dto.getUsername());
            throw BusinessException.of(401, "用户名或密码错误");
        }
        
        if (user.getStatus() != 1) {
            logger.warn("User account disabled: {}", dto.getUsername());
            throw BusinessException.of(403, "账号已被禁用");
        }
        
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        LoginVO vo = new LoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        vo.setRole(user.getRole());
        vo.setToken(token);
        
        logger.info("User logged in successfully: {}", dto.getUsername());
        return vo;
    }

    /**
     * 获取用户信息
     */
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        return convertToVO(user);
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public void updateUserInfo(Long userId, UserVO vo) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        if (StringUtils.hasText(vo.getNickname())) {
            user.setNickname(vo.getNickname());
        }
        if (StringUtils.hasText(vo.getEmail())) {
            user.setEmail(vo.getEmail());
        }
        if (StringUtils.hasText(vo.getPhone())) {
            user.setPhone(vo.getPhone());
        }
        if (StringUtils.hasText(vo.getAvatar())) {
            user.setAvatar(vo.getAvatar());
        }
        
        userMapper.updateById(user);
        logger.info("User info updated: {}", userId);
    }

    /**
     * 修改密码
     */
    @Transactional
    public void changePassword(Long userId, PasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw BusinessException.of(400, "原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);
        logger.info("Password changed for user: {}", userId);
    }

    // ==================== Admin methods ====================

    /**
     * 获取用户列表（管理员）
     */
    public PageResult<UserVO> getUserList(int page, int size, String keyword) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDeleted, 0);
        
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword));
        }
        
        wrapper.orderByDesc(User::getCreatedAt);
        Page<User> result = userMapper.selectPage(pageParam, wrapper);
        
        List<UserVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(voList, result.getTotal(), page, size);
    }

    /**
     * 更新用户信息（管理员）
     */
    @Transactional
    public void updateUser(Long userId, UserVO vo) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        if (StringUtils.hasText(vo.getNickname())) {
            user.setNickname(vo.getNickname());
        }
        if (StringUtils.hasText(vo.getEmail())) {
            user.setEmail(vo.getEmail());
        }
        if (StringUtils.hasText(vo.getPhone())) {
            user.setPhone(vo.getPhone());
        }
        if (StringUtils.hasText(vo.getRole())) {
            user.setRole(vo.getRole());
        }
        
        userMapper.updateById(user);
        logger.info("Admin updated user: {}", userId);
    }

    /**
     * 更新用户状态（管理员）
     */
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        user.setStatus(status);
        userMapper.updateById(user);
        logger.info("Admin updated user status: {} -> {}", userId, status);
    }

    /**
     * 删除用户（管理员）
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        
        user.setDeleted(1);
        userMapper.updateById(user);
        logger.info("Admin deleted user: {}", userId);
    }

    /**
     * 获取用户总数
     */
    public long getUserCount() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDeleted, 0);
        return userMapper.selectCount(wrapper);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        vo.setGender(user.getGender());
        vo.setBirthday(user.getBirthday());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        if (user.getCreatedAt() != null) {
            vo.setCreatedAt(user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return vo;
    }
}
