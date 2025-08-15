package com.LinkedIn.ConnectionService.Service;

import com.LinkedIn.ConnectionService.Auth.UserContextHolder;
import com.LinkedIn.ConnectionService.DTO.PersonDto;
import com.LinkedIn.ConnectionService.Entity.Person;
import com.LinkedIn.ConnectionService.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConnectionService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public ConnectionService(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
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
}
