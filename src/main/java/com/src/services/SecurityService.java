package com.src.services;

import com.src.components.JwtCore;
import com.src.exeptions.security.UnderageUserRegistration;
import com.src.exeptions.user.UserWithGivenUsernameAlreadyExists;
import com.src.models.DTO.UserInformation;
import com.src.models.DTO.UserLogin;
import com.src.models.DTO.UserRegistration;
import com.src.models.User;
import com.src.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtCore jwtCore;

    public SecurityService(AuthenticationManager authenticationManager,
                           UserRepository userRepository, PasswordEncoder passwordEncoder, JwtCore jwtCore) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtCore = jwtCore;
    }

    public UserInformation registerUser(UserRegistration signUpRequest) throws UserWithGivenUsernameAlreadyExists,
            UnderageUserRegistration {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserWithGivenUsernameAlreadyExists();
        }

        User user = new User();
        user.setName(signUpRequest.getFullName());
        user.setUsername(signUpRequest.getUsername());
        user.setBirthDate(signUpRequest.getBirthDate());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        if (!isAdult(signUpRequest.getBirthDate())) {
            throw new UnderageUserRegistration();
        }

        userRepository.save(user);

        return new UserInformation(user.getName(), user.getBirthDate());
    }

    private boolean isAdult(Date birthDate) {
        LocalDate birth = birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return birth.plusYears(18).isBefore(LocalDate.now());
    }

    public String signIn(UserLogin signInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtCore.generateToken(authentication);
    }
}