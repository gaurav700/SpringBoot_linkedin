package com.LinkedIn.postService.Service;

import com.LinkedIn.postService.DTO.PostCreateDto;
import com.LinkedIn.postService.DTO.PostCreatedDto;
import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Entity.Post;
import com.LinkedIn.postService.Exceptions.ResourceNotFoundException;
import com.LinkedIn.postService.Repository.PostsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;

    public PostService(PostsRepository postsRepository, ModelMapper modelMapper) {
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
    }

    public PostCreatedDto createPost(PostCreateDto postCreateDto, Long userId) {
        Post post = modelMapper.map(postCreateDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postsRepository.save(post);
        return modelMapper.map(savedPost, PostCreatedDto.class);
    }

    public PostCreatedDto getPost(Long postId) {
        Post post = postsRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+postId));
        return modelMapper.map(post, PostCreatedDto.class);
    }
}
