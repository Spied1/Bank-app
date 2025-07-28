package com.src.controllers;

import com.src.components.JwtCore;
import com.src.models.DTO.UserLoginDTO;
import com.src.models.DTO.UserRegistrationDTO;
import com.src.models.User;
import com.src.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @Autowired
    public SecurityController(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              AuthenticationManager authenticationManager,
                              JwtCore jwtCore) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationDTO signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Success");
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDTO signInRequest) {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);

        return ResponseEntity.ok(jwt);
    }
}
