package com.example.backform.admin;

import com.example.backform.admin.dto.AdminCommentResponse;
import com.example.backform.admin.dto.CommentStatusUpdateRequest;
import com.example.backform.admin.dto.DashboardResponse;
import com.example.backform.admin.dto.InteractionRankingResponse;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.common.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard() {
        DashboardResponse dashboard = adminService.dashboard();
        return ApiResponse.ok(dashboard);
    }

    @GetMapping("/comments")
    public ApiResponse<PageResponse<AdminCommentResponse>> comments(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<AdminCommentResponse> comments = adminService.comments(status, page, size);
        return ApiResponse.ok(comments);
    }

    @PatchMapping("/comments/{id}/status")
    public ApiResponse<AdminCommentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentStatusUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        CurrentUser currentUser = (CurrentUser) httpRequest.getAttribute("currentUser");
        AdminCommentResponse response = adminService.updateStatus(id, request, currentUser);
        return ApiResponse.ok(response);
    }

    @GetMapping("/interactions")
    public ApiResponse<List<InteractionRankingResponse>> interactions(
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<InteractionRankingResponse> rankings = adminService.interactions(limit);
        return ApiResponse.ok(rankings);
    }
}
