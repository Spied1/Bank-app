package com.src.controllers;

import com.src.components.UserDetailsImpl;
import com.src.models.DTO.UserRegistrationDTO;
import com.src.models.User;
import com.src.services.UserService;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/secured")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userCheck")
    public ResponseEntity<?> userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid principal type");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        return ResponseEntity.ok(userDetails.getId());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(authentication);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestParam("newUsername") String newUsername)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.updateUser(authentication, newUsername);

        return ResponseEntity.ok(user);
    }

}
