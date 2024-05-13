package org.barcode.songservice.controller;

import org.barcode.songservice.model.User;
import org.barcode.songservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "register",consumes = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PutMapping(value = "logIn",consumes = "application/json")
    public ResponseEntity<Boolean> logInUser(@RequestBody Map<String,String> details){
        return userService.logInUser(details);
    }
}
