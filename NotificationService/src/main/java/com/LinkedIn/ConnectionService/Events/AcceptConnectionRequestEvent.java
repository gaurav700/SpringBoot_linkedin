package com.LinkedIn.ConnectionService.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
