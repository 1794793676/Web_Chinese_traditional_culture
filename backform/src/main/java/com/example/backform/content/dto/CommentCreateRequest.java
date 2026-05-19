package com.example.backform.content.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class CommentCreateRequest {
 @NotBlank(message="评论内容不能为空") @Size(max=500,message="评论内容不能超过500字") private String content;
 public String getContent(){return content;} public void setContent(String content){this.content=content;}
}
