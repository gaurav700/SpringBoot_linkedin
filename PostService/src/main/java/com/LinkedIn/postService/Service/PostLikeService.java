package com.LinkedIn.postService.Service;

import com.LinkedIn.postService.Auth.UserContextHolder;
import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Entity.Post;
import com.LinkedIn.postService.Entity.PostLike;
import com.LinkedIn.postService.Events.PostLikedEvent;
import com.LinkedIn.postService.Exceptions.BadRequestException;
import com.LinkedIn.postService.Exceptions.ResourceNotFoundException;
import com.LinkedIn.postService.Repository.PostsLikeRepository;
import com.LinkedIn.postService.Repository.PostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PostLikeService {

    private  final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public PostLikeService(PostsLikeRepository postsLikeRepository, PostsRepository postsRepository, ModelMapper modelMapper, KafkaTemplate<Long, PostLikedEvent> kafkaTemplate) {
        this.postsLikeRepository = postsLikeRepository;
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public PostLikeDto likePost(Long postId) {
        log.info("Attempting to like the post with id: {}", postId);

        Long userId = UserContextHolder.getCurrentUserId();
//        log.info("Current user id: {}", userId);

        Post post = postsRepository.findById(postId).orElse(null);
        if (post == null) {
            log.error("Like failed: Post with id {} not found", postId);
            throw new ResourceNotFoundException("Post with id: " + postId + " not found");
        }

        boolean alreadyLiked = postsLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) {
            log.error("Like failed: User {} has already liked post {}", userId, postId);
            throw new BadRequestException("This post is already liked by the user, you are not allowed to like it again");
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        PostLike liked = postsLikeRepository.save(postLike);
        log.info("Post {} liked successfully by user {}", postId, userId);

        // Building and sending the event to Kafka
        PostLikedEvent postLikedEvent = new PostLikedEvent();
        postLikedEvent.setPostId(liked.getPostId());
        postLikedEvent.setCreatorId(post.getUserId());
        postLikedEvent.setLikedByUserId(userId);

        kafkaTemplate.send("Post-liked-topic", postId, postLikedEvent);
        log.info("PostLikedEvent sent to Kafka for postId: {}", postId);

        return modelMapper.map(liked, PostLikeDto.class);
    }

    @Transactional
    public void disLikePost(Long postId) {
        log.info("Attempting to dislike the post with id: {}", postId);

        Long userId = UserContextHolder.getCurrentUserId();
//        log.info("Current user id: {}", userId);

        boolean existPost = postsRepository.existsById(postId);
        if (!existPost) {
            log.error("Dislike failed: Post with id {} not found", postId);
            throw new ResourceNotFoundException("Post with id: " + postId + " not found");
        }

        boolean alreadyLiked = postsLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (!alreadyLiked) {
            log.error("Dislike failed: User {} has not liked post {}", userId, postId);
            throw new BadRequestException("This post is not liked by the user, you cannot dislike it");
        }

        postsLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post {} disliked successfully by user {}", postId, userId);
    }


}
