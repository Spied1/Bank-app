package com.src.controllers;

import com.src.models.DTO.UserInformation;
import com.src.models.Transfer;
import com.src.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/secured")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInformation() {
        UserInformation userInformation = userService.getUserInformation();

        return ResponseEntity.ok(userInformation);
    }

    @PutMapping("/user")
    public ResponseEntity<?> changeUserFullName(@RequestParam("newUsername") String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.updateUser(authentication, newUsername);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/transfers")
    public ResponseEntity<?> getAllTransfersByUser() {
        List<Transfer> transferList = userService.getAllSentTransfersByUser();

        return ResponseEntity.ok(transferList);
    }
}