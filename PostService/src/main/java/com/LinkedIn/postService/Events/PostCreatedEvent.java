package com.LinkedIn.postService.Events;

import lombok.Builder;
import lombok.Data;

public class PostCreatedEvent {
    Long creatorId;
    String content;
    Long postId;

    public PostCreatedEvent() {
    }

    public PostCreatedEvent(Long creatorId, String content, Long postId) {
        this.creatorId = creatorId;
        this.content = content;
        this.postId = postId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
