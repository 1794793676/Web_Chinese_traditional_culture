package com.example.backform.admin;

import com.example.backform.admin.dto.*;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.common.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) { this.adminService = adminService; }
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard() { return ApiResponse.ok(adminService.dashboard()); }
    @GetMapping("/comments")
    public ApiResponse<PageResponse<AdminCommentResponse>> comments(@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) { return ApiResponse.ok(adminService.comments(status, page, size)); }
    @PatchMapping("/comments/{id}/status")
    public ApiResponse<AdminCommentResponse> update(@PathVariable Long id, @Valid @RequestBody CommentStatusUpdateRequest request, HttpServletRequest httpRequest) { return ApiResponse.ok(adminService.updateStatus(id, request, (CurrentUser) httpRequest.getAttribute("currentUser"))); }
    @GetMapping("/interactions")
    public ApiResponse<List<InteractionRankingResponse>> interactions(@RequestParam(defaultValue = "20") int limit) { return ApiResponse.ok(adminService.interactions(limit)); }
}
