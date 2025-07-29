package com.src.controllers;

import com.src.components.UserDetailsImpl;
import com.src.models.DTO.UserInformation;
import com.src.models.Transfer;
import com.src.services.UserService;
import org.springframework.http.HttpStatus;
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

    //TODO just delete this
    @GetMapping("/userCheck")
    public ResponseEntity<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid principal type");
        }

        return ResponseEntity.ok(userDetails.getId());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInformation userInformation = userService.getUserInformation(authentication);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Transfer> transferList = userService.getAllSentTransfersByUser(authentication);

        return ResponseEntity.ok(transferList);
    }
}