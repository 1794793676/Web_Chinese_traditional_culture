package com.example.backform.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CommentStatusUpdateRequest {
    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "visible|hidden|deleted|pending", message = "非法状态")
    private String status;
    private String adminNote;
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
}
