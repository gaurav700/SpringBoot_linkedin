package com.LinkedIn.PostService.Clients;

import com.LinkedIn.PostService.DTO.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "CONNECTIONSERVICE")
public interface ConnectionClient {

    @GetMapping("/connections/firstDegree")
    List<PersonDto> getFirstConnections();
}
