package com.LinkedIn.postService.DTO;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class PostCreatedDto {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
}
