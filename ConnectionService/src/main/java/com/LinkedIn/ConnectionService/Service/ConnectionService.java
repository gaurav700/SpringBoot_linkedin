package com.LinkedIn.ConnectionService.Service;

import com.LinkedIn.ConnectionService.Auth.UserContextHolder;
import com.LinkedIn.ConnectionService.DTO.PersonDto;
import com.LinkedIn.ConnectionService.Entity.Person;
import com.LinkedIn.ConnectionService.Repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public ConnectionService(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    public List<PersonDto> getFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        List<Person> person = personRepository.getFirstDegreeConnections(userId);
        return person.stream()
                .map(person1 -> modelMapper.map(person1, PersonDto.class))
                .collect(Collectors.toList());
    }
}
