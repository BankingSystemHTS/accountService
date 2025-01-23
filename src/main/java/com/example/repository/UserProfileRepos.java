package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.UserProfile;

@Repository
public interface UserProfileRepos extends JpaRepository<UserProfile, Long>{
   Optional<UserProfile> findById(Long id);

   Optional<UserProfile> findByEmail(String email);
}
