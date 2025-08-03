package com.src.controllers;

import com.src.exeptions.security.UnderageUserRegistration;
import com.src.exeptions.user.UserWithGivenUsernameAlreadyExists;
import com.src.models.DTO.UserInformation;
import com.src.models.DTO.UserLogin;
import com.src.models.DTO.UserRegistration;
import com.src.services.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserRegistration signUpRequest) {
        try {
            UserInformation userInformation = securityService.registerUser(signUpRequest);

            return ResponseEntity.ok().body(userInformation);
        } catch (UserWithGivenUsernameAlreadyExists | UnderageUserRegistration e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserLogin signInRequest) {
        try {
            String token = securityService.signIn(signInRequest);

            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}