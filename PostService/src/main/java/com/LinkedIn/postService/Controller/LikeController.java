package com.LinkedIn.postService.Controller;

import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Service.PostLikeService;
import com.LinkedIn.postService.Service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final PostLikeService postLikeService;

    public LikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<PostLikeDto> likePost(@PathVariable Long postId){
        PostLikeDto like = postLikeService.likePost(postId);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> disLikePost(@PathVariable Long postId){
        postLikeService.disLikePost(postId);
        return ResponseEntity.ok("Post has been disliked successfully");
    }

}
