package com.LinkedIn.NotificationService.Service;

import com.LinkedIn.NotificationService.Entity.Notification;
import com.LinkedIn.NotificationService.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification send(Long userId, String message) {
        if (userId == null) {
            log.error("Cannot send notification: userId is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }
        if (message == null || message.isBlank()) {
            log.error("Cannot send notification: message is empty (userId={})", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message is required");
        }

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        // set defaults if your entity has these fields (optional)
        // notification.setRead(false);
        // notification.setCreatedAt(Instant.now());

        try {
            Notification saved = notificationRepository.save(notification);
            log.info("Notification saved (id={}, userId={})", saved.getId(), userId);
            return saved;
        } catch (DataAccessException dae) {
            log.error("DB error while saving notification for userId={}: {}", userId, dae.getMessage(), dae);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to persist notification");
        }
    }
}
