package com.LinkedIn.PostService.DTO;

public class PostCreateDto {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostCreateDto(String content) {
        this.content = content;
    }

    public PostCreateDto() {
    }

    @Override
    public String toString() {
        return "PostCreateDto{" +
                "content='" + content + '\'' +
                '}';
    }
}
