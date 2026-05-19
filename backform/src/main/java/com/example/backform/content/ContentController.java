package com.example.backform.content;

import com.example.backform.auth.CurrentUser;
import com.example.backform.common.ApiResponse;
import com.example.backform.common.PageResponse;
import com.example.backform.content.dto.ArticleCardResponse;
import com.example.backform.content.dto.ArticleDetailResponse;
import com.example.backform.content.dto.CategoryResponse;
import com.example.backform.content.dto.CommentCreateRequest;
import com.example.backform.content.dto.CommentResponse;
import com.example.backform.content.dto.InteractionCountResponse;
import com.example.backform.content.dto.ShareCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContentController {
    private final CategoryService categoryService;
    private final ArticleService articleService;
    private final InteractionService interactionService;

    public ContentController(
            CategoryService categoryService,
            ArticleService articleService,
            InteractionService interactionService
    ) {
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.interactionService = interactionService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> categories() {
        List<CategoryResponse> categories = categoryService.listPublishedCategories();
        return ApiResponse.ok(categories);
    }

    @GetMapping("/articles/featured")
    public ApiResponse<List<ArticleCardResponse>> featured(
            @RequestParam(defaultValue = "6") int limit
    ) {
        List<ArticleCardResponse> featuredArticles = articleService.featured(limit);
        return ApiResponse.ok(featuredArticles);
    }

    @GetMapping("/articles")
    public ApiResponse<PageResponse<ArticleCardResponse>> list(
            @RequestParam String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int offset = resolveOffset(page, size);
        List<ArticleCardResponse> articleCards = articleService.listByCategory(category, size, offset);
        long total = articleService.countByCategory(category);

        PageResponse<ArticleCardResponse> pageResponse = new PageResponse<>(
                articleCards,
                page,
                size,
                total
        );
        return ApiResponse.ok(pageResponse);
    }

    @GetMapping("/articles/{slug}")
    public ApiResponse<ArticleDetailResponse> detail(
            @PathVariable String slug,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        ArticleDetailResponse article = articleService.detail(slug, currentUser);
        return ApiResponse.ok(article);
    }

    @PostMapping("/articles/{id}/view")
    public ApiResponse<InteractionCountResponse> view(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        InteractionCountResponse result = articleService.addView(id, currentUser, request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/articles/{id}/like")
    public ApiResponse<InteractionCountResponse> like(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        InteractionCountResponse result = interactionService.like(id, currentUser);
        return ApiResponse.ok(result);
    }

    @DeleteMapping("/articles/{id}/like")
    public ApiResponse<InteractionCountResponse> unlike(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        CurrentUser currentUser = (CurrentUser) request.getAttribute("currentUser");
        InteractionCountResponse result = interactionService.unlike(id, currentUser);
        return ApiResponse.ok(result);
    }

    @GetMapping("/articles/{id}/comments")
    public ApiResponse<PageResponse<CommentResponse>> comments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int offset = resolveOffset(page, size);
        List<CommentResponse> comments = interactionService.listComments(id, size, offset);
        long total = interactionService.countComments(id);

        PageResponse<CommentResponse> pageResponse = new PageResponse<>(
                comments,
                page,
                size,
                total
        );
        return ApiResponse.ok(pageResponse);
    }

    @PostMapping("/articles/{id}/comments")
    public ApiResponse<InteractionCountResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        CurrentUser currentUser = (CurrentUser) httpRequest.getAttribute("currentUser");
        InteractionCountResponse result = interactionService.createComment(id, currentUser, request);
        return ApiResponse.ok(result);
    }

    @PostMapping("/articles/{id}/share")
    public ApiResponse<InteractionCountResponse> share(
            @PathVariable Long id,
            @Valid @RequestBody ShareCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        CurrentUser currentUser = (CurrentUser) httpRequest.getAttribute("currentUser");
        InteractionCountResponse result = interactionService.share(id, currentUser, request);
        return ApiResponse.ok(result);
    }

    private int resolveOffset(int page, int size) {
        return (page - 1) * size;
    }
}
