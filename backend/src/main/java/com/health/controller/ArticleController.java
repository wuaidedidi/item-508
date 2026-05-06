package com.health.controller;

import com.health.common.PageResult;
import com.health.common.Result;
import com.health.entity.Article;
import com.health.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章控制器（公开接口）
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取已发布的文章列表
     */
    @GetMapping
    public Result<PageResult<Article>> getArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category) {
        PageResult<Article> result = articleService.getPublishedArticles(page, size, category);
        return Result.success(result);
    }

    /**
     * 获取推荐文章
     */
    @GetMapping("/recommended")
    public Result<List<Article>> getRecommendedArticles(
            @RequestParam(defaultValue = "6") Integer limit) {
        List<Article> articles = articleService.getRecommendedArticles(limit);
        return Result.success(articles);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        return Result.success(article);
    }
}
