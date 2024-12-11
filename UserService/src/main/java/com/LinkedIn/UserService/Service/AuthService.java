package com.LinkedIn.UserService.Service;

import com.LinkedIn.UserService.DTO.SignUpRequestDto;
import com.LinkedIn.UserService.DTO.SignUpResponseDto;
import com.LinkedIn.UserService.Entity.User;
import com.LinkedIn.UserService.Repository.AuthRepository;
import com.LinkedIn.UserService.Utils.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;

    public AuthService(AuthRepository authRepository, ModelMapper modelMapper) {
        this.authRepository = authRepository;
        this.modelMapper = modelMapper;
    }

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = authRepository.save(user);
        return modelMapper.map(savedUser, SignUpResponseDto.class);
    }
}
