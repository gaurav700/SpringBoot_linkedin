package com.LinkedIn.postService.DTO;

import lombok.Data;


import java.time.LocalDateTime;

public class PostCreatedDto {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PostCreatedDto(Long id, String content, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public PostCreatedDto() {
    }

    @Override
    public String toString() {
        return "PostCreatedDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}
