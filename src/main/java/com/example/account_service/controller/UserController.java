package com.example.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.account_service.model.UserProfile;
import com.example.account_service.service.AccountService;


@RestController
@RequestMapping("/api/user")
public class UserController {

   @Autowired
   private AccountService accountService;

   @GetMapping("/{id}")
   public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
      // Logic to get user by id, from repository
      UserProfile user = accountService.getUserById(id);
      return ResponseEntity.ok(user);
   }

   @PostMapping
   public String createUser(@RequestBody UserProfile userProfile) {
      // call service and repo to create user in database
      accountService.createUser(userProfile);
      return "User created successfully";
   }

}
