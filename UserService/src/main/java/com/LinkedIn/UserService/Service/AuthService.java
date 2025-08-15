package com.LinkedIn.UserService.Service;

import com.LinkedIn.UserService.DTO.LoginRequestDto;
import com.LinkedIn.UserService.DTO.LoginResponseDto;
import com.LinkedIn.UserService.DTO.SignUpRequestDto;
import com.LinkedIn.UserService.DTO.SignUpResponseDto;
import com.LinkedIn.UserService.Entity.User;
import com.LinkedIn.UserService.Exceptions.BadRequestException;
import com.LinkedIn.UserService.Exceptions.ResourceNotFoundException;
import com.LinkedIn.UserService.Repository.AuthRepository;
import com.LinkedIn.UserService.Utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public AuthService(AuthRepository authRepository, ModelMapper modelMapper, JwtService jwtService) {
        this.authRepository = authRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("Attempting to sign up user with email: {}", signUpRequestDto.getEmail());

        User users = authRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if (users != null) {
            log.error("Sign up failed: User with email {} already exists", signUpRequestDto.getEmail());
            throw new BadRequestException("User with this email id already exists: " + signUpRequestDto.getEmail());
        }

//        log.info("Mapping request to User entity for email: {}", signUpRequestDto.getEmail());
        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

//        log.info("Saving new user with email: {}", signUpRequestDto.getEmail());
        User savedUser = authRepository.save(user);

        log.info("User with email {} signed up successfully", signUpRequestDto.getEmail());
        return modelMapper.map(savedUser, SignUpResponseDto.class);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        log.info("Attempting login for email: {}", loginRequestDto.getEmail());

        User user = authRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> {
            log.error("Login failed: No user found with email {}", loginRequestDto.getEmail());
            return new ResourceNotFoundException("User with this email id: " + loginRequestDto.getEmail() + " not exists");
        });

        boolean passwordCheck = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if (!passwordCheck) {
            log.error("Login failed: Incorrect password for email {}", loginRequestDto.getEmail());
            throw new BadRequestException("The password you entered is incorrect");
        }

        log.info("Password verified for email: {}", loginRequestDto.getEmail());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(jwtService.generateAccessToken(user));
        loginResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));

        log.info("Login successful for email: {}", loginRequestDto.getEmail());
        return loginResponseDto;
    }

}
