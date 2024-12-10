package com.LinkedIn.postService.Service;

import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Entity.PostLike;
import com.LinkedIn.postService.Exceptions.BadRequestException;
import com.LinkedIn.postService.Exceptions.ResourceNotFoundException;
import com.LinkedIn.postService.Repository.PostsLikeRepository;
import com.LinkedIn.postService.Repository.PostsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {

    private  final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;

    public PostLikeService(PostsLikeRepository postsLikeRepository, PostsRepository postsRepository, ModelMapper modelMapper) {
        this.postsLikeRepository = postsLikeRepository;
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
    }

    public PostLikeDto likePost(Long postId, long userId) {
        boolean existPost = postsRepository.existsById(postId);
        if(!existPost){
            throw new ResourceNotFoundException("Post with id : "+postId+" not found");
        }

        boolean alreadyLiked = postsLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLiked){
            throw new BadRequestException("This post is already liked by the user, you are not allowed to like this again");
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        PostLike liked = postsLikeRepository.save(postLike);
        return modelMapper.map(liked, PostLikeDto.class);
    }
}
