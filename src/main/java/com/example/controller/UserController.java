package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.UserProfile;

@RestController
@RequestMapping("/api/user")
public class UserController {
   @GetMapping("/{id}")
   public UserProfile getUserById(@PathVariable Long id) {
      // Logic to get user by id, from repository
      return new UserProfile();
   }

   @PostMapping
   public String createUser(@RequestBody UserProfile userProfile) {
      // call 
      return "User created successfully";
   }


}
