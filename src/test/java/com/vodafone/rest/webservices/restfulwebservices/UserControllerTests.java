package com.vodafone.rest.webservices.restfulwebservices;


import com.vodafone.rest.webservices.restfulwebservices.controller.UserRestController;
import com.vodafone.rest.webservices.restfulwebservices.dto.UserDto;
import com.vodafone.rest.webservices.restfulwebservices.jpa.UserRepository;
import com.vodafone.rest.webservices.restfulwebservices.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRestController userController;


    @Mock
    private ModelMapper mapper;


    @Test
    public void testCreateUser() throws Exception{
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto userDto = mapper.map(user,UserDto.class);

        ResponseEntity<UserDto> response = userController.addUser(userDto);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());

    }


    @Test
    public void testCreateUsers() {
        List<User> users = Arrays.asList(
                new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros"),
                new User(2, "pto", "pto@example.com", "Pot", "Ptos")
        );

        when(userRepository.saveAll(users)).thenReturn(users);

        List<UserDto> userDtos = users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ResponseEntity<List<UserDto>> response = userController.addUsers(userDtos);
        HttpStatusCode status = response.getStatusCode();

        assertEquals(HttpStatus.CREATED, status);
    }

    @Test
    public void testFindUsers() {
        List<User> users = Arrays.asList(
                new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros"),
                new User(2, "pto", "pto@example.com", "Pot", "Ptos")
        );

        when(userRepository.findAll()).thenReturn(users);

    List<UserDto> userDtos = users.stream()
            .map(user -> mapper.map(user, UserDto.class))
            .collect(Collectors.toList());


        List<UserDto> response = userController.findUsers();

        assertEquals(userDtos.size(), response.size());
        assertEquals(userDtos, response);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById() {
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<UserDto> response = userController.findUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mapper.map(user,UserDto.class), response.getBody());
        verify(userRepository, times(1)).findById(1);
    }


    @Test
    void testDeleteUserById() {
        User user = new User(1, "kkatsaros", "kkatsaros@example.com", "kostas", "katsaros");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        ResponseEntity<UserDto> response = userController.deleteUser(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUsers() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());


        ResponseEntity<List<Integer>> response = userController.deleteUsers(new ArrayList<>(ids));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ids, response.getBody());

        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any(User.class));
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

        UserDto userDto = mapper.map(updatedUser,UserDto.class);
        ResponseEntity<UserDto> response = userController.updateUser(userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void updateUsersValidUsersReturnsOkResponse() {
        // Arrange
        List<User> users = Arrays.asList(
                new User(1, "user1", "user1@example.com", "User 1", "Lastname"),
                new User(2, "user2", "user2@example.com", "User 2", "Lastname")
        );
        List<UserDto> userDtos = users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ResponseEntity<UserDto> response = userController.updateUsers(userDtos);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }


}
