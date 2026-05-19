package com.example.backform.content;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.BusinessException;
import com.example.backform.content.dto.*;
import com.example.backform.mapper.ArticleMapper;
import com.example.backform.mapper.LikeMapper;
import com.example.backform.mapper.ViewMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final LikeMapper likeMapper;
    private final ViewMapper viewMapper;
    public ArticleService(ArticleMapper articleMapper, LikeMapper likeMapper, ViewMapper viewMapper) { this.articleMapper = articleMapper; this.likeMapper = likeMapper; this.viewMapper = viewMapper; }
    public List<ArticleCardResponse> featured(int limit) { return articleMapper.findFeatured(limit).stream().map(this::toArticleCard).toList(); }
    public long countByCategory(String category) { return articleMapper.countByCategorySlug(category); }
    public List<ArticleCardResponse> listByCategory(String category, int size, int offset) { return articleMapper.findByCategorySlug(category, size, offset).stream().map(this::toArticleCard).toList(); }
    public ArticleDetailResponse detail(String slug, CurrentUser currentUser) {
        Map<String, Object> row = articleMapper.findDetailBySlug(slug);
        if (row == null) { throw new BusinessException(404, "文章不存在", HttpStatus.NOT_FOUND); }
        Long articleId = ((Number) row.get("id")).longValue();
        boolean likedByCurrentUser = currentUser != null && likeMapper.existsLike(articleId, currentUser.id()) > 0;
        List<ArticleSourceResponse> sources = articleMapper.findSourcesByArticleId(articleId).stream().map(this::toSource).toList();
        return new ArticleDetailResponse(articleId, String.valueOf(row.get("slug")), String.valueOf(row.get("title")), String.valueOf(row.get("summary")), String.valueOf(row.get("content")), String.valueOf(row.get("coverUrl")), String.valueOf(row.get("categoryName")), String.valueOf(row.get("categorySlug")), ((Number) row.get("likeCount")).longValue(), ((Number) row.get("commentCount")).longValue(), ((Number) row.get("shareCount")).longValue(), ((Number) row.get("viewCount")).longValue(), likedByCurrentUser, sources);
    }
    public InteractionCountResponse addView(Long articleId, CurrentUser currentUser, HttpServletRequest request) {
        viewMapper.insertView(articleId, currentUser == null ? null : currentUser.id(), request.getRequestURI(), request.getRemoteAddr(), request.getHeader("User-Agent"));
        articleMapper.incrementViewCount(articleId);
        return new InteractionCountResponse(articleId, articleMapper.countLikes(articleId), articleMapper.countComments(articleId), articleMapper.countShares(articleId), articleMapper.currentViewCount(articleId), null);
    }
    private ArticleCardResponse toArticleCard(Map<String, Object> row) { return new ArticleCardResponse(((Number) row.get("id")).longValue(), String.valueOf(row.get("slug")), String.valueOf(row.get("title")), String.valueOf(row.get("summary")), String.valueOf(row.get("coverUrl")), String.valueOf(row.get("categoryName")), toLong(row.get("likeCount")), toLong(row.get("commentCount")), toLong(row.get("shareCount")), toLong(row.get("viewCount")), row.get("publishedAt") == null ? null : String.valueOf(row.get("publishedAt"))); }
    private ArticleSourceResponse toSource(Map<String, Object> row) { return new ArticleSourceResponse(String.valueOf(row.get("sourceName")), String.valueOf(row.get("sourceUrl")), String.valueOf(row.get("sourceType")), String.valueOf(row.get("licenseNote"))); }
    private Long toLong(Object value) { return value == null ? 0L : ((Number) value).longValue(); }
}
