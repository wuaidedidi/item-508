package com.health.controller;

import com.health.common.PageResult;
import com.health.common.Result;
import com.health.entity.Article;
import com.health.service.AppointmentService;
import com.health.service.ArticleService;
import com.health.service.UserService;
import com.health.vo.StatisticsVO;
import com.health.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理后台控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AppointmentService appointmentService;

    // ==================== 统计数据 ====================

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics() {
        StatisticsVO stats = new StatisticsVO();
        stats.setTotalUsers(userService.getUserCount());
        stats.setTotalArticles(articleService.getArticleCount());
        stats.setTotalAppointments(appointmentService.getAppointmentCount());
        stats.setTodayAppointments(appointmentService.getTodayAppointmentCount());
        return Result.success(stats);
    }

    // ==================== 用户管理 ====================

    /**
     * 获取用户列表
     */
    @GetMapping("/users")
    public Result<PageResult<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageResult<UserVO> result = userService.getUserList(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/users/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody UserVO userVO) {
        userService.updateUser(id, userVO);
        return Result.success("用户更新成功", null);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        userService.updateUserStatus(id, body.get("status"));
        return Result.success("用户状态更新成功", null);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功", null);
    }

    // ==================== 文章管理 ====================

    /**
     * 获取所有文章
     */
    @GetMapping("/articles")
    public Result<PageResult<Article>> getArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageResult<Article> result = articleService.getAllArticles(page, size, keyword);
        return Result.success(result);
    }

    /**
     * 创建文章
     */
    @PostMapping("/articles")
    public Result<Void> createArticle(@RequestBody Article article) {
        articleService.createArticle(article);
        return Result.success("文章创建成功", null);
    }

    /**
     * 更新文章
     */
    @PutMapping("/articles/{id}")
    public Result<Void> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        articleService.updateArticle(id, article);
        return Result.success("文章更新成功", null);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/articles/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success("文章删除成功", null);
    }
}
