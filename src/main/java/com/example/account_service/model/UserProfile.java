package com.example.account_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
@Table (name = "user_profile")
public class UserProfile {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   @Email(message = "Email is invalid")
   private String email;

   private String firstName;
   private String lastName;
   private String phoneNumber;
   
   @Column(nullable = false, unique = true)
   private Long userId; // user id from Auth_Service
   //auth service register new user and notify account service to create user profile
   
}
