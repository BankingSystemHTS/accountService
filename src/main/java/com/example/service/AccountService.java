package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.UserProfileRepos;
import com.example.model.UserProfile;
@Service
public class AccountService {
   @Autowired
   private UserProfileRepos userProfileRepos;

   public UserProfile getUserById(Long id) {
      Optional<UserProfile> userProfileOpt = userProfileRepos.findById(id);
      
      return userProfileOpt.orElseThrow(() -> new RuntimeException("User profile not found"));
   }
}
