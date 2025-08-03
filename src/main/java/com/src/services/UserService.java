package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.exeptions.user.NoUserFound;
import com.src.exeptions.user.UserWithGivenUsernameAlreadyExists;
import com.src.models.DTO.UserInformation;
import com.src.models.DTO.UserRegistration;
import com.src.models.Transfer;
import com.src.models.User;
import com.src.repositorys.TransferRepository;
import com.src.repositorys.UserRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final TransferRepository transferRepository;


    public UserService(UserRepository userRepository, TransferRepository transferRepository) {
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    public User getUser(Authentication authentication) throws NoUserFound {
        String userId = getUserId(authentication);
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NoUserFound();
        }

        return user.get();
    }

    public void changeUsername(String newName) throws NoUserFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    public UserInformation getUserInformation() throws NoUserFound {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = getUser(authentication);

        return new UserInformation(user.getUsername(), user.getBirthDate());
    }

    public List<Transfer> getAllSentTransfersByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = getUserId(authentication);

        List<Transfer> userTransfers = transferRepository.getAllTransfersByUserId(userId);

        if (userTransfers.isEmpty()) {
            return null;
        }

        return userTransfers;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}