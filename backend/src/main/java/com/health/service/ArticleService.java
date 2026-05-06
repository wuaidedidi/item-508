package com.health.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.common.PageResult;
import com.health.entity.Article;
import com.health.exception.BusinessException;
import com.health.mapper.ArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文章服务类
 */
@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取已发布的文章列表
     */
    public PageResult<Article> getPublishedArticles(Integer page, Integer size, String category) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1);
        
        if (StringUtils.hasText(category)) {
            wrapper.eq(Article::getCategory, category);
        }
        wrapper.orderByDesc(Article::getCreatedAt);
        
        Page<Article> result = articleMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 获取推荐文章
     */
    public List<Article> getRecommendedArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1)
               .orderByDesc(Article::getViewCount)
               .last("LIMIT " + limit);
        return articleMapper.selectList(wrapper);
    }

    /**
     * 获取文章详情
     */
    public Article getArticleDetail(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getStatus() != 1) {
            throw BusinessException.notFound("Article not found");
        }
        
        // 增加浏览量
        article.setViewCount(article.getViewCount() + 1);
        articleMapper.updateById(article);
        
        return article;
    }

    /**
     * 获取所有文章列表（管理员）
     */
    public PageResult<Article> getAllArticles(Integer page, Integer size, String keyword) {
        Page<Article> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Article::getTitle, keyword)
                   .or().like(Article::getAuthor, keyword);
        }
        wrapper.orderByDesc(Article::getCreatedAt);
        
        Page<Article> result = articleMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    /**
     * 创建文章（管理员）
     */
    @Transactional
    public void createArticle(Article article) {
        logger.info("Creating article: {}", article.getTitle());
        
        article.setId(null);
        article.setViewCount(0);
        article.setDeleted(0);
        
        articleMapper.insert(article);
        logger.info("Article created successfully");
    }

    /**
     * 更新文章（管理员）
     */
    @Transactional
    public void updateArticle(Long articleId, Article updateData) {
        logger.info("Updating article: {}", articleId);
        
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw BusinessException.notFound("Article not found");
        }
        
        if (StringUtils.hasText(updateData.getTitle())) {
            article.setTitle(updateData.getTitle());
        }
        if (StringUtils.hasText(updateData.getSummary())) {
            article.setSummary(updateData.getSummary());
        }
        if (StringUtils.hasText(updateData.getContent())) {
            article.setContent(updateData.getContent());
        }
        if (StringUtils.hasText(updateData.getCoverImage())) {
            article.setCoverImage(updateData.getCoverImage());
        }
        if (StringUtils.hasText(updateData.getCategory())) {
            article.setCategory(updateData.getCategory());
        }
        if (StringUtils.hasText(updateData.getAuthor())) {
            article.setAuthor(updateData.getAuthor());
        }
        if (updateData.getStatus() != null) {
            article.setStatus(updateData.getStatus());
        }
        
        articleMapper.updateById(article);
        logger.info("Article updated successfully");
    }

    /**
     * 删除文章（管理员）
     */
    @Transactional
    public void deleteArticle(Long articleId) {
        logger.info("Deleting article: {}", articleId);
        articleMapper.deleteById(articleId);
    }

    /**
     * 获取文章总数
     */
    public Long getArticleCount() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1);
        return articleMapper.selectCount(wrapper);
    }
}
