package com.example.account_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.account_service.model.UserProfile;
import com.example.account_service.repository.UserProfileRepos;
import com.example.exception.service_exception.ResourceNotFoundException;
import com.example.exception.service_exception.UserAlreadyExistsException;
import com.example.exception.service_exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
   
   //creates a mock
   @Mock
   private UserProfileRepos userProfileRepos;

   //create a instance of the class and injects the mocks into it
   @InjectMocks
   private AccountService accountService;

   private UserProfile mockUser;
   private UserProfile mockUser2;
   private List<UserProfile> mockUsers;
   
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
   @Test
   void testGetUserById_Success() {
      when(userProfileRepos.findById(1L)).thenReturn(Optional.of(mockUser));
      UserProfile user = accountService.getUserById(1L);

      assertNotNull(user);
      assertEquals(mockUser.getEmail(), user.getEmail());
   }
   
   @Test
   void testGetUserById_NotFound() {
      when(userProfileRepos.findById(2L)).thenReturn(Optional.empty());
      assertThrows(UserNotFoundException.class, () -> accountService.getUserById(2L));
   }

   @Test
   void testGetUserByEmail_Success() {
      when(userProfileRepos.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
      UserProfile user = accountService.getByEmail("test@gmail.com");
      assertNotNull(user);
      assertEquals(mockUser.getId(), user.getId());
   }

   @Test
   void testGetUserByEmail_NotFound() {
      when(userProfileRepos.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());
      assertThrows(UserNotFoundException.class, () -> accountService.getByEmail("notfound@gmail.com"));
   }

   @Test
   void testGetPaginatedUser_Success() {
      int page = 0;
      int size = 2;
      Pageable pageable = PageRequest.of(page, size);

      Page<UserProfile> mockPage = new PageImpl<>(mockUsers, pageable, mockUsers.size());
      when(userProfileRepos.findAll(pageable)).thenReturn(mockPage);

      //Act
      List<UserProfile> resultPage = accountService.getPaginatedUser(page, size);

      //Assert
      assertNotNull(resultPage);
      assertEquals(2, resultPage.size());
      assertEquals("test@gmail.com", resultPage.get(0).getEmail());

      verify(userProfileRepos, times(1)).findAll(pageable);
   }
   
   @Test
   void testGetPaginatedUser_EmptyResult() {
      int page = 0;
      int size = 2;
      Pageable pageable = PageRequest.of(page, size);
      Page<UserProfile> emptyPage = new PageImpl<>(List.of(), pageable, 0);

      when(userProfileRepos.findAll(pageable)).thenReturn(emptyPage);

      //Act
      List<UserProfile> resultPage = accountService.getPaginatedUser(page, size);

      //Assert
      assertNotNull(resultPage);
      assertTrue(resultPage.isEmpty());

      //Verify that findALl was called with correct arguments
      verify(userProfileRepos, times(1)).findAll(pageable);
   }

   @Test
   void testCreateUser_Success() {
      when(userProfileRepos.findByEmail("test@gmail.com")).thenReturn(Optional.empty());
      when(userProfileRepos.save(mockUser)).thenReturn(mockUser);
      //Act
      UserProfile user = accountService.createUser(mockUser);
      //Assert
      assertNotNull(user);
      assertEquals(mockUser.getEmail(), user.getEmail());
      //verify findByEmail was called with correct arguments
      verify(userProfileRepos, times(1)).findByEmail("test@gmail.com");
      verify(userProfileRepos, times(1)).save(mockUser);
   }

   @Test 
   void testCreateUser_UserExist() {
      when(userProfileRepos.findByEmail("test@gmail.com")).thenReturn(Optional.of(mockUser));
      //Assert
      assertThrows(UserAlreadyExistsException.class, () -> accountService.createUser(mockUser));
      //Verify
      verify(userProfileRepos, never()).save(any(UserProfile.class));
   }

   @Test
   void testUpdateUser_Success() {
      //user updates mockUser
      UserProfile userInput = new UserProfile();
      userInput.setFirstName("Nathan");
      userInput.setLastName("Chan");
      userInput.setEmail("nathan@gamil.com");
      userInput.setPhoneNumber("6693361086");

      when(userProfileRepos.findById(1L)).thenReturn(Optional.of(mockUser));
      when(userProfileRepos.save(mockUser)).thenReturn(mockUser);
      //Act
      UserProfile updatedUser = accountService.updateUser(1L, userInput);

      //Assert
      assertNotNull(updatedUser);

      // assertEquals(userInput, updatedUser);
      assertEquals(userInput.getFirstName(), updatedUser.getFirstName());
      assertEquals(userInput.getLastName(), updatedUser.getLastName());
      assertEquals(userInput.getEmail(), updatedUser.getEmail());
      assertEquals(userInput.getPhoneNumber(), updatedUser.getPhoneNumber());

      verify(userProfileRepos, times(1)).findById(1L);
      verify(userProfileRepos, times(1)).save(updatedUser);

   }

   @Test
   void testUpdateUser_NotFound() {
      //test the case when user id not found
      when(userProfileRepos.findById(2L)).thenReturn(Optional.empty());
      //Assert
      assertThrows(UserNotFoundException.class,
            () -> accountService.updateUser(2L, mockUser));

      verify(userProfileRepos, times(1)).findById(2L);
   }

   @Test
   void testDeleteUser_Success() {
      when(userProfileRepos.existsById(1L)).thenReturn(true);
      doNothing().when(userProfileRepos).deleteById(1L);
      //Act
      accountService.deleteUser(1L);

      verify(userProfileRepos, times(1)).existsById(1L);
      verify(userProfileRepos, times(1)).deleteById(1L);
   }

   @Test
   void testDelete_NotFound() {
      when(userProfileRepos.existsById(1L)).thenReturn(false);

      assertThrows(UserNotFoundException.class,
            () -> accountService.deleteUser(1L));
      verify(userProfileRepos, times(1)).existsById(1L);
      verify(userProfileRepos, never()).deleteById(1L);
   }
   
   @Test
   void testGetAllUsers_Success() {
      when(userProfileRepos.findAll()).thenReturn(mockUsers);
      //Act
      List<UserProfile> users = accountService.getAllUsers();
      //assert
      assertNotNull(users);
      assertEquals(2, users.size());
      verify(userProfileRepos, times(1)).findAll();
   }

   @Test
   void testGetAllUsers_Empty() {
      List<UserProfile> users = Arrays.asList();
      when(userProfileRepos.findAll()).thenReturn(users);
      assertThrows(ResourceNotFoundException.class,
            () -> accountService.getAllUsers());
      verify(userProfileRepos, times(1)).findAll();
   }
}
