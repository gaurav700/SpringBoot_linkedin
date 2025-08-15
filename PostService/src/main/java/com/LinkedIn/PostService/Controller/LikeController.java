package com.LinkedIn.PostService.Controller;

import com.LinkedIn.PostService.DTO.PostLikeDto;
import com.LinkedIn.PostService.Service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
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
