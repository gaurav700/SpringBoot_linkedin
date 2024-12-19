package com.LinkedIn.ConnectionService.Contollers;

import com.LinkedIn.ConnectionService.DTO.PersonDto;
import com.LinkedIn.ConnectionService.Service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionService connectionService;

    public ConnectionsController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/firstDegree")
    public ResponseEntity<List<PersonDto>> getFirstConnections(@RequestHeader("UserId") Long userId){
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections(userId));
    }

}
