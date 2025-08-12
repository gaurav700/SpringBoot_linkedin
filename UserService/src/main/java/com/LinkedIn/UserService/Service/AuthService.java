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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
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
        User users = authRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if(users!=null){
            throw new BadRequestException("User with this email id already exists : "+ signUpRequestDto.getEmail());
        }

        User user = modelMapper.map(signUpRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = authRepository.save(user);
        return modelMapper.map(savedUser, SignUpResponseDto.class);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = authRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("User with this email id : "+loginRequestDto.getEmail()+" not exists"));

        boolean passwordCheck = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!passwordCheck){
            throw new BadRequestException("The password you entered is incorrect");
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(jwtService.generateAccessToken(user));
        loginResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));
        return loginResponseDto;
    }
}
