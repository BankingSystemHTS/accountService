package com.example.account_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.example.account_service.model.UserProfile;
import com.example.account_service.service.AccountService;
import com.example.exception.GlobalExceptionHandler;
import com.example.exception.service_exception.UserAlreadyExistsException;
import com.example.exception.service_exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(username = "testUser", roles = { "USER" })
@Import(GlobalExceptionHandler.class)
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
      mockUser.setUserId(2L);

      mockUser2 = new UserProfile();
      mockUser2.setId(2L);
      mockUser2.setEmail("test2@gmail.com");

      mockUsers = Arrays.asList(mockUser, mockUser2);
   }

   // refactor repeated json validation for UserProfile
   private void assertUserProfileJson(String json) throws Exception {
      // parse json string into Java object for comparison
      objectMapper = new ObjectMapper();
      UserProfile user = objectMapper.readValue(json, UserProfile.class);

      assertEquals(mockUser.getId(), user.getId());
      assertEquals(mockUser.getEmail(), user.getEmail());
      assertEquals(mockUser.getFirstName(), user.getFirstName());
      assertEquals(mockUser.getLastName(), user.getLastName());
      assertEquals(mockUser.getPhoneNumber(), user.getPhoneNumber());
   }

   /*
    * Test Case 1: Success Scenario
    * Ensures that retrieving a using by a valid ID returns the correct user
    * profile
    * with HTTP 200
    */
   @Test
   void testGetUserById_Success() throws Exception {
      when(accountService.getUserById(1L)).thenReturn(mockUser);
      // Act & Assert
      MvcResult mvcResult = mockMvc.perform(get("/api/user/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

      String responseBody = mvcResult.getResponse().getContentAsString();
      assertUserProfileJson(responseBody);
      verify(accountService, times(1)).getUserById(1L);
   }

   @Test
   void testGetUserById_NotFound() throws Exception {
      when(accountService.getUserById(1L))
            .thenThrow(new UserNotFoundException("User not found"));
      // Act & Assert
      mockMvc.perform(get("/api/user/1"))
            .andExpect(status().isBadRequest());
      verify(accountService, times(1)).getUserById(1L);
   }

   @Test
   void testGetByEmail_Success() throws Exception {
      when(accountService.getByEmail(mockUser.getEmail())).thenReturn(mockUser);

      // Act & Assert
      MvcResult result = mockMvc.perform(get("/api/user/getByEmail/test@gmail.com"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
      String responseBody = result.getResponse().getContentAsString();
      assertUserProfileJson(responseBody);
      verify(accountService, times(1)).getByEmail(mockUser.getEmail());
   }

   @Test
   void testGetByEmail_NotFound() throws Exception {
      when(accountService.getByEmail("notFound@gmail.com"))
            .thenThrow(new UserNotFoundException("User Email Not Found"));
      mockMvc.perform(get("/api/user/getByEmail/notFound@gmail.com"))
            .andExpect(status().isBadRequest());
      verify(accountService, times(1)).getByEmail("notFound@gmail.com");
   }

   @Test
   void testGetPaginatedUser() throws Exception {
      int page = 0;
      int size = 2;
      when(accountService.getPaginatedUser(page, size)).thenReturn(mockUsers);
      // act & assert
      mockMvc.perform(get("/api/user/paginated")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].email").value(mockUser.getEmail()))
            .andExpect(jsonPath("$[1].email").value(mockUser2.getEmail()));

      verify(accountService, times(1)).getPaginatedUser(page, size);
   }

   @Test
   void testCreateUser() throws Exception {
      when(accountService.createUser(mockUser)).thenReturn(mockUser);

      mockMvc.perform(post("/api/user/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockUser))
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().string("User created successfully"));

      verify(accountService, times(1)).createUser(mockUser);
   }

   @Test
   void testCreateUser_UserAlreadyExists() throws Exception {
      when(accountService.createUser(mockUser))
            .thenThrow(new UserAlreadyExistsException("User already exists"));

      mockMvc.perform(post("/api/user/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(mockUser))
            .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("User already exists"));
      verify(accountService, times(1)).createUser(mockUser);
   }
}
