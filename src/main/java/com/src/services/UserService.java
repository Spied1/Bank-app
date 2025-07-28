package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.models.DTO.UserInformationDTO;
import com.src.models.DTO.UserRegistrationDTO;
import com.src.models.User;
import com.src.repositorys.UserRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUser(Authentication authentication) {
        String userId = getUserId(authentication);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return user.get();
    }

    public void updateUser(Authentication authentication, String newName) {
        User updatedUser = getUser(authentication);

        if (Objects.equals(newName, updatedUser.getName())) {
            return;
        }

        updatedUser.setName(newName);

        userRepository.save(updatedUser);

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

    public UserInformationDTO getUserInformation(Authentication authentication) {
        User user = getUser(authentication);

        return new UserInformationDTO(user.getUsername(), user.getBirthDate());
    }

    public void registerUser(UserRegistrationDTO signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setName(signUpRequest.getFullName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
    }
}