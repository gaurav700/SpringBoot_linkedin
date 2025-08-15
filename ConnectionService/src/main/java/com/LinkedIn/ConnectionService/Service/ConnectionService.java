package com.LinkedIn.ConnectionService.Service;

import com.LinkedIn.ConnectionService.Auth.UserContextHolder;
import com.LinkedIn.ConnectionService.DTO.PersonDto;
import com.LinkedIn.ConnectionService.Entity.Person;
import com.LinkedIn.ConnectionService.Events.AcceptConnectionRequestEvent;
import com.LinkedIn.ConnectionService.Events.SendConnectionRequestEvent;
import com.LinkedIn.ConnectionService.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConnectionService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, SendConnectionRequestEvent>  sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    public ConnectionService(PersonRepository personRepository, ModelMapper modelMapper, KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate, KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.sendRequestKafkaTemplate = sendRequestKafkaTemplate;
        this.acceptRequestKafkaTemplate = acceptRequestKafkaTemplate;
    }

    public List<PersonDto> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Fetching first-degree connections for current user...");

        if (userId == null) {
            log.error("Missing UserId in context/header");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing UserId header");
        }

        try {
            log.info("Querying repository for first-degree connections, userId={}", userId);
            List<Person> people = personRepository.getFirstDegreeConnections(userId);
            log.info("Repository returned {} connections for userId={}", people.size(), userId);

            List<PersonDto> result = people.stream()
                    .map(p -> modelMapper.map(p, PersonDto.class))
                    .collect(Collectors.toList());

//            log.info("Mapped {} Person -> PersonDto for userId={}", result.size(), userId);
            return result;
        } catch (Exception ex) {
            log.error("Failed to fetch first-degree connections for userId={}", userId, ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to fetch connections at the moment"
            );
        }
    }

    @Transactional
    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("User {} is attempting to send a connection request to {}", senderId, receiverId);

        if (senderId == null) {
            log.error("Missing UserId in context while sending connection request to {}", receiverId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing UserId");
        }

        if (receiverId == null) {
            log.error("ReceiverId is null for sender {}", senderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ReceiverId is required");
        }

        if (senderId.equals(receiverId)) {
            log.warn("User {} tried to connect with themselves", senderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot connect with yourself");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            log.warn("Duplicate connection request: sender={} receiver={}", senderId, receiverId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Connection request already sent");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            log.warn("Users already connected: sender={} receiver={}", senderId, receiverId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already connected");
        }

        personRepository.addConnectionRequest(senderId, receiverId);
        log.info("Connection request created: sender={} receiver={}", senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        log.info("Publishing SendConnectionRequestEvent to Kafka topic='Send-connection-request-topic' for sender={} receiver={}", senderId, receiverId);
        sendRequestKafkaTemplate.send("Send-connection-request-topic", sendConnectionRequestEvent);
        return true;
    }


    @Transactional
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("User {} is attempting to accept connection request from {}", receiverId, senderId);

        if (receiverId == null) {
            log.error("Missing UserId in context while accepting connection request from {}", senderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing UserId");
        }

        if (senderId == null) {
            log.error("SenderId is null for receiver {}", receiverId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SenderId is required");
        }

        if (receiverId.equals(senderId)) {
            log.warn("User {} tried to accept a request from themselves", receiverId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot accept your own request");
        }

        boolean requestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if (!requestExists) {
            log.warn("No connection request found from sender {} to receiver {}", senderId, receiverId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Connection request not found");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Connection request from {} to {} accepted successfully", senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        log.info("Publishing AcceptConnectionRequestEvent to Kafka topic='Accept-connection-request-topic' for sender={} receiver={}", senderId, receiverId);
        acceptRequestKafkaTemplate.send("Accept-connection-request-topic", acceptConnectionRequestEvent);
        return true;
    }


    @Transactional
    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("User {} is attempting to reject connection request from {}", receiverId, senderId);

        if (receiverId == null) {
            log.error("Missing UserId in context while rejecting connection request from {}", senderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing UserId");
        }

        if (senderId == null) {
            log.error("SenderId is null for receiver {}", receiverId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SenderId is required");
        }

        if (receiverId.equals(senderId)) {
            log.warn("User {} tried to reject their own connection request", receiverId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot reject your own request");
        }

        boolean requestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if (!requestExists) {
            log.warn("No connection request found from sender {} to receiver {}", senderId, receiverId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Connection request not found");
        }

        // Just delete the pending request
        personRepository.deleteConnectionRequest(senderId, receiverId);
        log.info("Connection request from {} to {} rejected successfully", senderId, receiverId);

        return true;
    }

}
