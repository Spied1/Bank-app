package com.src.controllers;

import com.src.exeptions.user.NoUserFound;
import com.src.models.DTO.UserInformation;
import com.src.models.Transfer;
import com.src.services.UserService;
import org.springframework.http.ResponseEntity;
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
        try {
            UserInformation userInformation = userService.getUserInformation();

            return ResponseEntity.ok(userInformation);
        } catch (NoUserFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user")
    public ResponseEntity<?> changeUserFullName(@RequestParam("newUsername") String newUsername) {
        try {
            userService.changeUsername(newUsername);

            return ResponseEntity.ok().build();
        } catch (NoUserFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/transfers")
    public ResponseEntity<?> getAllTransfersByUser() {
        List<Transfer> transferList = userService.getAllSentTransfersByUser();

        return ResponseEntity.ok(transferList);
    }
}