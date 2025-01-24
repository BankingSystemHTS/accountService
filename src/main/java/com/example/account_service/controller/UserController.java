package com.example.account_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
      UserProfile user = accountService.getUserById(id);
      return ResponseEntity.ok(user);
   }

   @PostMapping("/create")
   public ResponseEntity<String> createUser(@RequestBody UserProfile userProfile) {

      accountService.createUser(userProfile);
      return ResponseEntity.ok("User created successfully");
   }
   
   @PutMapping("/update/{id}")
   public ResponseEntity<String> updateUser(
         @PathVariable Long id,
         @RequestBody UserProfile userProfile) {
      accountService.updateUser(id, userProfile);
      return ResponseEntity.ok("User updated successfully");
   }

}