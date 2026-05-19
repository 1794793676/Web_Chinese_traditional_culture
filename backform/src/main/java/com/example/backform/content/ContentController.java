package com.example.backform.content;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.common.PageResponse;
import com.example.backform.content.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContentController {
    private final CategoryService categoryService;
    private final ArticleService articleService;
    private final InteractionService interactionService;
    public ContentController(CategoryService categoryService, ArticleService articleService, InteractionService interactionService) {
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.interactionService = interactionService;
    }
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> categories() { return ApiResponse.ok(categoryService.listPublishedCategories()); }
    @GetMapping("/articles/featured")
    public ApiResponse<List<ArticleCardResponse>> featured(@RequestParam(defaultValue = "6") int limit) { return ApiResponse.ok(articleService.featured(limit)); }
    @GetMapping("/articles")
    public ApiResponse<PageResponse<ArticleCardResponse>> list(@RequestParam String category, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        return ApiResponse.ok(new PageResponse<>(articleService.listByCategory(category, size, offset), page, size, articleService.countByCategory(category)));
    }
    @GetMapping("/articles/{slug}")
    public ApiResponse<ArticleDetailResponse> detail(@PathVariable String slug, HttpServletRequest request) { return ApiResponse.ok(articleService.detail(slug, (CurrentUser) request.getAttribute("currentUser"))); }
    @PostMapping("/articles/{id}/view")
    public ApiResponse<InteractionCountResponse> view(@PathVariable Long id, HttpServletRequest request) { return ApiResponse.ok(articleService.addView(id, (CurrentUser) request.getAttribute("currentUser"), request)); }
    @PostMapping("/articles/{id}/like")
    public ApiResponse<InteractionCountResponse> like(@PathVariable Long id, HttpServletRequest request) { return ApiResponse.ok(interactionService.like(id, (CurrentUser) request.getAttribute("currentUser"))); }
    @DeleteMapping("/articles/{id}/like")
    public ApiResponse<InteractionCountResponse> unlike(@PathVariable Long id, HttpServletRequest request) { return ApiResponse.ok(interactionService.unlike(id, (CurrentUser) request.getAttribute("currentUser"))); }
    @GetMapping("/articles/{id}/comments")
    public ApiResponse<PageResponse<CommentResponse>> comments(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        return ApiResponse.ok(new PageResponse<>(interactionService.listComments(id, size, offset), page, size, interactionService.countComments(id)));
    }
    @PostMapping("/articles/{id}/comments")
    public ApiResponse<InteractionCountResponse> addComment(@PathVariable Long id, @Valid @RequestBody CommentCreateRequest request, HttpServletRequest httpRequest) {
        return ApiResponse.ok(interactionService.createComment(id, (CurrentUser) httpRequest.getAttribute("currentUser"), request));
    }
    @PostMapping("/articles/{id}/share")
    public ApiResponse<InteractionCountResponse> share(@PathVariable Long id, @Valid @RequestBody ShareCreateRequest request, HttpServletRequest httpRequest) {
        return ApiResponse.ok(interactionService.share(id, (CurrentUser) httpRequest.getAttribute("currentUser"), request));
    }
}
