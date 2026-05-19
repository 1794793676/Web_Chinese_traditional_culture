package com.example.backform.content;

import com.example.backform.auth.CurrentUser;
import com.example.backform.content.dto.CommentCreateRequest;
import com.example.backform.content.dto.CommentResponse;
import com.example.backform.content.dto.InteractionCountResponse;
import com.example.backform.content.dto.ShareCreateRequest;
import com.example.backform.mapper.ArticleMapper;
import com.example.backform.mapper.CommentMapper;
import com.example.backform.mapper.LikeMapper;
import com.example.backform.mapper.ShareMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InteractionService {
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;
    private final ShareMapper shareMapper;
    private final ArticleMapper articleMapper;

    public InteractionService(
            LikeMapper likeMapper,
            CommentMapper commentMapper,
            ShareMapper shareMapper,
            ArticleMapper articleMapper
    ) {
        this.likeMapper = likeMapper;
        this.commentMapper = commentMapper;
        this.shareMapper = shareMapper;
        this.articleMapper = articleMapper;
    }

    public InteractionCountResponse like(Long articleId, CurrentUser currentUser) {
        long existingLikeCount = likeMapper.existsLike(articleId, currentUser.id());
        if (existingLikeCount == 0) {
            likeMapper.insertLike(articleId, currentUser.id());
        }
        return buildInteractionCount(articleId, true);
    }

    public InteractionCountResponse unlike(Long articleId, CurrentUser currentUser) {
        likeMapper.deleteLike(articleId, currentUser.id());
        return buildInteractionCount(articleId, false);
    }

    public long countComments(Long articleId) {
        return commentMapper.countVisibleByArticleId(articleId);
    }

    public List<CommentResponse> listComments(Long articleId, int size, int offset) {
        List<Map<String, Object>> rows = commentMapper.findVisibleByArticleId(articleId, size, offset);
        return rows.stream().map(this::toComment).toList();
    }

    public InteractionCountResponse createComment(
            Long articleId,
            CurrentUser currentUser,
            CommentCreateRequest request
    ) {
        String content = request.getContent().trim();
        commentMapper.insertComment(articleId, currentUser.id(), content, "visible");
        return buildInteractionCount(articleId, true);
    }

    public InteractionCountResponse share(
            Long articleId,
            CurrentUser currentUser,
            ShareCreateRequest request
    ) {
        String channel = request.getChannel();
        shareMapper.insertShare(articleId, currentUser.id(), channel);
        return buildInteractionCount(articleId, null);
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

    private CommentResponse toComment(Map<String, Object> row) {
        Long id = ((Number) row.get("id")).longValue();
        Long articleId = ((Number) row.get("articleId")).longValue();
        Long userId = ((Number) row.get("userId")).longValue();
        String nickname = String.valueOf(row.get("nickname"));
        String username = String.valueOf(row.get("username"));
        String content = String.valueOf(row.get("content"));
        String status = String.valueOf(row.get("status"));
        String createdAt = String.valueOf(row.get("createdAt"));

        return new CommentResponse(id, articleId, userId, nickname, username, content, status, createdAt);
    }
}
