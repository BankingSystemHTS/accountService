package com.example.account_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.account_service.model.UserProfile;
import com.example.account_service.repository.UserProfileRepos;

import jakarta.transaction.Transactional;
@Service
public class AccountService {
   @Autowired
   private UserProfileRepos userProfileRepos;

   public UserProfile getUserById(Long id) {
      Optional<UserProfile> userProfileOpt = userProfileRepos.findById(id);

      return userProfileOpt.orElseThrow(() -> new RuntimeException("User profile not found"));
   }
   
   @Transactional
   public UserProfile createUser(UserProfile userProfile) {
      Optional<UserProfile> userProfileOpt = userProfileRepos.findByEmail(userProfile.getEmail());
      if (userProfileOpt.isPresent()) {
         throw new RuntimeException("User already exists");
      }
      return userProfileRepos.save(userProfile);
   }
}
