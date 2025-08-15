package com.LinkedIn.NotificationService.Consumer;

import com.LinkedIn.ConnectionService.Events.SendConnectionRequestEvent;
import com.LinkedIn.ConnectionService.Events.AcceptConnectionRequestEvent;
import com.LinkedIn.NotificationService.Service.SendNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectionServiceConsumer {

    private final SendNotificationService sendNotificationService;

    public ConnectionServiceConsumer(SendNotificationService sendNotificationService) {
        this.sendNotificationService = sendNotificationService;
    }

    @KafkaListener(
            topics = "Send-connection-request-topic",
            groupId = "notification-service",
            properties = "spring.json.value.default.type=com.LinkedIn.ConnectionService.Events.SendConnectionRequestEvent"
    )
    public void handleSendConnectionRequest(SendConnectionRequestEvent event) {
        Long receiverId = event.getReceiverId();
        Long senderId = event.getSenderId();

        String message = String.format("You have received a connection request from user %d.", senderId);
        log.info("Notify receiver={} about new connection request from sender={}", receiverId, senderId);

        try {
            sendNotificationService.send(receiverId, message);
            log.info("Notification sent successfully to receiver={}", receiverId);
        } catch (Exception ex) {
            log.error("Failed to send notification to receiver={} for sender={}", receiverId, senderId, ex);
        }
    }

    @KafkaListener(
            topics = "Accept-connection-request-topic",
            groupId = "notification-service",
            properties = "spring.json.value.default.type=com.LinkedIn.ConnectionService.Events.AcceptConnectionRequestEvent"
    )
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent event) {
        Long receiverId = event.getReceiverId();
        Long senderId = event.getSenderId();

        String message = String.format("User %d accepted your connection request.", receiverId);
        log.info("Notify user={} that their request was accepted by user={}", senderId, receiverId);

        try {
            sendNotificationService.send(senderId, message);
            log.info("Notification sent successfully to user={}", senderId);
        } catch (Exception ex) {
            log.error("Failed to send acceptance notification to user={} (accepted by={})", senderId, receiverId, ex);
        }
    }
}
