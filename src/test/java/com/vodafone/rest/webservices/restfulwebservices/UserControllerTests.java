package com.vodafone.rest.webservices.restfulwebservices;


import com.vodafone.rest.webservices.restfulwebservices.controller.UserRestController;
import com.vodafone.rest.webservices.restfulwebservices.jpa.UserInfosDaoService;
import com.vodafone.rest.webservices.restfulwebservices.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Mock
    private UserInfosDaoService userRepository;

    @InjectMocks
    private UserRestController userController;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testCreateUser() throws Exception{
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.addUser(user);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user, response.getBody());

        Mockito.verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testCreateUsers() {
        List<User> users = Arrays.asList(
                new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros"),
                new User(2, "pto", "pto@example.com", "Pot", "Ptos")
        );

        when(userRepository.saveAll(users)).thenReturn(users);

        ResponseEntity<User> response = userController.addUsers(users);
        HttpStatusCode status = response.getStatusCode();

        assertEquals(HttpStatus.OK, status);
        verify(userRepository, times(1)).saveAll(users);
    }

    @Test
    public void testFindUsers() {
        List<User> users = Arrays.asList(
                new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros"),
                new User(2, "pto", "pto@example.com", "Pot", "Ptos")
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> response = userController.findUsers();

        assertEquals(users.size(), response.size());
        assertEquals(users, response);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userRepository, times(1)).findById(1);
    }


    @Test
    void testDeleteUserById() {
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.deleteUser(1);
        verify(userRepository, times(1)).delete(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUsers() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        UserInfosDaoService userInfosDaoService = Mockito.mock(UserInfosDaoService.class);
        Mockito.when(userInfosDaoService.findById(Mockito.anyInt())).thenReturn(Optional.empty());


        ResponseEntity<List<Integer>> response = userController.deleteUsers(new ArrayList<>(ids));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ids, response.getBody());

        Mockito.verify(userInfosDaoService, Mockito.never()).delete(Mockito.any(User.class));
    }

    @Test
    void testDeleteForAllUsers() {
        List<Integer> ids = Arrays.asList(1, 2);

        User user1 = new User(1, "username1", "email1", "first1", "last1");
        User user2 = new User(2, "username2", "email2", "first2", "last2");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(2)).thenReturn(Optional.of(user2));


        ResponseEntity<List<Integer>> response = userController.deleteUsers(new ArrayList<>(ids));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        Mockito.verify(userRepository, Mockito.times(1)).delete(user1);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user2);
    }


    @Test
    void updateUserValidUserReturnsOkResponse() {
        User updatedUser = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        ResponseEntity<User> response = userController.updateUser(updatedUser);
        verify(userRepository, times(1)).save(updatedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUsers_ValidUsers_ReturnsOkResponse() {
        // Arrange
        List<User> users = Arrays.asList(
                new User(1, "user1", "user1@example.com", "User 1", "Lastname"),
                new User(2, "user2", "user2@example.com", "User 2", "Lastname")
        );
        ResponseEntity<User> response = userController.updateUsers(users);

        verify(userRepository, times(1)).saveAll(users);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }


}
