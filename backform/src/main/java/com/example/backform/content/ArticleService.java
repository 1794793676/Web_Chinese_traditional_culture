package com.example.backform.content;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.BusinessException;
import com.example.backform.content.dto.ArticleCardResponse;
import com.example.backform.content.dto.ArticleDetailResponse;
import com.example.backform.content.dto.ArticleSourceResponse;
import com.example.backform.content.dto.InteractionCountResponse;
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

    public ArticleService(
            ArticleMapper articleMapper,
            LikeMapper likeMapper,
            ViewMapper viewMapper
    ) {
        this.articleMapper = articleMapper;
        this.likeMapper = likeMapper;
        this.viewMapper = viewMapper;
    }

    public List<ArticleCardResponse> featured(int limit) {
        List<Map<String, Object>> rows = articleMapper.findFeatured(limit);
        return rows.stream().map(this::toArticleCard).toList();
    }

    public long countByCategory(String category) {
        return articleMapper.countByCategorySlug(category);
    }

    public List<ArticleCardResponse> listByCategory(String category, int size, int offset) {
        List<Map<String, Object>> rows = articleMapper.findByCategorySlug(category, size, offset);
        return rows.stream().map(this::toArticleCard).toList();
    }

    public ArticleDetailResponse detail(String slug, CurrentUser currentUser) {
        Map<String, Object> articleRow = articleMapper.findDetailBySlug(slug);
        if (articleRow == null) {
            throw new BusinessException(404, "文章不存在", HttpStatus.NOT_FOUND);
        }

        Long articleId = extractArticleId(articleRow);
        Long currentUserId = getCurrentUserIdOrNull(currentUser);
        boolean likedByCurrentUser = currentUserId != null
                && likeMapper.existsLike(articleId, currentUserId) > 0;

        List<ArticleSourceResponse> sources = articleMapper
                .findSourcesByArticleId(articleId)
                .stream()
                .map(this::toSource)
                .toList();

        return buildArticleDetail(articleRow, likedByCurrentUser, sources);
    }

    public InteractionCountResponse addView(
            Long articleId,
            CurrentUser currentUser,
            HttpServletRequest request
    ) {
        Long userId = getCurrentUserIdOrNull(currentUser);
        viewMapper.insertView(
                articleId,
                userId,
                request.getRequestURI(),
                request.getRemoteAddr(),
                request.getHeader("User-Agent")
        );

        articleMapper.incrementViewCount(articleId);
        return buildInteractionCount(articleId, null);
    }

    private Long extractArticleId(Map<String, Object> row) {
        Number idValue = (Number) row.get("id");
        return idValue.longValue();
    }

    private ArticleDetailResponse buildArticleDetail(
            Map<String, Object> row,
            boolean likedByCurrentUser,
            List<ArticleSourceResponse> sources
    ) {
        Long articleId = extractArticleId(row);
        String slug = String.valueOf(row.get("slug"));
        String title = String.valueOf(row.get("title"));
        String summary = String.valueOf(row.get("summary"));
        String content = String.valueOf(row.get("content"));
        String coverUrl = String.valueOf(row.get("coverUrl"));
        String categoryName = String.valueOf(row.get("categoryName"));
        String categorySlug = String.valueOf(row.get("categorySlug"));

        Long likeCount = toLong(row.get("likeCount"));
        Long commentCount = toLong(row.get("commentCount"));
        Long shareCount = toLong(row.get("shareCount"));
        Long viewCount = toLong(row.get("viewCount"));

        return new ArticleDetailResponse(
                articleId,
                slug,
                title,
                summary,
                content,
                coverUrl,
                categoryName,
                categorySlug,
                likeCount,
                commentCount,
                shareCount,
                viewCount,
                likedByCurrentUser,
                sources
        );
    }

    private InteractionCountResponse buildInteractionCount(Long articleId, Boolean likedByCurrentUser) {
        Long likeCount = articleMapper.countLikes(articleId);
        Long commentCount = articleMapper.countComments(articleId);
        Long shareCount = articleMapper.countShares(articleId);
        Long viewCount = articleMapper.currentViewCount(articleId);

        return new InteractionCountResponse(
                articleId,
                likeCount,
                commentCount,
                shareCount,
                viewCount,
                likedByCurrentUser
        );
    }

    private Long getCurrentUserIdOrNull(CurrentUser currentUser) {
        if (currentUser == null) {
            return null;
        }
        return currentUser.id();
    }

    private ArticleCardResponse toArticleCard(Map<String, Object> row) {
        Long id = toLong(row.get("id"));
        String slug = String.valueOf(row.get("slug"));
        String title = String.valueOf(row.get("title"));
        String summary = String.valueOf(row.get("summary"));
        String coverUrl = String.valueOf(row.get("coverUrl"));
        String categoryName = String.valueOf(row.get("categoryName"));
        Long likeCount = toLong(row.get("likeCount"));
        Long commentCount = toLong(row.get("commentCount"));
        Long shareCount = toLong(row.get("shareCount"));
        Long viewCount = toLong(row.get("viewCount"));
        String publishedAt = row.get("publishedAt") == null ? null : String.valueOf(row.get("publishedAt"));

        return new ArticleCardResponse(
                id,
                slug,
                title,
                summary,
                coverUrl,
                categoryName,
                likeCount,
                commentCount,
                shareCount,
                viewCount,
                publishedAt
        );
    }

    private ArticleSourceResponse toSource(Map<String, Object> row) {
        Long id = toLong(row.get("id"));
        Long articleId = toLong(row.get("articleId"));
        String sourceTitle = String.valueOf(row.get("sourceTitle"));
        String sourceUrl = String.valueOf(row.get("sourceUrl"));
        String sourceType = String.valueOf(row.get("sourceType"));

        return new ArticleSourceResponse(id, articleId, sourceTitle, sourceUrl, sourceType);
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        return ((Number) value).longValue();
    }
}
