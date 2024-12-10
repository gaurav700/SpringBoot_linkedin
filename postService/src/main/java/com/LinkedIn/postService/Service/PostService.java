package com.LinkedIn.postService.Service;

import com.LinkedIn.postService.DTO.PostCreateDto;
import com.LinkedIn.postService.DTO.PostCreatedDto;
import com.LinkedIn.postService.Entity.Post;
import com.LinkedIn.postService.Repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;

    public PostCreatedDto createPost(PostCreateDto postCreateDto, Long userId) {
        Post post = modelMapper.map(postCreateDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postsRepository.save(post);
        return modelMapper.map(savedPost, PostCreatedDto.class);
    }
}
