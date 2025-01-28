package com.example.account_service.controller;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.account_service.model.UserProfile;
import com.example.account_service.service.AccountService;
import com.example.exception.service_exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@WithMockUser
public class UserControllerUnitTest {
   @Autowired
   private MockMvc mockMvc;

   @SuppressWarnings("removal")
   @MockBean
   private AccountService accountService;

   @Autowired
   private ObjectMapper objectMapper;
   private UserProfile mockUser, mockUser2;
   List<UserProfile> mockUsers;

   @BeforeEach
   void setUp() {
      mockUser = new UserProfile();
      mockUser.setId(1L);
      mockUser.setEmail("test@gmail.com");
      mockUser.setFirstName("John");
      mockUser.setLastName("Doe");
      mockUser.setPhoneNumber("123123123");

      mockUser2 = new UserProfile();
      mockUser2.setId(2L);
      mockUser2.setEmail("test2@gmail.com");

      mockUsers = Arrays.asList(mockUser, mockUser2);
   }

   /* 
      Test Case 1: Success Scenario
      Ensures that retrieving a using by a valid ID returns the correct user profile
      with HTTP 200
   */ 
   @Test
   void testGetUserById_Success() throws Exception {
      when(accountService.getUserById(1L)).thenReturn(mockUser);
      //Act & Assert
      mockMvc.perform(get("/api/user/1"))
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         .andExpect(jsonPath("$.id").value(1L))
         .andExpect(jsonPath("$.email").value("test@gmail.com"))
         .andExpect(jsonPath("$.firstName").value("John"))
         .andExpect(jsonPath("$.lastName").value("Doe"))
         .andExpect(jsonPath("$.phoneNumber").value("123123123"));
         
      verify(accountService, times(1)).getUserById(1L);
   }

   @Test
   void testGetUserById_NotFound() throws Exception
   {
      when(accountService.getUserById(1L))
            .thenThrow(new UserNotFoundException("User not found"));
      //Act & Assert
      mockMvc.perform(get("/api/user/1"))
            .andExpect(status().isBadRequest());
   }
}
