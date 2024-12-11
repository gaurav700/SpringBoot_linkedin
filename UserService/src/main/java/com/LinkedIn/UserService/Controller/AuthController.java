package com.LinkedIn.UserService.Controller;

import com.LinkedIn.UserService.DTO.SignUpRequestDto;
import com.LinkedIn.UserService.DTO.SignUpResponseDto;
import com.LinkedIn.UserService.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        SignUpResponseDto sign = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(sign, HttpStatus.CREATED);
    }
}
