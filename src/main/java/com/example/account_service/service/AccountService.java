package com.example.account_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.account_service.model.UserProfile;
import com.example.account_service.repository.UserProfileRepos;
import com.example.exception.service_exception.UserAlreadyExistsException;

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
         throw new UserAlreadyExistsException("User already exists");
      }
      return userProfileRepos.save(userProfile);
   }

   @Transactional
   public UserProfile updateUser(Long id, UserProfile userProfile) {
      //findById() returns an Optional object
      //orElseThrow() is called directly on the Optional object
      //return user profile if found
      UserProfile user = userProfileRepos.findById(id)
            .orElseThrow(() -> new RuntimeException("User profile not found"));
      
      user.setFirstName(userProfile.getFirstName());
      user.setLastName(userProfile.getLastName());
      user.setEmail(userProfile.getEmail());
      user.setPhoneNumber(userProfile.getPhoneNumber());
      
      //save the updated user profile
      return userProfileRepos.save(user);      
   }
}
