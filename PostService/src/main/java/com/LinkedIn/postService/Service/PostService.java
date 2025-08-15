package com.LinkedIn.postService.Service;

import com.LinkedIn.postService.Auth.UserContextHolder;
import com.LinkedIn.postService.Clients.ConnectionClient;
import com.LinkedIn.postService.DTO.PersonDto;
import com.LinkedIn.postService.DTO.PostCreateDto;
import com.LinkedIn.postService.DTO.PostCreatedDto;
import com.LinkedIn.postService.DTO.PostLikeDto;
import com.LinkedIn.postService.Entity.Post;
import com.LinkedIn.postService.Events.PostCreatedEvent;
import com.LinkedIn.postService.Exceptions.ResourceNotFoundException;
import com.LinkedIn.postService.Repository.PostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final ConnectionClient connectionClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostService(PostsRepository postsRepository, ModelMapper modelMapper, ConnectionClient connectionClient, KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate) {
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
        this.connectionClient = connectionClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public PostCreatedDto createPost(PostCreateDto postCreateDto) {
        log.info("Attempting to create post for current user...");

        Post post = modelMapper.map(postCreateDto, Post.class);
        Long userId = UserContextHolder.getCurrentUserId();
//        log.info("Mapped PostCreateDto to Post entity for userId: {}", userId);

        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);
        log.info("Post created successfully with id: {} for userId: {}", savedPost.getId(), userId);

        // Build and send Kafka event
        PostCreatedEvent postCreatedEvent = new PostCreatedEvent();
        postCreatedEvent.setPostId(savedPost.getId());
        postCreatedEvent.setCreatorId(userId);
        postCreatedEvent.setContent(savedPost.getContent());

        kafkaTemplate.send("Post-created-topic", postCreatedEvent);
        log.info("PostCreatedEvent sent to Kafka for postId: {}", savedPost.getId());

        return modelMapper.map(savedPost, PostCreatedDto.class);
    }

    public PostCreatedDto getPost(Long postId) {
        log.info("Fetching post with id: {}", postId);

        Post post = postsRepository.findById(postId).orElseThrow(() -> {
            log.error("Post not found with id: {}", postId);
            return new ResourceNotFoundException("Post not found with id: " + postId);
        });

        log.info("Post retrieved successfully with id: {}", postId);
        return modelMapper.map(post, PostCreatedDto.class);
    }

    public List<PostCreatedDto> getAllPostByUserId(Long userId) {
        log.info("Fetching all posts for userId: {}", userId);

        List<Post> posts = postsRepository.findByUserId(userId);
        log.info("Found {} posts for userId: {}", posts.size(), userId);

        return posts
                .stream()
                .map(element -> modelMapper.map(element, PostCreatedDto.class))
                .collect(Collectors.toList());
    }

}
