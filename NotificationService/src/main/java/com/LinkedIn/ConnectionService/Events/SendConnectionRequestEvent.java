package com.LinkedIn.ConnectionService.Events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
