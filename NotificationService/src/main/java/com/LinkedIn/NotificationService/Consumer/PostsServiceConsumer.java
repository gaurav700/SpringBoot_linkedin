package com.LinkedIn.NotificationService.Consumer;

import com.LinkedIn.NotificationService.Clients.ConnectionClient;
import com.LinkedIn.NotificationService.DTO.PersonDto;
import com.LinkedIn.NotificationService.Entity.Notification;
import com.LinkedIn.NotificationService.Repository.NotificationRepository;
import com.LinkedIn.PostService.Events.PostCreatedEvent;
import com.LinkedIn.PostService.Events.PostLikedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsServiceConsumer {

    private final ConnectionClient connectionClient;
    private final NotificationRepository notificationRepository;

    public PostsServiceConsumer(ConnectionClient connectionClient, NotificationRepository notificationRepository) {
        this.connectionClient = connectionClient;
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "Post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        List<PersonDto> connections = connectionClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for(PersonDto personDto : connections){
            sendNotifications(personDto.getUserId(), "Your connection "+postCreatedEvent.getCreatorId()+" has created a new post, please check it out!!");
        }
    }


    @KafkaListener(topics = "Post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        String message = "Hi, Your post '"+postLikedEvent.getPostId()+"' has been liked by "+postLikedEvent.getLikedByUserId()+".";
        sendNotifications(postLikedEvent.getCreatorId(), message);
    }


    public void sendNotifications(Long userId, String message){
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

}
