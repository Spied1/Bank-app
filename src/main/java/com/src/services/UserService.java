package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.models.DTO.UserRegistrationDTO;
import com.src.models.User;
import com.src.repositorys.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Authentication authentication) {
        String userId = getUserId(authentication);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return user.get();
    }

    public User saveUser(UserRegistrationDTO user) {
        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setBirthDate(user.getBirthDate());


        userRepository.save(newUser);

        return newUser;
    }

    public User updateUser(Authentication authentication, String newName) {
        User updatedUser = getUser(authentication);

        if (Objects.equals(newName, updatedUser.getName())) {
            return updatedUser;
        }

        updatedUser.setName(newName);

        userRepository.save(updatedUser);

        return updatedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return UserDetailsImpl.build(user.get());
    }

    private String getUserId(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new AuthorizationServiceException("No such user");
        }

        return ((UserDetailsImpl) principal).getId();
    }
}