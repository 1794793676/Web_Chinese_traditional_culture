package com.example.backform.content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ShareCreateRequest {
    @NotBlank(message = "分享渠道不能为空")
    @Pattern(regexp = "link|wechat|qq|weibo|other", message = "非法分享渠道")
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
