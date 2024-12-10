package com.LinkedIn.postService.Controller;

import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Service.PostLikeService;
import com.LinkedIn.postService.Service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final PostLikeService postLikeService;

    public LikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<PostLikeDto> likePost(@PathVariable Long postId){
        PostLikeDto like = postLikeService.likePost(postId, 1L);
        return ResponseEntity.ok(like);
    }
}
