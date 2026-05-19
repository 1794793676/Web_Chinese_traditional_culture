package com.example.backform.admin;
import com.example.backform.common.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/admin")
public class AdminController {
 private final JdbcTemplate jdbc; public AdminController(JdbcTemplate jdbc){this.jdbc=jdbc;}
 @GetMapping("/dashboard") public ApiResponse<Map<String,Object>> d(){ return ApiResponse.ok(Map.of("stats",jdbc.queryForMap("select * from v_dashboard_stats"),"topArticles",jdbc.queryForList("select * from v_article_stats order by hot_score desc limit 10"),"latestComments",jdbc.queryForList("select * from v_latest_comments limit 10"))); }
}
