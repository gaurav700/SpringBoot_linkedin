package com.LinkedIn.NotificationService.Clients;


import com.LinkedIn.NotificationService.DTO.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ConnectionService", path = "/connections")
public interface ConnectionClient {

    @GetMapping("/core/firstDegree")
    List<PersonDto> getFirstConnections(@RequestHeader("UserId") Long userId);
}
