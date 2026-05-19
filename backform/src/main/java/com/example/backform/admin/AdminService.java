package com.example.backform.admin;

import com.example.backform.admin.dto.*;
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
    public AdminService(AdminMapper adminMapper, CommentMapper commentMapper) { this.adminMapper = adminMapper; this.commentMapper = commentMapper; }
    public DashboardResponse dashboard() {
        Map<String, Object> stats = adminMapper.dashboardStats();
        return new DashboardResponse(toLong(stats.get("totalUsers")), toLong(stats.get("totalArticles")), toLong(stats.get("totalLikes")), toLong(stats.get("totalComments")), toLong(stats.get("totalShares")), adminMapper.totalViews(), adminMapper.topArticles(10).stream().map(this::toRank).toList(), adminMapper.latestComments(10).stream().map(this::toAdminComment).toList());
    }
    public PageResponse<AdminCommentResponse> comments(String status, int page, int size) {
        int offset = (page - 1) * size;
        return new PageResponse<>(commentMapper.findAdminComments(status, size, offset).stream().map(this::toAdminComment).toList(), page, size, commentMapper.countAdminComments(status));
    }
    public AdminCommentResponse updateStatus(Long commentId, CommentStatusUpdateRequest request, CurrentUser currentUser) {
        int rows = commentMapper.updateStatus(commentId, request.getStatus(), request.getAdminNote());
        if (rows == 0) { throw new BusinessException(404, "评论不存在", HttpStatus.NOT_FOUND); }
        adminMapper.insertAdminLog(currentUser.id(), "update_comment_status", "comment", commentId, "status=" + request.getStatus());
        return toAdminComment(commentMapper.findById(commentId));
    }
    public List<InteractionRankingResponse> interactions(int limit) { return adminMapper.interactionRanking(limit).stream().map(this::toRank).toList(); }
    private InteractionRankingResponse toRank(Map<String, Object> row) { return new InteractionRankingResponse(toLong(row.get("articleId")), String.valueOf(row.get("title")), String.valueOf(row.get("categoryName")), toLong(row.get("viewCount")), toLong(row.get("likeCount")), toLong(row.get("commentCount")), toLong(row.get("shareCount")), Double.valueOf(String.valueOf(row.get("totalScore")))); }
    private AdminCommentResponse toAdminComment(Map<String, Object> row) { return new AdminCommentResponse(toLong(row.get("id")), toLong(row.get("articleId")), String.valueOf(row.get("articleTitle")), toLong(row.get("userId")), String.valueOf(row.get("userNickname")), String.valueOf(row.get("content")), String.valueOf(row.get("status")), String.valueOf(row.get("adminNote")), String.valueOf(row.get("createdAt"))); }
    private Long toLong(Object value) { return value == null ? 0L : ((Number) value).longValue(); }
}
