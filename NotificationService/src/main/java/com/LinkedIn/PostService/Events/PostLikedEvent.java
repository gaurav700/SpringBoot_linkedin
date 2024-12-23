package com.LinkedIn.PostService.Events;

public class PostLikedEvent {
    Long creatorId;
    Long postId;
    Long likedByUserId;

    public PostLikedEvent() {
    }

    public PostLikedEvent(Long creatorId, Long postId, Long likedByUserId) {
        this.creatorId = creatorId;
        this.postId = postId;
        this.likedByUserId = likedByUserId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getLikedByUserId() {
        return likedByUserId;
    }

    public void setLikedByUserId(Long likedByUserId) {
        this.likedByUserId = likedByUserId;
    }
}
