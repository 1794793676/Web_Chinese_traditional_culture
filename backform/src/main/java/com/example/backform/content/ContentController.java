package com.example.backform.content;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api")
public class ContentController {
 private final JdbcTemplate jdbc; public ContentController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @GetMapping("/categories") public ApiResponse<List<Map<String,Object>>> categories(){ return ApiResponse.ok(jdbc.queryForList("select id,name,slug,description,cover_image as coverImage,sort_order as sortOrder from categories where status='published' order by sort_order asc")); }
 @GetMapping("/articles/featured") public ApiResponse<List<Map<String,Object>>> featured(@RequestParam(defaultValue="6") int limit){ return ApiResponse.ok(jdbc.queryForList("select a.id,a.slug,a.title,a.summary,a.cover_image as coverImage,c.name categoryName,ifnull(v.like_count,0) likeCount,ifnull(v.comment_count,0) commentCount,ifnull(v.share_count,0) shareCount,ifnull(v.view_count,0) viewCount from articles a join categories c on c.id=a.category_id left join v_article_stats v on v.article_id=a.id where a.status='published' and c.status='published' order by a.published_at desc limit ?",limit)); }
 @GetMapping("/articles") public ApiResponse<PageResponse<Map<String,Object>>> list(@RequestParam String category,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="10") int size){ String cat=Map.of("ideas","thought","artifacts","craft","festivals","festival").getOrDefault(category,category); int off=(page-1)*size; Long total=jdbc.queryForObject("select count(*) from articles a join categories c on c.id=a.category_id where a.status='published' and c.status='published' and c.slug=?",Long.class,cat); var items=jdbc.queryForList("select a.id,a.slug,a.title,a.summary,a.published_at as publishedAt from articles a join categories c on c.id=a.category_id where a.status='published' and c.status='published' and c.slug=? order by a.published_at desc limit ? offset ?",cat,size,off); return ApiResponse.ok(new PageResponse<>(items,page,size,total)); }
}
