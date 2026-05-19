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
    public InteractionService(LikeMapper likeMapper, CommentMapper commentMapper, ShareMapper shareMapper, ArticleMapper articleMapper) {
        this.likeMapper = likeMapper;
        this.commentMapper = commentMapper;
        this.shareMapper = shareMapper;
        this.articleMapper = articleMapper;
    }
    public InteractionCountResponse like(Long articleId, CurrentUser currentUser) {
        if (likeMapper.existsLike(articleId, currentUser.id()) == 0) {
            likeMapper.insertLike(articleId, currentUser.id());
        }
        return new InteractionCountResponse(articleId, articleMapper.countLikes(articleId), articleMapper.countComments(articleId), articleMapper.countShares(articleId), articleMapper.currentViewCount(articleId), true);
    }
    public InteractionCountResponse unlike(Long articleId, CurrentUser currentUser) {
        likeMapper.deleteLike(articleId, currentUser.id());
        return new InteractionCountResponse(articleId, articleMapper.countLikes(articleId), articleMapper.countComments(articleId), articleMapper.countShares(articleId), articleMapper.currentViewCount(articleId), false);
    }
    public long countComments(Long articleId) { return commentMapper.countVisibleByArticleId(articleId); }
    public List<CommentResponse> listComments(Long articleId, int size, int offset) { return commentMapper.findVisibleByArticleId(articleId, size, offset).stream().map(this::toComment).toList(); }
    public InteractionCountResponse createComment(Long articleId, CurrentUser currentUser, CommentCreateRequest request) {
        commentMapper.insertComment(articleId, currentUser.id(), request.getContent().trim(), "visible");
        return new InteractionCountResponse(articleId, articleMapper.countLikes(articleId), articleMapper.countComments(articleId), articleMapper.countShares(articleId), articleMapper.currentViewCount(articleId), true);
    }
    public InteractionCountResponse share(Long articleId, CurrentUser currentUser, ShareCreateRequest request) {
        shareMapper.insertShare(articleId, currentUser.id(), request.getChannel());
        return new InteractionCountResponse(articleId, articleMapper.countLikes(articleId), articleMapper.countComments(articleId), articleMapper.countShares(articleId), articleMapper.currentViewCount(articleId), null);
    }
    private CommentResponse toComment(Map<String, Object> row) { return new CommentResponse(((Number) row.get("id")).longValue(), ((Number) row.get("articleId")).longValue(), ((Number) row.get("userId")).longValue(), String.valueOf(row.get("nickname")), String.valueOf(row.get("username")), String.valueOf(row.get("content")), String.valueOf(row.get("status")), String.valueOf(row.get("createdAt"))); }
}
