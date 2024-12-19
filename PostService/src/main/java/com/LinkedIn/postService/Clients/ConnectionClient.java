package com.LinkedIn.postService.Clients;

import com.LinkedIn.postService.DTO.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ConnectionService", path = "/connections")
public interface ConnectionClient {

    @GetMapping("/core/{userId}/firstDegree")
    List<PersonDto> getFirstConnections(@PathVariable Long userId);
}
