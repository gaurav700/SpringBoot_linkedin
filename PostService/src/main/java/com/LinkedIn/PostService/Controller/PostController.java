package com.LinkedIn.PostService.Controller;

import com.LinkedIn.PostService.DTO.PostCreateDto;
import com.LinkedIn.PostService.DTO.PostCreatedDto;
import com.LinkedIn.PostService.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostCreatedDto> createPost(@RequestBody PostCreateDto postCreateDto){
        PostCreatedDto createdPost = postService.createPost(postCreateDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostCreatedDto> getPost(@PathVariable Long postId){
        PostCreatedDto getPost = postService.getPost(postId);
        return ResponseEntity.ok(getPost);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostCreatedDto>> getAllPostByUserId(@PathVariable Long userId){
        List<PostCreatedDto> posts = postService.getAllPostByUserId(userId);
        return ResponseEntity.ok(posts);
    }


}
