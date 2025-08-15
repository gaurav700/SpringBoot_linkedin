package com.LinkedIn.NotificationService.Consumer;

import com.LinkedIn.NotificationService.Clients.ConnectionClient;
import com.LinkedIn.NotificationService.DTO.PersonDto;
import com.LinkedIn.NotificationService.Service.SendNotificationService;
import com.LinkedIn.PostService.Events.PostCreatedEvent;
import com.LinkedIn.PostService.Events.PostLikedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PostsServiceConsumer {

    private final ConnectionClient connectionClient;
    private final SendNotificationService sendNotificationService;

    public PostsServiceConsumer(ConnectionClient connectionClient,
                                SendNotificationService sendNotificationService) {
        this.connectionClient = connectionClient;
        this.sendNotificationService = sendNotificationService;
    }

    @KafkaListener(
            topics = "Post-created-topic",
            groupId = "notification-service",
            properties = "spring.json.value.default.type=com.LinkedIn.PostService.Events.PostCreatedEvent"
    )
    public void handlePostCreated(PostCreatedEvent event) {
        if (event == null || event.getCreatorId() == null) {
            log.warn("Received null/invalid PostCreatedEvent: {}", event);
            return;
        }

        Long creatorId = event.getCreatorId();
        Long postId = event.getPostId();
        log.info("Handling PostCreatedEvent: creatorId={}, postId={}", creatorId, postId);

        try {
            List<PersonDto> connections = connectionClient.getFirstConnections(creatorId);
            if (connections == null || connections.isEmpty()) {
                log.info("No connections found for creatorId={}; skipping notifications", creatorId);
                return;
            }

            String baseMsg = "Your connection %d has created a new post. Check it out!";
            for (PersonDto p : connections) {
                if (p == null || p.getUserId() == null) continue;
                String message = String.format(baseMsg, creatorId);
                try {
                    sendNotificationService.send(p.getUserId(), message);
                    log.debug("Notified userId={} about new postId={} from creatorId={}",
                            p.getUserId(), postId, creatorId);
                } catch (Exception ex) {
                    log.error("Failed to notify userId={} about postId={} from creatorId={}",
                            p.getUserId(), postId, creatorId, ex);
                }
            }

            log.info("Post-created notifications processed: creatorId={}, postId={}, recipients={}",
                    creatorId, postId, connections.stream().filter(Objects::nonNull).count());
        } catch (Exception ex) {
            log.error("Error while processing PostCreatedEvent for creatorId={}, postId={}",
                    creatorId, postId, ex);
        }
    }

    @KafkaListener(
            topics = "Post-liked-topic",
            groupId = "notification-service",
            properties = "spring.json.value.default.type=com.LinkedIn.PostService.Events.PostLikedEvent"
    )
    public void handlePostLiked(PostLikedEvent event) {
        if (event == null || event.getCreatorId() == null || event.getPostId() == null) {
            log.warn("Received null/invalid PostLikedEvent: {}", event);
            return;
        }

        Long creatorId = event.getCreatorId();
        Long postId = event.getPostId();
        Long likedBy = event.getLikedByUserId();

        log.info("Handling PostLikedEvent: postId={}, creatorId={}, likedBy={}",
                postId, creatorId, likedBy);

        String message = String.format("Your post %d was liked by user %d.", postId, likedBy);

        try {
            sendNotificationService.send(creatorId, message);
            log.debug("Sent like notification to creatorId={} for postId={} (likedBy={})",
                    creatorId, postId, likedBy);
        } catch (Exception ex) {
            log.error("Failed to send like notification to creatorId={} for postId={} (likedBy={})",
                    creatorId, postId, likedBy, ex);
        }
    }
}
