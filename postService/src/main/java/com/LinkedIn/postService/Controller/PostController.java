package com.LinkedIn.postService.Controller;

import com.LinkedIn.postService.DTO.PostCreateDto;
import com.LinkedIn.postService.DTO.PostCreatedDto;
import com.LinkedIn.postService.Service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
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
}
