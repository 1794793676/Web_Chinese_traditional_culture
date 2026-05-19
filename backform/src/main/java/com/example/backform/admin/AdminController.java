package com.example.backform.admin;
import com.example.backform.auth.CurrentUser;
import com.example.backform.common.*;
import com.example.backform.mapper.AdminMapper;
import com.example.backform.mapper.CommentMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/admin")
public class AdminController {
 private final AdminMapper adminMapper; private final CommentMapper commentMapper;
 public AdminController(AdminMapper adminMapper, CommentMapper commentMapper){this.adminMapper=adminMapper;this.commentMapper=commentMapper;}
 @GetMapping("/dashboard") public ApiResponse<Map<String,Object>> d(){ var stats=new HashMap<>(adminMapper.dashboardStats()); stats.put("totalViews",adminMapper.totalViews()); return ApiResponse.ok(Map.of("stats",stats,"topArticles",adminMapper.topArticles(10),"latestComments",adminMapper.latestComments(10))); }
 @GetMapping("/comments") public ApiResponse<PageResponse<Map<String,Object>>> comments(@RequestParam(defaultValue = "") String status,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size){ int off=(page-1)*size; long total=commentMapper.countAdminComments(status); return ApiResponse.ok(new PageResponse<>(commentMapper.findAdminComments(status,size,off),page,size,total)); }
 @PatchMapping("/comments/{id}/status") public ApiResponse<Map<String,Object>> update(@PathVariable Long id,@RequestBody Map<String,String> req,HttpServletRequest http){ String status=req.get("status"); if(!Set.of("visible","hidden","deleted","pending").contains(status)) throw new BusinessException(400,"非法状态"); commentMapper.updateStatus(id,status,req.get("adminNote")); CurrentUser u=(CurrentUser)http.getAttribute("currentUser"); adminMapper.insertAdminLog(u.id(),"update_comment_status","comment",id,"status="+status); return ApiResponse.ok(commentMapper.findById(id)); }
 @GetMapping("/interactions") public ApiResponse<List<Map<String,Object>>> interactions(@RequestParam(defaultValue = "30d") String period,@RequestParam(defaultValue = "20") int limit){ return ApiResponse.ok(adminMapper.interactionRanking(limit)); }
}
