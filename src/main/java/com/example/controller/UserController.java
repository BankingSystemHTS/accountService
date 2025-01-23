package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.UserProfile;

@RestController
@RequestMapping("/api/user")
public class UserController {

   @Autowired
   private UserService userService;

   @GetMapping("/{id}")
   public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
      // Logic to get user by id, from repository
      const user = userService.getUserById(id);
      if(!user){
         return ResponseEntity.notFound().build();
      }


      return ResponseEntity.ok(user);
   }

   @PostMapping
   public String createUser(@RequestBody UserProfile userProfile) {
      // call service and repo to create user in database
      return "User created successfully";
   }

}
