package com.example.account_service.controller;

import java.util.List;

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

   @GetMapping("/getByEmail/{email}")
   public ResponseEntity<UserProfile> getUserByEmail(@PathVariable String email) {
      UserProfile user = accountService.getByEmail(email);
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

   @DeleteMapping("/delete/{id}")
   public ResponseEntity<String> deleteUser(@PathVariable Long id) {
      accountService.deleteUser(id);
      return ResponseEntity.ok("User deleted successfully");
   }

   @GetMapping("/all")
   public ResponseEntity<List<UserProfile>> getAllUsers() {
      List<UserProfile> users = accountService.getAllUsers();
      return ResponseEntity.ok(users);
   }


}