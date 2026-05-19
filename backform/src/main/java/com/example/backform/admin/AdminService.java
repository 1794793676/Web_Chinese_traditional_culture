package com.example.backform.admin;

import com.example.backform.admin.dto.AdminCommentResponse;
import com.example.backform.admin.dto.CommentStatusUpdateRequest;
import com.example.backform.admin.dto.DashboardResponse;
import com.example.backform.admin.dto.InteractionRankingResponse;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.BusinessException;
import com.example.backform.common.PageResponse;
import com.example.backform.mapper.AdminMapper;
import com.example.backform.mapper.CommentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private final AdminMapper adminMapper;
    private final CommentMapper commentMapper;

    public AdminService(AdminMapper adminMapper, CommentMapper commentMapper) {
        this.adminMapper = adminMapper;
        this.commentMapper = commentMapper;
    }

    public DashboardResponse dashboard() {
        Map<String, Object> stats = adminMapper.dashboardStats();
        long totalUsers = toLong(stats.get("totalUsers"));
        long totalArticles = toLong(stats.get("totalArticles"));
        long totalLikes = toLong(stats.get("totalLikes"));
        long totalComments = toLong(stats.get("totalComments"));
        long totalShares = toLong(stats.get("totalShares"));

        List<InteractionRankingResponse> popularArticles = adminMapper
                .topArticles(10)
                .stream()
                .map(this::toRank)
                .toList();

        List<AdminCommentResponse> latestComments = adminMapper
                .latestComments(10)
                .stream()
                .map(this::toAdminComment)
                .toList();

        return new DashboardResponse(
                totalUsers,
                totalArticles,
                totalLikes,
                totalComments,
                totalShares,
                adminMapper.totalViews(),
                popularArticles,
                latestComments
        );
    }

    public PageResponse<AdminCommentResponse> comments(String status, int page, int size) {
        int offset = (page - 1) * size;
        List<AdminCommentResponse> commentList = commentMapper
                .findAdminComments(status, size, offset)
                .stream()
                .map(this::toAdminComment)
                .toList();
        long total = commentMapper.countAdminComments(status);

        return new PageResponse<>(commentList, page, size, total);
    }

    public AdminCommentResponse updateStatus(
            Long commentId,
            CommentStatusUpdateRequest request,
            CurrentUser currentUser
    ) {
        Map<String, Object> existingComment = commentMapper.findById(commentId);
        if (existingComment == null) {
            throw new BusinessException(404, "评论不存在", HttpStatus.NOT_FOUND);
        }

        commentMapper.updateStatus(commentId, request.getStatus(), request.getAdminNote());

        adminMapper.insertAdminLog(
                currentUser.id(),
                "update_comment_status",
                "comment",
                commentId,
                "status=" + request.getStatus()
        );

        Map<String, Object> updatedComment = commentMapper.findById(commentId);
        return toAdminComment(updatedComment);
    }

    public List<InteractionRankingResponse> interactions(int limit) {
        List<Map<String, Object>> rows = adminMapper.interactionRanking(limit);
        return rows.stream().map(this::toRank).toList();
    }

    private InteractionRankingResponse toRank(Map<String, Object> row) {
        Long articleId = toLong(row.get("articleId"));
        String title = String.valueOf(row.get("title"));
        String categoryName = String.valueOf(row.get("categoryName"));
        Long viewCount = toLong(row.get("viewCount"));
        Long likeCount = toLong(row.get("likeCount"));
        Long commentCount = toLong(row.get("commentCount"));
        Long shareCount = toLong(row.get("shareCount"));
        Double totalScore = Double.valueOf(String.valueOf(row.get("totalScore")));

        return new InteractionRankingResponse(
                articleId,
                title,
                categoryName,
                viewCount,
                likeCount,
                commentCount,
                shareCount,
                totalScore
        );
    }

    private AdminCommentResponse toAdminComment(Map<String, Object> row) {
        Long id = toLong(row.get("id"));
        Long articleId = toLong(row.get("articleId"));
        String articleTitle = String.valueOf(row.get("articleTitle"));
        Long userId = toLong(row.get("userId"));
        String userNickname = String.valueOf(row.get("userNickname"));
        String content = String.valueOf(row.get("content"));
        String status = String.valueOf(row.get("status"));
        String adminNote = String.valueOf(row.get("adminNote"));
        String createdAt = String.valueOf(row.get("createdAt"));

        return new AdminCommentResponse(
                id,
                articleId,
                articleTitle,
                userId,
                userNickname,
                content,
                status,
                adminNote,
                createdAt
        );
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        return ((Number) value).longValue();
    }
}
