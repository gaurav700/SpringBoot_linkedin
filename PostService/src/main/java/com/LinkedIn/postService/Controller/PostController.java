package com.LinkedIn.postService.Controller;

import com.LinkedIn.postService.DTO.PostCreateDto;
import com.LinkedIn.postService.DTO.PostCreatedDto;
import com.LinkedIn.postService.Service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostCreatedDto> createPost(@RequestBody PostCreateDto postCreateDto, HttpServletRequest httpServletRequest){
        PostCreatedDto createdPost = postService.createPost(postCreateDto, 1L);
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
