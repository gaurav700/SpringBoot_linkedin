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
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        Post post = modelMapper.map(postCreateDto, Post.class);

        Long userId = UserContextHolder.getCurrentUserId();
        post.setUserId(userId);

        Post savedPost = postsRepository.save(post);

        PostCreatedEvent postCreatedEvent = new PostCreatedEvent();
        postCreatedEvent.setPostId(savedPost.getId());
        postCreatedEvent.setCreatorId(userId);
        postCreatedEvent.setContent(savedPost.getContent());

        kafkaTemplate.send("Post-created-topic", postCreatedEvent);


        return modelMapper.map(savedPost, PostCreatedDto.class);
    }

    public PostCreatedDto getPost(Long postId) {
        Post post = postsRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post not found with id: "+postId));
        return modelMapper.map(post, PostCreatedDto.class);
    }

    public List<PostCreatedDto> getAllPostByUserId(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);
        return posts
                .stream()
                .map(element-> modelMapper.map(element, PostCreatedDto.class))
                .collect(Collectors.toList());
    }
}
